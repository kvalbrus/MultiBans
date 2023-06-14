package me.kvalbrus.multibans.bukkit.implementations

import me.kvalbrus.multibans.api.Player
import org.bukkit.OfflinePlayer
import java.util.*

open class BukkitPlayer : Player {

    private val player: OfflinePlayer

    constructor(player: OfflinePlayer) {
        this.player = player
    }

    override val name: String
        get() = if (player.name != null) player.name!! else ""

    override val uniqueId: UUID
        get() = player.uniqueId
}