package me.kvalbrus.multibans.common.managers;

import java.io.File;
import java.util.UUID;
import me.kvalbrus.multibans.api.DataProvider;
import me.kvalbrus.multibans.api.OfflinePlayer;
import me.kvalbrus.multibans.api.Player;
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
    OfflinePlayer[] getOfflinePlayers();

    @NotNull
    Player[] getOnlinePlayers();

    Player getPlayer(UUID uuid);

    Player getPlayer(String name);

    void activatePunishment(@NotNull Punishment punishment);

    void deactivatePunishment(@NotNull Punishment punishment);
}