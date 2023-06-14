package me.kvalbrus.multibans.common.punishment.creator

import me.kvalbrus.multibans.api.punishment.Punishment
import me.kvalbrus.multibans.api.punishment.executor.PunishmentExecutor
import java.util.*

open class MultiPunishmentExecutor : PunishmentExecutor {

    override val uniqueId: UUID?
    override val name: String

    override var punishment: Punishment? = null

    constructor(uniqueId: UUID?, name: String) {
        this.uniqueId = uniqueId
        this.name = name
    }
}