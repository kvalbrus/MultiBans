package me.kvalbrus.multibans.api.punishment.executor

import me.kvalbrus.multibans.api.Nameable
import me.kvalbrus.multibans.api.punishment.Punishment

interface PunishmentExecutor : Nameable {
    var punishment: Punishment?
}