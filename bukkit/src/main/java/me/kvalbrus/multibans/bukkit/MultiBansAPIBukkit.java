package me.kvalbrus.multibans.bukkit;

import me.kvalbrus.multibans.api.MultiBansAPI;
import me.kvalbrus.multibans.common.managers.PluginManager;
import org.jetbrains.annotations.NotNull;
import me.kvalbrus.multibans.api.managers.PunishmentManager;

public class MultiBansAPIBukkit implements MultiBansAPI {

    private final PunishmentManager punishmentManager;

    public MultiBansAPIBukkit(final PluginManager pluginManager) {
        this.punishmentManager = new me.kvalbrus.multibans.common.managers.PunishmentManager(pluginManager);
    }

    @Override
    @NotNull
    public PunishmentManager getPunishmentManager() {
        return this.punishmentManager;
    }
}
