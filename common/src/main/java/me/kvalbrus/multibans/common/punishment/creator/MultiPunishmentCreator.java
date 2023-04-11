package me.kvalbrus.multibans.common.punishment.creator;

import me.kvalbrus.multibans.api.punishment.Punishment;
import me.kvalbrus.multibans.api.punishment.creator.PunishmentCreator;
import org.jetbrains.annotations.NotNull;

public abstract class MultiPunishmentCreator implements PunishmentCreator {

    private Punishment punishment;

    public MultiPunishmentCreator() {}

    @Override
    public Punishment getPunishment() {
        return this.punishment;
    }

    @Override
    public void setPunishment(@NotNull Punishment punishment) {
        this.punishment = punishment;
    }
}