package me.kvalbrus.multibans.api.punishment.executor

import me.kvalbrus.multibans.api.punishment.Punishment
import java.util.UUID

interface PunishmentExecutor {

    val uniqueId: UUID?
    val name: String
    var punishment: Punishment?
}