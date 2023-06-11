package me.kvalbrus.multibans.common.storage.mysql

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import me.kvalbrus.multibans.api.DataProvider
import me.kvalbrus.multibans.api.Session
import me.kvalbrus.multibans.api.punishment.Punishment
import me.kvalbrus.multibans.api.punishment.action.*
import me.kvalbrus.multibans.api.punishment.executor.PunishmentExecutor
import me.kvalbrus.multibans.common.managers.PluginManager
import me.kvalbrus.multibans.common.punishment.action.MultiActivationAction
import me.kvalbrus.multibans.common.punishment.action.MultiDeactivationAction
import me.kvalbrus.multibans.common.punishment.creator.MultiConsolePunishmentExecutor
import me.kvalbrus.multibans.common.punishment.creator.MultiPlayerPunishmentExecutor
import me.kvalbrus.multibans.common.storage.DataProviderSettings
import me.kvalbrus.multibans.common.storage.DataProviderType
import java.sql.Connection
import java.sql.ResultSet
import java.sql.SQLException
import java.util.*

class MySqlProvider(private val pluginManager: PluginManager, private val dataProviderSettings: DataProviderSettings) : DataProvider {
    private var source: HikariDataSource? = null
    override val name: String
        get() = "MySQL"

    @Throws(SQLException::class)
    override fun initialization() {
        if (dataProviderSettings.type == DataProviderType.MY_SQL) {
            source = HikariDataSource()
            val properties = dataProviderSettings.properties
            source!!.jdbcUrl = "jdbc:mysql://" +
                    properties.getProperty("dataSource.serverName") + ":" +
                    properties.getProperty("dataSource.portNumber") + "/" + properties.getProperty("dataSource.databaseName")
            source!!.username = properties.getProperty("dataSource.user")
            source!!.password = properties.getProperty("dataSource.password")
        } else {
            source = HikariDataSource(HikariConfig(dataProviderSettings.properties))
        }

        source!!.poolName = "MultiBans-Pool"

        createTables()
    }

    override fun shutdown() {
        if (this.source != null) {
            this.source!!.close()
        }
    }

    @Throws(SQLException::class)
    override fun wipe() {
        this.source!!.connection.use { connection ->
            connection.createStatement().use { statement ->
                statement.addBatch(SQLQuery.WIPE_TABLE_PUNISHMENTS)
                statement.addBatch(SQLQuery.WIPE_TABLE_ACTIONS)
                statement.addBatch(SQLQuery.WIPE_TABLE_PLAYER_SESSIONS)

                statement.executeBatch()
            }
        }
    }

    // <-----Punishment----->

    @Throws(SQLException::class)
    override fun createPunishment(punishment: Punishment): Boolean {
        source!!.connection.use { connection ->
            val sqlPunishment = SQLPunishment(punishment)
            insertPunishment(connection, sqlPunishment)
            insertPunishmentActions(connection, sqlPunishment)
        }


        return true
    }

    @Throws(SQLException::class)
    override fun createPlayerSession(session: Session) {
        this.source!!.connection.use { connection ->
            insertPlayerSession(connection, session)
        }
    }

    @Throws(SQLException::class)
    override fun deletePunishment(punishment: Punishment) {
        source!!.connection.use { connection ->
            removePunishment(connection, punishment.id)
        }
    }

    @Throws(SQLException::class)
    override fun getPunishment(id: String): Punishment = source!!.connection.use { loadPunishment(it, id) }

    @Throws(SQLException::class)
    override fun hasPunishment(id: String): Boolean {
        source!!.connection.use { connection ->
            connection.prepareStatement(SQLQuery.GET_PUNISHMENT).use { statement ->
                statement.setString(1, id)
                return statement.executeQuery().next()
            }
        }
    }

    @Throws(SQLException::class)
    override fun savePunishment(punishment: Punishment): Boolean {
        source!!.connection.use { connection ->
            val sqlPunishment = SQLPunishment(punishment)

            updatePunishment(connection, sqlPunishment)
            insertPunishmentActions(connection, sqlPunishment)
        }

        return true
    }

    // <-----History----->

    @Throws(SQLException::class)
    override fun <T : Punishment?> getTargetHistory(uuid: UUID?): List<T> {
        val history: MutableList<T> = ArrayList()
        if (uuid != null) {
            source!!.connection.use { connection ->
                connection.prepareStatement(
                    SQLQuery.GET_PUNISHMENTS_BY_TARGET_UUID
                ).use { statement ->
                    statement.setString(1, uuid.toString())
                    val resultSet = statement.executeQuery()
                    while (resultSet.next()) {
                        val punishment = readPunishment(connection, resultSet)
                        history.add(punishment as T)
                    }
                }
            }
        }

        return history
    }

