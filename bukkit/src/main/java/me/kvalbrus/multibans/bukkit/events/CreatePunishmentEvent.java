package me.kvalbrus.multibans.bukkit.events;

import me.kvalbrus.multibans.api.punishment.action.CreationAction;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import me.kvalbrus.multibans.api.punishment.Punishment;

public class CreatePunishmentEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private final Punishment punishment;

    private final CreationAction action;

    public CreatePunishmentEvent(@NotNull Punishment punishment, @NotNull CreationAction action) {
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
    public CreationAction action() {
        return this.action;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return handlers;
    }
}