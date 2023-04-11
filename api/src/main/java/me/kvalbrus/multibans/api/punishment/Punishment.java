package me.kvalbrus.multibans.api.punishment;

import java.util.List;
import java.util.UUID;
import me.kvalbrus.multibans.api.punishment.creator.PunishmentCreator;
import me.kvalbrus.multibans.api.punishment.target.PunishmentTarget;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface Punishment extends Comparable<Punishment> {

    void activate();

    void delete();

    @NotNull
    PunishmentStatus getStatus();

    @NotNull
    PunishmentType getType();

    @NotNull
    String getId();

    @Deprecated
    @Nullable
    String getTargetIp();

    @Deprecated
    @NotNull
    String getTargetName();

    @Deprecated
    @NotNull
    UUID getTargetUniqueId();

    @Deprecated
    @NotNull
    String getCreatorName();

    PunishmentTarget getTarget();

    PunishmentCreator getCreator();

    long getCreatedDate();

    @Nullable
    String getCreatedReason();

    @Nullable
    String getComment();

    @NotNull
    List<String> getServers();

    void setCreatedReason(@Nullable String createdReason);

    void setComment(@Nullable String comment);

    void setServers(@NotNull List<String> servers);
}