package me.kvalbrus.multibans.common.punishment.punishments

import me.kvalbrus.multibans.api.punishment.action.CreationAction
import me.kvalbrus.multibans.api.punishment.punishments.PunishmentStatus
import me.kvalbrus.multibans.api.punishment.punishments.PunishmentType
import me.kvalbrus.multibans.common.managers.PluginManager
import me.kvalbrus.multibans.common.permissions.Permission
import me.kvalbrus.multibans.common.utils.Message.*
import me.kvalbrus.multibans.common.punishment.MultiPunishment

final class Kick : MultiPunishment {

    constructor(pluginManager: PluginManager,
                id: String,
                creationAction: CreationAction,
                comment: String,
                servers: List<String>) :
            super(pluginManager, PunishmentType.KICK, id, creationAction, comment, servers)

//    constructor(pluginManager: PluginManager,
//                id: String,
//                target: PunishmentTarget,
//                creator: PunishmentExecutor,
//                createdDate: Long,
//                reason: String,
//                comment: String,
//                servers: List<String>) : super(pluginManager, PunishmentType.KICK, id, target,
//        creator, createdDate, reason, comment, servers)

    override val createMessageForListener: String = KICK_ACTIVATE_LISTEN.message
    override val createMessageForExecutor: String = KICK_ACTIVATE_EXECUTOR.message
    override val createMessageForTarget: String = KICK_ACTIVATE_TARGET.message
    override val deleteMessageForListener: String = KICK_DELETE_LISTEN.message
    override val deleteMessageForExecutor: String = KICK_ACTIVATE_EXECUTOR.message
    override val deleteMessageForTarget: String = KICK_ACTIVATE_TARGET.message
    override val reasonChangeMessageForExecutor: String = KICK_REASON_CREATE_CHANGE_EXECUTOR.message
    override val reasonChangeMessageForListener: String = KICK_REASON_CREATE_CHANGE_LISTEN.message
    override val commentChangeMessageForExecutor: String = KICK_COMMENT_CHANGE_EXECUTOR.message
    override val commentChangeMessageForListener: String = KICK_COMMENT_CHANGE_LISTEN.message
    override val permissionForListener: Permission = Permission.PUNISHMENT_KICK_LISTEN

    override fun getStatus(): PunishmentStatus = PunishmentStatus.ACTIVE
}