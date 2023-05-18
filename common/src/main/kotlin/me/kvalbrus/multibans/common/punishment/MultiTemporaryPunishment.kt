package me.kvalbrus.multibans.common.punishment

import me.kvalbrus.multibans.api.punishment.PunishmentStatus
import me.kvalbrus.multibans.api.punishment.PunishmentType
import me.kvalbrus.multibans.api.punishment.TemporaryPunishment
import me.kvalbrus.multibans.api.punishment.creator.PunishmentCreator
import me.kvalbrus.multibans.api.punishment.target.PunishmentTarget
import me.kvalbrus.multibans.common.managers.PluginManager

abstract class MultiTemporaryPunishment : MultiPunishment, TemporaryPunishment {

    @set:Synchronized
    final override var cancellationCreator: PunishmentCreator?
        get() = this._cancellationCreator
        set(cancellationCreator) {
            this._cancellationCreator = cancellationCreator
            this.updateData()
        }

    @set:Synchronized
    final override var cancellationDate: Long
        get() = this._cancellationDate
        set(cancellationDate) {
            this._cancellationDate = cancellationDate
            this.updateData()
        }

    @set:Synchronized
    final override var cancellationReason: String
        get() = this._cancellationReason
        set(cancellationReason) {
            this._cancellationReason = cancellationReason
            this.updateData()
        }

    @set:Synchronized
    final override var cancelled: Boolean
        get() = this._cancelled
        set(cancelled) {
            this._cancelled = cancelled
            this.updateData()
        }

    @set:Synchronized
    final override var duration: Long
        get() = this._duration
        set(duration) {
            this._duration = duration
            this.updateData()
        }

    @set:Synchronized
    final override var startedDate: Long
        get() = this._startedDate
        set(startedDate) {
            this._startedDate = startedDate
            this.updateData()
        }

    private var _cancellationCreator: PunishmentCreator?
    private var _cancellationDate: Long
    private var _cancellationReason: String
    private var _cancelled: Boolean
    private var _duration: Long
    private var _startedDate: Long

    constructor(
        pluginManager: PluginManager,
        type: PunishmentType,
        id: String,
        target: PunishmentTarget,
        creator: PunishmentCreator,
        createdDate: Long,
        startedDate: Long,
        duration: Long,
        reason: String,
        comment: String,
        cancellationCreator: PunishmentCreator?,
        cancellationDate: Long?,
        cancellationReason: String?,
        servers: List<String>,
        cancelled: Boolean
    ) : super(pluginManager, type, id, target, creator, createdDate, reason, comment, servers) {
        this._cancellationCreator = cancellationCreator
        this._cancellationDate = cancellationDate ?: -1L
        this._cancellationReason = cancellationReason ?: ""
        this._cancelled = cancelled
        this._duration = duration
        this._startedDate = startedDate
    }

    @Synchronized
    override fun activate() {
        cancelled = false
        val activePunishments = pluginManager
            .punishmentManager.getActivePunishments(target.uniqueId, this.javaClass)
        val size = activePunishments.size
        if (size > 0) {
            activePunishments.sort()
            val last = activePunishments[size - 1]
            startedDate = last.startedDate + last.duration
        } else {
            startedDate = createdDate
        }

        super.activate()
    }

    override fun deactivate(cancellationCreator: PunishmentCreator,
                            cancellationDate: Long,
                            cancellationReason: String) {
        if (cancelled) {
            return
        }

        this.deleteAndSortPunishments()

        cancelled = true
        this.cancellationCreator = cancellationCreator
        this.cancellationDate = cancellationDate
        this.cancellationReason = cancellationReason
        pluginManager.deactivatePunishment(this)
        updateData()
        sendMessageAboutDeactivate()
    }

    @Synchronized
    override fun delete() {
        if (!cancelled) {
            this.deleteAndSortPunishments()
        }

        super.delete()
    }

    override fun getStatus(): PunishmentStatus {
        return if (cancelled) {
            PunishmentStatus.CANCELLED
        } else if (System.currentTimeMillis() > createdDate + duration) {
            PunishmentStatus.PASSED
        } else {
            PunishmentStatus.ACTIVE
        }
    }

    abstract fun getDeactivateMessageForListener(): String
    abstract fun getDeactivateMessageForExecutor(): String
    abstract fun getDeactivateMessageForTarget(): String?

    private fun sendMessageAboutDeactivate() {
        sendMessageToListeners(getDeactivateMessageForListener())
        sendMessageToCreator(getDeactivateMessageForExecutor())
        sendMessageToTarget(getDeactivateMessageForTarget())
    }

    @Synchronized
    private fun deleteAndSortPunishments() {
        val history = pluginManager.punishmentManager.getPlayerHistory(target.uniqueId, this.javaClass)
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
                        curr.startedDate = history[0].startedDate
                    } else {
                        curr.startedDate = prev.startedDate + prev.duration
                    }
                    prev = curr
                    index++
                    curr.updateData()
                }
            } else if (index != size - 1) {
                prev = history[index - 1]
                while (index < size - 1) {
                    curr = history[index + 1]
                    curr.startedDate = prev!!.startedDate + prev.duration
                    prev = curr
                    index++
                    curr.updateData()
                }
            }
        }
    }
}