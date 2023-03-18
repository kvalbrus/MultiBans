package me.kvalbrus.multibans.api.punishment;

import java.util.List;
import java.util.UUID;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface Punishment {

    void activate();

    void deactivate();

    void deactivate(String creator, String reason);

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

    long getDateCreated();

    long getDateStart();

    long getDuration();

    @NotNull
    String getReason();

    @Nullable
    String getComment();

    long getCancellationDate();

    @Nullable
    String getCancellationCreator();

    @Nullable
    String getCancellationReason();

    @NotNull
    List<String> getServers();

    boolean isCancelled();

    void setDateStart(long dateStart);

    void setDuration(long duration);

    void setReason(String reason);

    void setComment(String comment);

    void setCancellationDate(long date);

    void setCancellationCreator(String creator);

    void setCancellationReason(String reason);

    void setServers(@NotNull List<String> servers);

    void setCancelled(boolean cancelled);
}
