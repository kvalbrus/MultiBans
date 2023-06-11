package me.kvalbrus.multibans.common.punishment.punishments

import me.kvalbrus.multibans.api.punishment.action.Action
import me.kvalbrus.multibans.api.punishment.action.ActivationAction
import me.kvalbrus.multibans.api.punishment.action.CreationAction
import me.kvalbrus.multibans.api.punishment.action.DeactivationAction
import me.kvalbrus.multibans.api.punishment.punishments.PunishmentType
import me.kvalbrus.multibans.api.punishment.punishments.TemporaryBan
import me.kvalbrus.multibans.common.managers.PluginManager
import me.kvalbrus.multibans.common.permissions.Permission
import me.kvalbrus.multibans.common.utils.Message.*
import me.kvalbrus.multibans.common.punishment.MultiTemporaryPunishment

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

    override val activateMessageForListener: String = TEMPBAN_ACTIVATE_LISTEN.message
    override val activateMessageForExecutor: String = TEMPBAN_ACTIVATE_EXECUTOR.message
    override val activateMessageForTarget: String = ""
    override val deactivateMessageForListener: String = TEMPBAN_DEACTIVATE_LISTEN.message
    override val deactivateMessageForExecutor: String = TEMPBAN_DEACTIVATE_EXECUTOR.message
    override val deactivateMessageForTarget: String = ""
    override val createMessageForListener: String = TEMPBAN_ACTIVATE_LISTEN.message
    override val createMessageForExecutor: String = TEMPBAN_ACTIVATE_EXECUTOR.message
    override val createMessageForTarget: String = ""
    override val deleteMessageForListener: String = TEMPBAN_DELETE_LISTEN.message
    override val deleteMessageForExecutor: String = TEMPBAN_DELETE_EXECUTOR.message
    override val deleteMessageForTarget: String = ""
    override val reasonChangeMessageForExecutor: String = TEMPBAN_REASON_CREATE_CHANGE_EXECUTOR.message
    override val reasonChangeMessageForListener: String = TEMPBAN_REASON_CREATE_CHANGE_LISTEN.message
    override val commentChangeMessageForExecutor: String = TEMPBAN_COMMENT_CHANGE_EXECUTOR.message
    override val commentChangeMessageForListener: String = TEMPBAN_COMMENT_CHANGE_LISTEN.message
    override val permissionForListener: Permission = Permission.PUNISHMENT_TEMPBAN_LISTEN
}