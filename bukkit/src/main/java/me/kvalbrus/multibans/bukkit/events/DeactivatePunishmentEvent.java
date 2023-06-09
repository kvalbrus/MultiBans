package me.kvalbrus.multibans.bukkit.events;

import me.kvalbrus.multibans.api.punishment.Punishment;
import me.kvalbrus.multibans.api.punishment.action.DeactivationAction;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class DeactivatePunishmentEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private final Punishment punishment;

    private final DeactivationAction action;

    public DeactivatePunishmentEvent(@NotNull Punishment punishment, @NotNull DeactivationAction action) {
        this.punishment = punishment;
        this.action = action;
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
    public DeactivationAction getAction() {
        return this.action;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return handlers;
    }
}