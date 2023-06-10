package me.kvalbrus.multibans.common.punishment.punishments

import me.kvalbrus.multibans.api.punishment.action.ActivationAction
import me.kvalbrus.multibans.api.punishment.action.CreationAction
import me.kvalbrus.multibans.api.punishment.action.DeactivationAction
import me.kvalbrus.multibans.api.punishment.punishments.PunishmentType
import me.kvalbrus.multibans.api.punishment.punishments.PermanentlyBanIp
import me.kvalbrus.multibans.common.managers.PluginManager
import me.kvalbrus.multibans.common.permissions.Permission
import me.kvalbrus.multibans.common.utils.Message.*
import me.kvalbrus.multibans.common.punishment.MultiPermanentlyPunishment

final class MultiPermanentlyBanIp : MultiPermanentlyPunishment, PermanentlyBanIp {

    constructor(
        pluginManager: PluginManager,
        id: String,
        creationAction: CreationAction,
        activations: MutableList<ActivationAction> = mutableListOf(),
        deactivations: MutableList<DeactivationAction> = mutableListOf(),
        comment: String,
        servers: List<String>,
        cancelled: Boolean) : super(pluginManager, PunishmentType.BAN_IP, id, creationAction,
        activations, deactivations, comment, servers, cancelled)

    override val activateMessageForListener: String = BANIP_ACTIVATE_LISTEN.message
    override val activateMessageForExecutor: String = BANIP_ACTIVATE_EXECUTOR.message
    override val activateMessageForTarget: String = ""
    override val deactivateMessageForListener: String = BANIP_DEACTIVATE_LISTEN.message
    override val deactivateMessageForExecutor: String = BANIP_DEACTIVATE_EXECUTOR.message
    override val deactivateMessageForTarget: String = ""
    override val createMessageForListener: String = BANIP_ACTIVATE_LISTEN.message
    override val createMessageForExecutor: String = BANIP_ACTIVATE_EXECUTOR.message
    override val createMessageForTarget: String = ""
    override val deleteMessageForListener: String = BANIP_DELETE_LISTEN.message
    override val deleteMessageForExecutor: String = BANIP_DELETE_EXECUTOR.message
    override val deleteMessageForTarget: String = ""
    override val reasonChangeMessageForExecutor: String = BANIP_REASON_CREATE_CHANGE_EXECUTOR.message
    override val reasonChangeMessageForListener: String = BANIP_REASON_CREATE_CHANGE_LISTEN.message
    override val commentChangeMessageForExecutor: String = BANIP_COMMENT_CHANGE_EXECUTOR.message
    override val commentChangeMessageForListener: String = BANIP_COMMENT_CHANGE_LISTEN.message
    override val permissionForListener: Permission = Permission.PUNISHMENT_BANIP_LISTEN
}