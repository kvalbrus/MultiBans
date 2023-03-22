package me.kvalbrus.multibans.common.punishment;

import java.util.List;
import java.util.UUID;
import me.kvalbrus.multibans.api.punishment.PunishmentType;
import me.kvalbrus.multibans.common.managers.PunishmentManager;
import me.kvalbrus.multibans.common.punishment.punishments.PermanentlyBan;
import me.kvalbrus.multibans.common.punishment.punishments.PermanentlyChatMute;
import me.kvalbrus.multibans.common.punishment.punishments.TemporaryBan;
import me.kvalbrus.multibans.common.punishment.punishments.TemporaryBanIp;
import me.kvalbrus.multibans.common.punishment.punishments.TemporaryChatMute;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class MultiPunishment implements Punishment {

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
                           final long dateCreated,
                           @Nullable String reason,
                           @Nullable String comment,
                           @NotNull List<String> servers) {
        this.punishmentManager = punishmentManager;
        this.type = type;
        this.id = id;
        this.targetIp = targetIp;
        this.targetName = targetName;
        this.targetUUID = targetUUID;
        this.creatorName = creatorName;
        this.dateCreated = dateCreated;
        this.reason = reason;
        this.comment = comment;
        this.servers = servers;
    }

    public synchronized void updateData() {
        this.punishmentManager.getPluginManager().getDataProvider().updatePunishment(this);
    }

    @Override
    public synchronized void activate() {
        this.punishmentManager.getPluginManager().activatePunishment(this);
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
    public final String getCreatedReason() {
        return this.reason;
    }

    @Nullable
    public final String getComment() {
        return this.comment;
    }

    @NotNull
    public final List<String> getServers() {
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

    @NotNull
    public static <T extends Punishment> T constructPunishment(
        @NotNull PunishmentManager punishmentManager,
        @NotNull PunishmentType type,
        @NotNull String id,
        @NotNull String targetIp,
        @NotNull String targetName,
        @NotNull UUID targetUniqueId,
        @NotNull String creatorName,
        long createdDate,
        long startedDate,
        long duration,
        @Nullable String createdReason,
        @Nullable String comment,
        @Nullable String cancellationCreator,
        long cancellationDate,
        @Nullable String cancellationReason,
        @NotNull List<String> servers,
        boolean cancelled) {
        Punishment punishment;
        switch (type) {
            case BAN:
                punishment = new TemporaryBan(punishmentManager, id, targetIp, targetName, targetUniqueId,
                    creatorName, createdDate, startedDate, duration, createdReason, comment,
                    cancellationCreator, cancellationDate, cancellationReason, servers, cancelled);
                break;

            case TEMP_BAN:
                 punishment = new TemporaryBan(punishmentManager, id, targetIp, targetName, targetUniqueId,
                    creatorName, createdDate, startedDate, duration, createdReason, comment,
                    cancellationCreator, cancellationDate, cancellationReason, servers, cancelled);
                 break;

            case BAN_IP:
                punishment = new PermanentlyBan(punishmentManager, id, targetIp, targetName,
                    targetUniqueId, creatorName, createdDate, createdReason, comment,
                    cancellationCreator, cancellationDate, cancellationReason, servers, cancelled);
                break;

            case TEMP_BAN_IP:
                punishment = new TemporaryBanIp(punishmentManager, id, targetIp, targetName,
                    targetUniqueId, creatorName, createdDate, startedDate, duration, createdReason,
                    comment, cancellationCreator, cancellationDate, cancellationReason, servers,
                    cancelled);
                break;

            case MUTE:
                punishment = new PermanentlyChatMute(punishmentManager, id, targetIp, targetName,
                    targetUniqueId, creatorName, createdDate, createdReason, comment,
                    cancellationCreator, cancellationDate, cancellationReason, servers, cancelled);
                break;

            case TEMP_MUTE:
                punishment = new TemporaryChatMute(punishmentManager, id, targetIp, targetName,
                    targetUniqueId, creatorName, createdDate, startedDate, duration, createdReason,
                    comment, cancellationCreator, cancellationDate, cancellationReason, servers,
                    cancelled);
                break;

            default:
                throw new IllegalArgumentException("Punishment type hasn't its constructor");
        }

        return (T) punishment;
    }
}