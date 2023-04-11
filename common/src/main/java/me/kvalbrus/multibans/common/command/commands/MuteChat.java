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
import me.kvalbrus.multibans.common.exceptions.NotEnoughArgumentsException;
import me.kvalbrus.multibans.common.exceptions.NotMatchArgumentsException;
import me.kvalbrus.multibans.common.exceptions.NotPermissionException;
import me.kvalbrus.multibans.common.exceptions.PlayerNotFoundException;
import me.kvalbrus.multibans.common.managers.PluginManager;
import me.kvalbrus.multibans.common.punishment.creator.MultiConsolePunishmentCreator;
import me.kvalbrus.multibans.common.punishment.creator.MultiPlayerPunishmentCreator;
import me.kvalbrus.multibans.common.punishment.target.MultiPunishmentTarget;
import me.kvalbrus.multibans.common.utils.Message;
import me.kvalbrus.multibans.common.utils.ReplacedString;
import me.kvalbrus.multibans.common.utils.StringUtil;
import org.jetbrains.annotations.NotNull;

public class MuteChat extends Command {

    public MuteChat(@NotNull PluginManager pluginManager) {
        super(pluginManager, "mute", Permission.PUNISHMENT_MUTECHAT_EXECUTE.getName(), null);
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, String[] args) {
        int length = args.length;

        if (length < 2) {
            throw new NotEnoughArgumentsException(2);
        } else {
            if (!sender.hasPermission(super.getPermission())) {
                //sender.sendMessage(Message.NOT_PERMISSION_MUTECHAT_EXECUTE.message);
                throw new NotPermissionException();
            }

            Player player = super.getPluginManager().getOfflinePlayer(args[0]);
            if(player == null) {
                throw new PlayerNotFoundException(args[0]);
            }

            PunishmentTarget target = new MultiPunishmentTarget(player);

            try {
                StringUtil.getTime(args[1]);
                throw new NotMatchArgumentsException();
            } catch (IllegalDateFormatException exception) {
                StringBuilder reason = new StringBuilder();
                for (int i = 1; i < length; ++i) {
                    reason.append(args[i]);
                }

                PunishmentCreator creator = null;
                if (sender instanceof OnlinePlayer onlinePlayer) {
                    creator = new MultiPlayerPunishmentCreator(onlinePlayer);
                } else if(sender instanceof Console console) {
                    creator = new MultiConsolePunishmentCreator(console);
                }

                Punishment punishment = super.getPluginManager().getPunishmentManager()
                    .generatePunishment(PunishmentType.MUTE, target, creator,
                        -1, reason.toString());

                punishment.activate();

                return true;
            }
        }
    }

    @NotNull
    @Override
    public List<String> tab(@NotNull CommandSender sender, String[] args) {
        if(args.length == 1) {
            List<String> list = new ArrayList<>();
            Arrays.stream(this.getPluginManager().getOfflinePlayers()).forEach(p -> list.add(p.getName()));
            return list;
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
