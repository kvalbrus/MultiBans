package me.kvalbrus.multibans.bukkit.implementations

import me.kvalbrus.multibans.api.Console
import me.kvalbrus.multibans.common.implementations.MultiConsole
import org.bukkit.command.ConsoleCommandSender
import java.util.*

class BukkitConsole : MultiConsole, Console {

    private val sender: ConsoleCommandSender

    constructor(sender: ConsoleCommandSender) {
        this.sender = sender
    }

    override val uniqueId: UUID? = null

    override fun sendMessage(message: String) = sender.sendMessage(message)
}