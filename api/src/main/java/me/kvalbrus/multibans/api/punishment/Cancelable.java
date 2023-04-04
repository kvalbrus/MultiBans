package me.kvalbrus.multibans.api.punishment;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface Cancelable {

    void deactivate();

    void deactivate(@Nullable String creator, long date, @Nullable String reason);

    @Nullable
    String getCancellationCreator();

    long getCancellationDate();

    @Nullable
    String getCancellationReason();

    boolean isCancelled();

    void setCancelled(boolean cancelled);

    void setCancellationCreator(@Nullable String creator);

    void setCancellationDate(long date);

    void setCancellationReason(@NotNull String reason);
}