package me.kvalbrus.multibans.common.punishment.punishments

import me.kvalbrus.multibans.api.punishment.action.Action
import me.kvalbrus.multibans.api.punishment.action.CreationAction
import me.kvalbrus.multibans.api.punishment.punishments.PunishmentType
import me.kvalbrus.multibans.api.punishment.punishments.TemporaryChatMute
import me.kvalbrus.multibans.common.managers.PluginManager
import me.kvalbrus.multibans.common.permissions.Permission
import me.kvalbrus.multibans.common.utils.Message.*
import me.kvalbrus.multibans.common.punishment.MultiTemporaryPunishment
import me.kvalbrus.multibans.common.utils.Message

final class MultiTemporaryChatMute : MultiTemporaryPunishment, TemporaryChatMute {

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
        cancelled: Boolean) : super(pluginManager, PunishmentType.TEMP_MUTE, id, creationAction,
        activations, deactivations, startedDate, duration, comment, servers, cancelled)

    override val activateMessageForListener: Message = TEMPMUTECHAT_ACTIVATE_LISTEN
    override val activateMessageForExecutor: Message = TEMPMUTECHAT_ACTIVATE_EXECUTOR
    override val activateMessageForTarget: Message = TEMPMUTECHAT_ACTIVATE_TARGET
    override val deactivateMessageForListener: Message = TEMPMUTECHAT_DEACTIVATE_LISTEN
    override val deactivateMessageForExecutor: Message = TEMPMUTECHAT_DEACTIVATE_EXECUTOR
    override val deactivateMessageForTarget: Message = TEMPMUTECHAT_DEACTIVATE_TARGET
    override val createMessageForListener: Message = TEMPMUTECHAT_ACTIVATE_LISTEN
    override val createMessageForExecutor: Message = TEMPMUTECHAT_ACTIVATE_EXECUTOR
    override val createMessageForTarget: Message = TEMPMUTECHAT_ACTIVATE_TARGET
    override val deleteMessageForListener: Message = TEMPMUTECHAT_DELETE_LISTEN
    override val deleteMessageForExecutor: Message = TEMPMUTECHAT_DELETE_EXECUTOR
    override val deleteMessageForTarget: Message = TEMPMUTECHAT_DELETE_TARGET
    override val reasonChangeMessageForExecutor: Message = TEMPMUTECHAT_REASON_CREATE_CHANGE_EXECUTOR
    override val reasonChangeMessageForListener: Message = TEMPMUTECHAT_REASON_CREATE_CHANGE_LISTEN
    override val commentChangeMessageForExecutor: Message = TEMPMUTECHAT_COMMENT_CHANGE_EXECUTOR
    override val commentChangeMessageForListener: Message = TEMPMUTECHAT_COMMENT_CHANGE_LISTEN
    override val permissionForListener: Permission = Permission.PUNISHMENT_TEMPMUTECHAT_LISTEN
}