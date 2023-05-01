package me.kvalbrus.multibans.bukkit.implementations;

import me.kvalbrus.multibans.api.Console;
import org.bukkit.command.ConsoleCommandSender;
import org.jetbrains.annotations.NotNull;

public class BukkitConsole extends BukkitCommandSender implements Console {

    private final ConsoleCommandSender sender;

    public BukkitConsole(ConsoleCommandSender sender) {
        super(sender);
        this.sender = sender;
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
