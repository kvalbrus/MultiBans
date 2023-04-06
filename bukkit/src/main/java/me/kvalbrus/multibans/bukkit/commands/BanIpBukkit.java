package me.kvalbrus.multibans.bukkit.commands;

import java.util.List;
import me.kvalbrus.multibans.bukkit.implementations.BukkitCommandSender;
import me.kvalbrus.multibans.common.command.commands.BanIp;
import me.kvalbrus.multibans.common.command.commands.TempBanIp;
import me.kvalbrus.multibans.common.exceptions.NotEnoughArgumentsException;
import me.kvalbrus.multibans.common.exceptions.NotMatchArgumentsException;
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

public class BanIpBukkit implements CommandExecutor, TabCompleter {

    private final PluginManager pluginManager;

    private final me.kvalbrus.multibans.common.command.Command banipCommand;

    private final me.kvalbrus.multibans.common.command.Command tempbanipCommand;

    public BanIpBukkit(@NotNull PluginManager pluginManager, @NotNull JavaPlugin plugin) {
        this.pluginManager = pluginManager;
        this.banipCommand = new BanIp(pluginManager);
        this.tempbanipCommand = new TempBanIp(pluginManager);

        PluginCommand pluginCommand = plugin.getCommand(this.banipCommand.getName());
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
            try {
                return this.tempbanipCommand.execute(bukkitCommandSender, args);
            } catch (NotMatchArgumentsException exception) {
                return this.banipCommand.execute(bukkitCommandSender, args);
            }
        } catch (NotEnoughArgumentsException exception) {
            return false;
        } catch (NotPermissionException exception) {
            return false;
        } catch (PlayerNotFoundException exception) {
            return false;
        } catch (NotMatchArgumentsException exception) {
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
        return this.banipCommand.tab(bukkitCommandSender, args);
    }
}
