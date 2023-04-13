package me.kvalbrus.multibans.bukkit.listeners;

import java.util.ArrayList;
import java.util.List;
import me.kvalbrus.multibans.api.punishment.Punishment;
import me.kvalbrus.multibans.api.punishment.punishments.PermanentlyBan;
import me.kvalbrus.multibans.api.punishment.punishments.PermanentlyBanIp;
import me.kvalbrus.multibans.api.punishment.punishments.TemporaryBan;
import me.kvalbrus.multibans.api.punishment.punishments.TemporaryBanIp;
import me.kvalbrus.multibans.common.managers.PluginManager;
import me.kvalbrus.multibans.common.managers.PunishmentManager;
import me.kvalbrus.multibans.common.utils.Message;
import me.kvalbrus.multibans.common.utils.ReplacedString;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent.Result;
import org.jetbrains.annotations.NotNull;

public class PlayerJoinListener implements Listener {

    private final PluginManager pluginManager;

    public PlayerJoinListener(@NotNull PluginManager pluginManager) {
        this.pluginManager = pluginManager;
    }

    @EventHandler
    public void onJoinEvent(AsyncPlayerPreLoginEvent event) {

        PunishmentManager punManager = (PunishmentManager) pluginManager.getPunishmentManager();

        if (punManager.hasActiveBan(event.getUniqueId()) || punManager.hasActiveBanIp(event.getUniqueId())) {
            List<Punishment> activeBans = new ArrayList<>();

            activeBans.addAll(this.pluginManager.getPunishmentManager()
                .getActivePunishments(event.getUniqueId(), PermanentlyBanIp.class));

            activeBans.addAll(this.pluginManager.getPunishmentManager()
                .getActivePunishments(event.getUniqueId(), PermanentlyBan.class));

            activeBans.addAll(this.pluginManager.getPunishmentManager()
                .getActivePunishments(event.getUniqueId(), TemporaryBanIp.class));

            activeBans.addAll(this.pluginManager.getPunishmentManager()
                .getActivePunishments(event.getUniqueId(), TemporaryBan.class));

            StringBuilder punishmentsString = new StringBuilder();
            for (Punishment punishment : activeBans) {
                punishmentsString.append(new ReplacedString(Message.TITLE_TEMPBAN.getMessage()).replacePunishment(punishment).string());
            }

            ReplacedString title = new ReplacedString(Message.TITLE_HEADER.getMessage() +
                punishmentsString + Message.TITLE_FOOTER.getMessage());

            event.disallow(Result.KICK_BANNED, title.string());
        }

//        if (punManager.hasActiveBan(event.getUniqueId()) || punManager.hasActiveBanIp(event.getUniqueId())) {
//            event.disallow(Result.KICK_BANNED, punManager.getPlayerTitle(event.getUniqueId()));
//        }
//
//        if (punManager.hasActiveBan(event.getName()) || punManager.hasActiveBanIp(event.getName())) {
//            event.disallow(Result.KICK_BANNED, punManager.getPlayerTitle(event.getName()));
//        }
    }
}