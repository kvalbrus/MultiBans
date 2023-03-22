package me.kvalbrus.multibans.common.punishment;

import java.util.List;
import java.util.UUID;
import me.kvalbrus.multibans.api.punishment.PunishmentStatus;
import me.kvalbrus.multibans.api.punishment.PunishmentType;
import me.kvalbrus.multibans.common.managers.PunishmentManager;
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
                                 long dateCreated,
                                 @Nullable String reason,
                                 @Nullable String comment,
                                 @Nullable String cancellationCreator,
                                 long cancellationDate,
                                 @Nullable String cancellationReason,
                                 @NotNull List<String> servers,
                                 boolean cancelled) {
        super(punishmentManager, type, id, targetIp, targetName, targetUUID, creatorName,
            dateCreated, reason, comment, servers);
        this.cancellationCreator = cancellationCreator;
        this.cancellationDate = cancellationDate;
        this.cancellationReason = cancellationReason;
        this.cancelled = cancelled;
    }

    @Override
    public synchronized void activate() {
        super.activate();
        this.cancelled = false;
    }

    @Override
    public synchronized void deactivate() {
        this.getPunishmentManager().getPluginManager().deactivatePunishment(this);
        this.cancelled = true;
    }

    @Override
    public synchronized void deactivate(@Nullable String cancellationCreator,
                                        long cancellationDate,
                                        @Nullable String cancellationReason) {
        this.deactivate();
        this.cancellationCreator = cancellationCreator;
        this.cancellationDate = cancellationDate;
        this.cancellationReason = cancellationReason;
    }

    public synchronized void delete() {}

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
    public int compareTo(@NotNull Punishment o) {
        return 0;
    }
}
