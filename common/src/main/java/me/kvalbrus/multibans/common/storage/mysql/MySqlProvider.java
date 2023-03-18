package me.kvalbrus.multibans.common.storage.mysql;

import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLTimeoutException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import me.kvalbrus.multibans.common.managers.PluginManager;
import org.jetbrains.annotations.NotNull;
import me.kvalbrus.multibans.common.punishment.Punishment;
import me.kvalbrus.multibans.api.punishment.PunishmentType;
import me.kvalbrus.multibans.common.storage.DataProvider;
import me.kvalbrus.multibans.common.storage.StorageData;

public class MySqlProvider implements DataProvider {

    private final PluginManager pluginManager;

    private final StorageData data;

    private HikariDataSource source;

    public MySqlProvider(@NotNull PluginManager pluginManager,
                         @NotNull StorageData data) {
        this.pluginManager = pluginManager;
        this.data = data;
    }

    @Override
    public void connect() throws Exception {
        this.source = new HikariDataSource();

        this.source.setPoolName("TNBans-Pool");
        this.source.setJdbcUrl("jdbc:mysql://"
            + this.data.getAddress() + ":"
            + this.data.getPort() + "/"
            + this.data.getDatabaseName() + "?"
            + this.data.getProperties());
        this.source.setUsername(this.data.getUsername());
        this.source.setPassword(this.data.getPassword());
    }

    @Override
    public void disconnect() {
        if (this.source != null) {
            this.source.close();
        }
    }

    @Override
    public synchronized boolean createPunishmentsTable() {
        try (Connection connection = this.source.getConnection();
            PreparedStatement statement = connection.prepareStatement(
                SQLQuery.CREATE_TABLE_PUNISHMENTS.toString())) {
            statement.execute();
            return true;
        } catch (SQLException exception) {
            exception.printStackTrace();
            // TODO: logger
        }

        return false;
    }

    @Override
    public boolean deletePunishmentTable() {
        return false;
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
            statement.setLong(i++, punishment.getDateCreated());
            statement.setLong(i++, punishment.getDateStart());
            statement.setLong(i++, punishment.getDuration());
            statement.setString(i++, punishment.getReason());
            statement.setString(i++, punishment.getComment());
            statement.setString(i++, punishment.getCancellationCreator());
            statement.setLong(i++, punishment.getCancellationDate());
            statement.setString(i++, punishment.getCancellationReason());

            StringBuilder serversStringBuilder = new StringBuilder();
            for (int j = 0; j < punishment.getServers().size(); ++j) {
                if (j != punishment.getServers().size() - 1) {
                    serversStringBuilder.append(punishment.getServers().get(j)).append(";");
                } else {
                    serversStringBuilder.append(punishment.getServers().get(j));
                }
            }

            statement.setString(i++, serversStringBuilder.toString());
            statement.setBoolean(i++, punishment.isCancelled());

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

            statement.setLong(i++, punishment.getDateStart());
            statement.setLong(i++, punishment.getDuration());
            statement.setString(i++, punishment.getReason());
            statement.setString(i++, punishment.getComment());
            statement.setString(i++, punishment.getCancellationCreator());
            statement.setLong(i++, punishment.getCancellationDate());
            statement.setString(i++, punishment.getCancellationReason());

            StringBuilder serversStringBuilder = new StringBuilder();
            for (int j = 0; j < punishment.getServers().size(); ++j) {
                if (j != punishment.getServers().size() - 1) {
                    serversStringBuilder.append(punishment.getServers().get(j)).append(";");
                } else {
                    serversStringBuilder.append(punishment.getServers().get(j));
                }
            }

            statement.setString(i++, serversStringBuilder.toString());
            statement.setBoolean(i++, punishment.isCancelled());

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

    @NotNull
    @Override
    public List<Punishment> getTargetHistory(String target) {
        List<Punishment> history = new ArrayList<>();

        if (target != null) {
            try (Connection connection = this.source.getConnection();
                PreparedStatement statement = connection.prepareStatement(
                    SQLQuery.HISTORY_PUNISHMENTS_TARGET_NAME.toString())) {

                statement.setString(1, target);

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

                    Punishment punishment = new Punishment(
                        this.pluginManager.getPunishmentManager(), type, id, targetIp, targetName, targetUUID,
                        creatorName, dateCreated, dateStart, duration, reason, comment, cancellationCreator,
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

    @NotNull
    @Override
    public List<Punishment> getTargetHistory(UUID uuid) {
        List<Punishment> history = new ArrayList<>();

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

                    Punishment punishment = new Punishment(
                        this.pluginManager.getPunishmentManager(), type, id, targetIp, targetName, targetUUID,
                        creatorName, dateCreated, dateStart, duration, reason, comment, cancellationCreator,
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

    @NotNull
    @Override
    public List<Punishment> getCreatorHistory(String creator) {
        List<Punishment> history = new ArrayList<>();

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

                    Punishment punishment = new Punishment(
                        this.pluginManager.getPunishmentManager(), type, id, targetIp, targetName, targetUUID,
                        creatorName, dateCreated, dateStart, duration, reason, comment, cancellationCreator,
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