package me.kvalbrus.multibans.common.punishment;

import java.util.List;
import java.util.UUID;
import me.kvalbrus.multibans.api.punishment.Punishment;
import me.kvalbrus.multibans.api.punishment.PunishmentType;
import me.kvalbrus.multibans.api.punishment.TemporaryPunishment;
import me.kvalbrus.multibans.common.managers.PunishmentManager;
import me.kvalbrus.multibans.common.punishment.punishments.MultiPermanentlyBan;
import me.kvalbrus.multibans.common.punishment.punishments.MultiPermanentlyBanIp;
import me.kvalbrus.multibans.common.punishment.punishments.MultiPermanentlyChatMute;
import me.kvalbrus.multibans.common.punishment.punishments.MultiTemporaryBan;
import me.kvalbrus.multibans.common.punishment.punishments.MultiTemporaryBanIp;
import me.kvalbrus.multibans.common.punishment.punishments.MultiTemporaryChatMute;
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

    private final long createdDate;

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
                           final long createdDate,
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
        this.createdDate = createdDate;
        this.reason = reason;
        this.comment = comment;
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
                punishment = new MultiPermanentlyBan(punishmentManager, id, targetIp, targetName,
                    targetUniqueId, creatorName, createdDate, createdReason, comment,
                    cancellationCreator, cancellationDate, cancellationReason, servers, cancelled);
                break;

            case TEMP_BAN:
                punishment = new MultiTemporaryBan(punishmentManager, id, targetIp, targetName,
                    targetUniqueId,
                    creatorName, createdDate, startedDate, duration, createdReason, comment,
                    cancellationCreator, cancellationDate, cancellationReason, servers, cancelled);
                break;

            case BAN_IP:
                punishment = new MultiPermanentlyBanIp(punishmentManager, id, targetIp, targetName,
                    targetUniqueId, creatorName, createdDate, createdReason, comment,
                    cancellationCreator, cancellationDate, cancellationReason, servers, cancelled);
                break;

            case TEMP_BAN_IP:
                punishment = new MultiTemporaryBanIp(punishmentManager, id, targetIp, targetName,
                    targetUniqueId, creatorName, createdDate, startedDate, duration, createdReason,
                    comment, cancellationCreator, cancellationDate, cancellationReason, servers,
                    cancelled);
                break;

            case MUTE:
                punishment = new MultiPermanentlyChatMute(punishmentManager, id, targetIp,
                    targetName,
                    targetUniqueId, creatorName, createdDate, createdReason, comment,
                    cancellationCreator, cancellationDate, cancellationReason, servers, cancelled);
                break;

            case TEMP_MUTE:
                punishment = new MultiTemporaryChatMute(punishmentManager, id, targetIp, targetName,
                    targetUniqueId, creatorName, createdDate, startedDate, duration, createdReason,
                    comment, cancellationCreator, cancellationDate, cancellationReason, servers,
                    cancelled);
                break;

            default:
                throw new IllegalArgumentException("Punishment type hasn't its constructor");
        }

        return (T) punishment;
    }

    @Override
    public synchronized void activate() {
        this.punishmentManager.getPluginManager().activatePunishment(this);
        this.updateData();
    }

    @Override
    public synchronized void delete() {
        this.punishmentManager.getPluginManager().getDataProvider().deletePunishment(this);
        this.deleteData();
    }

    @NotNull
    public final PunishmentManager getPunishmentManager() {
        return this.punishmentManager;
    }

    @NotNull
    @Override
    public final PunishmentType getType() {
        return this.type;
    }

    @NotNull
    @Override
    public final String getId() {
        return this.id;
    }

    @NotNull
    @Override
    public final String getTargetIp() {
        return this.targetIp;
    }

    @NotNull
    @Override
    public final String getTargetName() {
        return this.targetName;
    }

    @NotNull
    @Override
    public final UUID getTargetUniqueId() {
        return this.targetUUID;
    }

    @NotNull
    @Override
    public final String getCreatorName() {
        return this.creatorName;
    }

    @Override
    public final long getCreatedDate() {
        return this.createdDate;
    }

    @Nullable
    @Override
    public final String getCreatedReason() {
        return this.reason;
    }

    @Nullable
    @Override
    public final String getComment() {
        return this.comment;
    }

    @NotNull
    @Override
    public final List<String> getServers() {
        return this.servers;
    }

    @Override
    public synchronized void setCreatedReason(@Nullable String reason) {
        this.reason = reason;
        this.updateData();
    }

    @Override
    public synchronized void setComment(@Nullable String comment) {
        this.comment = comment;
        this.updateData();
    }

    @Override
    public synchronized void setServers(@NotNull List<String> servers) {
        this.servers = servers;
        this.updateData();
    }

    @Override
    public int compareTo(@NotNull Punishment punishment2) {
        Punishment punishment1 = this;

        if (punishment1.getType() != punishment2.getType()) {
            return punishment1.getType().compareTo(punishment2.getType());
        }

        if (!punishment1.getTargetUniqueId().equals(punishment2.getTargetUniqueId())) {
            return punishment1.getTargetUniqueId().compareTo(punishment2.getTargetUniqueId());
        }

        if (punishment1 instanceof TemporaryPunishment t1 &&
            punishment2 instanceof TemporaryPunishment t2) {
            if (t1.getStartedDate() != t2.getStartedDate()) {
                return Long.compare(t1.getStartedDate(), t2.getStartedDate());
            }

            if (t1.getDuration() != t2.getDuration()) {
                return Long.compare(t1.getDuration(), t2.getDuration());
            }
        }

        if (!punishment1.getCreatorName().equals(punishment2.getCreatorName())) {
            return punishment1.getCreatorName().compareTo(punishment2.getCreatorName());
        }

        if (punishment1.getCreatedDate() != punishment2.getCreatedDate()) {
            return Long.compare(punishment1.getCreatedDate(), punishment2.getCreatedDate());
        }

        return punishment1.getId().compareTo(punishment2.getId());
    }

    public abstract void updateData();

    public abstract void deleteData();
}