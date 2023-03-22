package me.kvalbrus.multibans.api.punishment;

import lombok.Getter;
import me.kvalbrus.multibans.common.punishment.Punishment;
import me.kvalbrus.multibans.common.punishment.punishments.PermanentlyBan;
import me.kvalbrus.multibans.common.punishment.punishments.PermanentlyBanIp;
import me.kvalbrus.multibans.common.punishment.punishments.PermanentlyChatMute;
import me.kvalbrus.multibans.common.punishment.punishments.TemporaryBan;
import me.kvalbrus.multibans.common.punishment.punishments.TemporaryBanIp;
import me.kvalbrus.multibans.common.punishment.punishments.TemporaryChatMute;
import org.jetbrains.annotations.NotNull;

public enum PunishmentType {

    TEMP_BAN_IP("TEMP_BAN_IP", TemporaryBanIp.class),
    BAN_IP("BAN_IP", PermanentlyBanIp.class),

    TEMP_BAN("TEMP_BAN", TemporaryBan.class),

    BAN("BAN", PermanentlyBan.class),

    TEMP_MUTE("TEMP_MUTE", TemporaryChatMute.class),
    MUTE("MUTE", PermanentlyChatMute.class);

    @Getter
    private final String prefix;

    @Getter
    private final Class<? extends Punishment> clazz;

    PunishmentType(@NotNull String prefix, @NotNull Class<? extends Punishment> clazz) {
        this.prefix = prefix;
        this.clazz = clazz;
    }
}