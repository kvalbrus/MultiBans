package me.kvalbrus.multibans.api.punishment.action

import me.kvalbrus.multibans.api.punishment.target.PunishmentTarget

interface CreationAction : Action {
    val target: PunishmentTarget
    override val type: ActionType
        get() = ActionType.CREATE
}