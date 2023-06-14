package me.kvalbrus.multibans.common.command.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import me.kvalbrus.multibans.api.CommandSender;
import me.kvalbrus.multibans.api.Console;
import me.kvalbrus.multibans.api.OnlinePlayer;
import me.kvalbrus.multibans.api.Player;
import me.kvalbrus.multibans.api.punishment.Punishment;
import me.kvalbrus.multibans.api.punishment.punishments.PunishmentType;
import me.kvalbrus.multibans.api.punishment.executor.PunishmentExecutor;
import me.kvalbrus.multibans.api.punishment.target.PunishmentTarget;
import me.kvalbrus.multibans.common.permissions.Permission;
import me.kvalbrus.multibans.common.command.Command;
import me.kvalbrus.multibans.common.exceptions.IllegalDateFormatException;
import me.kvalbrus.multibans.common.exceptions.NotMatchArgumentsException;
import me.kvalbrus.multibans.common.managers.PluginManager;
import me.kvalbrus.multibans.common.punishment.creator.MultiOnlinePunishmentExecutor;
import me.kvalbrus.multibans.common.punishment.creator.MultiPunishmentExecutor;
import me.kvalbrus.multibans.common.punishment.target.MultiOnlinePunishmentTarget;
import me.kvalbrus.multibans.common.punishment.target.MultiPunishmentTarget;
import me.kvalbrus.multibans.common.utils.Message;
import me.kvalbrus.multibans.common.utils.StringUtil;
import org.jetbrains.annotations.NotNull;

public class TempBan extends Command {

    public TempBan(@NotNull PluginManager pluginManager) {
        super(pluginManager, "ban", Permission.PUNISHMENT_TEMPBAN_EXECUTE.getPerm(), null);
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

            PunishmentTarget target;

            if (player instanceof OnlinePlayer onlinePlayer) {
                target = new MultiOnlinePunishmentTarget(onlinePlayer);
            } else {
                target = new MultiPunishmentTarget(player);
            }

            if (length == 2) {
                try {
                    StringUtil.getTime(args[1]);
                    sender.sendMessage(Message.NOT_ENOUGH_ARGUMENTS.getMessage());
                    return false;
                } catch (IllegalDateFormatException exception) {
                    throw new NotMatchArgumentsException();
                }
            } else {
                try {
                    long date = StringUtil.getTime(args[1]);

                    StringBuilder reason = new StringBuilder();
                    for (int i = 2; i < length; ++i) {
                        reason.append(args[i]);
                    }

                    PunishmentExecutor creator = new MultiOnlinePunishmentExecutor(sender);
                    Punishment punishment = super.getPluginManager().getPunishmentManager()
                        .generatePunishment(PunishmentType.TEMP_BAN, target, creator,
                            date, reason.toString());
                    creator.setPunishment(punishment);
                    punishment.create();

                    return true;
                } catch (IllegalDateFormatException exception) {
                    throw new NotMatchArgumentsException();
                } catch (Exception exception) {
                    // TODO: Send message for player that he wasn't ban target
                }
            }
        }

        return true;
    }

    @Override
    public String getNotPermissionMessage() {
        return Message.NOT_PERMISSION_TEMPBAN_EXECUTE.getMessage();
    }

    @NotNull
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