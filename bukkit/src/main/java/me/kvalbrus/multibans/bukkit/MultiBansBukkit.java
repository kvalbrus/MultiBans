package me.kvalbrus.multibans.bukkit;

import me.kvalbrus.multibans.api.MultiBans;
import me.kvalbrus.multibans.common.managers.PluginManager;
import org.jetbrains.annotations.NotNull;
import me.kvalbrus.multibans.api.punishment.PunishmentManager;

public class MultiBansBukkit implements MultiBans {

    private final PunishmentManager punishmentManager;

    public MultiBansBukkit(final PluginManager pluginManager) {
        this.punishmentManager = new me.kvalbrus.multibans.common.managers.PunishmentManager(pluginManager);
    }

    @Override
    @NotNull
    public PunishmentManager getPunishmentManager() {
        return this.punishmentManager;
    }
}
