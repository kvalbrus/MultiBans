package me.kvalbrus.multibans.bukkit;

import me.kvalbrus.multibans.api.MultiBans;
import me.kvalbrus.multibans.common.api.PunishmentManagerAPI;
import me.kvalbrus.multibans.common.managers.PluginManager;
import org.jetbrains.annotations.NotNull;
import me.kvalbrus.multibans.api.punishment.PunishmentManager;

public class MultiBansBukkit implements MultiBans {

    private final PluginManager pluginManager;

    private final PunishmentManager punishmentManager;

    public MultiBansBukkit(final PluginManager pluginManager) {
        this.pluginManager = pluginManager;
        this.punishmentManager = new PunishmentManagerAPI(this.pluginManager.getPunishmentManager());
    }

    @Override
    @NotNull
    public PunishmentManager getPunishmentManager() {
        return this.punishmentManager;
    }
}
