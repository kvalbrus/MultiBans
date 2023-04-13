package me.kvalbrus.multibans.common.command.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import me.kvalbrus.multibans.api.CommandSender;
import me.kvalbrus.multibans.api.Player;
import me.kvalbrus.multibans.common.command.Command;
import me.kvalbrus.multibans.common.managers.PluginManager;
import me.kvalbrus.multibans.common.permissions.Permission;
import me.kvalbrus.multibans.common.punishment.punishments.MultiPermanentlyChatMute;
import me.kvalbrus.multibans.common.punishment.punishments.MultiTemporaryChatMute;
import me.kvalbrus.multibans.common.utils.Message;
import org.jetbrains.annotations.NotNull;

public class UnmuteChat extends Command {

    public UnmuteChat(@NotNull PluginManager pluginManager) {
        super(pluginManager, "unmute", Permission.PUNISHMENT_UNMUTECHAT_EXECUTE.getName(), null);
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, String[] args){
        if (!sender.hasPermission(this.getPermission())) {
            sender.sendMessage(Message.NOT_PERMISSION_UNMUTECHAT_EXECUTE.getMessage());
            return false;
        }

        if (args.length < 2) {
            sender.sendMessage(Message.NOT_ENOUGH_ARGUMENTS.getMessage());
            return false;
        }

        Player player = super.getPluginManager().getOfflinePlayer(args[0]);
        if(player == null) {
            sender.sendMessage(Message.NOT_FOUND_PLAYER.getMessage());
            return false;
        }

        StringBuilder reason = new StringBuilder();
        for (int i = 1; i < args.length; ++i) {
            reason.append(args[i]);
        }

        List<MultiPermanentlyChatMute> activeMutes = this.getPluginManager().getPunishmentManager()
            .getActivePunishments(player.getUniqueId(), MultiPermanentlyChatMute.class);

        for (MultiPermanentlyChatMute punishment : activeMutes) {
            punishment.deactivate(sender.getName(), System.currentTimeMillis(), reason.toString());
        }

        List<MultiTemporaryChatMute> activeTempMutes = this.getPluginManager().getPunishmentManager()
            .getActivePunishments(player.getUniqueId(), MultiTemporaryChatMute.class);

        for (MultiTemporaryChatMute punishment : activeTempMutes) {
            punishment.deactivate(player.getName(), System.currentTimeMillis(), reason.toString());
        }

        return true;
    }

    @Override
    public List<String> tab(@NotNull CommandSender sender, String[] args) {
        if(args.length == 1) {
            List<String> list = new ArrayList<>();
            Arrays.stream(this.getPluginManager().getOfflinePlayers()).forEach(p -> list.add(p.getName()));
            return list;
        } else {
            return new ArrayList<>();
        }
    }
}