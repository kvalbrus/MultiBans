package me.kvalbrus.multibans.bukkit.commands;

import java.util.List;
import me.kvalbrus.multibans.bukkit.implementations.BukkitCommandSender;
import me.kvalbrus.multibans.common.command.commands.Ban;
import me.kvalbrus.multibans.common.exceptions.NotEnoughArgumentsException;
import me.kvalbrus.multibans.common.exceptions.NotPermissionException;
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

public class BanBukkit implements CommandExecutor, TabCompleter {

    private final PluginManager pluginManager;

    private final me.kvalbrus.multibans.common.command.Command command;

    public BanBukkit(@NotNull PluginManager pluginManager, @NotNull JavaPlugin plugin) {
        this.pluginManager = pluginManager;
        this.command = new Ban(pluginManager);

        PluginCommand pluginCommand = plugin.getCommand(this.command.getName());
        if (pluginCommand != null) {
            pluginCommand.setExecutor(this);
        }
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender,
                             @NotNull Command command,
                             @NotNull String label,
                             @NotNull String[] args) {

        BukkitCommandSender bukkitCommandSender = new BukkitCommandSender(sender);

        try {
            return this.command.execute(bukkitCommandSender, args);
        } catch (NotEnoughArgumentsException exception) {
            return false;
        } catch (NotPermissionException exception) {
            return false;
        } catch (PlayerNotFoundException exception) {
            return false;
        }
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender,
                                      @NotNull Command command,
                                      @NotNull String label,
                                      @NotNull String[] args) {
        BukkitCommandSender bukkitCommandSender = new BukkitCommandSender(sender);
        return this.command.tab(bukkitCommandSender, args);
    }
}
