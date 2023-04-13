package me.kvalbrus.multibans.bukkit.implementations;

import me.kvalbrus.multibans.api.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public abstract class BukkitCommandSender implements CommandSender {

    private final org.bukkit.command.CommandSender commandSender;

    public BukkitCommandSender(org.bukkit.command.CommandSender commandSender) {
        this.commandSender = commandSender;
    }

    @NotNull
    @Override
    public String getName() {
        return this.commandSender.getName();
    }

    @Override
    public void sendMessage(String message) {
        this.commandSender.sendMessage(message);
    }

    @Override
    public void sendMessage(String... messages) {
        this.commandSender.sendMessage(messages);
    }

    @Override
    public boolean hasPermission(@NotNull String permission) {
        return this.commandSender.hasPermission(permission);
    }

    public static CommandSender getSender(org.bukkit.command.CommandSender sender) {
        if (sender instanceof Player player) {
            return new BukkitOnlinePlayer(player);
        } else if (sender instanceof ConsoleCommandSender console) {
            return new BukkitConsole(console);
        } else {
            return null;
        }
    }
}