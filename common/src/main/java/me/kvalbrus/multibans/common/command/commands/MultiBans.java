package me.kvalbrus.multibans.common.command.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import me.kvalbrus.multibans.api.CommandSender;
import me.kvalbrus.multibans.common.command.Command;
import me.kvalbrus.multibans.common.command.commands.multibans.Reload;
import me.kvalbrus.multibans.common.managers.PluginManager;
import org.jetbrains.annotations.NotNull;

public class MultiBans extends Command {

    private final HashMap<String, Command> subCommands = new HashMap<>();

    public MultiBans(@NotNull PluginManager pluginManager) {
        super(pluginManager, "multibans", null, null);
        this.subCommands.put("reload", new Reload(pluginManager));
    }

    @Override
    public boolean cmd(@NotNull CommandSender sender, String[] args) {
        if (args.length == 0) {
            return false;
        }

        Command command = this.getSubCommand(args[0]);
        if (command != null) {
            return command.execute(sender, args);
        }

        return false;
    }

    @Override
    public String getNotPermissionMessage() {
        return null;
    }

    @Override
    public List<String> tab(@NotNull CommandSender sender, String[] args) {
        if (args.length == 1) {
            List<String> tab = new ArrayList<>();
            tab.add("reload");

            return tab;
        }

        return null;
    }

    @Override
    public Command getSubCommand(@NotNull String arg) {
        return this.subCommands.get(arg);
    }

    @Override
    public void addSubCommand(@NotNull String key, @NotNull Command command) {
        this.subCommands.put(key, command);
    }
}