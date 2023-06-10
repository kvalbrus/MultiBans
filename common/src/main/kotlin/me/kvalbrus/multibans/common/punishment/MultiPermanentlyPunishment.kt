package me.kvalbrus.multibans.common.punishment

import me.kvalbrus.multibans.api.punishment.PermanentlyPunishment
import me.kvalbrus.multibans.api.punishment.action.ActivationAction
import me.kvalbrus.multibans.api.punishment.action.CreationAction
import me.kvalbrus.multibans.api.punishment.action.DeactivationAction
import me.kvalbrus.multibans.api.punishment.punishments.PunishmentStatus
import me.kvalbrus.multibans.api.punishment.punishments.PunishmentType
import me.kvalbrus.multibans.api.punishment.executor.PunishmentExecutor
import me.kvalbrus.multibans.common.managers.PluginManager
import me.kvalbrus.multibans.common.punishment.action.MultiActivationAction
import me.kvalbrus.multibans.common.punishment.action.MultiDeactivationAction
import me.kvalbrus.multibans.common.punishment.punishments.ActionLoader

abstract class MultiPermanentlyPunishment : MultiPunishment, PermanentlyPunishment, ActionLoader {

    private var _activations: MutableList<ActivationAction> = mutableListOf()
    private var _deactivations: MutableList<DeactivationAction> = mutableListOf()
    private var _cancelled: Boolean

    constructor(
        pluginManager: PluginManager,
        type: PunishmentType,
        id: String,
        creationAction: CreationAction,
        comment: String,
        servers: List<String>,
        cancelled: Boolean) : super(pluginManager, type, id, creationAction, comment, servers) {
            this._cancelled = cancelled
    }

    constructor(
        pluginManager: PluginManager,
        type: PunishmentType,
        id: String,
        creationAction: CreationAction,
        activations: MutableList<ActivationAction>,
        deactivations: MutableList<DeactivationAction>,
        comment: String,
        servers: List<String>,
        cancelled: Boolean) : this(pluginManager, type, id, creationAction, comment, servers, cancelled) {
            this._activations = activations
            this._deactivations = deactivations
    }

    final override val activations: List<ActivationAction>
        get() = ArrayList(this._activations)

    final override val deactivations: List<DeactivationAction>
        get() = ArrayList(this._deactivations)

    final override val cancelled: Boolean
        get() = this._cancelled

    @Synchronized
    override fun activate(executor: PunishmentExecutor, date: Long, reason: String) : Boolean {
        return this.activate(MultiActivationAction(this.id, this._activations.size + 1, executor, date, reason))
    }

    @Synchronized
    override fun activate(action: ActivationAction) : Boolean {
        if (this._cancelled) {
            this._cancelled = false

            this._activations.add(action)
            this.updateData()
            this.pluginManager.activatePunishment(this)
            this.sendMessageAboutActivate()

            return true
        } else {
            return false
        }
    }

    @Synchronized
    override fun deactivate(executor: PunishmentExecutor, date: Long, reason: String): Boolean {
        return this.deactivate(MultiDeactivationAction(this.id, this._deactivations.size + 1, executor, date, reason))
    }

    @Synchronized
    override fun deactivate(action: DeactivationAction) : Boolean {
        if (!this._cancelled) {
            this._cancelled = true

            this._deactivations.add(action)
            this.updateData()
            this.pluginManager.deactivatePunishment(this)
            this.sendMessageAboutDeactivate()

            return true
        } else {
            return false
        }
    }

    override fun getStatus(): PunishmentStatus {
        return if (this._cancelled) {
            PunishmentStatus.CANCELLED
        } else {
            PunishmentStatus.ACTIVE
        }
    }

    override fun lastActivator(): PunishmentExecutor {
        if (this.activations.isEmpty()) {
            return this.creationAction.executor
        } else {
            var maxTimeIndex = 0
            val activations = this.activations
            for (i in 0..activations.size) {
                if (activations[maxTimeIndex].date < activations[i].date) {
                    maxTimeIndex = i
                }
            }

            return activations[maxTimeIndex].executor
        }
    }

    override fun lastDeactivator(): PunishmentExecutor? {
        if (this.deactivations.isEmpty()) {
            return null
        } else {
            var maxTimeIndex = 0
            val deactivations = this.deactivations
            for (i in 0..deactivations.size) {
                if (deactivations[maxTimeIndex].date < deactivations[i].date) {
                    maxTimeIndex = i
                }
            }

            return deactivations[maxTimeIndex].executor
        }
    }

    override fun loadActivationsFromDataProvider(activations: MutableList<ActivationAction>) {
        this._activations = activations
    }

    override fun loadDeactivationsFromDataProvider(deactivations: MutableList<DeactivationAction>) {
        this._deactivations = deactivations
    }

    private fun sendMessageAboutActivate() {
        sendMessageToListeners(this.activateMessageForListener)
        sendMessageToCreator(this.activateMessageForExecutor)
        sendMessageToTarget(this.activateMessageForTarget)
    }

    private fun sendMessageAboutDeactivate() {
        sendMessageToListeners(this.deactivateMessageForListener)
        sendMessageToCreator(this.deactivateMessageForExecutor)
        sendMessageToTarget(this.deactivateMessageForTarget)
    }

    abstract val activateMessageForListener: String
    abstract val activateMessageForExecutor: String
    abstract val activateMessageForTarget: String

    abstract val deactivateMessageForListener: String
    abstract val deactivateMessageForExecutor: String
    abstract val deactivateMessageForTarget: String
}