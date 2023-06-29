package me.kvalbrus.multibans.common.command.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import me.kvalbrus.multibans.api.CommandSender;
import me.kvalbrus.multibans.api.Player;
import me.kvalbrus.multibans.api.punishment.executor.PunishmentExecutor;
import me.kvalbrus.multibans.common.command.Command;
import me.kvalbrus.multibans.common.managers.PluginManager;
import me.kvalbrus.multibans.common.permissions.Permission;
import me.kvalbrus.multibans.common.punishment.creator.MultiOnlinePunishmentExecutor;
import me.kvalbrus.multibans.common.punishment.punishments.MultiPermanentlyBan;
import me.kvalbrus.multibans.common.punishment.punishments.MultiTemporaryBan;
import me.kvalbrus.multibans.common.utils.Message;
import org.jetbrains.annotations.NotNull;

public class Unban extends Command {

    public Unban(@NotNull PluginManager pluginManager) {
        super(pluginManager, "unban", Permission.PUNISHMENT_UNBAN_EXECUTE.getPerm(), null);
    }

    @Override
    public boolean cmd(@NotNull CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(Message.NOT_ENOUGH_ARGUMENTS.getText());
            return false;
        }

        Player player = super.getPluginManager().getOfflinePlayer(args[0]);
        if(player == null) {
            sender.sendMessage(Message.NOT_FOUND_PLAYER.getText());
            return false;
        }

        StringBuilder reason = new StringBuilder();
        for (int i = 1; i < args.length; ++i) {
            reason.append(args[i]);
        }

        PunishmentExecutor executor = new MultiOnlinePunishmentExecutor(sender);

        try {
            List<MultiPermanentlyBan> activeBans = this.getPluginManager().getPunishmentManager()
                .getActivePunishments(player.getUniqueId(), MultiPermanentlyBan.class);

            for (MultiPermanentlyBan punishment : activeBans) {
                punishment.deactivate(executor, System.currentTimeMillis(),
                    reason.toString());
            }

            List<MultiTemporaryBan> activeTempBans = this.getPluginManager().getPunishmentManager()
                .getActivePunishments(player.getUniqueId(), MultiTemporaryBan.class);

            for (MultiTemporaryBan punishment : activeTempBans) {
                punishment.deactivate(executor, System.currentTimeMillis(),
                    reason.toString());
            }
        } catch (Exception exception) {
            // TODO: Send message for player or console that player wasn't unbanned
        }

        return true;
    }

    @Override
    public String getNotPermissionMessage() {
        return Message.NOT_PERMISSION_UNBAN_EXECUTE.getText();
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