package me.kvalbrus.multibans.common.punishment.punishments

import me.kvalbrus.multibans.api.punishment.action.Action
import me.kvalbrus.multibans.api.punishment.action.CreationAction
import me.kvalbrus.multibans.api.punishment.punishments.PunishmentType
import me.kvalbrus.multibans.api.punishment.punishments.TemporaryBanIp
import me.kvalbrus.multibans.common.managers.PluginManager
import me.kvalbrus.multibans.common.permissions.Permission
import me.kvalbrus.multibans.common.utils.Message.*
import me.kvalbrus.multibans.common.punishment.MultiTemporaryPunishment
import me.kvalbrus.multibans.common.utils.Message

final class MultiTemporaryBanIp : MultiTemporaryPunishment, TemporaryBanIp {

    constructor(
        pluginManager: PluginManager,
        id: String,
        creationAction: CreationAction,
        activations: MutableList<Action> = mutableListOf(),
        deactivations: MutableList<Action> = mutableListOf(),
        startedDate: Long,
        duration: Long,
        comment: String,
        servers: List<String>,
        cancelled: Boolean) : super(pluginManager, PunishmentType.TEMP_BAN_IP, id, creationAction,
        activations, deactivations, startedDate, duration, comment, servers, cancelled)

    override val activateMessageForListener: Message = TEMPBANIP_ACTIVATE_LISTEN
    override val activateMessageForExecutor: Message = TEMPBANIP_ACTIVATE_EXECUTOR
    override val activateMessageForTarget: Message = TEMPBANIP_ACTIVATE_TARGET
    override val deactivateMessageForListener: Message = TEMPBANIP_DEACTIVATE_LISTEN
    override val deactivateMessageForExecutor: Message = TEMPBANIP_DEACTIVATE_EXECUTOR
    override val deactivateMessageForTarget: Message = EMPTY
    override val createMessageForListener: Message = TEMPBANIP_ACTIVATE_LISTEN
    override val createMessageForExecutor: Message = TEMPBANIP_ACTIVATE_EXECUTOR
    override val createMessageForTarget: Message = TEMPBAN_ACTIVATE_TARGET
    override val deleteMessageForListener: Message = TEMPBANIP_DELETE_LISTEN
    override val deleteMessageForExecutor: Message = TEMPBANIP_DELETE_EXECUTOR
    override val deleteMessageForTarget: Message = TEMPBANIP_DELETE_TARGET
    override val reasonChangeMessageForExecutor: Message = TEMPBANIP_REASON_CREATE_CHANGE_EXECUTOR
    override val reasonChangeMessageForListener: Message = TEMPBANIP_REASON_CREATE_CHANGE_LISTEN
    override val commentChangeMessageForExecutor: Message = TEMPBANIP_COMMENT_CHANGE_EXECUTOR
    override val commentChangeMessageForListener: Message = TEMPBANIP_COMMENT_CHANGE_LISTEN
    override val permissionForListener: Permission = Permission.PUNISHMENT_TEMPBANIP_LISTEN
}