package me.kvalbrus.multibans.common.punishment.action

import me.kvalbrus.multibans.api.punishment.action.CreationAction
import me.kvalbrus.multibans.api.punishment.executor.PunishmentExecutor
import me.kvalbrus.multibans.api.punishment.target.PunishmentTarget

class MultiCreationAction(
    override val punId: String,
    override val id: Int,
    override val target: PunishmentTarget,
    override val executor: PunishmentExecutor,
    override val date: Long,
    override var reason: String) : CreationAction