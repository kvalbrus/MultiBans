package me.kvalbrus.multibans.api;

import org.jetbrains.annotations.NotNull;
import me.kvalbrus.multibans.api.managers.PunishmentManager;

public interface MultiBans {

    @NotNull
    PunishmentManager getPunishmentManager();
}