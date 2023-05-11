package me.kvalbrus.multibans.api.punishment;

import me.kvalbrus.multibans.api.punishment.creator.PunishmentCreator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface Cancelable {

    void deactivate(@Nullable PunishmentCreator cancellationCreator, long cancellationDate, @Nullable String cancellationReason);

    @Nullable
    PunishmentCreator getCancellationCreator();

    long getCancellationDate();

    @Nullable
    String getCancellationReason();

    boolean isCancelled();

    void setCancelled(boolean cancelled);

    void setCancellationCreator(@Nullable PunishmentCreator cancellationCreator);

    void setCancellationDate(long cancellationDate);

    void setCancellationReason(@NotNull String cancellationReason);
}