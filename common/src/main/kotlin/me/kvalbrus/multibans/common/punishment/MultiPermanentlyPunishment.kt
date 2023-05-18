package me.kvalbrus.multibans.common.punishment

import me.kvalbrus.multibans.api.punishment.PermanentlyPunishment
import me.kvalbrus.multibans.api.punishment.PunishmentStatus
import me.kvalbrus.multibans.api.punishment.PunishmentType
import me.kvalbrus.multibans.api.punishment.creator.PunishmentCreator
import me.kvalbrus.multibans.api.punishment.target.PunishmentTarget
import me.kvalbrus.multibans.common.managers.PluginManager

abstract class MultiPermanentlyPunishment : MultiPunishment, PermanentlyPunishment {

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

    private var _cancellationCreator: PunishmentCreator?
    private var _cancellationDate: Long
    private var _cancellationReason: String
    private var _cancelled: Boolean

    constructor(pluginManager: PluginManager,
                type: PunishmentType,
                id: String,
                target: PunishmentTarget,
                creator: PunishmentCreator,
                createdDate: Long,
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
    }


    @Synchronized
    override fun activate() {
        cancelled = false
        super.activate()
    }

    @Synchronized
    override fun deactivate(
        cancellationCreator: PunishmentCreator,
        cancellationDate: Long,
        cancellationReason: String
    ) {
        if (cancelled) {
            return
        }
        cancelled = true
        this.cancellationCreator = cancellationCreator
        this.cancellationDate = cancellationDate
        this.cancellationReason = cancellationReason
        updateData()
        sendMessageAboutDeactivate()
    }

    @Synchronized
    override fun delete() {
        super.delete()
    }

    override fun getStatus(): PunishmentStatus {
        return if (cancelled) {
            PunishmentStatus.CANCELLED
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
}