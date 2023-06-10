package me.kvalbrus.multibans.api.punishment.action

interface ActivationAction : Action {
    override val type: ActionType
        get() = ActionType.ACTIVATE
}