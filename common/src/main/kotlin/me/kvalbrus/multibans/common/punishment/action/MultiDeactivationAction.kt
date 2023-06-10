package me.kvalbrus.multibans.common.punishment.action

import me.kvalbrus.multibans.api.punishment.action.DeactivationAction
import me.kvalbrus.multibans.api.punishment.executor.PunishmentExecutor

class MultiDeactivationAction(
    override val punId: String,
    override val id: Int,
    override val executor: PunishmentExecutor,
    override val date: Long,
    override var reason: String) : DeactivationAction {
}