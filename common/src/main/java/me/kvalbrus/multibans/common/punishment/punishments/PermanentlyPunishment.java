package me.kvalbrus.multibans.common.punishment.punishments;

import java.util.UUID;
import me.kvalbrus.multibans.api.punishment.PunishmentStatus;
import me.kvalbrus.multibans.api.punishment.PunishmentType;
import me.kvalbrus.multibans.common.managers.PunishmentManager;
import me.kvalbrus.multibans.common.punishment.Cancelable;
import me.kvalbrus.multibans.common.punishment.IPunishment;
import me.kvalbrus.multibans.common.punishment.Permanently;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class PermanentlyPunishment extends MultiPunishment implements Permanently, Cancelable {

    private String cancellationCreator;

    private long cancellationDate;

    private String cancellationReason;

    private boolean cancelled;

    public PermanentlyPunishment(@NotNull PunishmentManager punishmentManager,
                                 @NotNull PunishmentType type,
                                 @NotNull String id,
                                 @NotNull String targetIp,
                                 @NotNull String targetName,
                                 @NotNull UUID targetUUID,
                                 @NotNull String creatorName,
                                 long dateCreated) {
        super(punishmentManager, type, id, targetIp, targetName, targetUUID, creatorName, dateCreated);
    }

    @NotNull
    @Override
    public PunishmentStatus getStatus() {
        if (this.cancelled) {
            return PunishmentStatus.CANCELLED;
        } else {
            return PunishmentStatus.ACTIVE;
        }
    }

    @Nullable
    @Override
    public String getCancellationCreator() {
        return this.cancellationCreator;
    }

    @Override
    public long getCancellationDate() {
        return this.cancellationDate;
    }

    @Nullable
    @Override
    public String getCancellationReason() {
        return this.cancellationReason;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public synchronized void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    @Override
    public synchronized void setCancellationCreator(@Nullable String creator) {
        this.cancellationCreator = creator;
    }

    @Override
    public synchronized void setCancellationDate(long date) {
        this.cancellationDate = date;
    }

    @Override
    public synchronized void setCancellationReason(@NotNull String reason) {
        this.cancellationReason = reason;
    }

    @Override
    public int compareTo(@NotNull IPunishment o) {
        return 0;
    }
}
