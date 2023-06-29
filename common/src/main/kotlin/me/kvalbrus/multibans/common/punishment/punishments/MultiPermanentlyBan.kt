package me.kvalbrus.multibans.common.punishment.punishments

import me.kvalbrus.multibans.api.punishment.action.Action
import me.kvalbrus.multibans.api.punishment.action.CreationAction
import me.kvalbrus.multibans.api.punishment.punishments.PunishmentType
import me.kvalbrus.multibans.api.punishment.punishments.PermanentlyBan
import me.kvalbrus.multibans.common.managers.PluginManager
import me.kvalbrus.multibans.common.permissions.Permission
import me.kvalbrus.multibans.common.utils.Message.*
import me.kvalbrus.multibans.common.punishment.MultiPermanentlyPunishment
import me.kvalbrus.multibans.common.utils.Message

final class MultiPermanentlyBan : MultiPermanentlyPunishment, PermanentlyBan {

    constructor(
        pluginManager: PluginManager,
        id: String,
        creationAction: CreationAction,
        activations: MutableList<Action> = mutableListOf(),
        deactivations: MutableList<Action> = mutableListOf(),
        comment: String,
        servers: List<String>,
        cancelled: Boolean) : super(pluginManager, PunishmentType.BAN, id, creationAction,
        activations, deactivations, comment, servers, cancelled)

    override val activateMessageForListener: Message = BAN_ACTIVATE_LISTEN
    override val activateMessageForExecutor: Message = BAN_ACTIVATE_EXECUTOR
    override val activateMessageForTarget: Message = BAN_ACTIVATE_TARGET
    override val deactivateMessageForListener: Message = BAN_DEACTIVATE_LISTEN
    override val deactivateMessageForExecutor: Message = BAN_DEACTIVATE_EXECUTOR
    override val deactivateMessageForTarget: Message = EMPTY
    override val createMessageForListener: Message = BAN_ACTIVATE_LISTEN
    override val createMessageForExecutor: Message = BAN_ACTIVATE_EXECUTOR
    override val createMessageForTarget: Message = BAN_ACTIVATE_TARGET
    override val deleteMessageForListener: Message = BAN_DELETE_LISTEN
    override val deleteMessageForExecutor: Message = BAN_DELETE_EXECUTOR
    override val deleteMessageForTarget: Message = BAN_DELETE_TARGET
    override val reasonChangeMessageForExecutor: Message = BAN_REASON_CREATE_CHANGE_EXECUTOR
    override val reasonChangeMessageForListener: Message = BAN_REASON_CREATE_CHANGE_LISTEN
    override val commentChangeMessageForExecutor: Message = BAN_COMMENT_CHANGE_EXECUTOR
    override val commentChangeMessageForListener: Message = BAN_COMMENT_CHANGE_LISTEN
    override val permissionForListener: Permission = Permission.PUNISHMENT_BAN_LISTEN
}