package me.kvalbrus.multibans.common.punishment.punishments

import me.kvalbrus.multibans.api.punishment.action.CreationAction
import me.kvalbrus.multibans.api.punishment.punishments.PunishmentStatus
import me.kvalbrus.multibans.api.punishment.punishments.PunishmentType
import me.kvalbrus.multibans.common.managers.PluginManager
import me.kvalbrus.multibans.common.permissions.Permission
import me.kvalbrus.multibans.common.utils.Message.*
import me.kvalbrus.multibans.common.punishment.MultiPunishment
import me.kvalbrus.multibans.common.utils.Message

final class Kick : MultiPunishment {

    constructor(pluginManager: PluginManager,
                id: String,
                creationAction: CreationAction,
                comment: String,
                servers: List<String>) :
            super(pluginManager, PunishmentType.KICK, id, creationAction, comment, servers)

    override val createMessageForListener: Message = KICK_ACTIVATE_LISTEN
    override val createMessageForExecutor: Message = KICK_ACTIVATE_EXECUTOR
    override val createMessageForTarget: Message = KICK_ACTIVATE_TARGET
    override val deleteMessageForListener: Message = KICK_DELETE_LISTEN
    override val deleteMessageForExecutor: Message = KICK_ACTIVATE_EXECUTOR
    override val deleteMessageForTarget: Message = KICK_ACTIVATE_TARGET
    override val reasonChangeMessageForExecutor: Message = KICK_REASON_CREATE_CHANGE_EXECUTOR
    override val reasonChangeMessageForListener: Message = KICK_REASON_CREATE_CHANGE_LISTEN
    override val commentChangeMessageForExecutor: Message = KICK_COMMENT_CHANGE_EXECUTOR
    override val commentChangeMessageForListener: Message = KICK_COMMENT_CHANGE_LISTEN
    override val permissionForListener: Permission = Permission.PUNISHMENT_KICK_LISTEN

    override fun getStatus(): PunishmentStatus = PunishmentStatus.ACTIVE
}