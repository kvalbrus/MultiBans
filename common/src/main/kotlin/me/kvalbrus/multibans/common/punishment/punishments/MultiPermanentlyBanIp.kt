package me.kvalbrus.multibans.common.punishment.punishments

import me.kvalbrus.multibans.api.punishment.PunishmentType
import me.kvalbrus.multibans.api.punishment.creator.PunishmentCreator
import me.kvalbrus.multibans.api.punishment.punishments.PermanentlyBanIp
import me.kvalbrus.multibans.api.punishment.target.PunishmentTarget
import me.kvalbrus.multibans.common.managers.PluginManager
import me.kvalbrus.multibans.common.permissions.Permission
import me.kvalbrus.multibans.common.utils.Message
import me.kvalbrus.multibans.common.punishment.MultiPermanentlyPunishment

final class MultiPermanentlyBanIp : MultiPermanentlyPunishment,
    PermanentlyBanIp {

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
        pluginManager, PunishmentType.BAN_IP, id, target, creator, createdDate, reason, comment,
        cancellationCreator, cancellationDate, cancellationReason, servers, cancelled)

    override fun getActivateMessageForListener(): String = Message.BANIP_ACTIVATE_LISTEN.message
    override fun getActivateMessageForExecutor(): String = Message.BANIP_ACTIVATE_EXECUTOR.message
    override fun getActivateMessageForTarget(): String? = null

    override fun getDeactivateMessageForListener(): String = Message.BANIP_DEACTIVATE_LISTEN.message
    override fun getDeactivateMessageForExecutor(): String = Message.BANIP_DEACTIVATE_EXECUTOR.message
    override fun getDeactivateMessageForTarget(): String? = null

    override fun getDeleteMessageForListener(): String = Message.BANIP_DELETE_LISTEN.message
    override fun getDeleteMessageForExecutor(): String = Message.BANIP_DELETE_EXECUTOR.message
    override fun getDeleteMessageForTarget(): String? = null

    override fun getReasonChangeMessageForExecutor(): String = Message.BANIP_REASON_CREATE_CHANGE_EXECUTOR.message
    override fun getReasonChangeMessageForListener(): String = Message.BANIP_REASON_CREATE_CHANGE_LISTEN.message

    override fun getCommentChangeMessageForExecutor(): String = Message.BANIP_COMMENT_CHANGE_EXECUTOR.message
    override fun getCommentChangeMessageForListener(): String = Message.BANIP_COMMENT_CHANGE_LISTEN.message

    override fun getPermissionForListener(): Permission = Permission.PUNISHMENT_BANIP_LISTEN
}