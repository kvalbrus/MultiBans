package me.kvalbrus.multibans.common.command.commands.multibans;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import me.kvalbrus.multibans.api.CommandSender;
import me.kvalbrus.multibans.common.command.Command;
import me.kvalbrus.multibans.common.managers.MultiBansPluginManager;
import me.kvalbrus.multibans.common.managers.PluginManager;
import me.kvalbrus.multibans.common.permissions.Permission;
import me.kvalbrus.multibans.common.utils.Message;
import me.kvalbrus.multibans.common.utils.StringUtil;
import org.jetbrains.annotations.NotNull;

public class IPHistory extends Command {

    public IPHistory(@NotNull PluginManager pluginManager) {
        super(pluginManager, "iphistory", Permission.COMMAND_IPHISTORY.getPerm(), null);
    }

    @Override
    public boolean cmd(@NotNull CommandSender sender, String[] args) throws Exception {
        if (args.length < 1) {
            sender.sendMessage(Message.NOT_ENOUGH_ARGUMENTS.getText());
            return false;
        } else {
            var player = this.getPluginManager().getOfflinePlayer(args[0]);
            if (player != null) {
                var sessions = this.getPluginManager().getSessionManager().getIPHistory(player.getUniqueId());
                sender.sendMessage(((MultiBansPluginManager) this.getPluginManager()).getSettings()
                    .getPrefix() + "<reset> IP History " + player.getName() + "<newline>" + getIPHistory(sessions));

                return true;
            }
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
            List<String> players = new ArrayList<>();
            Arrays.stream(this.getPluginManager().getOfflinePlayers()).forEach(p -> players.add(p.getName()));

            return Command.getSearchList(players, args[0]);
        } else {
            return new ArrayList<>();
        }
    }

    private String getIPHistory(List<String> ips) {
        if (((MultiBansPluginManager) this.getPluginManager()).getSettings().getMaskIp()) {
            ips.replaceAll(StringUtil::maskIP);
        }

        String history = "";
        var size = ips.size();
        for (var i = 0; i < size; ++i) {
            if (i % 4 != 3 && i != size - 1) {
                history += String.format("%-15s", ips.get(i)) + " | ";
            } else if (i % 4 == 3 && i != size - 1) {
                history += ips.get(i) + "<newline>";
            } else {
                history += ips.get(i);
            }
        }

        return history;
    }
}
