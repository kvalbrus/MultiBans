package me.kvalbrus.multibans.common.punishment.punishments

import me.kvalbrus.multibans.api.punishment.action.ActivationAction
import me.kvalbrus.multibans.api.punishment.action.DeactivationAction

interface ActionLoader {
    fun loadActivationsFromDataProvider(activations: MutableList<ActivationAction>)
    fun loadDeactivationsFromDataProvider(deactivations: MutableList<DeactivationAction>)
}