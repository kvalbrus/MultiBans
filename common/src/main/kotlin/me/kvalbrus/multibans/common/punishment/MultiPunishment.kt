package me.kvalbrus.multibans.common.punishment

import me.kvalbrus.multibans.api.OnlinePlayer
import me.kvalbrus.multibans.api.punishment.Punishment
import me.kvalbrus.multibans.api.punishment.punishments.PunishmentType
import me.kvalbrus.multibans.api.punishment.TemporaryPunishment
import me.kvalbrus.multibans.api.punishment.action.ActivationAction
import me.kvalbrus.multibans.api.punishment.action.CreationAction
import me.kvalbrus.multibans.api.punishment.action.DeactivationAction
import me.kvalbrus.multibans.api.punishment.executor.OnlinePunishmentExecutor
import me.kvalbrus.multibans.api.punishment.executor.PunishmentExecutor
import me.kvalbrus.multibans.api.punishment.target.OnlinePunishmentTarget
import me.kvalbrus.multibans.api.punishment.target.PunishmentTarget
import me.kvalbrus.multibans.common.managers.MultiBansPluginManager
import me.kvalbrus.multibans.common.managers.PluginManager
import me.kvalbrus.multibans.common.permissions.Permission
import me.kvalbrus.multibans.common.punishment.action.MultiCreationAction
import me.kvalbrus.multibans.common.punishment.punishments.*
import me.kvalbrus.multibans.common.utils.ReplacedString
import java.util.*

