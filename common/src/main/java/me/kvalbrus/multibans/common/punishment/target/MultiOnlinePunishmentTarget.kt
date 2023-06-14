package me.kvalbrus.multibans.common.punishment.target

import me.kvalbrus.multibans.api.OnlinePlayer
import me.kvalbrus.multibans.api.punishment.target.OnlinePunishmentTarget

class MultiOnlinePunishmentTarget : MultiPunishmentTarget, OnlinePunishmentTarget {

    private val player: OnlinePlayer

    constructor(player: OnlinePlayer) : super(player) {
        this.player = player
    }

    override val hostAddress: String
        get() = this.player.hostAddress

    override fun sendMessage(message: String) = this.player.sendMessage(message)
    override fun hasPermission(permission: String): Boolean = this.player.hasPermission(permission)
    override fun kick(reason: String) = this.player.kick(reason)
}