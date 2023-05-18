package me.kvalbrus.multibans.common.punishment

import me.kvalbrus.multibans.api.OnlinePlayer
import me.kvalbrus.multibans.api.punishment.Punishment
import me.kvalbrus.multibans.api.punishment.PunishmentType
import me.kvalbrus.multibans.api.punishment.TemporaryPunishment
import me.kvalbrus.multibans.api.punishment.creator.OnlinePunishmentCreator
import me.kvalbrus.multibans.api.punishment.creator.PunishmentCreator
import me.kvalbrus.multibans.api.punishment.target.OnlinePunishmentTarget
import me.kvalbrus.multibans.api.punishment.target.PunishmentTarget
import me.kvalbrus.multibans.common.managers.MultiBansPluginManager
import me.kvalbrus.multibans.common.managers.PluginManager
import me.kvalbrus.multibans.common.permissions.Permission
import me.kvalbrus.multibans.common.punishment.punishments.*
import me.kvalbrus.multibans.common.utils.ReplacedString
import java.util.*

abstract class MultiPunishment : Punishment {

    protected val pluginManager: PluginManager

    final override val type: PunishmentType
    final override val id: String
    final override val target: PunishmentTarget
    final override val creator: PunishmentCreator
    final override val createdDate: Long
    
    @set:Synchronized
    final override var createdReason: String
        get() = this._createdReason
        set(createdReason) {
            this._createdReason = createdReason
            this.updateData()
            this.sendMessageAboutReasonChange()
    }

    @set:Synchronized
    final override var comment: String
        get() = this._comment
        set(comment) {
            this._comment = comment
            this.updateData()
            this.sendMessageAboutCommentChange()
    }

    @set:Synchronized
    final override var servers: List<String>
        get() = this._servers
        set(servers) {
            this._servers = servers
            this.updateData()
    }

    private var _createdReason: String
    private var _comment: String
    private var _servers: List<String>

    constructor(pluginManager: PluginManager,
                type: PunishmentType,
                id: String,
                target: PunishmentTarget,
                creator: PunishmentCreator,
                createdDate: Long = System.currentTimeMillis(),
                createdReason: String = "",
                comment: String = "",
                servers: List<String> = listOf()) {
        this.pluginManager = pluginManager
        this.type = type
        this.id = id
        this.target = target
        this.creator = creator
        this.createdDate = createdDate
        this._createdReason = createdReason
        this._comment = comment
        this._servers = servers

        this.target.setPunishment(this)
        this.creator.punishment = this
    }

    @Synchronized
    override fun activate() {
        this.pluginManager.activatePunishment(this)
        this.updateData()
        this.sendMessageAboutActivate()
    }

    @Synchronized
    override fun delete() {
        this.pluginManager.dataProvider?.deletePunishment(this)
        this.deleteData()
        this.sendMessageAboutDelete()
    }

    override fun compareTo(other: Punishment?): Int {
        other?.let {
            val punishment1: Punishment = this
            if (punishment1.type != other.type) {
                return punishment1.type.compareTo(other.type)
            }

            if (punishment1.target.uniqueId != other.target.uniqueId) {
                return punishment1.target.uniqueId.compareTo(other.target.uniqueId)
            }

            if (punishment1 is TemporaryPunishment && other is TemporaryPunishment) {
                if (punishment1.startedDate != other.startedDate) {
                    return java.lang.Long.compare(punishment1.startedDate, other.startedDate)
                }

                if (punishment1.duration != other.duration) {
                    return java.lang.Long.compare(punishment1.duration, other.duration)
                }
            }

            if (punishment1.creator.name != other.creator.name) {
                return punishment1.creator.name.compareTo(other.creator.name)
            }

            if (punishment1.createdDate != other.createdDate) {
                return punishment1.createdDate.compareTo(other.createdDate)
            } else {
                return punishment1.id.compareTo(other.id)
            }
        }

        return -1
    }

    @Synchronized
    fun updateData() {
        val dataProvider = pluginManager.dataProvider

        if (dataProvider?.hasPunishment(id) == true) {
            dataProvider.updatePunishment(this)
        } else {
            dataProvider?.createPunishment(this)
        }

    }

    @Synchronized
    fun deleteData() {
        val dataProvider = pluginManager.dataProvider
        if (dataProvider != null && dataProvider.hasPunishment(id)) {
            dataProvider.deletePunishment(this)
        }
    }

