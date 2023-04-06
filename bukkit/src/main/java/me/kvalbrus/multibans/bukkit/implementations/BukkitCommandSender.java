package me.kvalbrus.multibans.bukkit.implementations;

import me.kvalbrus.multibans.api.CommandSender;
import org.jetbrains.annotations.NotNull;

public class BukkitCommandSender implements CommandSender {

    private final org.bukkit.command.CommandSender commandSender;

    public BukkitCommandSender(org.bukkit.command.CommandSender commandSender) {
        this.commandSender = commandSender;
    }

    @Override
    public @NotNull String getName() {
        return this.commandSender.getName();
    }

    @Override
    public void sendMessage(String... messages) {
        this.commandSender.sendMessage(messages);
    }

    @Override
    public boolean hasPermission(@NotNull String permission) {
        return this.commandSender.hasPermission(permission);
    }
}
