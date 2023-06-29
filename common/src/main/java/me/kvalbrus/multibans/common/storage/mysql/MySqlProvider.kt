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
import me.kvalbrus.multibans.common.punishment.action.MultiCreationAction
import me.kvalbrus.multibans.common.punishment.action.MultiDeactivationAction
import me.kvalbrus.multibans.common.punishment.creator.MultiOnlinePunishmentExecutor
import me.kvalbrus.multibans.common.punishment.creator.MultiPunishmentExecutor
import me.kvalbrus.multibans.common.session.MultiSession
import me.kvalbrus.multibans.common.storage.DataProviderSettings
import me.kvalbrus.multibans.common.storage.DataProviderType
import java.sql.Connection
import java.sql.ResultSet
import java.sql.SQLException
import java.util.*
import kotlin.collections.ArrayList

class MySqlProvider(private val pluginManager: PluginManager, private val dataProviderSettings: DataProviderSettings) : DataProvider {

    private var source: HikariDataSource? = null
    override val name: String = "MySQL"

    @Throws(SQLException::class)
    override fun initialization() {
        if (dataProviderSettings.type == DataProviderType.MYSQL) {
            this.source = HikariDataSource()
            val properties = dataProviderSettings.properties
            this.source!!.poolName = "multibans-hikari"
            this.source!!.jdbcUrl = "jdbc:mysql://" +
                    properties.getProperty("dataSource.serverName") + ":" +
                    properties.getProperty("dataSource.portNumber") + "/" + properties.getProperty("dataSource.databaseName")
            this.source!!.username = properties.getProperty("dataSource.user")
            this.source!!.password = properties.getProperty("dataSource.password")
        } else if (dataProviderSettings.type == DataProviderType.MARIADB) {
            val properties = dataProviderSettings.properties
            val config = HikariConfig()
            for (property in this.dataProviderSettings.properties.entries) {
                config.addDataSourceProperty(property.key.toString(), property.value)
            }

            config.poolName = "multibans-hikari"
            config.driverClassName = "org.mariadb.jdbc.Driver"
            config.jdbcUrl = "jdbc:mariadb://" + properties.getProperty("dataSource.serverName") +
                    ":" + properties.getProperty("dataSource.portNumber") + "/" +
                    properties.getProperty("dataSource.databaseName")
            config.username = properties.getProperty("dataSource.user")
            config.password = properties.getProperty("dataSource.password")

            this.source = HikariDataSource(config)
        } else {
            this.source = HikariDataSource(HikariConfig(dataProviderSettings.properties))
        }


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
    override fun getPunishment(id: String): Punishment? = source!!.connection.use { loadPunishment(it, id) }
    override fun getAllPunishments(): List<Punishment> {
        val punishments = mutableListOf<Punishment>()
        this.source!!.connection.use { connection ->
            connection.prepareStatement(SQLQuery.GET_ALL_PUNISHMENTS).use { statement ->
                statement.executeQuery().use { resultSet ->
                    while(resultSet.next()) {
                        val punishment = readPunishment(connection, resultSet);
                        punishments.add(punishment)
                    }
                }
            }
        }

        return punishments
    }

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
                connection.prepareStatement(SQLQuery.GET_PUNISHMENTS_BY_TARGET_NAME).use { statement ->
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

    override fun getTargetHistoryByIp(ip: String): List<Punishment> {
        val history: MutableList<Punishment> = ArrayList()
        source!!.connection.use { connection ->
            connection.prepareStatement(SQLQuery.GET_PUNISHMENTS_BY_TARGET_IP).use { statement ->
                statement.setString(1, ip)
                val resultSet = statement.executeQuery()
                while (resultSet.next()) {
                    val punishment = readPunishment(connection, resultSet)
                    history.add(punishment)
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

    override fun getSessionHistory(uuid: UUID): List<Session> {
        val history = mutableListOf<Session>()
        this.source!!.connection.use { connection ->
            connection.prepareStatement(SQLQuery.GET_SESSION_HISTORY_BY_UUID).use { statement ->
                statement.setString(1, uuid.toString())

                statement.executeQuery().use { resultSet ->
                    while(resultSet.next()) {
                        val session = loadSession(resultSet)
                        history.add(session)
                    }
                }
            }
        }

        return history
    }

    override fun getSessionHistory(name: String): List<Session> {
        val history = mutableListOf<Session>()
        this.source!!.connection.use { connection ->
            connection.prepareStatement(SQLQuery.GET_SESSION_HISTORY_BY_NAME).use { statement ->
                statement.setString(1, name)

                statement.executeQuery().use { resultSet ->
                    while(resultSet.next()) {
                        val session = loadSession(resultSet)
                        history.add(session)
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
            statement.setString(3, punishment.targetUUID)
            statement.setString(4, punishment.targetIp)
            statement.setString(5, punishment.targetName)
            statement.setString(6, punishment.creatorUUID)
            statement.setString(7, punishment.creatorName)
            statement.setLong(8, punishment.createDate)
            statement.setLong(9, punishment.startDate)
            statement.setLong(10, punishment.duration)
            statement.setString(11, punishment.reason)
            statement.setString(12, punishment.comment)
            statement.setString(13, punishment.servers)
            statement.setBoolean(14, punishment.cancelled)

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
                statement.setString(4, action.executor.uniqueId?.toString())
                statement.setString(5, action.executor.name)
                statement.setLong(6, action.date)
                statement.setString(7, action.reason)

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
    private fun loadAction(resultSet: ResultSet): Action {
        val punId = resultSet.getString("pun_id")
        val id = resultSet.getInt("id")
        val type = ActionType.valueOf(resultSet.getString("type"))
        val executorUUID = UUID.fromString(resultSet.getString("executor_uuid"))
        val executorName = resultSet.getString("executor_name")
        val date = resultSet.getLong("date")
        val reason = resultSet.getString("reason")

        val executor: PunishmentExecutor

        if (executorName.equals(this.pluginManager.console.name) && executorUUID == null) {
            executor = MultiOnlinePunishmentExecutor(pluginManager.console)
        } else {
            if (this.pluginManager.getPlayer(executorName) != null) {
                executor = MultiOnlinePunishmentExecutor(this.pluginManager.getPlayer(executorUUID))
            } else {
                executor = MultiPunishmentExecutor(executorUUID, executorName)
            }
        }

        val action: Action
        when(type) {
            ActionType.ACTIVATE -> action = MultiActivationAction(punId, id, executor, date, reason)
            ActionType.DEACTIVATE -> action = MultiDeactivationAction(punId, id, executor, date, reason)
            ActionType.CREATE -> { TODO("Not yet implemented") }
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
                    actions.add(action)
                }
            }
        }

        return actions
    }

    @Throws(SQLException::class)
    private fun loadSession(resultSet: ResultSet): Session {
        val uuid = UUID.fromString(resultSet.getString(1))
        val name = resultSet.getString(2)
        val ip = resultSet.getString(3)
        val join = resultSet.getLong(4)
        val quit = resultSet.getLong(5)

        return MultiSession(uuid, name, ip, join, quit)
    }

    @Throws(SQLException::class)
    private fun loadPunishment(connection: Connection, id: String) : Punishment? {
        connection.prepareStatement(SQLQuery.GET_PUNISHMENT).use { statement ->
            statement.setString(1, id)
            statement.executeQuery().use { resultSet ->
                if (resultSet.next()) {
                    return readPunishment(connection, resultSet)
                } else {
                    return null
                }
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
        val creatorUUID = resultSet.getString("creator_uuid")
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

        return SQLPunishment(id, type, targetUUID, targetIp, targetName, creatorUUID, creatorName,
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