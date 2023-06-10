package me.kvalbrus.multibans.common.punishment.action

import me.kvalbrus.multibans.api.punishment.action.ActivationAction
import me.kvalbrus.multibans.api.punishment.executor.PunishmentExecutor

class MultiActivationAction(
    override val punId: String,
    override val id: Int,
    override val executor: PunishmentExecutor,
    override val date: Long,
    override var reason: String) : ActivationAction