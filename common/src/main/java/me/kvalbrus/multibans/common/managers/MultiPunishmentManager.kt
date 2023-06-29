package me.kvalbrus.multibans.common.managers

import me.kvalbrus.multibans.api.OnlinePlayer
import me.kvalbrus.multibans.api.managers.PunishmentManager
import me.kvalbrus.multibans.api.punishment.Punishment
import me.kvalbrus.multibans.api.punishment.executor.PunishmentExecutor
import me.kvalbrus.multibans.api.punishment.punishments.*
import me.kvalbrus.multibans.api.punishment.punishments.PunishmentStatus.*
import me.kvalbrus.multibans.api.punishment.punishments.PunishmentType.*
import me.kvalbrus.multibans.api.punishment.target.PunishmentTarget
import me.kvalbrus.multibans.api.utils.TimeType
import me.kvalbrus.multibans.common.punishment.MultiPunishment.Companion.constructPunishment
import me.kvalbrus.multibans.common.punishment.action.MultiCreationAction
import me.kvalbrus.multibans.common.utils.Message.*
import me.kvalbrus.multibans.common.utils.ReplacedString
import java.util.*
import kotlin.collections.ArrayList

class MultiPunishmentManager(private val pluginManager: PluginManager) : PunishmentManager {

    @Throws(Exception::class)
    override fun <T : Punishment> getPlayerHistory(uuid: UUID): List<T> = pluginManager.dataProvider!!.getTargetHistory(uuid)

    @Throws(Exception::class)
    override fun <T : Punishment> getPlayerHistory(name: String): List<T> = pluginManager.dataProvider!!.getTargetHistory(name)

    @Throws(Exception::class)
    override fun <T : Punishment> getPlayerHistory(uuid: UUID, clazz: Class<T>): List<T> {
        val punishments = this.getPlayerHistory<T>(uuid).toMutableList()
        punishments.removeIf { !clazz.isInstance(it) }
//        punishments.removeIf { punishment: T -> !clazz.isInstance(punishment) }
        return punishments
    }

    @Throws(Exception::class)
    @Deprecated("")
    override fun <T : Punishment> getCreatorHistory(creator: String): List<T> = pluginManager.dataProvider!!.getCreatorHistory(creator)

    @Throws(Exception::class)
    override fun <T : Punishment> getActivePunishments(uuid: UUID): List<T> {
        val punishments = this.getPlayerHistory<T>(uuid).toMutableList()
        punishments.removeIf { it.getStatus() !== ACTIVE }
//        punishments.removeIf(Predicate<T> { punishment: T -> punishment!!.getStatus() !== ACTIVE })
        return punishments
    }

    @Throws(Exception::class)
    @Deprecated("")
    fun <T : Punishment> getActivePunishments(name: String): List<T> {
        val punishments = this.getPlayerHistory<T>(name).toMutableList()
        punishments.removeIf({ punishment: T -> punishment!!.getStatus() !== ACTIVE })
        return punishments
    }

    @Throws(Exception::class)
    override fun <T : Punishment> getActivePunishments(uuid: UUID, clazz: Class<T>): List<T> {
        val punishments = this.getActivePunishments<T>(uuid).toMutableList()
        punishments.removeIf { !clazz.isInstance(it) }
        return punishments
    }

    @Throws(Exception::class)
    override fun hasPunishment(id: String): Boolean = pluginManager.dataProvider!!.hasPunishment(id)

    @Throws(Exception::class)
    override fun getPunishment(id: String): Punishment = pluginManager.dataProvider!!.getPunishment(id)!!

    override fun hasActiveBan(uuid: UUID): Boolean {
        val punishments = ArrayList(this.getPlayerHistory<Punishment>(uuid))

        punishments.removeIf { it.getStatus() != ACTIVE }
        punishments.removeIf { it.type != BAN && it.type != TEMP_BAN && it.type != BAN_IP && it.type != TEMP_BAN_IP }

        return punishments.size > 0
    }

    @Deprecated("")
    override fun hasActiveBan(name: String): Boolean = false

    override fun hasActiveBanIp(ip: String): Boolean {
        val punishments: MutableList<Punishment> = ArrayList(this.getPlayerHistoryByIp(ip))
        
        punishments.removeIf { it.getStatus() != ACTIVE }
        punishments.removeIf { it.type != BAN_IP && it.type != TEMP_BAN_IP }
        
        return punishments.size > 0
    }

    override fun hasActiveChatMute(uuid: UUID): Boolean {
        val punishments: MutableList<Punishment> = ArrayList(this.getPlayerHistory(uuid))

        punishments.removeIf { it.getStatus() != ACTIVE }
        punishments.removeIf { it.type != MUTE && it.type != TEMP_MUTE }
        
        return punishments.size > 0
    }

    @Deprecated("")
    override fun hasActiveChatMute(name: String): Boolean = false

    @Synchronized
    @Throws(Exception::class)
    override fun generatePunishment(type: PunishmentType, target: PunishmentTarget, creator: PunishmentExecutor, duration: Long, reason: String): Punishment {
        return this.generatePunishment(type, target, creator, duration, reason, "", ArrayList())
    }

