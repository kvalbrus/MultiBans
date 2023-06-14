package me.kvalbrus.multibans.common.punishment.creator

import me.kvalbrus.multibans.api.CommandSender
import me.kvalbrus.multibans.api.punishment.executor.OnlinePunishmentExecutor

class MultiOnlinePunishmentExecutor : MultiPunishmentExecutor, OnlinePunishmentExecutor {

    private val sender: CommandSender

    constructor(sender: CommandSender) : super(sender.uniqueId, sender.name) {
        this.sender = sender
    }

    override fun hasPermission(permission: String): Boolean = this.sender.hasPermission(permission)
    override fun sendMessage(message: String) = this.sender.sendMessage(message)
}