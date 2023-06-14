package me.kvalbrus.multibans.common.punishment

import me.kvalbrus.multibans.api.punishment.punishments.PunishmentStatus
import me.kvalbrus.multibans.api.punishment.punishments.PunishmentType
import me.kvalbrus.multibans.api.punishment.TemporaryPunishment
import me.kvalbrus.multibans.api.punishment.action.Action
import me.kvalbrus.multibans.api.punishment.action.ActivationAction
import me.kvalbrus.multibans.api.punishment.action.CreationAction
import me.kvalbrus.multibans.api.punishment.action.DeactivationAction
import me.kvalbrus.multibans.api.punishment.executor.PunishmentExecutor
import me.kvalbrus.multibans.common.managers.PluginManager
import me.kvalbrus.multibans.common.punishment.action.MultiActivationAction
import me.kvalbrus.multibans.common.punishment.action.MultiDeactivationAction

abstract class MultiTemporaryPunishment : MultiPunishment, TemporaryPunishment {

    private var _activations: MutableList<Action> = mutableListOf()
    private var _deactivations: MutableList<Action> = mutableListOf()
    private var _startedDate: Long
    private var _duration: Long
    private var _cancelled: Boolean

    constructor(
        pluginManager: PluginManager,
        type: PunishmentType,
        id: String,
        creationAction: CreationAction,
        startedDate: Long,
        duration: Long,
        comment: String,
        servers: List<String>,
        cancelled: Boolean) : super(pluginManager, type, id, creationAction, comment, servers) {
        this._startedDate = startedDate
        this._duration = duration
        this._cancelled = cancelled
    }

    constructor(
        pluginManager: PluginManager,
        type: PunishmentType,
        id: String,
        creationAction: CreationAction,
        activations: MutableList<Action>,
        deactivations: MutableList<Action>,
        startedDate: Long,
        duration: Long,
        comment: String,
        servers: List<String>,
        cancelled: Boolean) : this(pluginManager, type, id, creationAction, startedDate, duration, comment, servers, cancelled) {
        this._activations = activations
        this._deactivations = deactivations
    }

    final override val activations: List<Action>
        get() = ArrayList(this._activations)

    final override val deactivations: List<Action>
        get() = ArrayList(this._deactivations)

    final override val cancelled: Boolean
        get() = this._cancelled

    final override val duration: Long
        get() = this._duration

    final override val startedDate: Long
        get() = this._startedDate

    @Synchronized
    override fun create() {
        val activePunishments = pluginManager
            .punishmentManager.getActivePunishments(this.target.uniqueId, this.javaClass)
        val size = activePunishments.size
        if (size > 0) {
            activePunishments.sorted()
            val last = activePunishments[size - 1]
            this._startedDate = last.startedDate + last.duration
        } else {
            this._startedDate = this.createdDate
        }

        super.create()
    }

    @Synchronized
    override fun activate(executor: PunishmentExecutor, date: Long, reason: String) : Boolean {
        return this.activate(MultiActivationAction(this.id, this._activations.size + 1, executor, date, reason))
    }

    @Synchronized
    override fun activate(action: ActivationAction) : Boolean {
        if (this._cancelled) {
            this._cancelled = false

            this._activations.add(action)

            val activePunishments = pluginManager
                .punishmentManager.getActivePunishments(this.target.uniqueId, this.javaClass)
            val size = activePunishments.size
            if (size > 0) {
                activePunishments.sorted()
                val last = activePunishments[size - 1]
                this._startedDate = last.startedDate + last.duration
            } else {
                this._startedDate = this.createdDate
            }

            this.updateData()
            this.pluginManager.activatePunishment(this, action)
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
            this.deleteAndSortPunishments()

            this._cancelled = true

            this._deactivations.add(action)
            this.updateData()
            this.pluginManager.deactivatePunishment(this, action)
            this.sendMessageAboutDeactivate()

            return true
        } else {
            return false
        }
    }

    @Synchronized
    override fun delete() {
        if (!this._cancelled) {
            this.deleteAndSortPunishments()
        }

        super.delete()
    }

    override fun getStatus(): PunishmentStatus {
        return if (this.cancelled) {
            PunishmentStatus.CANCELLED
        } else if (System.currentTimeMillis() > createdDate + duration) {
            PunishmentStatus.PASSED
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

    @Synchronized
    private fun deleteAndSortPunishments() {
        val history = ArrayList<MultiTemporaryPunishment>()
        for (pun in pluginManager.punishmentManager.getPlayerHistory(target.uniqueId, this.javaClass)) {
            if (pun is MultiTemporaryPunishment) {
                history.add(pun)
            }
        }

        if (history.stream().noneMatch { punishment: MultiTemporaryPunishment -> punishment.id == id }) {
            return
        }

        history.removeIf { obj: MultiTemporaryPunishment -> obj.cancelled }
        history.sort()
        val size = history.size
        if (size > 0) {
            var prev: MultiTemporaryPunishment? = null
            var curr: MultiTemporaryPunishment?
            var index = -1
            for (punishment in history) {
                if (punishment.id == id) {
                    index = history.indexOf(punishment)
                    break
                }
            }

            if (index == 0) {
                while (index < size - 1) {
                    curr = history[index + 1]
                    if (prev == null) {
                        curr._startedDate = history[0].startedDate
                    } else {
                        curr._startedDate = prev.startedDate + prev.duration
                    }
                    prev = curr
                    index++
                    curr.updateData()
                }
            } else if (index != size - 1) {
                prev = history[index - 1]
                while (index < size - 1) {
                    curr = history[index + 1]
                    curr._startedDate = prev!!.startedDate + prev.duration
                    prev = curr
                    index++
                    curr.updateData()
                }
            }
        }
    }
}