    @Synchronized
    @Throws(Exception::class)
    override fun generatePunishment(type: PunishmentType, target: PunishmentTarget, creator: PunishmentExecutor,
                                    duration: Long, reason: String, comment: String, servers: List<String>): Punishment {
        var id = generateId()
        val start = System.currentTimeMillis()
        while (pluginManager.dataProvider!!.hasPunishment(id)) {
            if (System.currentTimeMillis() - start > 5 * TimeType.SECOND.duration) {
                id = id + Random().nextInt(422)
                break
            }

            id = generateId()
        }

        val realTime = System.currentTimeMillis()
        return constructPunishment(pluginManager, type, id, MultiCreationAction(id, 1, target,
            creator, realTime, reason), ArrayList(), ArrayList(), realTime, duration, comment, servers, false)
    }

    @Synchronized
    private fun generateId(): String {
        val builder = StringBuilder()
        val random = Random()
        for (i in 0 until (pluginManager as MultiBansPluginManager).settings.idSize) {
            builder.append(random.nextInt(10))
        }

        return builder.toString()
    }

    @Throws(Exception::class)
    override fun getPlayerHistoryByIp(ip: String): List<Punishment> = pluginManager.dataProvider!!.getTargetHistoryByIp(ip)

    @Throws(Exception::class)
    override fun getAllPunishments(): List<Punishment> = pluginManager.dataProvider!!.getAllPunishments()

    fun getBanMessage(uuid: UUID): String {
        val activeBans: MutableList<Punishment> = ArrayList()

        activeBans.addAll(getActivePunishments(uuid, PermanentlyBanIp::class.java))
        activeBans.addAll(getActivePunishments(uuid, PermanentlyBan::class.java))
        activeBans.addAll(getActivePunishments(uuid, TemporaryBanIp::class.java))
        activeBans.addAll(getActivePunishments(uuid, TemporaryBan::class.java))

        var punishmentsString = String()
        for (punishment: Punishment in activeBans) {
            var listenMessage: ReplacedString? = null
            if (punishment.type === BAN_IP) {
                punishmentsString += ReplacedString(BANIP_ACTIVATE_EXECUTOR.getText()).replacePunishment(punishment).string()
                listenMessage = ReplacedString(BANIP_TRY_LISTEN.getText()).replacePunishment(punishment)
            } else if (punishment.type === BAN) {
                punishmentsString += ReplacedString(BAN_ACTIVATE_TARGET.getText()).replacePunishment(punishment).string()
                listenMessage = ReplacedString(BAN_TRY_LISTEN.getText()).replacePunishment(punishment)
            } else if (punishment.type === TEMP_BAN_IP) {
                punishmentsString += ReplacedString(TEMPBANIP_ACTIVATE_TARGET.getText()).replacePunishment(punishment).string()
                listenMessage = ReplacedString(TEMPBANIP_TRY_LISTEN.getText()).replacePunishment(punishment)
            } else if (punishment.type === TEMP_BAN) {
                punishmentsString += ReplacedString(TEMPBANIP_ACTIVATE_TARGET.getText()).replacePunishment(punishment).string()
                listenMessage = ReplacedString(TEMPBAN_TRY_LISTEN.getText()).replacePunishment(punishment)
            }

            if (listenMessage != null) {
                for (listener: OnlinePlayer in pluginManager.onlinePlayers) {
                    listener.sendMessage(listenMessage.string())
                }

                this.pluginManager.console.sendMessage(listenMessage.string())
            }
        }

        return BAN_TITLE_HEADER.getText() + punishmentsString + BAN_TITLE_FOOTER.getText()
    }

    fun getBanIPMessage(ip: String): String {
        val activeBansIP: MutableList<Punishment> = ArrayList()

        activeBansIP.addAll(getPlayerHistoryByIp(ip))
        activeBansIP.removeIf { it.getStatus() !== ACTIVE }

        var punishmentsString = String()
        for (punishment: Punishment in activeBansIP) {
            var listenMessage: ReplacedString? = null
            if (punishment.type === BAN_IP) {
                punishmentsString += ReplacedString(BANIP_ACTIVATE_EXECUTOR.getText()).replacePunishment(punishment).string()
                listenMessage = ReplacedString(BANIP_TRY_LISTEN.getText()).replacePunishment(punishment)
            } else if (punishment.type === TEMP_BAN_IP) {
                punishmentsString += ReplacedString(TEMPBANIP_ACTIVATE_TARGET.getText()).replacePunishment(punishment).string()
                listenMessage = ReplacedString(TEMPBANIP_TRY_LISTEN.getText()).replacePunishment(punishment)
            }

            if (listenMessage != null) {
                for (listener: OnlinePlayer in pluginManager.onlinePlayers) {
                    listener.sendMessage(listenMessage.string())
                }

                this.pluginManager.console.sendMessage(listenMessage.string())
            }
        }

        return BAN_TITLE_HEADER.getText() + punishmentsString + BAN_TITLE_FOOTER.getText()
    }

    fun getMuteMessage(uuid: UUID): String {
        val activeMutes = ArrayList<Punishment>()

        activeMutes.addAll(getActivePunishments(uuid, PermanentlyChatMute::class.java))
        activeMutes.addAll(getActivePunishments(uuid, TemporaryChatMute::class.java))

        if (activeMutes.size > 0) {
            return MUTECHAT_TRY_TARGET.getText()
        } else {
            return ""
        }
    }
}