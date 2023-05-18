package me.kvalbrus.multibans.common.punishment.punishments

import me.kvalbrus.multibans.api.punishment.PunishmentStatus
import me.kvalbrus.multibans.api.punishment.PunishmentType
import me.kvalbrus.multibans.api.punishment.creator.PunishmentCreator
import me.kvalbrus.multibans.api.punishment.target.PunishmentTarget
import me.kvalbrus.multibans.common.managers.PluginManager
import me.kvalbrus.multibans.common.permissions.Permission
import me.kvalbrus.multibans.common.utils.Message
import me.kvalbrus.multibans.common.punishment.MultiPunishment

final class Kick : MultiPunishment {

    constructor(pluginManager: PluginManager,
                id: String,
                target: PunishmentTarget,
                creator: PunishmentCreator,
                createdDate: Long,
                reason: String,
                comment: String,
                servers: List<String>) : super(pluginManager, PunishmentType.KICK, id, target,
        creator, createdDate, reason, comment, servers)

    override fun getStatus(): PunishmentStatus = PunishmentStatus.ACTIVE

    override fun getActivateMessageForListener(): String = Message.KICK_ACTIVATE_LISTEN.message
    override fun getActivateMessageForExecutor(): String = Message.KICK_ACTIVATE_EXECUTOR.message
    override fun getActivateMessageForTarget(): String = Message.KICK_ACTIVATE_TARGET.message

    override fun getDeleteMessageForListener(): String = Message.KICK_DELETE_LISTEN.message
    override fun getDeleteMessageForExecutor(): String = Message.KICK_DELETE_EXECUTOR.message
    override fun getDeleteMessageForTarget(): String = Message.KICK_DELETE_TARGET.message

    override fun getReasonChangeMessageForExecutor(): String = Message.KICK_REASON_CREATE_CHANGE_EXECUTOR.message
    override fun getReasonChangeMessageForListener(): String = Message.KICK_REASON_CREATE_CHANGE_LISTEN.message

    override fun getCommentChangeMessageForExecutor(): String = Message.KICK_COMMENT_CHANGE_EXECUTOR.message
    override fun getCommentChangeMessageForListener(): String = Message.KICK_COMMENT_CHANGE_LISTEN.message

    override fun getPermissionForListener(): Permission = Permission.PUNISHMENT_KICK_EXECUTOR
}