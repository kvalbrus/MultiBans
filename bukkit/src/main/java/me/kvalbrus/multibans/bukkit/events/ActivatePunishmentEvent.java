package me.kvalbrus.multibans.bukkit.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import me.kvalbrus.multibans.api.punishment.Punishment;

public class ActivatePunishmentEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private final Punishment punishment;

    public ActivatePunishmentEvent(@NotNull Punishment punishment) {
        this.punishment = punishment;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    @NotNull
    public Punishment getPunishment() {
        return this.punishment;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return handlers;
    }
}
