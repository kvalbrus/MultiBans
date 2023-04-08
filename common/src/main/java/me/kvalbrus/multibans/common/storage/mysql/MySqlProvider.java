package me.kvalbrus.multibans.common.storage.mysql;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLSyntaxErrorException;
import java.sql.SQLTimeoutException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.UUID;
import me.kvalbrus.multibans.api.punishment.Cancelable;
import me.kvalbrus.multibans.api.punishment.Punishment;
import me.kvalbrus.multibans.api.punishment.PunishmentType;
import me.kvalbrus.multibans.api.punishment.TemporaryPunishment;
import me.kvalbrus.multibans.common.managers.PluginManager;
import me.kvalbrus.multibans.common.punishment.MultiPunishment;
import me.kvalbrus.multibans.common.punishment.MultiTemporaryPunishment;
import me.kvalbrus.multibans.api.DataProvider;
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
            PreparedStatement statement = connection.prepareStatement(
                SQLQuery.CREATE_TABLE_PUNISHMENTS.toString())) {
            statement.execute();
        } catch (SQLSyntaxErrorException exception) {
            exception.printStackTrace();//
        }
    }

    @Override
    public void shutdown() {
        if (this.source != null) {
            this.source.close();
        }
    }

    @Override
    public void wipe() {

    }

    @Override
    public synchronized boolean createPunishment(@NotNull Punishment punishment) {
        try (Connection connection = this.source.getConnection();
            PreparedStatement statement = connection.prepareStatement(
                SQLQuery.CREATE_PUNISHMENT.toString())) {
            int i = 1;
            statement.setString(i++, punishment.getId());
            statement.setString(i++, punishment.getType().getPrefix());
            statement.setString(i++, punishment.getTargetIp());
            statement.setString(i++, punishment.getTargetName());
            statement.setString(i++, punishment.getTargetUniqueId().toString());
            statement.setString(i++, punishment.getCreatorName());
            statement.setLong(i++, punishment.getCreatedDate());
            statement.setLong(i++,
                punishment instanceof TemporaryPunishment temporary ? temporary.getStartedDate() :
                    punishment.getCreatedDate());
            statement.setLong(i++,
                punishment instanceof MultiTemporaryPunishment temporary ? temporary.getDuration() :
                    -1L);
            statement.setString(i++, punishment.getCreatedReason());
            statement.setString(i++, punishment.getComment());
            statement.setString(i++, punishment instanceof Cancelable ?
                ((Cancelable) punishment).getCancellationCreator() : null);
            statement.setLong(i++,
                punishment instanceof Cancelable ? ((Cancelable) punishment).getCancellationDate() :
                    -1);
            statement.setString(i++, punishment instanceof Cancelable ?
                ((Cancelable) punishment).getCancellationReason() : null);

            StringBuilder serversStringBuilder = new StringBuilder();
            for (int j = 0; j < punishment.getServers().size(); ++j) {
                if (j != punishment.getServers().size() - 1) {
                    serversStringBuilder.append(punishment.getServers().get(j)).append(";");
                } else {
                    serversStringBuilder.append(punishment.getServers().get(j));
                }
            }

            statement.setString(i++, serversStringBuilder.toString());
            statement.setBoolean(i++,
                punishment instanceof Cancelable ? ((Cancelable) punishment).isCancelled() : false);

            return statement.executeUpdate() > 0;
        } catch (SQLTimeoutException exception) {
            // TODO: logger
        } catch (SQLException exception) {
            // TODO: logger
        }

        return false;
    }

    @Override
    public synchronized boolean updatePunishment(@NotNull Punishment punishment) {
        try (Connection connection = this.source.getConnection();
            PreparedStatement statement = connection.prepareStatement(
                SQLQuery.UPDATE_PUNISHMENT.toString())) {
            int i = 1;

            statement.setLong(i++,
                punishment instanceof TemporaryPunishment temporary ?
                    temporary.getStartedDate() : punishment.getCreatedDate());
            statement.setLong(i++, punishment instanceof MultiTemporaryPunishment temporary ?
                temporary.getDuration() : -1L);
            statement.setString(i++, punishment.getCreatedReason());
            statement.setString(i++, punishment.getComment());
            statement.setString(i++, punishment instanceof Cancelable cancelable ?
                cancelable.getCancellationCreator() : null);
            statement.setLong(i++, punishment instanceof Cancelable cancelable ?
                cancelable.getCancellationDate() : -1);
            statement.setString(i++, punishment instanceof Cancelable cancelable ?
                cancelable.getCancellationCreator() : null);

            StringBuilder serversStringBuilder = new StringBuilder();
            for (int j = 0; j < punishment.getServers().size(); ++j) {
                if (j != punishment.getServers().size() - 1) {
                    serversStringBuilder.append(punishment.getServers().get(j)).append(";");
                } else {
                    serversStringBuilder.append(punishment.getServers().get(j));
                }
            }

            statement.setString(i++, serversStringBuilder.toString());
            statement.setBoolean(i++, punishment instanceof Cancelable cancelable ?
                cancelable.isCancelled() : false);

            statement.setString(i++, punishment.getId());

            return statement.executeUpdate() > 0;
        } catch (SQLTimeoutException exception) {
            // TODO: logger
        } catch (SQLException exception) {
            // TODO: logger
        }

        return false;
    }

    @Override
    public synchronized boolean deletePunishment(@NotNull Punishment punishment) {
        try (Connection connection = this.source.getConnection();
            PreparedStatement statement = connection.prepareStatement(
                SQLQuery.DELETE_PUNISHMENT.toString())) {
            statement.setString(1, punishment.getId());

            return statement.executeUpdate() > 0;
        } catch (SQLTimeoutException exception) {
            // TODO: logger
        } catch (SQLException exception) {
            // TODO: logger
        }

        return false;
    }

    @Override
    public boolean hasPunishment(String id) {
        if (id == null) {
            return false;
        }

        try (Connection connection = this.source.getConnection();
            PreparedStatement statement = connection.prepareStatement(
                SQLQuery.HAS_PUNISHMENT.toString())) {
            statement.setString(1, id);
            return statement.executeQuery().next();
        } catch (SQLTimeoutException exception) {
            // TODO: logger
        } catch (SQLException exception) {
            // TODO: logger
        }

        return false;
    }

    @Nullable
    public Punishment getPunishment(String id) {
        if (id == null) {
            return null;
        }

        try (Connection connection = this.source.getConnection();
            PreparedStatement statement = connection.prepareStatement(
                SQLQuery.HAS_PUNISHMENT.toString())) {
            statement.setString(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                PunishmentType type = PunishmentType.valueOf(resultSet.getString("type"));
                String targetIp = resultSet.getString("target_ip");
                String targetName = resultSet.getString("target_name");
                UUID targetUUID = UUID.fromString(resultSet.getString("target_uuid"));
                String creatorName = resultSet.getString("creator_name");
                long dateCreated = resultSet.getLong("date_created");
                long dateStart = resultSet.getLong("date_start");
                long duration = resultSet.getLong("duration");
                String reason = resultSet.getString("reason");
                String comment = resultSet.getString("comment");
                String cancellationCreator = resultSet.getString("cancellation_creator");
                long cancellationDate = resultSet.getLong("cancellation_date");
                String cancellationReason = resultSet.getString("cancellation_reason");

                String[] serversString = resultSet.getString("servers").split(";");
                List<String> servers = new ArrayList<>(Arrays.stream(serversString).toList());

                boolean cancelled = resultSet.getBoolean("cancelled");

                Punishment punishment = MultiPunishment.constructPunishment(
                    this.pluginManager, type, id, targetIp, targetName,
                    targetUUID,
                    creatorName, dateCreated, dateStart, duration, reason, comment,
                    cancellationCreator,
                    cancellationDate, cancellationReason, servers, cancelled);

                return punishment;
            }
        } catch (SQLTimeoutException exception) {
            // TODO: logger
        } catch (SQLException exception) {
            // TODO: logger
        }

        return null;
    }

    @NotNull
    @Override
    public <T extends Punishment> List<T> getTargetHistory(UUID uuid) {
        List<T> history = new ArrayList<>();

        if (uuid != null) {
            try (Connection connection = this.source.getConnection();
                PreparedStatement statement = connection.prepareStatement(
                    SQLQuery.HISTORY_PUNISHMENTS_TARGET_UUID.toString())) {

                statement.setString(1, uuid.toString());

                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    String id = resultSet.getString("id");
                    PunishmentType type = PunishmentType.valueOf(resultSet.getString("type"));
                    String targetIp = resultSet.getString("target_ip");
                    String targetName = resultSet.getString("target_name");
                    UUID targetUUID = UUID.fromString(resultSet.getString("target_uuid"));
                    String creatorName = resultSet.getString("creator_name");
                    long dateCreated = resultSet.getLong("date_created");
                    long dateStart = resultSet.getLong("date_start");
                    long duration = resultSet.getLong("duration");
                    String reason = resultSet.getString("reason");
                    String comment = resultSet.getString("comment");
                    String cancellationCreator = resultSet.getString("cancellation_creator");
                    long cancellationDate = resultSet.getLong("cancellation_date");
                    String cancellationReason = resultSet.getString("cancellation_reason");

                    String[] serversString = resultSet.getString("servers").split(";");
                    List<String> servers = new ArrayList<>(Arrays.stream(serversString).toList());

                    boolean cancelled = resultSet.getBoolean("cancelled");

                    T punishment = MultiPunishment.constructPunishment(
                        this.pluginManager, type, id, targetIp, targetName,
                        targetUUID, creatorName, dateCreated, dateStart, duration, reason, comment,
                        cancellationCreator, cancellationDate, cancellationReason, servers,
                        cancelled);

                    history.add(punishment);
                }
            } catch (SQLTimeoutException exception) {
                // TODO: logger
            } catch (SQLException exception) {
                // TODO: logger
            }
        }

        return history;
    }

    @NotNull
    @Override
    public <T extends Punishment> List<T> getTargetHistory(String name) {
        List<T> history = new ArrayList<>();

        if (name != null) {
            try (Connection connection = this.source.getConnection();
                PreparedStatement statement = connection.prepareStatement(
                    SQLQuery.HISTORY_PUNISHMENTS_TARGET_NAME.toString())) {

                statement.setString(1, name.toString());

                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    String id = resultSet.getString("id");
                    PunishmentType type = PunishmentType.valueOf(resultSet.getString("type"));
                    String targetIp = resultSet.getString("target_ip");
                    String targetName = resultSet.getString("target_name");
                    UUID targetUUID = UUID.fromString(resultSet.getString("target_uuid"));
                    String creatorName = resultSet.getString("creator_name");
                    long dateCreated = resultSet.getLong("date_created");
                    long dateStart = resultSet.getLong("date_start");
                    long duration = resultSet.getLong("duration");
                    String reason = resultSet.getString("reason");
                    String comment = resultSet.getString("comment");
                    String cancellationCreator = resultSet.getString("cancellation_creator");
                    long cancellationDate = resultSet.getLong("cancellation_date");
                    String cancellationReason = resultSet.getString("cancellation_reason");

                    String[] serversString = resultSet.getString("servers").split(";");
                    List<String> servers = new ArrayList<>(Arrays.stream(serversString).toList());

                    boolean cancelled = resultSet.getBoolean("cancelled");

                    T punishment = MultiPunishment.constructPunishment(
                        this.pluginManager, type, id, targetIp, targetName,
                        targetUUID, creatorName, dateCreated, dateStart, duration, reason, comment,
                        cancellationCreator, cancellationDate, cancellationReason, servers,
                        cancelled);

                    history.add(punishment);
                }
            } catch (SQLTimeoutException exception) {
                // TODO: logger
            } catch (SQLException exception) {
                // TODO: logger
            }
        }

        return history;
    }

    @NotNull
    @Override
    public <T extends Punishment> List<T> getCreatorHistory(String creator) {
        List<T> history = new ArrayList<>();

        if (creator != null) {
            try (Connection connection = this.source.getConnection();
                PreparedStatement statement = connection.prepareStatement(
                    SQLQuery.HISTORY_PUNISHMENTS_CREATOR.toString())) {

                statement.setString(1, creator);

                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    String id = resultSet.getString("id");
                    PunishmentType type = PunishmentType.valueOf(resultSet.getString("type"));
                    String targetIp = resultSet.getString("target_ip");
                    String targetName = resultSet.getString("target_name");
                    UUID targetUUID = UUID.fromString(resultSet.getString("target_uuid"));
                    String creatorName = resultSet.getString("creator_name");
                    long dateCreated = resultSet.getLong("date_created");
                    long dateStart = resultSet.getLong("date_start");
                    long duration = resultSet.getLong("duration");
                    String reason = resultSet.getString("reason");
                    String comment = resultSet.getString("comment");
                    String cancellationCreator = resultSet.getString("cancellation_creator");
                    long cancellationDate = resultSet.getLong("cancellation_date");
                    String cancellationReason = resultSet.getString("cancellation_reason");

                    String[] serversString = resultSet.getString("servers").split(";");
                    List<String> servers = new ArrayList<>(Arrays.stream(serversString).toList());

                    boolean cancelled = resultSet.getBoolean("cancelled");

                    T punishment = MultiPunishment.constructPunishment(
                        this.pluginManager, type, id, targetIp, targetName,
                        targetUUID,
                        creatorName, dateCreated, dateStart, duration, reason, comment,
                        cancellationCreator,
                        cancellationDate, cancellationReason, servers, cancelled);

                    history.add(punishment);
                }
            } catch (SQLTimeoutException exception) {
                // TODO: logger
            } catch (SQLException exception) {
                // TODO: logger
            }
        }

        return history;
    }
}