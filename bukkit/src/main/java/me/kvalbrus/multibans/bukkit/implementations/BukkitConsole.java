package me.kvalbrus.multibans.bukkit.implementations;

import me.kvalbrus.multibans.api.Console;
import org.bukkit.command.ConsoleCommandSender;
import org.jetbrains.annotations.NotNull;

public class BukkitConsole implements Console {

    private final ConsoleCommandSender sender;

    public BukkitConsole(ConsoleCommandSender sender) {
        this.sender = sender;
    }

    @Override
    public void sendMessage(String message) {
        this.sender.sendMessage(message);
    }

    @Override
    public void sendMessage(String... messages) {
        this.sender.sendMessage(messages);
    }

    @NotNull
    @Override
    public String getName() {
        return this.sender.getName();
    }

    @Override
    public boolean hasPermission(String permission) {
        return this.sender.hasPermission(permission);
    }
}
