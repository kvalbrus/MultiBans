package me.kvalbrus.multibans.common.punishment;

import java.util.List;
import java.util.UUID;
import me.kvalbrus.multibans.api.punishment.PunishmentStatus;
import me.kvalbrus.multibans.api.punishment.PunishmentType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface IPunishment extends Comparable<IPunishment> {

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

    @Nullable
    String getComment();

    @NotNull
    List<String> getServers();

    @NotNull
    PunishmentStatus getStatus();

    void setCreatedReason(@Nullable String reason);

    void setComment(@Nullable String comment);

    void activate();

    void delete();
}