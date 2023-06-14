package me.kvalbrus.multibans.bukkit.implementations

import me.kvalbrus.multibans.api.OnlinePlayer
import me.kvalbrus.multibans.bukkit.BukkitPluginManager
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.entity.Player

class BukkitOnlinePlayer : BukkitPlayer, OnlinePlayer {

    private val player: Player

    constructor(player: Player) : super(player) {
        this.player = player
    }

    override fun sendMessage(message: String) {
        if (message != null && message.length > 0) {
            BukkitPluginManager.getAudiences().sender(player).sendMessage(
                MiniMessage.miniMessage()
                    .deserialize(message)
            )
        }
    }

    override fun hasPermission(permission: String): Boolean = player.hasPermission(permission)

    override val hostAddress: String
        get() = if (player.address != null) player.address!!.address.hostAddress else "unknown"

    override fun kick(reason: String) = player.kickPlayer(reason)

}