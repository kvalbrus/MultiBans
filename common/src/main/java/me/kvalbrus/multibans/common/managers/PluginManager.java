package me.kvalbrus.multibans.common.managers;

import java.io.File;
import org.jetbrains.annotations.NotNull;
import me.kvalbrus.multibans.common.punishment.Punishment;
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

    void activatePunishment(@NotNull Punishment punishment);

    void deactivatePunishment(@NotNull Punishment punishment);
}