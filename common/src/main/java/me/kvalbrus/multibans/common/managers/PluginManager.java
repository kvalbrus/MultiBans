package me.kvalbrus.multibans.common.managers;

import java.io.File;
import java.util.UUID;
import me.kvalbrus.multibans.api.Console;
import me.kvalbrus.multibans.api.DataProvider;
import me.kvalbrus.multibans.api.OnlinePlayer;
import me.kvalbrus.multibans.api.Player;
import me.kvalbrus.multibans.api.managers.SessionManager;
import me.kvalbrus.multibans.api.punishment.Punishment;
import me.kvalbrus.multibans.api.managers.PunishmentManager;
import me.kvalbrus.multibans.api.punishment.action.ActivationAction;
import me.kvalbrus.multibans.api.punishment.action.CreationAction;
import me.kvalbrus.multibans.api.punishment.action.DeactivationAction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface PluginManager {

    void enable();

    void disable();

    void onLoad();

    void onEnable();

    void onDisable();

    void reload();

    @Nullable
    DataProvider getDataProvider();

    @NotNull
    PunishmentManager getPunishmentManager();

    @NotNull
    SessionManager getSessionManager();

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

    @NotNull
    Console getConsole();

    void createPunishment(@NotNull Punishment punishment, @NotNull CreationAction action);

    void activatePunishment(@NotNull Punishment punishment, @NotNull ActivationAction action);

    void deactivatePunishment(@NotNull Punishment punishment, @NotNull DeactivationAction action);

    void deletePunishment(@NotNull Punishment punishment);
}