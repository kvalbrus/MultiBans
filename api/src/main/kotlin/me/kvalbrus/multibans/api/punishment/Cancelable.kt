package me.kvalbrus.multibans.api.punishment

import me.kvalbrus.multibans.api.punishment.action.Action
import me.kvalbrus.multibans.api.punishment.action.ActivationAction
import me.kvalbrus.multibans.api.punishment.action.DeactivationAction
import me.kvalbrus.multibans.api.punishment.executor.PunishmentExecutor

interface Cancelable {

    fun activate(action: ActivationAction) : Boolean
    fun activate(executor: PunishmentExecutor, date: Long, reason: String) : Boolean
    fun deactivate(action: DeactivationAction) : Boolean
    fun deactivate(executor: PunishmentExecutor, date: Long, reason: String) : Boolean

    val activations: List<Action>
    val deactivations: List<Action>

    fun lastActivator() : PunishmentExecutor
    fun lastDeactivator() : PunishmentExecutor?

    val cancelled : Boolean

//    var cancellationCreator: PunishmentCreator?
//    var cancellationDate: Long
//    var cancellationReason: String
//    var cancelled: Boolean
}