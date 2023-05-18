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
import me.kvalbrus.multibans.common.permissions.Permission;
import me.kvalbrus.multibans.common.command.Command;
import me.kvalbrus.multibans.common.exceptions.IllegalDateFormatException;
import me.kvalbrus.multibans.common.exceptions.NotMatchArgumentsException;
import me.kvalbrus.multibans.common.managers.PluginManager;
import me.kvalbrus.multibans.common.punishment.creator.MultiConsolePunishmentCreator;
import me.kvalbrus.multibans.common.punishment.creator.MultiOnlinePlayerPunishmentCreator;
import me.kvalbrus.multibans.common.punishment.target.MultiOnlinePunishmentTarget;
import me.kvalbrus.multibans.common.punishment.target.MultiPunishmentTarget;
import me.kvalbrus.multibans.common.utils.Message;
import me.kvalbrus.multibans.common.utils.StringUtil;
import org.jetbrains.annotations.NotNull;

public class TempBanIp extends Command {

    public TempBanIp(@NotNull PluginManager pluginManager) {
        super(pluginManager, "ban-ip", Permission.PUNISHMENT_TEMPBANIP_EXECUTE.getName(), null);
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

                    PunishmentCreator creator;
                    if (sender instanceof OnlinePlayer onlinePlayer) {
                        creator = new MultiOnlinePlayerPunishmentCreator(onlinePlayer);
                    } else if(sender instanceof Console console) {
                        creator = new MultiConsolePunishmentCreator(console);
                    } else {
                        throw new IllegalArgumentException("Creator is illegal");
                    }

                    Punishment punishment = super.getPluginManager().getPunishmentManager()
                        .generatePunishment(PunishmentType.TEMP_BAN_IP, target, creator,
                            date, reason.toString());

                    punishment.activate();

                    return true;
                } catch (IllegalDateFormatException exception) {
                    throw new NotMatchArgumentsException();
                }
            }
        }
    }

    @Override
    public String getNotPermissionMessage() {
        return Message.NOT_PERMISSION_TEMPBANIP_EXECUTE.getMessage();
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