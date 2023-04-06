package me.kvalbrus.multibans.common.command;

import java.util.List;
import java.util.HashMap;
import lombok.Getter;
import me.kvalbrus.multibans.api.CommandSender;
import me.kvalbrus.multibans.common.exceptions.NotEnoughArgumentsException;
import me.kvalbrus.multibans.common.exceptions.NotMatchArgumentsException;
import me.kvalbrus.multibans.common.exceptions.NotPermissionException;
import me.kvalbrus.multibans.common.exceptions.PlayerNotFoundException;
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

    private final HashMap<String, Command> subcommands;

    public Command(@NotNull PluginManager pluginManager,
                   @NotNull String name,
                   @NotNull String permission,
                   @Nullable HashMap<String, Command> subcommands) {
        this.pluginManager = pluginManager;
        this.name = name;
        this.permission = permission;
        this.subcommands = subcommands;
    }

    public abstract boolean execute(@NotNull CommandSender sender, String[] args)
        throws NotEnoughArgumentsException, NotPermissionException, PlayerNotFoundException, NotMatchArgumentsException;

    public abstract List<String> tab(@NotNull CommandSender sender,
                                     String[] args);

    public Command getSubCommand(@NotNull String arg) {
        return this.subcommands != null ? this.subcommands.get(arg) : null;
    }
}