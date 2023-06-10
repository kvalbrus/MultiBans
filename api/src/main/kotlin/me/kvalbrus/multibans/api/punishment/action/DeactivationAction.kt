package me.kvalbrus.multibans.api.punishment.action

interface DeactivationAction : Action {
    override val type: ActionType
        get() = ActionType.DEACTIVATE
}