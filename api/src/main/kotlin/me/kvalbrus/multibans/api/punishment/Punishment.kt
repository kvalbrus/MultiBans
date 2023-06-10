package me.kvalbrus.multibans.api.punishment

import me.kvalbrus.multibans.api.punishment.action.CreationAction
import me.kvalbrus.multibans.api.punishment.executor.PunishmentExecutor
import me.kvalbrus.multibans.api.punishment.punishments.PunishmentStatus
import me.kvalbrus.multibans.api.punishment.punishments.PunishmentType
import me.kvalbrus.multibans.api.punishment.target.PunishmentTarget

interface Punishment : Comparable<Punishment?> {
    
    val type: PunishmentType
    val id: String

    val creationAction: CreationAction

    val target: PunishmentTarget
    val creator: PunishmentExecutor
    val createdDate: Long
    var createdReason: String
    
    var comment: String
    var servers: List<String>

    fun create()
    fun delete()

    fun getStatus() : PunishmentStatus
}