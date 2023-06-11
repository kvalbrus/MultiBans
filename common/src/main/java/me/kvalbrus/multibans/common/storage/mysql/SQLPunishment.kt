package me.kvalbrus.multibans.common.storage.mysql

import me.kvalbrus.multibans.api.OnlinePlayer
import me.kvalbrus.multibans.api.punishment.Cancelable
import me.kvalbrus.multibans.api.punishment.Punishment
import me.kvalbrus.multibans.api.punishment.TemporaryPunishment
import me.kvalbrus.multibans.api.punishment.action.Action
import me.kvalbrus.multibans.api.punishment.action.ActivationAction
import me.kvalbrus.multibans.api.punishment.action.DeactivationAction
import me.kvalbrus.multibans.api.punishment.executor.PunishmentExecutor
import me.kvalbrus.multibans.api.punishment.punishments.PunishmentType
import me.kvalbrus.multibans.api.punishment.target.OnlinePunishmentTarget
import me.kvalbrus.multibans.api.punishment.target.PunishmentTarget
import me.kvalbrus.multibans.common.managers.PluginManager
import me.kvalbrus.multibans.common.punishment.MultiPunishment
import me.kvalbrus.multibans.common.punishment.action.MultiCreationAction
import me.kvalbrus.multibans.common.punishment.creator.MultiConsolePunishmentExecutor
import me.kvalbrus.multibans.common.punishment.creator.MultiPlayerPunishmentExecutor
import me.kvalbrus.multibans.common.punishment.target.MultiOnlinePunishmentTarget
import me.kvalbrus.multibans.common.punishment.target.MultiPunishmentTarget
import java.util.*
import kotlin.collections.ArrayList

class SQLPunishment {

    val id: String
    val type: String
    val targetUuid: String
    val targetIp: String
    val targetName: String
    val creatorName: String
    val createDate: Long
    val startDate: Long
    val duration: Long
    val reason: String
    val comment: String
    val servers: String
    val cancelled: Boolean
    val activations: List<Action>
    val deactivations: List<Action>

    constructor(punishment: Punishment) {
        this.id = punishment.id
        this.type = punishment.type.prefix
        this.targetUuid = punishment.target.uniqueId.toString()

        val target = punishment.target

        if (target is OnlinePunishmentTarget) {
            this.targetIp = target.hostAddress
        } else{
            this.targetIp = ""
        }

        this.targetName = punishment.target.name
        this.creatorName = punishment.creator.name
        this.createDate = punishment.createdDate


        if (punishment is TemporaryPunishment) {
            this.startDate = punishment.startedDate
            this.duration = punishment.duration
        } else {
            this.startDate = -1
            this.duration = -1
        }

        this.reason = punishment.createdReason
        this.comment = punishment.comment

        this.servers = getServers(punishment.servers)

        if (punishment is Cancelable) {
            this.cancelled = punishment.cancelled
            this.activations = punishment.activations
            this.deactivations = punishment.deactivations
        } else {
            this.cancelled = false
            this.activations = listOf()
            this.deactivations = listOf()
        }
    }

    constructor(id: String, type: String, targetUuid: String, targetIp: String, targetName: String,
                creatorName: String, createDate: Long, startDate: Long, duration: Long, reason: String,
                comment: String, servers: String, cancelled: Boolean, activations: List<Action>,
                deactivations: List<Action>) {
        this.id = id
        this.type = type
        this.targetUuid = targetUuid
        this.targetIp = targetIp
        this.targetName = targetName
        this.creatorName = creatorName
        this.createDate = createDate
        this.startDate = startDate
        this.duration = duration
        this.reason = reason
        this.comment = comment
        this.servers = servers
        this.cancelled = cancelled
        this.activations = activations
        this.deactivations = deactivations
    }

    private fun getServers(serversList: List<String>) : String {
        var servers = ""

        var i = 0;
        while (i != serversList.size) {
            servers += serversList[i]
            if (i != serversList.size - 1) {
                servers += ';'
            }

            i++
        }

        return servers
    }

    private fun getServersList(servers: String) : List<String> {
        val serverList = mutableListOf<String>()

        for (server in servers.split(';')) {
            serverList.add(server)
        }

        return serverList
    }

    fun getPunishment(pluginManager: PluginManager) : Punishment {
        val type = PunishmentType.valueOf(this.type)
        val target: PunishmentTarget
        val creator: PunishmentExecutor

        var player = pluginManager.getOfflinePlayer(UUID.fromString(this.targetUuid))
        if (player is OnlinePlayer) {
            target = MultiOnlinePunishmentTarget(player)
        } else {
            target = MultiPunishmentTarget(player)
        }

        if (this.creatorName.equals(pluginManager.console.name)) {
            creator = MultiConsolePunishmentExecutor(pluginManager.console)
        } else {
            creator = MultiPlayerPunishmentExecutor(pluginManager.getOfflinePlayer(this.creatorName))
        }

        val creationAction = MultiCreationAction(this.id, 1, target, creator, this.createDate, this.reason)
        val serverList = getServersList(this.servers)
        return MultiPunishment.constructPunishment(pluginManager, type, id, creationAction, ArrayList(this.activations),
            ArrayList(this.deactivations), this.startDate, this.duration, this.comment, serverList, this.cancelled)
    }
}