package me.kvalbrus.multibans.common.command.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import me.kvalbrus.multibans.api.CommandSender;
import me.kvalbrus.multibans.common.command.Command;
import me.kvalbrus.multibans.common.command.commands.punish.Comment;
import me.kvalbrus.multibans.common.command.commands.punish.Reason;
import me.kvalbrus.multibans.common.managers.PluginManager;
import org.jetbrains.annotations.NotNull;

public class Punish extends Command {

    private final HashMap<String, Command> subCommands = new HashMap<>();

    public Punish(@NotNull PluginManager pluginManager) {
        super(pluginManager, "punish", null, null);
        this.subCommands.put("comment", new Comment(pluginManager));
        this.subCommands.put("reason", new Reason(pluginManager));
    }

    @Override
    public boolean cmd(@NotNull CommandSender sender, String[] args) {
        if (args.length < 2) {
            return false;
        }

        Command subCommand = this.getSubCommand(args[1]);
        if (subCommand != null) {
            return subCommand.execute(sender, args);
        } else {
            return false;
        }
    }

    @Override
    public String getNotPermissionMessage() {
        return null;
    }

    @Override
    public List<String> tab(@NotNull CommandSender sender, String[] args) {
        if (args.length > 2) {
            Command subCommand = this.getSubCommand(args[1]);
            if (subCommand != null) {
                return subCommand.tab(sender, args);
            }
        } else if (args.length == 2) {
            List<String> tab = new ArrayList<>();
            for (Map.Entry<String, Command> entry : this.subCommands.entrySet()) {
                tab.add(entry.getKey());
            }

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