package me.kvalbrus.multibans.common.punishment.punishments

import me.kvalbrus.multibans.api.punishment.action.Action
import me.kvalbrus.multibans.api.punishment.action.ActivationAction
import me.kvalbrus.multibans.api.punishment.action.CreationAction
import me.kvalbrus.multibans.api.punishment.action.DeactivationAction
import me.kvalbrus.multibans.api.punishment.punishments.PunishmentType
import me.kvalbrus.multibans.api.punishment.punishments.TemporaryChatMute
import me.kvalbrus.multibans.common.managers.PluginManager
import me.kvalbrus.multibans.common.permissions.Permission
import me.kvalbrus.multibans.common.utils.Message.*
import me.kvalbrus.multibans.common.punishment.MultiTemporaryPunishment

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

    override val activateMessageForListener: String = TEMPMUTECHAT_ACTIVATE_LISTEN.message
    override val activateMessageForExecutor: String = TEMPMUTECHAT_ACTIVATE_EXECUTOR.message
    override val activateMessageForTarget: String = TEMPMUTECHAT_ACTIVATE_TARGET.message
    override val deactivateMessageForListener: String = TEMPMUTECHAT_DEACTIVATE_LISTEN.message
    override val deactivateMessageForExecutor: String = TEMPMUTECHAT_DEACTIVATE_EXECUTOR.message
    override val deactivateMessageForTarget: String = TEMPMUTECHAT_DEACTIVATE_TARGET.message
    override val createMessageForListener: String = TEMPMUTECHAT_ACTIVATE_LISTEN.message
    override val createMessageForExecutor: String = TEMPMUTECHAT_ACTIVATE_EXECUTOR.message
    override val createMessageForTarget: String = TEMPMUTECHAT_ACTIVATE_TARGET.message
    override val deleteMessageForListener: String = TEMPMUTECHAT_DELETE_LISTEN.message
    override val deleteMessageForExecutor: String = TEMPMUTECHAT_DELETE_EXECUTOR.message
    override val deleteMessageForTarget: String = TEMPMUTECHAT_DELETE_TARGET.message
    override val reasonChangeMessageForExecutor: String = TEMPMUTECHAT_REASON_CREATE_CHANGE_EXECUTOR.message
    override val reasonChangeMessageForListener: String = TEMPMUTECHAT_REASON_CREATE_CHANGE_LISTEN.message
    override val commentChangeMessageForExecutor: String = TEMPMUTECHAT_COMMENT_CHANGE_EXECUTOR.message
    override val commentChangeMessageForListener: String = TEMPMUTECHAT_COMMENT_CHANGE_LISTEN.message
    override val permissionForListener: Permission = Permission.PUNISHMENT_TEMPMUTECHAT_LISTEN
}