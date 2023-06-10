package me.kvalbrus.multibans.common.punishment.punishments

import me.kvalbrus.multibans.api.punishment.action.ActivationAction
import me.kvalbrus.multibans.api.punishment.action.CreationAction
import me.kvalbrus.multibans.api.punishment.action.DeactivationAction
import me.kvalbrus.multibans.api.punishment.punishments.PunishmentType
import me.kvalbrus.multibans.api.punishment.punishments.TemporaryBanIp
import me.kvalbrus.multibans.common.managers.PluginManager
import me.kvalbrus.multibans.common.permissions.Permission
import me.kvalbrus.multibans.common.utils.Message.*
import me.kvalbrus.multibans.common.punishment.MultiTemporaryPunishment

final class MultiTemporaryBanIp : MultiTemporaryPunishment, TemporaryBanIp {

    constructor(
        pluginManager: PluginManager,
        id: String,
        creationAction: CreationAction,
        activations: MutableList<ActivationAction> = mutableListOf(),
        deactivations: MutableList<DeactivationAction> = mutableListOf(),
        startedDate: Long,
        duration: Long,
        comment: String,
        servers: List<String>,
        cancelled: Boolean) : super(pluginManager, PunishmentType.TEMP_BAN_IP, id, creationAction,
        activations, deactivations, startedDate, duration, comment, servers, cancelled)

    override val activateMessageForListener: String = TEMPBANIP_ACTIVATE_LISTEN.message
    override val activateMessageForExecutor: String = TEMPBANIP_ACTIVATE_EXECUTOR.message
    override val activateMessageForTarget: String = ""
    override val deactivateMessageForListener: String = TEMPBANIP_DEACTIVATE_LISTEN.message
    override val deactivateMessageForExecutor: String = TEMPBANIP_DEACTIVATE_EXECUTOR.message
    override val deactivateMessageForTarget: String = ""
    override val createMessageForListener: String = TEMPBANIP_ACTIVATE_LISTEN.message
    override val createMessageForExecutor: String = TEMPBANIP_ACTIVATE_EXECUTOR.message
    override val createMessageForTarget: String = ""
    override val deleteMessageForListener: String = TEMPBANIP_DELETE_LISTEN.message
    override val deleteMessageForExecutor: String = TEMPBANIP_DELETE_EXECUTOR.message
    override val deleteMessageForTarget: String = ""
    override val reasonChangeMessageForExecutor: String = TEMPBANIP_REASON_CREATE_CHANGE_EXECUTOR.message
    override val reasonChangeMessageForListener: String = TEMPBANIP_REASON_CREATE_CHANGE_LISTEN.message
    override val commentChangeMessageForExecutor: String = TEMPBANIP_COMMENT_CHANGE_EXECUTOR.message
    override val commentChangeMessageForListener: String = TEMPBANIP_COMMENT_CHANGE_LISTEN.message
    override val permissionForListener: Permission = Permission.PUNISHMENT_TEMPBANIP_LISTEN
}