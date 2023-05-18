package me.kvalbrus.multibans.api.punishment

import me.kvalbrus.multibans.api.punishment.creator.PunishmentCreator
import me.kvalbrus.multibans.api.punishment.target.PunishmentTarget

interface Punishment : Comparable<Punishment?> {

    val type: PunishmentType
    val id: String
    val target: PunishmentTarget
    val creator: PunishmentCreator
    val createdDate: Long
    
    var createdReason: String
    var comment: String
    var servers: List<String>

    fun activate()
    fun delete()
    fun getStatus() : PunishmentStatus
}