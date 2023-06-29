package me.kvalbrus.multibans.common.punishment

import me.kvalbrus.multibans.api.OnlinePlayer
import me.kvalbrus.multibans.api.punishment.Punishment
import me.kvalbrus.multibans.api.punishment.punishments.PunishmentType
import me.kvalbrus.multibans.api.punishment.TemporaryPunishment
import me.kvalbrus.multibans.api.punishment.action.Action
import me.kvalbrus.multibans.api.punishment.action.CreationAction
import me.kvalbrus.multibans.api.punishment.executor.OnlinePunishmentExecutor
import me.kvalbrus.multibans.api.punishment.executor.PunishmentExecutor
import me.kvalbrus.multibans.api.punishment.target.OnlinePunishmentTarget
import me.kvalbrus.multibans.api.punishment.target.PunishmentTarget
import me.kvalbrus.multibans.common.managers.MultiBansPluginManager
import me.kvalbrus.multibans.common.managers.PluginManager
import me.kvalbrus.multibans.common.permissions.Permission
import me.kvalbrus.multibans.common.punishment.action.MultiCreationAction
import me.kvalbrus.multibans.common.punishment.punishments.*
import me.kvalbrus.multibans.common.utils.Message
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

    abstract val createMessageForListener: Message
    abstract val createMessageForExecutor: Message
    abstract val createMessageForTarget: Message
    abstract val deleteMessageForListener: Message
    abstract val deleteMessageForExecutor: Message
    abstract val deleteMessageForTarget: Message
    abstract val reasonChangeMessageForExecutor: Message
    abstract val reasonChangeMessageForListener: Message
    abstract val commentChangeMessageForExecutor: Message
    abstract val commentChangeMessageForListener: Message
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
        this.updateData()
        this.pluginManager.createPunishment(this, this.creationAction)
        this.sendMessageAboutCreation()
    }

    @Synchronized
    override fun delete() {
        this.pluginManager.dataProvider?.deletePunishment(this)
        this.deleteData()
        this.pluginManager.deletePunishment(this)
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
            dataProvider.savePunishment(this)
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

    protected fun sendMessageToListeners(message: Message) {
        val listenMessage = ReplacedString(message.getText()).replacePunishment(this)

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

    protected fun sendMessageToCreator(message: Message) {
        if (this.creator is OnlinePunishmentExecutor) {
            val creatorMessage = ReplacedString(message.getText()).replacePunishment(this)
            (this.creator as OnlinePunishmentExecutor).sendMessage(creatorMessage.string())
        }
    }

    protected fun sendMessageToTarget(message: Message) {
        if (this.target is OnlinePunishmentTarget) {
            val targetMessage = ReplacedString(message.getText()).replacePunishment(this)
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
        fun <T : Punishment?> constructPunishment(
            pluginManager: PluginManager,
            type: PunishmentType,
            id: String,
            creationAction: CreationAction,
            activations: MutableList<Action> = mutableListOf(),
            deactivations: MutableList<Action> = mutableListOf(),
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