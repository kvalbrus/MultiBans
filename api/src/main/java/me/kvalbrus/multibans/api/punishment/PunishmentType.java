package me.kvalbrus.multibans.api.punishment;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;

public enum PunishmentType {

    BAN_IP("BAN_IP"),
    BAN("BAN"),
    MUTE("MUTE"),
    KICK("KICK");

    @Getter
    private final String prefix;

    PunishmentType(@NotNull String prefix) {
        this.prefix = prefix;
    }
}