package me.kvalbrus.multibans.common.punishment.creator

import me.kvalbrus.multibans.api.punishment.Punishment
import me.kvalbrus.multibans.api.punishment.executor.PunishmentExecutor

abstract class MultiPunishmentExecutor : PunishmentExecutor {
    override var punishment: Punishment? = null
}