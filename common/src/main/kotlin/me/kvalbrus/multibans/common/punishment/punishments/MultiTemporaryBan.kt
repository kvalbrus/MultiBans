package me.kvalbrus.multibans.common.punishment.punishments

import me.kvalbrus.multibans.api.punishment.action.Action
import me.kvalbrus.multibans.api.punishment.action.CreationAction
import me.kvalbrus.multibans.api.punishment.punishments.PunishmentType
import me.kvalbrus.multibans.api.punishment.punishments.TemporaryBan
import me.kvalbrus.multibans.common.managers.PluginManager
import me.kvalbrus.multibans.common.permissions.Permission
import me.kvalbrus.multibans.common.utils.Message.*
import me.kvalbrus.multibans.common.punishment.MultiTemporaryPunishment
import me.kvalbrus.multibans.common.utils.Message

class MultiTemporaryBan : MultiTemporaryPunishment, TemporaryBan {

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
        cancelled: Boolean) : super(pluginManager, PunishmentType.TEMP_BAN, id, creationAction,
        activations, deactivations, startedDate, duration, comment, servers, cancelled)

    override val activateMessageForListener: Message = TEMPBAN_ACTIVATE_LISTEN
    override val activateMessageForExecutor: Message = TEMPBAN_ACTIVATE_EXECUTOR
    override val activateMessageForTarget: Message = TEMPBAN_ACTIVATE_TARGET
    override val deactivateMessageForListener: Message = TEMPBAN_DEACTIVATE_LISTEN
    override val deactivateMessageForExecutor: Message = TEMPBAN_DEACTIVATE_EXECUTOR
    override val deactivateMessageForTarget: Message = EMPTY
    override val createMessageForListener: Message = TEMPBAN_ACTIVATE_LISTEN
    override val createMessageForExecutor: Message = TEMPBAN_ACTIVATE_EXECUTOR
    override val createMessageForTarget: Message = TEMPBAN_ACTIVATE_TARGET
    override val deleteMessageForListener: Message = TEMPBAN_DELETE_LISTEN
    override val deleteMessageForExecutor: Message = TEMPBAN_DELETE_EXECUTOR
    override val deleteMessageForTarget: Message = TEMPBAN_DELETE_TARGET
    override val reasonChangeMessageForExecutor: Message = TEMPBAN_REASON_CREATE_CHANGE_EXECUTOR
    override val reasonChangeMessageForListener: Message = TEMPBAN_REASON_CREATE_CHANGE_LISTEN
    override val commentChangeMessageForExecutor: Message = TEMPBAN_COMMENT_CHANGE_EXECUTOR
    override val commentChangeMessageForListener: Message = TEMPBAN_COMMENT_CHANGE_LISTEN
    override val permissionForListener: Permission = Permission.PUNISHMENT_TEMPBAN_LISTEN
}