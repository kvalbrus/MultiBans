package me.kvalbrus.multibans.common.punishment.action

import me.kvalbrus.multibans.api.punishment.action.Action
import me.kvalbrus.multibans.api.punishment.action.ActionType
import me.kvalbrus.multibans.api.punishment.executor.PunishmentExecutor
import me.kvalbrus.multibans.common.punishment.creator.MultiPunishmentExecutor

abstract class MultiAction(
    override val punId: String,
    override val id: Int,
    override val executor: PunishmentExecutor,
    override val date: Long,
    override var reason: String,
    override val type: ActionType) : Action