package me.kvalbrus.multibans.common.punishment.punishments

import me.kvalbrus.multibans.api.punishment.action.Action
import me.kvalbrus.multibans.api.punishment.action.CreationAction
import me.kvalbrus.multibans.api.punishment.punishments.PunishmentType
import me.kvalbrus.multibans.api.punishment.punishments.PermanentlyBanIp
import me.kvalbrus.multibans.common.managers.PluginManager
import me.kvalbrus.multibans.common.permissions.Permission
import me.kvalbrus.multibans.common.utils.Message.*
import me.kvalbrus.multibans.common.punishment.MultiPermanentlyPunishment
import me.kvalbrus.multibans.common.utils.Message

final class MultiPermanentlyBanIp : MultiPermanentlyPunishment, PermanentlyBanIp {

    constructor(
        pluginManager: PluginManager,
        id: String,
        creationAction: CreationAction,
        activations: MutableList<Action> = mutableListOf(),
        deactivations: MutableList<Action> = mutableListOf(),
        comment: String,
        servers: List<String>,
        cancelled: Boolean) : super(pluginManager, PunishmentType.BAN_IP, id, creationAction,
        activations, deactivations, comment, servers, cancelled)

    override val activateMessageForListener: Message = BANIP_ACTIVATE_LISTEN
    override val activateMessageForExecutor: Message = BANIP_ACTIVATE_EXECUTOR
    override val activateMessageForTarget: Message = BANIP_ACTIVATE_TARGET
    override val deactivateMessageForListener: Message = BANIP_DEACTIVATE_LISTEN
    override val deactivateMessageForExecutor: Message = BANIP_DEACTIVATE_EXECUTOR
    override val deactivateMessageForTarget: Message = EMPTY
    override val createMessageForListener: Message = BANIP_ACTIVATE_LISTEN
    override val createMessageForExecutor: Message = BANIP_ACTIVATE_EXECUTOR
    override val createMessageForTarget: Message = BANIP_ACTIVATE_TARGET
    override val deleteMessageForListener: Message = BANIP_DELETE_LISTEN
    override val deleteMessageForExecutor: Message = BANIP_DELETE_EXECUTOR
    override val deleteMessageForTarget: Message = BANIP_DELETE_TARGET
    override val reasonChangeMessageForExecutor: Message = BANIP_REASON_CREATE_CHANGE_EXECUTOR
    override val reasonChangeMessageForListener: Message = BANIP_REASON_CREATE_CHANGE_LISTEN
    override val commentChangeMessageForExecutor: Message = BANIP_COMMENT_CHANGE_EXECUTOR
    override val commentChangeMessageForListener: Message = BANIP_COMMENT_CHANGE_LISTEN
    override val permissionForListener: Permission = Permission.PUNISHMENT_BANIP_LISTEN
}