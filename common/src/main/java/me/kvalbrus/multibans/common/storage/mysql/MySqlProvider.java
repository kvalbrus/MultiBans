package me.kvalbrus.multibans.common.storage.mysql;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLTimeoutException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.UUID;
import me.kvalbrus.multibans.api.OnlinePlayer;
import me.kvalbrus.multibans.api.Player;
import me.kvalbrus.multibans.api.punishment.Cancelable;
import me.kvalbrus.multibans.api.punishment.Punishment;
import me.kvalbrus.multibans.api.punishment.action.Action;
import me.kvalbrus.multibans.api.punishment.action.ActionType;
import me.kvalbrus.multibans.api.punishment.action.ActivationAction;
import me.kvalbrus.multibans.api.punishment.action.CreationAction;
import me.kvalbrus.multibans.api.punishment.action.DeactivationAction;
import me.kvalbrus.multibans.api.punishment.punishments.PunishmentType;
import me.kvalbrus.multibans.api.punishment.TemporaryPunishment;
import me.kvalbrus.multibans.api.punishment.executor.PunishmentExecutor;
import me.kvalbrus.multibans.api.punishment.target.OnlinePunishmentTarget;
import me.kvalbrus.multibans.api.punishment.target.PunishmentTarget;
import me.kvalbrus.multibans.common.managers.PluginManager;
import me.kvalbrus.multibans.common.punishment.MultiPunishment;
import me.kvalbrus.multibans.common.punishment.MultiTemporaryPunishment;
import me.kvalbrus.multibans.api.DataProvider;
import me.kvalbrus.multibans.common.punishment.action.MultiActivationAction;
import me.kvalbrus.multibans.common.punishment.action.MultiCreationAction;
import me.kvalbrus.multibans.common.punishment.action.MultiDeactivationAction;
import me.kvalbrus.multibans.common.punishment.creator.MultiConsolePunishmentExecutor;
import me.kvalbrus.multibans.common.punishment.creator.MultiPlayerPunishmentExecutor;
import me.kvalbrus.multibans.common.punishment.punishments.ActionLoader;
import me.kvalbrus.multibans.common.punishment.target.MultiOnlinePunishmentTarget;
import me.kvalbrus.multibans.common.punishment.target.MultiPunishmentTarget;
import me.kvalbrus.multibans.common.storage.DataProviderSettings;
import me.kvalbrus.multibans.common.storage.DataProviderType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MySqlProvider implements DataProvider {

    private final PluginManager pluginManager;

    private final DataProviderSettings dataProviderSettings;

    private HikariDataSource source;

    public MySqlProvider(@NotNull PluginManager pluginManager,
                         @NotNull DataProviderSettings dataProviderSettings) {
        this.pluginManager = pluginManager;
        this.dataProviderSettings = dataProviderSettings;
    }

    @Override
    public String getName() {
        return "MySQL";
    }

    @Override
    public void initialization() throws SQLException {
        if (this.dataProviderSettings.getType() == DataProviderType.MY_SQL) {
            this.source = new HikariDataSource();

            Properties properties = this.dataProviderSettings.getProperties();
            this.source.setJdbcUrl("jdbc:mysql://" +
                properties.getProperty("dataSource.serverName") + ":" +
                properties.getProperty("dataSource.portNumber") + "/" +
                properties.getProperty("dataSource.databaseName"));
            this.source.setUsername(properties.getProperty("dataSource.user"));
            this.source.setPassword(properties.getProperty("dataSource.password"));
        } else {
            this.source = new HikariDataSource(new HikariConfig(this.dataProviderSettings.getProperties()));
        }

        this.source.setPoolName("MultiBans-Pool");

        try (Connection connection = this.source.getConnection();
            Statement statement = connection.createStatement()) {
            statement.addBatch(SQLQuery.CREATE_TABLE_PUNISHMENTS.toString());
            statement.addBatch(SQLQuery.CREATE_TABLE_ACTIONS.toString());
            statement.executeBatch();
        }
    }

    @Override
    public void shutdown() {
        if (this.source != null) {
            this.source.close();
        }
    }

    @Override
    public void wipe() throws SQLException {
        try (Connection connection = this.source.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQLQuery.WIPE_TABLE_PUNISHMENTS.toString())) {
            statement.executeUpdate();
        }
    }

    @Override
    public synchronized boolean createPunishment(@NotNull Punishment punishment) throws SQLException {
        try (Connection connection = this.source.getConnection();
            PreparedStatement statement = connection.prepareStatement(SQLQuery.CREATE_PUNISHMENT.toString())) {
            int i = 1;

            PunishmentTarget target = punishment.getTarget();
            PunishmentExecutor creator = punishment.getCreator();

            statement.setString(i++, punishment.getId());
            statement.setString(i++, punishment.getType().getPrefix());
            statement.setString(i++, target instanceof OnlinePunishmentTarget onlineTarget ?
                onlineTarget.getHostAddress() : "");
            statement.setString(i++, target.getName());
            statement.setString(i++, target.getUniqueId().toString());
            statement.setString(i++, creator.getName());
            statement.setLong(i++, punishment.getCreatedDate());
            statement.setLong(i++,
                punishment instanceof TemporaryPunishment temporary ? temporary.getStartedDate() :
                    punishment.getCreatedDate());
            statement.setLong(i++,
                punishment instanceof MultiTemporaryPunishment temporary ? temporary.getDuration() :
                    -1L);
            statement.setString(i++, punishment.getCreatedReason());
            statement.setString(i++, punishment.getComment());

            // TODO: Deprecated
            statement.setString(i++, null); // TODO: Deprecated
            statement.setLong(i++, -1);     // TODO: Deprecated
            statement.setString(i++, null); // TODO: Deprecated
            // TODO: Deprecated

            StringBuilder serversStringBuilder = new StringBuilder();
            for (int j = 0; j < punishment.getServers().size(); ++j) {
                if (j != punishment.getServers().size() - 1) {
                    serversStringBuilder.append(punishment.getServers().get(j)).append(";");
                } else {
                    serversStringBuilder.append(punishment.getServers().get(j));
                }
            }

            statement.setString(i++, serversStringBuilder.toString());
            statement.setBoolean(i++, punishment instanceof Cancelable ? ((Cancelable) punishment).getCancelled() : false);

            statement.executeUpdate();
        }

        this.updatePunishmentActions(punishment);

        return true;
    }

    private synchronized void writePunishment(@NotNull PreparedStatement statement, @NotNull Punishment punishment) throws SQLException {
        long startedDate = punishment instanceof TemporaryPunishment temporary ?
            temporary.getStartedDate() : punishment.getCreatedDate();

        long duration = punishment instanceof TemporaryPunishment temporary ?
            temporary.getDuration() : -1L;

        StringBuilder serversStringBuilder = new StringBuilder();
        for (int j = 0; j < punishment.getServers().size(); ++j) {
            if (j != punishment.getServers().size() - 1) {
                serversStringBuilder.append(punishment.getServers().get(j)).append(";");
            } else {
                serversStringBuilder.append(punishment.getServers().get(j));
            }
        }

        boolean cancelled = punishment instanceof Cancelable cancelable ? cancelable.getCancelled() : false;

        statement.setLong(1, startedDate);
        statement.setLong(2, duration);
        statement.setString(3, punishment.getCreatedReason());
        statement.setString(4, punishment.getComment());

        // TODO: Deprecated
        statement.setString(5, null); // TODO: Deprecated
        statement.setLong(6, -1); // TODO: Deprecated
        statement.setString(7, null); // TODO: Deprecated
        // TODO: Deprecated

        statement.setString(8, serversStringBuilder.toString());
        statement.setBoolean(9, cancelled);

        statement.setString(10, punishment.getId());
    }

    @Override
    public synchronized boolean updatePunishment(@NotNull Punishment punishment) throws SQLException {
        try (Connection connection = this.source.getConnection();
            PreparedStatement statement = connection.prepareStatement(SQLQuery.UPDATE_PUNISHMENT.toString())) {
            this.writePunishment(statement, punishment);
            statement.executeUpdate();
        }

        this.updatePunishmentActions(punishment);

        return true;
    }

    private synchronized void updateAction(@NotNull Action action) throws SQLException {
        if (this.hasAction(action)) {
            return;
        }

        try (Connection connection = this.source.getConnection();
            PreparedStatement statement = connection.prepareStatement(SQLQuery.CREATE_ACTION.toString())) {
            this.writeAction(statement, action);
            statement.executeUpdate();
        }
    }

    private void writeAction(@NotNull PreparedStatement statement, @NotNull Action action) throws SQLException {
        statement.setString(1, action.getPunId());
        statement.setInt(2, action.getId());
        statement.setString(3, action.getType().name());
        statement.setString(4, action.getExecutor().getName());
        statement.setLong(5, action.getDate());
        statement.setString(6, action.getReason());
    }

    private synchronized void updatePunishmentActions(@NotNull Punishment punishment) throws SQLException {
        if (punishment instanceof Cancelable cancelable) {
            for (ActivationAction action : cancelable.getActivations()) {
                this.updateAction(action);
            }

            for (DeactivationAction action : cancelable.getDeactivations()) {
                this.updateAction(action);
            }
        }
    }

    @Override
    public synchronized void deletePunishment(@NotNull Punishment punishment) throws SQLException {
        try (Connection connection = this.source.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(
                SQLQuery.DELETE_PUNISHMENT.toString())) {
                statement.setString(1, punishment.getId());
                statement.executeUpdate();
            }

            try (PreparedStatement statement = connection.prepareStatement(
                SQLQuery.DELETE_ACTIONS.toString())) {
                statement.setString(1, punishment.getId());
                statement.executeUpdate();
            }
        }
    }

    @Override
    public boolean hasPunishment(String id) throws SQLException {
        if (id == null) {
            return false;
        }

        try (Connection connection = this.source.getConnection();
            PreparedStatement statement = connection.prepareStatement(
                SQLQuery.HAS_PUNISHMENT.toString())) {
            statement.setString(1, id);
            return statement.executeQuery().next();
        }
    }

    private Punishment loadPunishment(ResultSet resultSet) throws SQLException {
        String id = resultSet.getString("id");
        PunishmentType type = PunishmentType.valueOf(resultSet.getString("type"));
        String targetIp = resultSet.getString("target_ip");
        String targetName = resultSet.getString("target_name");
        UUID targetUUID = UUID.fromString(resultSet.getString("target_uuid"));
        String creatorName = resultSet.getString("creator_name");
        long createdDate = resultSet.getLong("date_created");
        long startDate = resultSet.getLong("date_start");
        long duration = resultSet.getLong("duration");
        String createdReason = resultSet.getString("reason");
        String comment = resultSet.getString("comment");
        String[] serversString = resultSet.getString("servers").split(";");
        boolean cancelled = resultSet.getBoolean("cancelled");

        List<String> servers = new ArrayList<>(Arrays.stream(serversString).toList());
        Player player = this.pluginManager.getOfflinePlayer(targetUUID);
        PunishmentTarget target;
        if (player instanceof OnlinePlayer onlinePlayer) {
            target = new MultiOnlinePunishmentTarget(onlinePlayer);
        } else {
            target = new MultiPunishmentTarget(player);
        }

        PunishmentExecutor creator;
        if (creatorName.equals(this.pluginManager.getConsole().getName())) {
            creator = new MultiConsolePunishmentExecutor(
                this.pluginManager.getConsole());
        } else {
            creator = new MultiPlayerPunishmentExecutor(
                this.pluginManager.getOfflinePlayer(creatorName));
        }

        comment = comment != null ? comment : "";

        CreationAction creationAction = new MultiCreationAction(id, 1, target, creator, createdDate, createdReason);

        Punishment punishment = MultiPunishment.Companion.constructPunishment(
            this.pluginManager, type, id, creationAction, new ArrayList<>(), new ArrayList<>(),
            startDate, duration, comment, servers, cancelled);

        return punishment;
    }

    private void loadPunishmentActions(Punishment punishment) throws SQLException {
        if (punishment instanceof ActionLoader loader) {
            try (Connection connection = this.source.getConnection();
                PreparedStatement statement = connection.prepareStatement(
                    SQLQuery.GET_PUNISHMENT_ACTIONS.toString())) {
                statement.setString(1, punishment.getId());

                try (ResultSet resultSet = statement.executeQuery()) {
                    List<ActivationAction> activations = new ArrayList<>();
                    List<DeactivationAction> deactivations = new ArrayList<>();
                    while (resultSet.next()) {
                        Action action = this.loadAction(resultSet);
                        if (action instanceof ActivationAction activation) {
                            activations.add(activation);
                        } else if (action instanceof DeactivationAction deactivation) {
                            deactivations.add(deactivation);
                        }
                    }

                    loader.loadActivationsFromDataProvider(activations);
                    loader.loadDeactivationsFromDataProvider(deactivations);
                }
            }
        }
    }

    private Action loadAction(ResultSet resultSet) throws SQLException {
        String punId = resultSet.getString("pun_id");
        int id = resultSet.getInt("id");
        ActionType type = ActionType.valueOf(resultSet.getString("type"));
        String executorName = resultSet.getString("executor");
        long date = resultSet.getLong("date");
        String reason = resultSet.getString("reason");

        PunishmentExecutor executor;
        if (executorName.equals(this.pluginManager.getConsole().getName())) {
            executor = new MultiConsolePunishmentExecutor(this.pluginManager.getConsole());
        } else {
            executor = new MultiPlayerPunishmentExecutor(this.pluginManager.getOfflinePlayer(executorName));
        }

        Action action = null;
        if (type == ActionType.ACTIVATE) {
            action = new MultiActivationAction(punId, id, executor, date, reason);
        } else if (type == ActionType.DEACTIVATE) {
            action = new MultiDeactivationAction(punId, id, executor, date, reason);
        }

        return action;
    }

    @Nullable
    public Punishment getPunishment(String id) throws SQLException {
        if (id == null) {
            return null;
        }

        try (Connection connection = this.source.getConnection();
            PreparedStatement statement = connection.prepareStatement(SQLQuery.HAS_PUNISHMENT.toString());
            ResultSet resultSet = statement.executeQuery()) {

            Punishment punishment = this.loadPunishment(resultSet);
            this.loadPunishmentActions(punishment);

            return punishment;
        }
    }

    @NotNull
    @Override
    public <T extends Punishment> List<T> getTargetHistory(UUID uuid) throws SQLException {
        List<T> history = new ArrayList<>();
        if (uuid != null) {
            try (Connection connection = this.source.getConnection();
                PreparedStatement statement = connection.prepareStatement(
                    SQLQuery.HISTORY_PUNISHMENTS_TARGET_UUID.toString())) {

                statement.setString(1, uuid.toString());

                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    Punishment punishment = this.loadPunishment(resultSet);
                    this.loadPunishmentActions(punishment);
                    history.add((T) punishment);

//                    String id = resultSet.getString("id");
//                    PunishmentType type = PunishmentType.valueOf(resultSet.getString("type"));
//                    String targetIp = resultSet.getString("target_ip");
//                    String targetName = resultSet.getString("target_name");
//                    UUID targetUUID = UUID.fromString(resultSet.getString("target_uuid"));
//                    String creatorName = resultSet.getString("creator_name");
//                    long dateCreated = resultSet.getLong("date_created");
//                    long dateStart = resultSet.getLong("date_start");
//                    long duration = resultSet.getLong("duration");
//                    String reason = resultSet.getString("reason");
//                    String comment = resultSet.getString("comment");
//                    String cancellationCreatorName = resultSet.getString("cancellation_creator");
//                    long cancellationDate = resultSet.getLong("cancellation_date");
//                    String cancellationReason = resultSet.getString("cancellation_reason");
//
//                    String[] serversString = resultSet.getString("servers").split(";");
//                    List<String> servers = new ArrayList<>(Arrays.stream(serversString).toList());
//
//                    boolean cancelled = resultSet.getBoolean("cancelled");
//
//                    PunishmentTarget target = new MultiPunishmentTarget(this.pluginManager.getOfflinePlayer(targetUUID));
//
//                    PunishmentExecutor creator = null;
//                    if (creatorName.equals(this.pluginManager.getConsole().getName())) {
//                        creator = new MultiConsolePunishmentExecutor(this.pluginManager.getConsole());
//                    } else {
//                        creator = new MultiPlayerPunishmentExecutor(this.pluginManager.getOfflinePlayer(creatorName));
//                    }
//
//                    PunishmentExecutor cancellationCreator = null;
//                    if (cancellationCreatorName != null) {
//                        if (cancellationCreatorName.equals(
//                            this.pluginManager.getConsole().getName())) {
//                            cancellationCreator = new MultiConsolePunishmentExecutor(
//                                this.pluginManager.getConsole());
//                        } else {
//                            Player cancellationPlayer = this.pluginManager.getOfflinePlayer(
//                                cancellationCreatorName);
//                            if (cancellationPlayer != null) {
//                                cancellationCreator = new MultiPlayerPunishmentExecutor(
//                                    cancellationPlayer);
//                            }
//                        }
//                    }
//
//                    T punishment = MultiPunishment.Companion.constructPunishment(
//                        this.pluginManager, type, id, target, creator, dateCreated, dateStart, duration, reason, comment,
//                        cancellationCreator, cancellationDate, cancellationReason, servers,
//                        cancelled);
//
//                    history.add(punishment);
                }
            }
        }

        return history;
    }

    @NotNull
    @Override
    public <T extends Punishment> List<T> getTargetHistory(String name) throws SQLException {
        List<T> history = new ArrayList<>();

        if (name != null) {
            try (Connection connection = this.source.getConnection();
                PreparedStatement statement = connection.prepareStatement(
                    SQLQuery.HISTORY_PUNISHMENTS_TARGET_NAME.toString())) {
                statement.setString(1, name);

                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    System.out.println(2);//
                    Punishment punishment = this.loadPunishment(resultSet);
                    this.loadPunishmentActions(punishment);
                    history.add((T) punishment);

//                    String id = resultSet.getString("id");
//                    PunishmentType type = PunishmentType.valueOf(resultSet.getString("type"));
//                    String targetIp = resultSet.getString("target_ip");
//                    String targetName = resultSet.getString("target_name");
//                    UUID targetUUID = UUID.fromString(resultSet.getString("target_uuid"));
//                    String creatorName = resultSet.getString("creator_name");
//                    long dateCreated = resultSet.getLong("date_created");
//                    long dateStart = resultSet.getLong("date_start");
//                    long duration = resultSet.getLong("duration");
//                    String reason = resultSet.getString("reason");
//                    String comment = resultSet.getString("comment");
//                    String cancellationCreatorName = resultSet.getString("cancellation_creator");
//                    long cancellationDate = resultSet.getLong("cancellation_date");
//                    String cancellationReason = resultSet.getString("cancellation_reason");
//
//                    String[] serversString = resultSet.getString("servers").split(";");
//                    List<String> servers = new ArrayList<>(Arrays.stream(serversString).toList());
//
//                    boolean cancelled = resultSet.getBoolean("cancelled");
//
//                    PunishmentTarget target = new MultiPunishmentTarget(this.pluginManager.getOfflinePlayer(targetUUID));
//
//                    PunishmentExecutor creator = null;
//                    if (creatorName.equals(this.pluginManager.getConsole().name)) {
//                        creator = new MultiConsolePunishmentExecutor(this.pluginManager.getConsole());
//                    } else {
//                        creator = new MultiPlayerPunishmentExecutor(this.pluginManager.getOfflinePlayer(creatorName));
//                    }
//
//                    PunishmentExecutor cancellationCreator = null;
//                    if (cancellationCreatorName != null) {
//                        if (cancellationCreatorName.equals(
//                            this.pluginManager.getConsole().name)) {
//                            cancellationCreator = new MultiConsolePunishmentExecutor(
//                                this.pluginManager.getConsole());
//                        } else {
//                            Player cancellationPlayer = this.pluginManager.getOfflinePlayer(
//                                cancellationCreatorName);
//                            if (cancellationPlayer != null) {
//                                cancellationCreator = new MultiPlayerPunishmentExecutor(
//                                    cancellationPlayer);
//                            }
//                        }
//                    }
//
//                    T punishment = MultiPunishment.Companion.constructPunishment(
//                        this.pluginManager, type, id, target, creator, dateCreated, dateStart, duration, reason, comment,
//                        cancellationCreator, cancellationDate, cancellationReason, servers,
//                        cancelled);
//
//                    history.add(punishment);
                }
            }
        }

        return history;
    }

    @NotNull
    @Override
    public <T extends Punishment> List<T> getCreatorHistory(String name) throws SQLException {
        List<T> history = new ArrayList<>();

        if (name != null) {
            try (Connection connection = this.source.getConnection();
                PreparedStatement statement = connection.prepareStatement(
                    SQLQuery.HISTORY_PUNISHMENTS_CREATOR.toString())) {

                statement.setString(1, name);

                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    Punishment punishment = this.loadPunishment(resultSet);
                    this.loadPunishmentActions(punishment);
                    history.add((T) punishment);
                }

//                while (resultSet.next()) {
//                    String id = resultSet.getString("id");
//                    PunishmentType type = PunishmentType.valueOf(resultSet.getString("type"));
//                    String targetIp = resultSet.getString("target_ip");
//                    String targetName = resultSet.getString("target_name");
//                    UUID targetUUID = UUID.fromString(resultSet.getString("target_uuid"));
//                    String creatorName = resultSet.getString("creator_name");
//                    long dateCreated = resultSet.getLong("date_created");
//                    long dateStart = resultSet.getLong("date_start");
//                    long duration = resultSet.getLong("duration");
//                    String reason = resultSet.getString("reason");
//                    String comment = resultSet.getString("comment");
//                    String cancellationCreatorName = resultSet.getString("cancellation_creator");
//                    long cancellationDate = resultSet.getLong("cancellation_date");
//                    String cancellationReason = resultSet.getString("cancellation_reason");
//
//                    String[] serversString = resultSet.getString("servers").split(";");
//                    List<String> servers = new ArrayList<>(Arrays.stream(serversString).toList());
//
//                    boolean cancelled = resultSet.getBoolean("cancelled");
//
//                    PunishmentTarget target = new MultiPunishmentTarget(this.pluginManager.getOfflinePlayer(targetUUID));
//
//                    PunishmentExecutor creator = null;
//                    if (creatorName.equals(this.pluginManager.getConsole().name)) {
//                        creator = new MultiConsolePunishmentExecutor(this.pluginManager.getConsole());
//                    } else {
//                        creator = new MultiPlayerPunishmentExecutor(this.pluginManager.getOfflinePlayer(creatorName));
//                    }
//
//                    PunishmentExecutor cancellationCreator = null;
//                    if (cancellationCreatorName != null) {
//                        if (cancellationCreatorName.equals(
//                            this.pluginManager.getConsole().name)) {
//                            cancellationCreator = new MultiConsolePunishmentExecutor(
//                                this.pluginManager.getConsole());
//                        } else {
//                            Player cancellationPlayer = this.pluginManager.getOfflinePlayer(
//                                cancellationCreatorName);
//                            if (cancellationPlayer != null) {
//                                cancellationCreator = new MultiPlayerPunishmentExecutor(
//                                    cancellationPlayer);
//                            }
//                        }
//                    }
//
//                    T punishment = MultiPunishment.Companion.constructPunishment(
//                        this.pluginManager, type, id, target, creator, dateCreated, dateStart,
//                        duration, reason, comment, cancellationCreator, cancellationDate,
//                        cancellationReason, servers, cancelled);
//
//                    history.add(punishment);
//                }
            }
        }

        return history;
    }

    private boolean hasAction(Action action) throws SQLException {
        try (Connection connection = this.source.getConnection();
            PreparedStatement statement = connection.prepareStatement(SQLQuery.HAS_ACTION.toString())) {
            statement.setString(1, action.getPunId());

            if (action instanceof DeactivationAction deactivation) {
                statement.setInt(2, deactivation.getId());
            } else if (action instanceof ActivationAction activation) {
                statement.setInt(2, activation.getId());
            } else {
                statement.setInt(2, 1);
            }

            statement.setString(3, action.getType().name());

            return statement.executeQuery().next();
        }
    }
}