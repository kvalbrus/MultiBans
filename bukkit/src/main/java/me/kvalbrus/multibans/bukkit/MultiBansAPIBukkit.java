package me.kvalbrus.multibans.bukkit;

import me.kvalbrus.multibans.api.MultiBansAPI;
import me.kvalbrus.multibans.api.managers.SessionManager;
import me.kvalbrus.multibans.common.managers.PluginManager;
import org.jetbrains.annotations.NotNull;
import me.kvalbrus.multibans.api.managers.PunishmentManager;

public class MultiBansAPIBukkit implements MultiBansAPI {

    private final PluginManager pluginManager;

    public MultiBansAPIBukkit(final PluginManager pluginManager) {
        this.pluginManager = pluginManager;
    }

    @Override
    @NotNull
    public PunishmentManager getPunishmentManager() {
        return this.pluginManager.getPunishmentManager();
    }

    @NotNull
    @Override
    public SessionManager getSessionManager() {
        return this.pluginManager.getSessionManager();
    }
}
