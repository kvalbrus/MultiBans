package me.kvalbrus.multibans.common.command.commands;

import java.util.ArrayList;
import java.util.List;
import me.kvalbrus.multibans.api.CommandSender;
import me.kvalbrus.multibans.api.Player;
import me.kvalbrus.multibans.api.punishment.Punishment;
import me.kvalbrus.multibans.api.punishment.PunishmentType;
import me.kvalbrus.multibans.common.Permission;
import me.kvalbrus.multibans.common.command.Command;
import me.kvalbrus.multibans.common.exceptions.IllegalDateFormatException;
import me.kvalbrus.multibans.common.exceptions.NotEnoughArgumentsException;
import me.kvalbrus.multibans.common.exceptions.NotMatchArgumentsException;
import me.kvalbrus.multibans.common.exceptions.NotPermissionException;
import me.kvalbrus.multibans.common.exceptions.PlayerNotFoundException;
import me.kvalbrus.multibans.common.managers.PluginManager;
import me.kvalbrus.multibans.common.utils.StringUtil;
import org.jetbrains.annotations.NotNull;

public class BanIp extends Command {

    public BanIp(@NotNull PluginManager pluginManager) {
        super(pluginManager, "ban-ip", Permission.PUNISHMENT_BANIP.getName(), null);
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, String[] args)
        throws NotEnoughArgumentsException, NotPermissionException, PlayerNotFoundException, NotMatchArgumentsException {
        int length = args.length;

        if (length < 2) {
            throw new NotEnoughArgumentsException(2);
        } else {
            if (!sender.hasPermission(super.getPermission())) {
                throw new NotPermissionException();
            }

            Player player = super.getPluginManager().getPlayer(args[0]);
            if(player == null) {
                throw new PlayerNotFoundException(args[0]);
            }

            if (length == 2) {
                try {
                    StringUtil.getTime(args[1]);
                    throw new NotMatchArgumentsException();
                } catch (IllegalDateFormatException exception) {
                    Punishment punishment = super.getPluginManager().getPunishmentManager()
                        .generatePunishment(PunishmentType.BAN_IP, player, sender.getName(), -1, args[1]);

                    punishment.activate();

                    return true;
                }
            } else {
                try {
                    StringUtil.getTime(args[1]);
                    throw new NotMatchArgumentsException();
                } catch (IllegalDateFormatException exception) {
                    StringBuilder reason = new StringBuilder();
                    for (int i = 1; i < length; ++i) {
                        reason.append(args[i]);
                    }

                    Punishment punishment = super.getPluginManager().getPunishmentManager()
                        .generatePunishment(PunishmentType.BAN_IP, player, sender.getName(),
                            -1, reason.toString());

                    punishment.activate();

                    return true;
                }
            }
        }
    }

    @NotNull
    @Override
    public List<String> tab(@NotNull CommandSender sender, String[] args) {
        if(args.length == 1) {
            return this.getPluginManager().getPlayers();
        } else if (args.length == 2) {
            List<String> list = new ArrayList<>();
            list.add("1d");
            list.add("2d");
            list.add("5d");

            return list;
        } else {
            return new ArrayList<>();
        }
    }
}
