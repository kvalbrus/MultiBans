package me.kvalbrus.multibans.api.punishment

import me.kvalbrus.multibans.api.punishment.creator.PunishmentCreator

interface Cancelable {

    fun deactivate(cancellationCreator: PunishmentCreator,
                   cancellationDate: Long = System.currentTimeMillis(),
                   cancellationReason: String = "")

    var cancellationCreator: PunishmentCreator?
    var cancellationDate: Long
    var cancellationReason: String
    var cancelled: Boolean
}