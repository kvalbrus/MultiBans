package me.kvalbrus.multibans.common.punishment.punishments;

import java.util.List;
import java.util.UUID;
import me.kvalbrus.multibans.api.punishment.PunishmentType;
import me.kvalbrus.multibans.common.managers.PunishmentManager;
import me.kvalbrus.multibans.common.punishment.IPunishment;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class MultiPunishment implements IPunishment {

    private final PunishmentManager punishmentManager;

    private final PunishmentType type;

    private final String id;

    private final String targetIp;

    private final String targetName;

    private final UUID targetUUID;

    private final String creatorName;

    private final long dateCreated;

    private String reason;

    private String comment;

    private List<String> servers;

    public MultiPunishment(@NotNull PunishmentManager punishmentManager,
                           @NotNull final PunishmentType type,
                           @NotNull final String id,
                           @NotNull final String targetIp,
                           @NotNull final String targetName,
                           @NotNull final UUID targetUUID,
                           @NotNull final String creatorName,
                           final long dateCreated) {
        this.punishmentManager = punishmentManager;
        this.type = type;
        this.id = id;
        this.targetIp = targetIp;
        this.targetName = targetName;
        this.targetUUID = targetUUID;
        this.creatorName = creatorName;
        this.dateCreated = dateCreated;
    }

    @NotNull
    public final PunishmentManager getPunishmentManager() {
        return this.punishmentManager;
    }

    @NotNull
    public final PunishmentType getType() {
        return this.type;
    }

    @NotNull
    public final String getId() {
        return this.id;
    }

    @NotNull
    public final String getTargetIp() {
        return this.targetIp;
    }

    @NotNull
    public final String getTargetName() {
        return this.targetName;
    }

    @NotNull
    public final UUID getTargetUniqueId() {
        return this.targetUUID;
    }

    @NotNull
    public final String getCreatorName() {
        return this.creatorName;
    }

    public final long getCreatedDate() {
        return this.dateCreated;
    }

    @Nullable
    public String getCreatedReason() {
        return this.reason;
    }

    @Nullable
    public String getComment() {
        return this.comment;
    }

    @NotNull
    public List<String> getServers() {
        return this.servers;
    }

    public synchronized void setCreatedReason(@Nullable String reason) {
        this.reason = reason;
    }

    public synchronized void setComment(@Nullable String comment) {
        this.comment = comment;
    }

    public synchronized void setServers(@NotNull List<String> servers) {
        this.servers = servers;
    }
}