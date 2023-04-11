package me.kvalbrus.multibans.bukkit.listeners;

import me.kvalbrus.multibans.common.managers.PluginManager;
import me.kvalbrus.multibans.common.managers.PunishmentManager;
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
            event.disallow(Result.KICK_BANNED, punManager.getPlayerTitle(event.getUniqueId()));
        }

        if (punManager.hasActiveBan(event.getName()) || punManager.hasActiveBanIp(event.getName())) {
            event.disallow(Result.KICK_BANNED, punManager.getPlayerTitle(event.getName()));
        }
    }
}