package me.kvalbrus.multibans.api.punishment.target;

import me.kvalbrus.multibans.api.Player;
import me.kvalbrus.multibans.api.punishment.Punishment;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface PunishmentTarget extends Player {

    @Nullable
    Punishment getPunishment();

    void setPunishment(@NotNull Punishment punishment);
}