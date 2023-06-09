package me.kvalbrus.multibans.bukkit.implementations;

import me.kvalbrus.multibans.api.CommandSender;
import me.kvalbrus.multibans.bukkit.BukkitPluginManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
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
        if (message != null && message.length() > 0) {
            BukkitPluginManager.getAudiences().sender(this.commandSender).sendMessage(MiniMessage.miniMessage()
                .deserialize(message));
        }
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