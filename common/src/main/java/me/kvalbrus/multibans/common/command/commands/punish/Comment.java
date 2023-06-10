package me.kvalbrus.multibans.common.command.commands.punish;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import me.kvalbrus.multibans.api.CommandSender;
import me.kvalbrus.multibans.api.punishment.Punishment;
import me.kvalbrus.multibans.common.command.Command;
import me.kvalbrus.multibans.common.managers.PluginManager;
import org.jetbrains.annotations.NotNull;

public class Comment extends Command {

    public Comment(@NotNull final PluginManager pluginManager) {
        super(pluginManager, "comment", "multibans.command.comment", new HashMap<>());
    }

    @Override
    public boolean cmd(@NotNull CommandSender sender, String[] args) throws Exception {
        if (args.length < 4) {
            return false;
        }

        Punishment punishment = this.getPluginManager().getPunishmentManager().getPunishment(args[0]);
        if (punishment == null) {
            return false;
        }

        switch (args[2]) {
            case "set":
                StringBuilder commentBuilder = new StringBuilder();
                for (var i = 3; i < args.length; ++i) {
                    commentBuilder.append(args[i]).append((i == args.length - 1) ? "" : " ");
                }

                punishment.setComment(commentBuilder.toString());
                return true;

            default:
                return false;
        }
    }

    @Override
    public String getNotPermissionMessage() {
        return null;
    }

    @Override
    public List<String> tab(@NotNull CommandSender sender, String[] args) {
        if (args.length == 3) {
            List<String> tab = new ArrayList<>();
            tab.add("set");

            return tab;
        } else {
            return null;
        }
    }
}