abstract class MultiPunishment(
    pluginManager: PluginManager,
    type: PunishmentType,
    id: String,
    creationAction: CreationAction,
    comment: String,
    servers: List<String>) : Punishment {

    private var _comment: String
    private var _servers: List<String>

    protected val pluginManager: PluginManager

    final override val type: PunishmentType
    final override val id: String
    final override val creationAction: CreationAction

    init {
        this.pluginManager = pluginManager
        this.type = type
        this.id = id
        this.creationAction = creationAction
        this._comment = comment
        this._servers = servers

        this.target.punishment = this
        this.creator.punishment = this
    }

    final override val target: PunishmentTarget
        get() = this.creationAction.target

    final override val creator: PunishmentExecutor
        get() = this.creationAction.executor

    final override val createdDate: Long
        get() = this.creationAction.date

    final override var createdReason: String
        get() = this.creationAction.reason

        @Synchronized
        set(createdReason) {
            this.creationAction.reason = createdReason
            this.updateData()
            this.sendMessageAboutReasonChange()
        }

    final override var comment: String
        get() = this._comment

        @Synchronized
        set(comment) {
            this._comment = comment
            this.updateData()
            this.sendMessageAboutCommentChange()
        }

    final override var servers: List<String>
        get() = this._servers

        @Synchronized
        set(servers) {
            this._servers = servers
            this.updateData()
        }

    abstract val createMessageForListener: String
    abstract val createMessageForExecutor: String
    abstract val createMessageForTarget: String
    abstract val deleteMessageForListener: String
    abstract val deleteMessageForExecutor: String
    abstract val deleteMessageForTarget: String
    abstract val reasonChangeMessageForExecutor: String
    abstract val reasonChangeMessageForListener: String
    abstract val commentChangeMessageForExecutor: String
    abstract val commentChangeMessageForListener: String
    abstract val permissionForListener: Permission

    constructor(
        pluginManager: PluginManager,
        type: PunishmentType,
        id: String,
        target: PunishmentTarget,
        creator: PunishmentExecutor,
        createdDate: Long,
        createdReason: String,
        comment: String,
        servers: List<String>) :
            this(pluginManager, type, id,
                MultiCreationAction(id, 1, target, creator, createdDate, createdReason), comment, servers)

    @Synchronized
    override fun create() {
        this.pluginManager.activatePunishment(this)
        this.updateData()
        this.sendMessageAboutCreation()
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
                    return punishment1.startedDate.compareTo(other.startedDate)
                }

                if (punishment1.duration != other.duration) {
                    return punishment1.duration.compareTo(other.duration)
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
    protected fun updateData() {
        val dataProvider = pluginManager.dataProvider

        if (dataProvider?.hasPunishment(id) == true) {
            dataProvider.updatePunishment(this)
        } else {
            dataProvider?.createPunishment(this)
        }

    }

    @Synchronized
    protected fun deleteData() {
        val dataProvider = pluginManager.dataProvider
        if (dataProvider != null && dataProvider.hasPunishment(id)) {
            dataProvider.deletePunishment(this)
        }
    }

    protected fun sendMessageToListeners(message: String?) {
        val listenMessage = ReplacedString(message).replacePunishment(this)

        // Listener #1 - online players
        Arrays.stream(pluginManager.onlinePlayers)
            .filter { player: OnlinePlayer ->
                player.hasPermission(this.permissionForListener.perm) }
            .forEach { player: OnlinePlayer -> player.sendMessage(listenMessage.string()) }

        // Listener #2 - console
        if (pluginManager is MultiBansPluginManager) {
            if (pluginManager.settings.consoleLog) {
                pluginManager.getConsole().sendMessage(listenMessage.string())
            }
        }
    }

    protected fun sendMessageToCreator(message: String?) {
        if (creator is OnlinePunishmentExecutor) {
            val creatorMessage = ReplacedString(message).replacePunishment(this)
            (creator as OnlinePunishmentExecutor).sendMessage(creatorMessage.string())
        }
    }

    protected fun sendMessageToTarget(message: String?) {
        if (this.target is OnlinePunishmentTarget) {
            val targetMessage = ReplacedString(message).replacePunishment(this)
            (this.target as OnlinePunishmentTarget).sendMessage(targetMessage.string())
        }
    }

        private fun sendMessageAboutCreation() {
        sendMessageToListeners(this.createMessageForListener)
        sendMessageToCreator(this.createMessageForExecutor)
        sendMessageToTarget(this.createMessageForTarget)
    }

    private fun sendMessageAboutDelete() {
        sendMessageToListeners(this.deleteMessageForListener)
        sendMessageToCreator(this.deleteMessageForExecutor)
        sendMessageToTarget(this.deleteMessageForTarget)
    }

    private fun sendMessageAboutCommentChange() {
        sendMessageToListeners(this.commentChangeMessageForListener)
        sendMessageToCreator(this.commentChangeMessageForExecutor)
    }

    private fun sendMessageAboutReasonChange() {
        sendMessageToListeners(this.reasonChangeMessageForListener)
        sendMessageToCreator(this.reasonChangeMessageForExecutor)
    }

    companion object {
//        fun <T : Punishment?> constructPunishment(
//            pluginManager: PluginManager,
//            type: PunishmentType,
//            id: String,
//            creationAction: CreationAction,
//            startedDate: Long,
//            duration: Long,
//            comment: String,
//            servers: List<String>,
//            cancelled: Boolean): T {
//            val punishment: Punishment = when (type) {
//                PunishmentType.BAN -> MultiPermanentlyBan(
//                    pluginManager, id, creationAction, comment = comment, servers =  servers, cancelled = cancelled)
//
//                PunishmentType.TEMP_BAN -> MultiTemporaryBan(pluginManager, id, creationAction,
//                    startedDate = startedDate, duration = duration, comment = comment, servers = servers, cancelled = cancelled)
//
//                PunishmentType.BAN_IP -> MultiPermanentlyBanIp(
//                    pluginManager, id, creationAction, comment = comment, servers =  servers, cancelled = cancelled)
//
//                PunishmentType.TEMP_BAN_IP -> MultiTemporaryBanIp(pluginManager, id, creationAction,
//                    startedDate = startedDate, duration = duration, comment = comment, servers = servers, cancelled = cancelled)
//
//                PunishmentType.MUTE -> MultiPermanentlyChatMute(
//                    pluginManager, id, creationAction,  comment = comment, servers =  servers, cancelled = cancelled)
//
//                PunishmentType.TEMP_MUTE -> MultiTemporaryChatMute(pluginManager, id, creationAction,
//                    startedDate = startedDate, duration = duration, comment = comment, servers = servers, cancelled = cancelled)
//
//                PunishmentType.KICK -> Kick(pluginManager, id, creationAction, comment, servers)
//
//                else -> throw IllegalArgumentException("Punishment type hasn't its constructor")
//            }
//
//            return punishment as T
//        }

        fun <T : Punishment?> constructPunishment(
            pluginManager: PluginManager,
            type: PunishmentType,
            id: String,
            creationAction: CreationAction,
            activations: MutableList<ActivationAction> = mutableListOf(),
            deactivations: MutableList<DeactivationAction> = mutableListOf(),
            startedDate: Long,
            duration: Long,
            comment: String,
            servers: List<String>,
            cancelled: Boolean): T {
            val punishment: Punishment = when (type) {
                PunishmentType.BAN -> MultiPermanentlyBan(
                    pluginManager, id, creationAction, activations, deactivations, comment, servers, cancelled)

                PunishmentType.TEMP_BAN -> MultiTemporaryBan(pluginManager, id, creationAction,
                    activations, deactivations, startedDate, duration, comment, servers, cancelled)

                PunishmentType.BAN_IP -> MultiPermanentlyBanIp(
                    pluginManager, id, creationAction, activations, deactivations, comment, servers, cancelled)

                PunishmentType.TEMP_BAN_IP -> MultiTemporaryBanIp(pluginManager, id, creationAction,
                    activations, deactivations, startedDate, duration, comment, servers, cancelled)

                PunishmentType.MUTE -> MultiPermanentlyChatMute(
                    pluginManager, id, creationAction, activations, deactivations, comment, servers, cancelled)

                PunishmentType.TEMP_MUTE -> MultiTemporaryChatMute(pluginManager, id, creationAction,
                    activations, deactivations, startedDate, duration, comment, servers, cancelled)

                PunishmentType.KICK -> Kick(pluginManager, id, creationAction, comment, servers)

                else -> throw IllegalArgumentException("Punishment type hasn't its constructor")
            }

            return punishment as T
        }
    }
}