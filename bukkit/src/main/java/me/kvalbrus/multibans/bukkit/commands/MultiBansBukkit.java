package me.kvalbrus.multibans.bukkit.commands;

import java.util.List;
import me.kvalbrus.multibans.bukkit.implementations.BukkitCommandSender;
import me.kvalbrus.multibans.common.command.commands.MultiBans;
import me.kvalbrus.multibans.common.exceptions.NotMatchArgumentsException;
import me.kvalbrus.multibans.common.exceptions.PlayerNotFoundException;
import me.kvalbrus.multibans.common.managers.PluginManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MultiBansBukkit implements CommandExecutor, TabCompleter {

    private final PluginManager pluginManager;

    private final me.kvalbrus.multibans.common.command.Command multibans;

    public MultiBansBukkit(@NotNull PluginManager pluginManager, @NotNull JavaPlugin plugin) {
        this.pluginManager = pluginManager;
        this.multibans = new MultiBans(pluginManager);

        PluginCommand pluginCommand = plugin.getCommand(this.multibans.getName());
        if (pluginCommand != null) {
            pluginCommand.setExecutor(this);
        }
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender,
                             @NotNull Command command,
                             @NotNull String label,
                             @NotNull String[] args) {
        me.kvalbrus.multibans.api.CommandSender commandSender = BukkitCommandSender.getSender(sender);
        if (commandSender == null) {
            return false;
        }

        try {
            return this.multibans.execute(commandSender, args);
        } catch (PlayerNotFoundException | NotMatchArgumentsException exception) {
            return false;
        }
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender,
                                      @NotNull Command command,
                                      @NotNull String label,
                                      @NotNull String[] args) {
        return this.multibans.tab(BukkitCommandSender.getSender(sender), args);
    }
}