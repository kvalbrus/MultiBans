package me.kvalbrus.multibans.common.punishment.punishments

import me.kvalbrus.multibans.api.punishment.PunishmentType
import me.kvalbrus.multibans.api.punishment.creator.PunishmentCreator
import me.kvalbrus.multibans.api.punishment.punishments.TemporaryBan
import me.kvalbrus.multibans.api.punishment.target.PunishmentTarget
import me.kvalbrus.multibans.common.managers.PluginManager
import me.kvalbrus.multibans.common.permissions.Permission
import me.kvalbrus.multibans.common.utils.Message
import me.kvalbrus.multibans.common.punishment.MultiTemporaryPunishment

class MultiTemporaryBan : MultiTemporaryPunishment,
    TemporaryBan {

    constructor(pluginManager: PluginManager,
                id: String,
                target: PunishmentTarget,
                creator: PunishmentCreator,
                createdDate: Long, startedDate: Long,
                duration: Long,
                reason: String,
                comment: String,
                cancellationCreator: PunishmentCreator?,
                cancellationDate: Long?,
                cancellationReason: String?,
                servers: List<String>,
                cancelled: Boolean) : super(pluginManager, PunishmentType.TEMP_BAN, id, target,
        creator, createdDate, startedDate, duration, reason, comment, cancellationCreator,
        cancellationDate, cancellationReason, servers, cancelled)

    override fun getActivateMessageForListener(): String = Message.TEMPBAN_ACTIVATE_LISTEN.message
    override fun getActivateMessageForExecutor(): String = Message.TEMPBAN_ACTIVATE_EXECUTOR.message
    override fun getActivateMessageForTarget(): String? = null

    override fun getDeactivateMessageForListener(): String = Message.TEMPBAN_DEACTIVATE_LISTEN.message
    override fun getDeactivateMessageForExecutor(): String = Message.TEMPBAN_DEACTIVATE_EXECUTOR.message
    override fun getDeactivateMessageForTarget(): String? = null

    override fun getDeleteMessageForListener(): String = Message.TEMPBAN_DELETE_LISTEN.message
    override fun getDeleteMessageForExecutor(): String = Message.TEMPBAN_DELETE_EXECUTOR.message
    override fun getDeleteMessageForTarget(): String? = null

    override fun getReasonChangeMessageForExecutor(): String = Message.TEMPBAN_REASON_CREATE_CHANGE_EXECUTOR.message
    override fun getReasonChangeMessageForListener(): String = Message.TEMPBAN_REASON_CREATE_CHANGE_LISTEN.message

    override fun getCommentChangeMessageForExecutor(): String = Message.TEMPBAN_REASON_CREATE_CHANGE_EXECUTOR.message
    override fun getCommentChangeMessageForListener(): String = Message.TEMPBAN_COMMENT_CHANGE_EXECUTOR.message

    override fun getPermissionForListener(): Permission = Permission.PUNISHMENT_TEMPBAN_LISTEN
}