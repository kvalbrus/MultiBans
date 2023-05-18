package me.kvalbrus.multibans.common.command.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import me.kvalbrus.multibans.api.CommandSender;
import me.kvalbrus.multibans.api.Console;
import me.kvalbrus.multibans.api.OnlinePlayer;
import me.kvalbrus.multibans.api.Player;
import me.kvalbrus.multibans.api.punishment.Punishment;
import me.kvalbrus.multibans.api.punishment.PunishmentType;
import me.kvalbrus.multibans.api.punishment.creator.PunishmentCreator;
import me.kvalbrus.multibans.api.punishment.target.PunishmentTarget;
import me.kvalbrus.multibans.common.command.Command;
import me.kvalbrus.multibans.common.managers.PluginManager;
import me.kvalbrus.multibans.common.permissions.Permission;
import me.kvalbrus.multibans.common.punishment.creator.MultiConsolePunishmentCreator;
import me.kvalbrus.multibans.common.punishment.creator.MultiOnlinePlayerPunishmentCreator;
import me.kvalbrus.multibans.common.punishment.target.MultiOnlinePunishmentTarget;
import me.kvalbrus.multibans.common.punishment.target.MultiPunishmentTarget;
import me.kvalbrus.multibans.common.utils.Message;
import org.jetbrains.annotations.NotNull;

public class Kick extends Command {
    public Kick(@NotNull PluginManager pluginManager) {
        super(pluginManager, "kick", Permission.PUNISHMENT_KICK_EXECUTOR.getName(), null);
    }

    @Override
    public boolean cmd(@NotNull CommandSender sender, String[] args) {
        int length = args.length;

        if (length < 2) {
            sender.sendMessage(Message.NOT_ENOUGH_ARGUMENTS.getMessage());
            return false;
        } else {
            Player player = super.getPluginManager().getOfflinePlayer(args[0]);
            if(player == null) {
                sender.sendMessage(Message.NOT_FOUND_PLAYER.getMessage());
                return false;
            }

            PunishmentTarget target = null;
            if (player instanceof OnlinePlayer onlinePlayer) {
                target = new MultiOnlinePunishmentTarget(onlinePlayer);
            } else {
                target = new MultiPunishmentTarget(player);
            }

            PunishmentCreator creator = null;
            if (sender instanceof OnlinePlayer onlinePlayer) {
                creator = new MultiOnlinePlayerPunishmentCreator(onlinePlayer);
            } else if(sender instanceof Console console) {
                creator = new MultiConsolePunishmentCreator(console);
            } else {
                throw new IllegalArgumentException("Sender is illegal");
            }

            StringBuilder reason = new StringBuilder();
            for (int i = 1; i < length; ++i) {
                reason.append(args[i]);
            }

            Punishment punishment = super.getPluginManager().getPunishmentManager()
                .generatePunishment(PunishmentType.KICK, target, creator, -1, reason.toString());
            punishment.activate();

            return true;
        }
    }

    @Override
    public String getNotPermissionMessage() {
        return Message.NOT_PERMISSION_KICK_EXECUTE.getMessage();
    }

    @NotNull
    @Override
    public List<String> tab(@NotNull CommandSender sender, String[] args) {
        if(args.length == 1) {
            List<String> players = new ArrayList<>();
            Arrays.stream(this.getPluginManager().getOfflinePlayers()).forEach(p -> players.add(p.getName()));

            return Command.getSearchList(players, args[0]);
        } else {
            return new ArrayList<>();
        }
    }
}