    @Throws(SQLException::class)
    override fun <T : Punishment?> getTargetHistory(name: String?): List<T> {
        val history: MutableList<T> = ArrayList()
        if (name != null) {
            source!!.connection.use { connection ->
                connection.prepareStatement(SQLQuery.GET_PUNISHMENTS_BY_TARGET_NAME)
                    .use { statement ->
                        statement.setString(1, name)
                        val resultSet = statement.executeQuery()
                        while (resultSet.next()) {
                            val punishment = readPunishment(connection, resultSet)
                            history.add(punishment as T)
                        }
                    }
            }
        }

        return history
    }

    @Throws(SQLException::class)
    override fun <T : Punishment?> getCreatorHistory(name: String?): List<T> {
        val history: MutableList<T> = ArrayList()
        if (name != null) {
            source!!.connection.use { connection ->
                connection.prepareStatement(
                    SQLQuery.GET_PUNISHMENTS_BY_CREATOR_NAME
                ).use { statement ->
                    statement.setString(1, name)
                    val resultSet = statement.executeQuery()
                    while (resultSet.next()) {
                        val punishment = readPunishment(connection, resultSet)
                        history.add(punishment as T)
                    }
                }
            }
        }

        return history
    }

    // <-----Private----->

    @Throws(SQLException::class)
    private fun createTables() {
        this.source!!.connection.use { connection ->
            connection.createStatement().use { statement ->
                statement.addBatch(SQLQuery.CREATE_TABLE_PUNISHMENTS)
                statement.addBatch(SQLQuery.CREATE_TABLE_ACTIONS)
                statement.addBatch(SQLQuery.CREATE_TABLE_PLAYER_SECTIONS)

                statement.executeBatch()
            }
        }
    }

    @Throws(SQLException::class)
    private fun insertPlayerSession(connection: Connection, session: Session) {
        connection.prepareStatement(SQLQuery.CREATE_PLAYER_SESSION).use { statement ->
            statement.setString(1, session.playerUUID.toString())
            statement.setString(2, session.playerName)
            statement.setString(3, session.playerIp)
            statement.setLong(4, session.joinTime)
            statement.setLong(5, session.quitTime)

            statement.executeUpdate()
        }
    }

    @Throws(SQLException::class)
    private fun insertPunishment(connection: Connection, punishment: SQLPunishment) {
        connection.prepareStatement(SQLQuery.CREATE_PUNISHMENT).use { statement ->
            statement.setString(1, punishment.id)
            statement.setString(2, punishment.type)
            statement.setString(3, punishment.targetUuid)
            statement.setString(4, punishment.targetIp)
            statement.setString(5, punishment.targetName)
            statement.setString(6, punishment.creatorName)
            statement.setLong(7, punishment.createDate)
            statement.setLong(8, punishment.startDate)
            statement.setLong(9, punishment.duration)
            statement.setString(10, punishment.reason)
            statement.setString(11, punishment.comment)
            statement.setString(12, punishment.servers)
            statement.setBoolean(13, punishment.cancelled)

            statement.executeUpdate()
        }
    }

    @Throws(SQLException::class)
    private fun insertPunishmentActions(connection: Connection, punishment: SQLPunishment) {
        for (action in punishment.activations) {
            insertPunishmentAction(connection, action)
        }

        for (action in punishment.deactivations) {
            insertPunishmentAction(connection, action)
        }
    }

    @Throws(SQLException::class)
    private fun insertPunishmentAction(connection: Connection, action: Action) {
        if (!hasAction(action)) {
            connection.prepareStatement(SQLQuery.CREATE_ACTION).use { statement ->
                statement.setString(1, action.punId)
                statement.setInt(2, action.id)
                statement.setString(3, action.type.name)
                statement.setString(4, action.executor.name)
                statement.setLong(5, action.date)
                statement.setString(6, action.reason)

                statement.executeUpdate()
            }
        }
    }

