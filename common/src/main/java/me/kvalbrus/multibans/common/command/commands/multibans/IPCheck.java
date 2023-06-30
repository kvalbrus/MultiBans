package me.kvalbrus.multibans.common.command.commands.multibans;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import me.kvalbrus.multibans.api.CommandSender;
import me.kvalbrus.multibans.api.OnlinePlayer;
import me.kvalbrus.multibans.common.command.Command;
import me.kvalbrus.multibans.common.managers.MultiBansPluginManager;
import me.kvalbrus.multibans.common.managers.PluginManager;
import me.kvalbrus.multibans.common.permissions.Permission;
import me.kvalbrus.multibans.common.utils.Message;
import me.kvalbrus.multibans.common.utils.StringUtil;
import org.jetbrains.annotations.NotNull;

public class IPCheck extends Command {

    public IPCheck(@NotNull PluginManager pluginManager) {
        super(pluginManager, "ipcheck", Permission.COMMAND_IPCHECK.getPerm(), null);
    }

    @Override
    public boolean cmd(@NotNull CommandSender sender, String[] args) throws Exception {
        if (args.length < 1) {
            sender.sendMessage(Message.NOT_ENOUGH_ARGUMENTS.getText());
            return false;
        } else {
            var player = this.getPluginManager().getOfflinePlayer(args[0]);
            if (player != null) {
                var sessions = this.getPluginManager().getSessionManager().getSessionHistory(player.getUniqueId());

                var message = getIPCheck(player.getUniqueId());
                if (!message.isEmpty()) {
                    sender.sendMessage(((MultiBansPluginManager) this.getPluginManager()).getSettings()
                        .getPrefix() + "<reset> IP Check " + player.getName() + "<newline>" + getIPCheck(player.getUniqueId()));
                } else {
                    sender.sendMessage(((MultiBansPluginManager) this.getPluginManager()).getSettings()
                        .getPrefix() + "<reset> IP Check " + player.getName() + " <red>(clear)<reset>");
                }

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

    private String getIPCheck(UUID uuid) {
        var ips = this.getPluginManager().getSessionManager().getIPHistory(uuid);
        var message = "";
        for (var i = 0; i < ips.size(); ++i) {
            var ip = ips.get(i);
            var players = this.getPluginManager().getSessionManager().getPlayersByIP(ip);

            if (((MultiBansPluginManager) this.getPluginManager()).getSettings().getMaskIp()) {
                ip = StringUtil.maskIP(ip);
            }

            players.removeIf(player -> player.getUniqueId().equals(uuid));
            if (!players.isEmpty()) {
                message += ip + ": ";

                for (var j = 0; j < players.size(); ++j) {
                    var player = players.get(j);
                    try {
                        if (player instanceof OnlinePlayer) {
                            message += "<green>" + player.getName() + "</green><reset>";
                        } else if (this.getPluginManager().getPunishmentManager().hasActiveBan(player.getUniqueId())) {
                            message += "<red>" + player.getName() + "</red><reset>";
                        } else {
                            message += "<gray>" + player.getName() + "</gray><reset>";
                        }

                        if (j != players.size() - 1) {
                            message += ", ";
                        }
                    } catch (Exception ignore) {}
                }

                if (i != ips.size() - 1) {
                    message += "<newline>";
                }
            }
        }

        return message;
    }
}
