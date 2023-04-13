package me.kvalbrus.multibans.bukkit.commands;

import java.util.List;
import me.kvalbrus.multibans.bukkit.implementations.BukkitCommandSender;
import me.kvalbrus.multibans.common.command.commands.MuteChat;
import me.kvalbrus.multibans.common.command.commands.TempMuteChat;
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

public class MuteChatBukkit implements CommandExecutor, TabCompleter {

    private final PluginManager pluginManager;

    private final me.kvalbrus.multibans.common.command.Command muteChatCommand;

    private final me.kvalbrus.multibans.common.command.Command tempMuteChatCommand;

    public MuteChatBukkit(@NotNull PluginManager pluginManager, @NotNull JavaPlugin plugin) {
        this.pluginManager = pluginManager;
        this.muteChatCommand = new MuteChat(pluginManager);
        this.tempMuteChatCommand = new TempMuteChat(pluginManager);

        PluginCommand pluginCommand = plugin.getCommand(this.muteChatCommand.getName());
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
            try {
                return this.tempMuteChatCommand.execute(commandSender, args);
            } catch (NotMatchArgumentsException exception) {
                return this.muteChatCommand.execute(commandSender, args);
            }
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
        return this.muteChatCommand.tab(BukkitCommandSender.getSender(sender), args);
    }
}