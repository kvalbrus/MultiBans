package me.kvalbrus.multibans.common.managers;

import java.io.File;
import java.util.UUID;
import me.kvalbrus.multibans.api.Console;
import me.kvalbrus.multibans.api.DataProvider;
import me.kvalbrus.multibans.api.Player;
import me.kvalbrus.multibans.api.OnlinePlayer;
import me.kvalbrus.multibans.api.punishment.Punishment;
import me.kvalbrus.multibans.api.managers.PunishmentManager;
import org.jetbrains.annotations.NotNull;

public interface PluginManager {

    void onLoad();

    void onEnable();

    void onDisable();

    void reload();

    DataProvider getDataProvider();

    @NotNull
    PunishmentManager getPunishmentManager();

    @NotNull
    File getDataFolder();

    void saveResource(String path, boolean replace);

    @NotNull
    Player[] getOfflinePlayers();

    @NotNull
    OnlinePlayer[] getOnlinePlayers();

    OnlinePlayer getPlayer(UUID uuid);

    OnlinePlayer getPlayer(String name);

    Player getOfflinePlayer(UUID uuid);

    Player getOfflinePlayer(String name);

    Console getConsole();

    void activatePunishment(@NotNull Punishment punishment);

    void deactivatePunishment(@NotNull Punishment punishment);
}