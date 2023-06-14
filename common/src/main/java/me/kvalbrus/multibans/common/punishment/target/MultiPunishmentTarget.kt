package me.kvalbrus.multibans.common.punishment.target

import me.kvalbrus.multibans.api.Player
import me.kvalbrus.multibans.api.punishment.Punishment
import me.kvalbrus.multibans.api.punishment.target.PunishmentTarget
import java.util.*

open class MultiPunishmentTarget : PunishmentTarget {

    private val player: Player

    constructor(player: Player) {
        this.player = player
    }

    override var punishment: Punishment? = null

    override val name: String
        get() = player.name

    override val uniqueId: UUID
        get() = player.uniqueId
}