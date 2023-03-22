package me.kvalbrus.multibans.common.punishment.punishments;

import java.util.UUID;
import me.kvalbrus.multibans.api.punishment.PunishmentStatus;
import me.kvalbrus.multibans.api.punishment.PunishmentType;
import me.kvalbrus.multibans.common.managers.PunishmentManager;
import me.kvalbrus.multibans.common.punishment.Cancelable;
import me.kvalbrus.multibans.common.punishment.IPunishment;
import me.kvalbrus.multibans.common.punishment.Temporary;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class TemporaryPunishment extends MultiPunishment implements Temporary, Cancelable {

    private String cancellationCreator;

    private long cancellationDate;

    private String cancellationReason;

    private boolean cancelled;

    private long duration;

    public TemporaryPunishment(@NotNull PunishmentManager punishmentManager,
                               @NotNull PunishmentType type,
                               @NotNull String id,
                               @NotNull String targetIp,
                               @NotNull String targetName,
                               @NotNull UUID targetUUID,
                               @NotNull String creatorName,
                               long dateCreated,
                               long duration) {
        super(punishmentManager, type, id, targetIp, targetName, targetUUID, creatorName, dateCreated);
        this.duration = duration;
    }

    @Override
    public void activate() {
        this.cancelled = false;
    }

    @Override
    public void deactivate() {
        this.cancelled = true;
    }

    @Override
    public void deactivate(@Nullable String creator, long date, @Nullable String reason) {
        this.deactivate();
        this.cancellationCreator = creator;
        this.cancellationDate = date;
        this.cancellationReason = reason;
    }

    @Override
    public void delete() {

    }

    @NotNull
    @Override
    public PunishmentStatus getStatus() {
        if (this.cancelled) {
            return PunishmentStatus.CANCELLED;
        } else if (System.currentTimeMillis() > this.getCreatedDate() + this.duration) {
            return PunishmentStatus.PASSED;
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
    public long getDuration() {
        return this.duration;
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
    public synchronized void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    @Override
    public synchronized void setDuration(long duration) {
        this.duration = duration;
    }

    @Override
    public int compareTo(@NotNull IPunishment o) {
        return 0;
    }
}