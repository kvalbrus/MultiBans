package me.kvalbrus.multibans.api.punishment;

import java.util.List;
import java.util.UUID;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface Punishment {

    void activate();

    void delete();

    @NotNull
    PunishmentStatus getStatus();

    @NotNull
    PunishmentType getType();

    @NotNull
    String getId();

    @Nullable
    String getTargetIp();

    @NotNull
    String getTargetName();

    @NotNull
    UUID getTargetUniqueId();

    @NotNull
    String getCreatorName();

    long getCreatedDate();

    @NotNull
    String getCreatedReason();

    @Nullable
    String getComment();

    @NotNull
    List<String> getServers();

    void setCreatedReason(String createdReason);

    void setComment(String comment);

    void setServers(@NotNull List<String> servers);
}
