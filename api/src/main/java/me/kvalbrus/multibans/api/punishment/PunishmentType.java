package me.kvalbrus.multibans.api.punishment;

import lombok.Getter;
import me.kvalbrus.multibans.api.punishment.punishments.Kick;
import me.kvalbrus.multibans.api.punishment.punishments.PermanentlyBan;
import me.kvalbrus.multibans.api.punishment.punishments.PermanentlyBanIp;
import me.kvalbrus.multibans.api.punishment.punishments.PermanentlyChatMute;
import me.kvalbrus.multibans.api.punishment.punishments.TemporaryBan;
import me.kvalbrus.multibans.api.punishment.punishments.TemporaryBanIp;
import me.kvalbrus.multibans.api.punishment.punishments.TemporaryChatMute;
import org.jetbrains.annotations.NotNull;

public enum PunishmentType {

    TEMP_BAN_IP("TEMP_BAN_IP", TemporaryBanIp.class),
    BAN_IP("BAN_IP", PermanentlyBanIp.class),

    TEMP_BAN("TEMP_BAN", TemporaryBan.class),
    BAN("BAN", PermanentlyBan.class),

    TEMP_MUTE("TEMP_MUTE", TemporaryChatMute.class),
    MUTE("MUTE", PermanentlyChatMute.class),

    KICK("KICK", Kick.class);

    @Getter
    private final String prefix;

    @Getter
    private final Class<? extends Punishment> clazz;

    PunishmentType(@NotNull String prefix, @NotNull Class<? extends Punishment> clazz) {
        this.prefix = prefix;
        this.clazz = clazz;
    }
}