    @Throws(SQLException::class)
    private fun hasAction(action: Action): Boolean {
        source!!.connection.use { connection ->
            connection.prepareStatement(SQLQuery.GET_PUNISHMENT_ACTION).use { statement ->
                val actionId: Int
                when (action) {
                    is DeactivationAction -> actionId = action.id
                    is ActivationAction -> actionId = action.id
                    else -> actionId = 1
                }

                statement.setString(1, action.punId)
                statement.setInt(2, actionId)
                statement.setString(3, action.type.name)

                return statement.executeQuery().next()
            }
        }
    }

    @Throws(SQLException::class)
    private fun loadAction(resultSet: ResultSet): Action? {
        val punId = resultSet.getString("pun_id")
        val id = resultSet.getInt("id")
        val type = ActionType.valueOf(resultSet.getString("type"))
        val executorName = resultSet.getString("executor")
        val date = resultSet.getLong("date")
        val reason = resultSet.getString("reason")

        val executor: PunishmentExecutor

        if (executorName == pluginManager.console.name) {
            executor = MultiConsolePunishmentExecutor(pluginManager.console)
        } else {
            executor = MultiPlayerPunishmentExecutor(pluginManager.getOfflinePlayer(executorName))
        }

        var action: Action? = null
        if (type === ActionType.ACTIVATE) {
            action = MultiActivationAction(punId, id, executor, date, reason)
        } else if (type === ActionType.DEACTIVATE) {
            action = MultiDeactivationAction(punId, id, executor, date, reason)
        }

        return action
    }

    @Throws(SQLException::class)
    private fun loadActionsByType(connection: Connection, id: String, type: ActionType) : List<Action> {
        val actions = mutableListOf<Action>()

        connection.prepareStatement(SQLQuery.GET_PUNISHMENT_ACTION_BY_TYPE).use { statement ->
            statement.setString(1, id)
            statement.setString(2, type.toString())

            statement.executeQuery().use { resultSet ->
                while (resultSet.next()) {
                    val action = loadAction(resultSet)
                    if (action != null) {
                        actions.add(action)
                    }
                }
            }
        }

        return actions
    }

    @Throws(SQLException::class)
    private fun loadPunishment(connection: Connection, id: String) : Punishment {
        connection.prepareStatement(SQLQuery.GET_PUNISHMENT).use { statement ->
            statement.setString(1, id)
            statement.executeQuery().use { resultSet ->
                return readPunishment(connection, resultSet)
            }
        }
    }

    @Throws(SQLException::class)
    private fun readPunishment(connection: Connection, resultSet: ResultSet) : Punishment {
        val id = resultSet.getString("id")
        val type = resultSet.getString("type")
        val targetUUID = resultSet.getString("target_uuid")
        val targetIp = resultSet.getString("target_ip")
        val targetName = resultSet.getString("target_name")
        val creatorName = resultSet.getString("creator_name")
        val createdDate = resultSet.getLong("create_date")
        val startDate = resultSet.getLong("start_date")
        val duration = resultSet.getLong("duration")
        val reason = resultSet.getString("reason")
        val comment = resultSet.getString("comment")
        val servers = resultSet.getString("servers")
        val cancelled = resultSet.getBoolean("cancelled")
        val activations = loadActionsByType(connection, id, ActionType.ACTIVATE)
        val deactivations = loadActionsByType(connection, id, ActionType.DEACTIVATE)

        return SQLPunishment(id, type, targetUUID, targetIp, targetName, creatorName,
            createdDate, startDate, duration, reason, comment, servers, cancelled,
            activations, deactivations).getPunishment(this.pluginManager)
    }

    @Throws(SQLException::class)
    private fun updatePunishment(connection: Connection, punishment: SQLPunishment) {
        connection.prepareStatement(SQLQuery.UPDATE_PUNISHMENT).use { statement ->
            statement.setLong(1, punishment.startDate)
            statement.setLong(2, punishment.duration)
            statement.setString(3, punishment.reason)
            statement.setString(4, punishment.comment)
            statement.setString(5, punishment.servers)
            statement.setBoolean(6, punishment.cancelled)
            statement.setString(7, punishment.id)

            statement.executeUpdate()
        }
    }

    @Throws(SQLException::class)
    private fun removePunishment(connection: Connection, id: String) {
        connection.prepareStatement(SQLQuery.DELETE_PUNISHMENT).use { statement ->
            statement.setString(1, id)
            statement.executeUpdate()
        }

        connection.prepareStatement(SQLQuery.DELETE_ACTIONS).use { statement ->
            statement.setString(1, id)
            statement.executeUpdate()
        }
    }
}