package me.kvalbrus.multibans.common.punishment;

import java.util.List;
import java.util.UUID;
import me.kvalbrus.multibans.api.punishment.PunishmentStatus;
import me.kvalbrus.multibans.api.punishment.PunishmentType;
import me.kvalbrus.multibans.api.punishment.TemporaryPunishment;
import me.kvalbrus.multibans.common.managers.PunishmentManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class MultiTemporaryPunishment extends MultiPunishment implements
    TemporaryPunishment {

    private long startedDate;

    private String cancellationCreator;

    private long cancellationDate;

    private String cancellationReason;

    private boolean cancelled;

    private long duration;

    public MultiTemporaryPunishment(@NotNull PunishmentManager punishmentManager,
                                    @NotNull PunishmentType type,
                                    @NotNull String id,
                                    @NotNull String targetIp,
                                    @NotNull String targetName,
                                    @NotNull UUID targetUUID,
                                    @NotNull String creatorName,
                                    long createdDate,
                                    long startedDate,
                                    long duration,
                                    @Nullable String reason,
                                    @Nullable String comment,
                                    @Nullable String cancellationCreator,
                                    long cancellationDate,
                                    @Nullable String cancellationReason,
                                    @NotNull List<String> servers,
                                    boolean cancelled) {
        super(punishmentManager, type, id, targetIp, targetName, targetUUID, creatorName,
            createdDate, reason, comment, servers);
        this.startedDate = startedDate;
        this.cancellationCreator = cancellationCreator;
        this.cancellationDate = cancellationDate;
        this.cancellationReason = cancellationReason;
        this.cancelled = cancelled;
        this.duration = duration;
    }

    @Override
    public synchronized void activate() {
        if (!this.cancelled) {
            return;
        }

        super.activate();

        List<? extends MultiTemporaryPunishment> activePunishments = this.getPunishmentManager()
            .getActivePunishments(this.getTargetUniqueId(), this.getClass());

        final int size = activePunishments.size();
        if (size > 0) {
            activePunishments.sort(null);

            MultiTemporaryPunishment last = activePunishments.get(size - 1);
            this.startedDate = last.startedDate + last.duration;
        } else {
            this.startedDate = this.getCreatedDate();
        }
    }

    @Override
    public synchronized void deactivate() {
        this.deactivate(null, -1, null);
    }

    @Override
    public synchronized void deactivate(@Nullable String cancellationCreator,
                                        long cancellationDate,
                                        @Nullable String cancellationReason) {
        if (this.cancelled) {
            return;
        }

        List<? extends MultiTemporaryPunishment> history = this.getPunishmentManager()
            .getPlayerHistory(this.getTargetUniqueId(), this.getClass());

        if(history.stream().noneMatch(punishment -> punishment.getId().equals(this.getId()))) {
            return;
        }

        history.sort(null);
        history.removeIf(MultiTemporaryPunishment::isCancelled);
        int size = history.size();
        if (this.duration > 0 && size > 0) {
            MultiTemporaryPunishment prev = null, curr = null;
            int index = -1;
            for (MultiTemporaryPunishment punishment : history) {
                if (punishment.getId().equals(this.getId())) {
                    index = history.indexOf(punishment);
                    break;
                }
            }

            if (index != -1) {
                if (index == 0) {
                    while (index < size - 1) {
                        curr = history.get(index + 1);
                        if (prev == null) {
                            curr.startedDate = curr.getCreatedDate();
                        } else {
                            curr.startedDate = prev.startedDate + prev.duration;
                        }

                        prev = curr;
                        index++;

                        curr.updateData();
                    }
                } else if (index != size - 1) {
                    prev = history.get(index - 1);
                    while (index < size - 1) {
                        curr = history.get(index + 1);
                        curr.startedDate = prev.startedDate + prev.duration;

                        prev = curr;
                        index++;

                        curr.updateData();
                    }
                }
            }
        }

        this.cancelled = true;
        this.cancellationCreator = cancellationCreator;
        this.cancellationDate = cancellationDate;
        this.cancellationReason = cancellationReason;
    }

    @Override
    public synchronized void delete() {
        this.deactivate();
        super.delete();
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

    public long getStartedDate() {
        return this.startedDate;
    }

    @Nullable
    @Override
    public final String getCancellationCreator() {
        return this.cancellationCreator;
    }

    @Override
    public final long getCancellationDate() {
        return this.cancellationDate;
    }

    @Nullable
    @Override
    public final String getCancellationReason() {
        return this.cancellationReason;
    }

    public final long getDuration() {
        return this.duration;
    }

    @Override
    public final boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public synchronized void setCancellationCreator(@Nullable String creator) {
        this.cancellationCreator = creator;
    }

    public synchronized void setStartedDate(long startedDate) {
        this.startedDate = startedDate;
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

    public synchronized void setDuration(long duration) {
        this.duration = duration;
    }




}