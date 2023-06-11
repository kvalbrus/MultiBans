package me.kvalbrus.multibans.common.punishment.punishments

import me.kvalbrus.multibans.api.punishment.action.Action
import me.kvalbrus.multibans.api.punishment.action.ActivationAction
import me.kvalbrus.multibans.api.punishment.action.CreationAction
import me.kvalbrus.multibans.api.punishment.action.DeactivationAction
import me.kvalbrus.multibans.api.punishment.punishments.PunishmentType
import me.kvalbrus.multibans.api.punishment.executor.PunishmentExecutor
import me.kvalbrus.multibans.api.punishment.punishments.PermanentlyBan
import me.kvalbrus.multibans.api.punishment.target.PunishmentTarget
import me.kvalbrus.multibans.common.managers.PluginManager
import me.kvalbrus.multibans.common.permissions.Permission
import me.kvalbrus.multibans.common.utils.Message.*
import me.kvalbrus.multibans.common.punishment.MultiPermanentlyPunishment

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

    override val activateMessageForListener: String = BAN_ACTIVATE_LISTEN.message
    override val activateMessageForExecutor: String = BAN_ACTIVATE_EXECUTOR.message
    override val activateMessageForTarget: String = ""
    override val deactivateMessageForListener: String = BAN_DEACTIVATE_LISTEN.message
    override val deactivateMessageForExecutor: String = BAN_DEACTIVATE_EXECUTOR.message
    override val deactivateMessageForTarget: String = ""
    override val createMessageForListener: String = BAN_ACTIVATE_LISTEN.message
    override val createMessageForExecutor: String = BAN_ACTIVATE_EXECUTOR.message
    override val createMessageForTarget: String = ""
    override val deleteMessageForListener: String = BAN_DELETE_LISTEN.message
    override val deleteMessageForExecutor: String = BAN_DELETE_EXECUTOR.message
    override val deleteMessageForTarget: String = ""
    override val reasonChangeMessageForExecutor: String = BAN_REASON_CREATE_CHANGE_EXECUTOR.message
    override val reasonChangeMessageForListener: String = BAN_REASON_CREATE_CHANGE_LISTEN.message
    override val commentChangeMessageForExecutor: String = BAN_COMMENT_CHANGE_EXECUTOR.message
    override val commentChangeMessageForListener: String = BAN_COMMENT_CHANGE_LISTEN.message
    override val permissionForListener: Permission = Permission.PUNISHMENT_BAN_LISTEN
}