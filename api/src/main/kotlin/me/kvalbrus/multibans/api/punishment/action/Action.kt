package me.kvalbrus.multibans.api.punishment.action

import me.kvalbrus.multibans.api.punishment.executor.PunishmentExecutor

interface Action {
    val punId: String
    val id: Int
    val executor: PunishmentExecutor
    val date: Long
    var reason: String
    val type: ActionType
}