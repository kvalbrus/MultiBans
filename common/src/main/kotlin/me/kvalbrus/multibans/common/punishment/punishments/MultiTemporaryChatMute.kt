package me.kvalbrus.multibans.common.punishment.punishments

import me.kvalbrus.multibans.api.punishment.PunishmentType
import me.kvalbrus.multibans.api.punishment.creator.PunishmentCreator
import me.kvalbrus.multibans.api.punishment.punishments.TemporaryChatMute
import me.kvalbrus.multibans.api.punishment.target.PunishmentTarget
import me.kvalbrus.multibans.common.managers.PluginManager
import me.kvalbrus.multibans.common.permissions.Permission
import me.kvalbrus.multibans.common.utils.Message
import me.kvalbrus.multibans.common.punishment.MultiTemporaryPunishment

final class MultiTemporaryChatMute : MultiTemporaryPunishment,
    TemporaryChatMute {

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
                cancelled: Boolean) : super(pluginManager, PunishmentType.TEMP_MUTE, id, target,
        creator, createdDate, startedDate, duration, reason, comment, cancellationCreator,
        cancellationDate, cancellationReason, servers, cancelled)

    override fun getActivateMessageForListener(): String = Message.TEMPMUTECHAT_ACTIVATE_LISTEN.message
    override fun getActivateMessageForExecutor(): String = Message.TEMPMUTECHAT_ACTIVATE_EXECUTOR.message
    override fun getActivateMessageForTarget(): String = Message.TEMPMUTECHAT_ACTIVATE_TARGET.message

    override fun getDeactivateMessageForListener(): String = Message.TEMPMUTECHAT_DEACTIVATE_LISTEN.message
    override fun getDeactivateMessageForExecutor(): String = Message.TEMPMUTECHAT_DEACTIVATE_EXECUTOR.message
    override fun getDeactivateMessageForTarget(): String = Message.TEMPMUTECHAT_DEACTIVATE_TARGET.message

    override fun getDeleteMessageForListener(): String = Message.TEMPMUTECHAT_DELETE_LISTEN.message
    override fun getDeleteMessageForExecutor(): String = Message.TEMPMUTECHAT_DELETE_EXECUTOR.message
    override fun getDeleteMessageForTarget(): String = Message.TEMPMUTECHAT_DELETE_TARGET.message

    override fun getReasonChangeMessageForExecutor(): String = Message.TEMPMUTECHAT_REASON_CREATE_CHANGE_EXECUTOR.message
    override fun getReasonChangeMessageForListener(): String = Message.TEMPMUTECHAT_REASON_CREATE_CHANGE_LISTEN.message

    override fun getCommentChangeMessageForExecutor(): String = Message.TEMPMUTECHAT_COMMENT_CHANGE_EXECUTOR.message
    override fun getCommentChangeMessageForListener(): String = Message.TEMPMUTECHAT_COMMENT_CHANGE_LISTEN.message

    override fun getPermissionForListener(): Permission = Permission.PUNISHMENT_TEMPMUTECHAT_LISTEN
}