package me.kvalbrus.multibans.common.punishment.punishments

import me.kvalbrus.multibans.api.punishment.action.Action
import me.kvalbrus.multibans.api.punishment.action.ActivationAction
import me.kvalbrus.multibans.api.punishment.action.CreationAction
import me.kvalbrus.multibans.api.punishment.action.DeactivationAction
import me.kvalbrus.multibans.api.punishment.punishments.PunishmentType
import me.kvalbrus.multibans.api.punishment.punishments.PermanentlyChatMute
import me.kvalbrus.multibans.common.managers.PluginManager
import me.kvalbrus.multibans.common.permissions.Permission
import me.kvalbrus.multibans.common.utils.Message.*
import me.kvalbrus.multibans.common.punishment.MultiPermanentlyPunishment

final class MultiPermanentlyChatMute : MultiPermanentlyPunishment, PermanentlyChatMute {

    constructor(
        pluginManager: PluginManager,
        id: String,
        creationAction: CreationAction,
        activations: MutableList<Action> = mutableListOf(),
        deactivations: MutableList<Action> = mutableListOf(),
        comment: String,
        servers: List<String>,
        cancelled: Boolean) : super(pluginManager, PunishmentType.MUTE, id, creationAction,
        activations, deactivations, comment, servers, cancelled)

    override val activateMessageForListener: String = MUTECHAT_ACTIVATE_LISTEN.message
    override val activateMessageForExecutor: String = MUTECHAT_ACTIVATE_EXECUTOR.message
    override val activateMessageForTarget: String = MUTECHAT_ACTIVATE_TARGET.message
    override val deactivateMessageForListener: String = MUTECHAT_DEACTIVATE_LISTEN.message
    override val deactivateMessageForExecutor: String = MUTECHAT_DEACTIVATE_EXECUTOR.message
    override val deactivateMessageForTarget: String = MUTECHAT_DEACTIVATE_TARGET.message
    override val createMessageForListener: String = MUTECHAT_ACTIVATE_LISTEN.message
    override val createMessageForExecutor: String = MUTECHAT_ACTIVATE_EXECUTOR.message
    override val createMessageForTarget: String = MUTECHAT_ACTIVATE_TARGET.message
    override val deleteMessageForListener: String = MUTECHAT_DELETE_LISTEN.message
    override val deleteMessageForExecutor: String = MUTECHAT_DELETE_EXECUTOR.message
    override val deleteMessageForTarget: String = MUTECHAT_DELETE_TARGET.message
    override val reasonChangeMessageForExecutor: String = MUTECHAT_REASON_CREATE_CHANGE_EXECUTOR.message
    override val reasonChangeMessageForListener: String = MUTECHAT_REASON_CREATE_CHANGE_LISTEN.message
    override val commentChangeMessageForExecutor: String = MUTECHAT_COMMENT_CHANGE_EXECUTOR.message
    override val commentChangeMessageForListener: String = MUTECHAT_COMMENT_CHANGE_LISTEN.message
    override val permissionForListener: Permission = Permission.PUNISHMENT_MUTECHAT_LISTEN
}