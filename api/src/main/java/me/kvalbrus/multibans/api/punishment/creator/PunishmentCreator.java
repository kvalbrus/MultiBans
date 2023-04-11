package me.kvalbrus.multibans.api.punishment.creator;

import me.kvalbrus.multibans.api.Nameable;
import me.kvalbrus.multibans.api.punishment.Punishment;
import org.jetbrains.annotations.NotNull;

public interface PunishmentCreator extends Nameable {

    Punishment getPunishment();

    void setPunishment(@NotNull Punishment punishment);
}