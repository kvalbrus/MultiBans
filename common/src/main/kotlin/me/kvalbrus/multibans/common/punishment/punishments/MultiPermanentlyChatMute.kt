package me.kvalbrus.multibans.common.punishment.punishments

import me.kvalbrus.multibans.api.punishment.action.Action
import me.kvalbrus.multibans.api.punishment.action.CreationAction
import me.kvalbrus.multibans.api.punishment.punishments.PunishmentType
import me.kvalbrus.multibans.api.punishment.punishments.PermanentlyChatMute
import me.kvalbrus.multibans.common.managers.PluginManager
import me.kvalbrus.multibans.common.permissions.Permission
import me.kvalbrus.multibans.common.utils.Message.*
import me.kvalbrus.multibans.common.punishment.MultiPermanentlyPunishment
import me.kvalbrus.multibans.common.utils.Message

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

    override val activateMessageForListener: Message = MUTECHAT_ACTIVATE_LISTEN
    override val activateMessageForExecutor: Message = MUTECHAT_ACTIVATE_EXECUTOR
    override val activateMessageForTarget: Message = MUTECHAT_ACTIVATE_TARGET
    override val deactivateMessageForListener: Message = MUTECHAT_DEACTIVATE_LISTEN
    override val deactivateMessageForExecutor: Message = MUTECHAT_DEACTIVATE_EXECUTOR
    override val deactivateMessageForTarget: Message = MUTECHAT_DEACTIVATE_TARGET
    override val createMessageForListener: Message = MUTECHAT_ACTIVATE_LISTEN
    override val createMessageForExecutor: Message = MUTECHAT_ACTIVATE_EXECUTOR
    override val createMessageForTarget: Message = MUTECHAT_ACTIVATE_TARGET
    override val deleteMessageForListener: Message = MUTECHAT_DELETE_LISTEN
    override val deleteMessageForExecutor: Message = MUTECHAT_DELETE_EXECUTOR
    override val deleteMessageForTarget: Message = MUTECHAT_DELETE_TARGET
    override val reasonChangeMessageForExecutor: Message = MUTECHAT_REASON_CREATE_CHANGE_EXECUTOR
    override val reasonChangeMessageForListener: Message = MUTECHAT_REASON_CREATE_CHANGE_LISTEN
    override val commentChangeMessageForExecutor: Message = MUTECHAT_COMMENT_CHANGE_EXECUTOR
    override val commentChangeMessageForListener: Message = MUTECHAT_COMMENT_CHANGE_LISTEN
    override val permissionForListener: Permission = Permission.PUNISHMENT_MUTECHAT_LISTEN
}