    abstract fun getActivateMessageForListener(): String
    abstract fun getActivateMessageForExecutor(): String
    abstract fun getActivateMessageForTarget(): String?
    abstract fun getDeleteMessageForListener(): String
    abstract fun getDeleteMessageForExecutor(): String
    abstract fun getDeleteMessageForTarget(): String?
    abstract fun getReasonChangeMessageForExecutor(): String
    abstract fun getReasonChangeMessageForListener(): String
    abstract fun getCommentChangeMessageForExecutor(): String
    abstract fun getCommentChangeMessageForListener(): String
    abstract fun getPermissionForListener(): Permission

    private fun sendMessageAboutActivate() {
        sendMessageToListeners(getActivateMessageForListener())
        sendMessageToCreator(getActivateMessageForExecutor())
        sendMessageToTarget(getActivateMessageForTarget())
    }

    private fun sendMessageAboutDelete() {
        sendMessageToListeners(getDeleteMessageForListener())
        sendMessageToCreator(getDeleteMessageForExecutor())
        sendMessageToTarget(getDeleteMessageForTarget())
    }

    private fun sendMessageAboutCommentChange() {
        sendMessageToListeners(getCommentChangeMessageForListener())
        sendMessageToCreator(getCommentChangeMessageForExecutor())
    }

    private fun sendMessageAboutReasonChange() {
        sendMessageToListeners(getReasonChangeMessageForListener())
        sendMessageToCreator(getReasonChangeMessageForExecutor())
    }

    fun sendMessageToListeners(message: String?) {
        val listenMessage = ReplacedString(message).replacePunishment(this)

        // Listener #1 - online players
        Arrays.stream(pluginManager.onlinePlayers)
            .filter { player: OnlinePlayer -> player.hasPermission(getPermissionForListener().name) }
            .forEach { player: OnlinePlayer -> player.sendMessage(listenMessage.string()) }

        // Listener #2 - console
        if (pluginManager is MultiBansPluginManager) {
            if (pluginManager.settings.consoleLog) {
                pluginManager.getConsole().sendMessage(listenMessage.string())
            }
        }
    }

    fun sendMessageToCreator(message: String?) {
        if (creator is OnlinePunishmentCreator) {
            val creatorMessage = ReplacedString(message).replacePunishment(this)
            creator.sendMessage(creatorMessage.string())
        }
    }

    fun sendMessageToTarget(message: String?) {
        if (target is OnlinePunishmentTarget) {
            val targetMessage = ReplacedString(message).replacePunishment(this)
            target.sendMessage(targetMessage.string())
        }
    }

    companion object {
        fun <T : Punishment?> constructPunishment(
            pluginManager: PluginManager,
            type: PunishmentType,
            id: String,
            target: PunishmentTarget,
            creator: PunishmentCreator,
            createdDate: Long,
            startedDate: Long,
            duration: Long,
            createdReason: String?,
            comment: String?,
            cancellationCreator: PunishmentCreator?,
            cancellationDate: Long?,
            cancellationReason: String?,
            servers: List<String>,
            cancelled: Boolean): T {
            val punishment: Punishment = when (type) {
                PunishmentType.BAN -> MultiPermanentlyBan(
                    pluginManager, id, target, creator, createdDate,
                    createdReason ?: "", comment ?: "", cancellationCreator, cancellationDate,
                    cancellationReason, servers, cancelled)

                PunishmentType.TEMP_BAN -> MultiTemporaryBan(
                    pluginManager, id, target, creator, createdDate,
                    startedDate, duration, createdReason ?: "", comment ?: "",
                    cancellationCreator, cancellationDate, cancellationReason, servers, cancelled)

                PunishmentType.BAN_IP -> MultiPermanentlyBanIp(
                    pluginManager, id, target, creator,
                    createdDate, createdReason ?: "", comment ?: "", cancellationCreator, cancellationDate,
                    cancellationReason, servers, cancelled)

                PunishmentType.TEMP_BAN_IP -> MultiTemporaryBanIp(
                    pluginManager, id, target, creator, createdDate,
                    startedDate, duration, createdReason ?: "", comment ?: "", cancellationCreator,
                    cancellationDate, cancellationReason, servers, cancelled)

                PunishmentType.MUTE -> MultiPermanentlyChatMute(
                    pluginManager, id, target, creator,
                    createdDate, createdReason ?: "", comment ?: "", cancellationCreator, cancellationDate,
                    cancellationReason, servers, cancelled)

                PunishmentType.TEMP_MUTE -> MultiTemporaryChatMute(
                    pluginManager, id, target, creator,
                    createdDate, startedDate, duration, createdReason ?: "", comment ?: "", cancellationCreator,
                    cancellationDate, cancellationReason, servers, cancelled)

                PunishmentType.KICK -> Kick(pluginManager, id, target, creator, createdDate,
                    createdReason ?: "", comment ?: "", servers)

                else -> throw IllegalArgumentException("Punishment type hasn't its constructor")
            }
            return punishment as T
        }
    }
}