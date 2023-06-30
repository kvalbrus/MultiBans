package me.kvalbrus.multibans.common.command.commands.multibans;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import me.kvalbrus.multibans.api.CommandSender;
import me.kvalbrus.multibans.api.Session;
import me.kvalbrus.multibans.common.command.Command;
import me.kvalbrus.multibans.common.managers.MultiBansPluginManager;
import me.kvalbrus.multibans.common.managers.PluginManager;
import me.kvalbrus.multibans.common.permissions.Permission;
import me.kvalbrus.multibans.common.utils.Message;
import me.kvalbrus.multibans.common.utils.StringUtil;
import org.jetbrains.annotations.NotNull;

public class History extends Command {

    public History(@NotNull PluginManager pluginManager) {
        super(pluginManager, "history", Permission.COMMAND_HISTORY.getPerm(), null);
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

                try {
                    sender.sendMessage(
                        ((MultiBansPluginManager) this.getPluginManager()).getSettings()
                            .getPrefix() + "<reset> History " + player.getName() + "<newline>" +
                            getYearHistory(sessions));
                } catch (Exception exception) {
                    exception.printStackTrace();
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

    private String getYearHistory(List<Session> sessions) {
        var map = new HashMap<Integer, List<Session>>();


        var firstDay = -1;
        int addTime = ((MultiBansPluginManager) this.getPluginManager()).getSettings().getTimeZone().getRawOffset();
        var currentTime = System.currentTimeMillis();
        for (var session : sessions) {
            var startDay = (int) ((currentTime) / 86_400_000L - session.getJoinTime() / 86_400_000L);
            //((currentTime + addTime + (86_400_000L - (currentTime % 86_400_000L)) - session.getJoinTime()) / 86_400_000L);
//            var endDay   = (int) ((currentTime + addTime + (86_400_000L - (currentTime % 86_400_000L)) - session.getQuitTime()) / 86_400_000L);
            var endDay   = (int) ((currentTime) / 86_400_000L - session.getQuitTime() / 86_400_000L);
            firstDay = Math.max(firstDay, startDay);

            if (startDay < 0) {
                startDay = 0;
            }

            if (endDay < 0) {
                endDay = 0;
            }

            for (var i = startDay; i <= endDay; ++i) {
                if (!map.containsKey(i)) {
                    map.put(i, new ArrayList<>());
                }

                map.get(i).add(session);
            }
        }

        var currentDate = LocalDate.now();

        var history = "";
        var currentDayOfWeek = currentDate.getDayOfWeek().getValue();
        for (var dayOfWeek = 1; dayOfWeek <= 7; dayOfWeek++) {
            for (var day = currentDayOfWeek - dayOfWeek + 50 * 7; day >= 0; day -= 7) {
                var date = LocalDate.ofYearDay(currentDate.getYear(), currentDate.getDayOfYear());
                date = date.minusDays(day);
                if (day > firstDay) {
                    history += "<hover:show_text:'" + date + "<newline>-----'><gray>⏹</gray>";
                    continue;
                }

                var dailySessions = map.get(day);
                if (dailySessions == null) {
                    history += "<hover:show_text:'" + date + "<newline>empty'>" + getDaySymbol(0);
                } else {
                    long allTime = 0L;

                    for (var session : dailySessions) {
                        var joinTime = Math.max(session.getJoinTime(), (currentTime / 86_400_000L - day) * 86_400_000L);
                        var quitTime = Math.min(session.getQuitTime(), (currentTime / 86_400_000L - day + 1) * 86_400_000L);

                        allTime += (quitTime - joinTime);
                    }

                    history += "<hover:show_text:'" + date + " (" + LocalTime.ofSecondOfDay(allTime / 1000L) + ")<newline>";
                    for (var session : dailySessions) {
                        if (((MultiBansPluginManager) this.getPluginManager()).getSettings().getMaskIp()) {
                            history += LocalTime.ofSecondOfDay(((session.getJoinTime() % 86_400_000L) / 1000L +
                                ((MultiBansPluginManager) this.getPluginManager()).getSettings().getTimeZone().getRawOffset() / 1000L) % 86400) +
                                " - " + LocalTime.ofSecondOfDay(((session.getQuitTime() % 86_400_000L) / 1000L +
                                ((MultiBansPluginManager) this.getPluginManager()).getSettings().getTimeZone().getRawOffset() / 1000L) % 86400) +
                                " " + StringUtil.maskIP(session.getPlayerIp()) + "<newline>";
                        } else {
                            history += LocalTime.ofSecondOfDay(((session.getJoinTime() % 86_400_000L) / 1000L +
                                ((MultiBansPluginManager) this.getPluginManager()).getSettings().getTimeZone().getRawOffset() / 1000L) % 86400) +
                                " - " + LocalTime.ofSecondOfDay(((session.getQuitTime() % 86_400_000L) / 1000L +
                                ((MultiBansPluginManager) this.getPluginManager()).getSettings().getTimeZone().getRawOffset() / 1000L) % 86400) +
                                " " + session.getPlayerIp() + "<newline>";
                        }

                    }

                    history += "'>" + getDaySymbol(allTime);
                }
            }

            if (dayOfWeek < 7) {
                history += "<newline>";
            }
        }

        return history;
    }

    private static char symbol = '⏹';
    private String getDaySymbol(long time) {
        if (time == 0L) {
            return "<dark_gray>" + symbol + "</dark_gray>";
        } else if (time > 0L && time < 3_600_000L) {
            return "<color:#B03A2E>" + symbol + "</color:#B03A2E>";
        } else if (time >= 3_600_000L && time < 10_800_000L) {
            return "<color:#E67E22>" + symbol + "</color:#E67E22>";
        } else if (time >= 10_800_000L && time < 21_600_000L) {
            return "<color:#F1C40F>" + symbol + "</color:#F1C40F>";
        } else if (time >= 21_600_000L && time < 43_200_000L) {
            return "<color:#2ECC71>" + symbol + "</color:#2ECC71>";
        } else {
            return "<color:#5DADE2>" + symbol + "</color:#5DADE2>";
        }
    }
}