package me.kvalbrus.multibans.common.command.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import me.kvalbrus.multibans.api.CommandSender;
import me.kvalbrus.multibans.api.Console;
import me.kvalbrus.multibans.api.OnlinePlayer;
import me.kvalbrus.multibans.api.Player;
import me.kvalbrus.multibans.api.punishment.executor.PunishmentExecutor;
import me.kvalbrus.multibans.common.command.Command;
import me.kvalbrus.multibans.common.managers.PluginManager;
import me.kvalbrus.multibans.common.permissions.Permission;
import me.kvalbrus.multibans.common.punishment.creator.MultiConsolePunishmentExecutor;
import me.kvalbrus.multibans.common.punishment.creator.MultiOnlinePlayerPunishmentExecutor;
import me.kvalbrus.multibans.common.punishment.punishments.MultiPermanentlyChatMute;
import me.kvalbrus.multibans.common.punishment.punishments.MultiTemporaryChatMute;
import me.kvalbrus.multibans.common.utils.Message;
import org.jetbrains.annotations.NotNull;

public class UnmuteChat extends Command {

    public UnmuteChat(@NotNull PluginManager pluginManager) {
        super(pluginManager, "unmute", Permission.PUNISHMENT_UNMUTECHAT_EXECUTE.getPerm(), null);
    }

    @Override
    public boolean cmd(@NotNull CommandSender sender, String[] args) {
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

        try {
            List<MultiPermanentlyChatMute> activeMutes = this.getPluginManager()
                .getPunishmentManager()
                .getActivePunishments(player.getUniqueId(), MultiPermanentlyChatMute.class);

            PunishmentExecutor cancellationCreator;
            if (sender instanceof OnlinePlayer onlinePlayer) {
                cancellationCreator = new MultiOnlinePlayerPunishmentExecutor(onlinePlayer);
            } else if (sender instanceof Console console) {
                cancellationCreator = new MultiConsolePunishmentExecutor(console);
            } else {
                throw new IllegalArgumentException("Creator is illegal");
            }

            for (MultiPermanentlyChatMute punishment : activeMutes) {
                punishment.deactivate(cancellationCreator, System.currentTimeMillis(),
                    reason.toString());
            }

            List<MultiTemporaryChatMute> activeTempMutes = this.getPluginManager()
                .getPunishmentManager()
                .getActivePunishments(player.getUniqueId(), MultiTemporaryChatMute.class);

            for (MultiTemporaryChatMute punishment : activeTempMutes) {
                punishment.deactivate(cancellationCreator, System.currentTimeMillis(),
                    reason.toString());
            }
        } catch (Exception exception) {
            // TODO: Send message for player
        }

        return true;
    }

    @Override
    public String getNotPermissionMessage() {
        return Message.NOT_PERMISSION_UNMUTECHAT_EXECUTE.getMessage();
    }

    @Override
    public List<String> tab(@NotNull CommandSender sender, String[] args) {
        if(args.length == 1) {
            List<String> players = new ArrayList<>();
            Arrays.stream(this.getPluginManager().getOfflinePlayers()).forEach(p -> players.add(
                p.getName()));

            return Command.getSearchList(players, args[0]);
        } else {
            return new ArrayList<>();
        }
    }
}