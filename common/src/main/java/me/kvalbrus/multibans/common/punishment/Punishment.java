package me.kvalbrus.multibans.common.punishment;

import java.util.List;
import java.util.UUID;
import me.kvalbrus.multibans.api.punishment.PunishmentStatus;
import me.kvalbrus.multibans.api.punishment.PunishmentType;
import me.kvalbrus.multibans.common.managers.PunishmentManager;
import me.kvalbrus.multibans.common.punishment.punishments.PermanentlyBan;
import me.kvalbrus.multibans.common.punishment.punishments.PermanentlyChatMute;
import me.kvalbrus.multibans.common.punishment.punishments.TemporaryBan;
import me.kvalbrus.multibans.common.punishment.punishments.TemporaryBanIp;
import me.kvalbrus.multibans.common.punishment.punishments.TemporaryChatMute;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface Punishment extends Comparable<Punishment> {

    @NotNull
    PunishmentType getType();

    @NotNull
    String getId();

    @NotNull
    String getTargetIp();

    @NotNull
    String getTargetName();

    @NotNull
    UUID getTargetUniqueId();

    @NotNull
    String getCreatorName();

    long getCreatedDate();

    @Nullable
    String getCreatedReason();

    void setCreatedReason(@Nullable String reason);

    @Nullable
    String getComment();

    void setComment(@Nullable String comment);

    @NotNull
    List<String> getServers();

    @NotNull
    PunishmentStatus getStatus();

    void activate();

    void delete();
}