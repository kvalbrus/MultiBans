package me.kvalbrus.multibans.common.punishment.punishments

import me.kvalbrus.multibans.api.punishment.PunishmentType
import me.kvalbrus.multibans.api.punishment.creator.PunishmentCreator
import me.kvalbrus.multibans.api.punishment.punishments.PermanentlyChatMute
import me.kvalbrus.multibans.api.punishment.target.PunishmentTarget
import me.kvalbrus.multibans.common.managers.PluginManager
import me.kvalbrus.multibans.common.permissions.Permission
import me.kvalbrus.multibans.common.utils.Message
import me.kvalbrus.multibans.common.punishment.MultiPermanentlyPunishment

final class MultiPermanentlyChatMute : MultiPermanentlyPunishment,
    PermanentlyChatMute {

    constructor(pluginManager: PluginManager,
                id: String, target:
                PunishmentTarget,
                creator: PunishmentCreator,
                createdDate: Long, reason:
                String, comment: String,
                cancellationCreator: PunishmentCreator?,
                cancellationDate: Long?,
                cancellationReason: String?,
                servers: List<String>,
                cancelled: Boolean) : super(
        pluginManager, PunishmentType.MUTE, id, target, creator, createdDate, reason, comment,
        cancellationCreator, cancellationDate, cancellationReason, servers, cancelled)

    override fun getActivateMessageForListener(): String = Message.MUTECHAT_ACTIVATE_LISTEN.message
    override fun getActivateMessageForExecutor(): String = Message.MUTECHAT_ACTIVATE_EXECUTOR.message
    override fun getActivateMessageForTarget(): String = Message.MUTECHAT_ACTIVATE_EXECUTOR.message

    override fun getDeactivateMessageForListener(): String = Message.MUTECHAT_DEACTIVATE_LISTEN.message
    override fun getDeactivateMessageForExecutor(): String = Message.MUTECHAT_DEACTIVATE_EXECUTOR.message
    override fun getDeactivateMessageForTarget(): String = Message.MUTECHAT_DEACTIVATE_TARGET.message

    override fun getDeleteMessageForListener(): String = Message.MUTECHAT_DELETE_LISTEN.message
    override fun getDeleteMessageForExecutor(): String = Message.MUTECHAT_DELETE_EXECUTOR.message
    override fun getDeleteMessageForTarget(): String = Message.MUTECHAT_DELETE_EXECUTOR.message

    override fun getReasonChangeMessageForExecutor(): String = Message.MUTECHAT_REASON_CREATE_CHANGE_EXECUTOR.message
    override fun getReasonChangeMessageForListener(): String = Message.MUTECHAT_REASON_CREATE_CHANGE_LISTEN.message

    override fun getCommentChangeMessageForExecutor(): String = Message.MUTECHAT_COMMENT_CHANGE_EXECUTOR.message
    override fun getCommentChangeMessageForListener(): String = Message.MUTECHAT_COMMENT_CHANGE_LISTEN.message

    override fun getPermissionForListener(): Permission = Permission.PUNISHMENT_MUTECHAT_LISTEN
}