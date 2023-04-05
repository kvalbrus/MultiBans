package me.kvalbrus.multibans.common.managers;

import java.util.List;
import java.io.File;
import java.util.UUID;
import me.kvalbrus.multibans.api.punishment.Punishment;
import me.kvalbrus.multibans.api.Player;
import org.jetbrains.annotations.NotNull;
import me.kvalbrus.multibans.common.storage.DataProvider;

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
    List<String> getPlayers();

    Player getPlayer(UUID uuid);

    Player getPlayer(String name);

    @Deprecated
    void activatePunishment(@NotNull Punishment punishment);

    @Deprecated
    void deactivatePunishment(@NotNull Punishment punishment);
}