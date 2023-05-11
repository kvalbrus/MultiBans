package me.kvalbrus.multibans.common.command;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import lombok.Getter;
import me.kvalbrus.multibans.api.CommandSender;
import me.kvalbrus.multibans.common.managers.PluginManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class Command {

    @Getter
    private final PluginManager pluginManager;

    @Getter
    private final String name;

    @Getter
    private final String permission;

    private final HashMap<String, Command> subCommands;

    public Command(@NotNull PluginManager pluginManager,
                   @NotNull String name,
                   @Nullable String permission,
                   @Nullable HashMap<String, Command> subCommands) {
        this.pluginManager = pluginManager;
        this.name = name;
        this.permission = permission;
        this.subCommands = subCommands;
    }

    public boolean execute(@NotNull CommandSender sender, String[] args) {
        if (hasPermission(sender, args)) {
            return this.cmd(sender, args);
        } else {
            sender.sendMessage(this.getNotPermissionMessage());
            return false;
        }
    }

    public boolean hasPermission(@NotNull CommandSender sender, String[] args) {
        if (this.permission != null) {
            return sender.hasPermission(this.getPermission());
        } else {
            return true;
        }
    }

    public abstract boolean cmd(@NotNull CommandSender sender, String[] args);

    public abstract String getNotPermissionMessage();

    public abstract List<String> tab(@NotNull CommandSender sender, String[] args);

    public Command getSubCommand(@NotNull String arg) {
        return this.subCommands != null ? this.subCommands.get(arg) : null;
    }

    public void addSubCommand(@NotNull String key, @NotNull Command command) {
        if (this.subCommands != null) {
            this.subCommands.put(key, command);
        }
    }

    public static List<String> getSearchList(List<String> list, String arg) {
        if (list == null) {
            return null;
        }

        List<String> resultList = new ArrayList<>();

        for (var string : list) {
            if (string.startsWith(arg)) {
                resultList.add(string);
            }
        }

        return resultList;
    }
}