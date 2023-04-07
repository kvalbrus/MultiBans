package me.kvalbrus.multibans.common.punishment;

import java.util.List;
import java.util.UUID;
import me.kvalbrus.multibans.api.punishment.PunishmentStatus;
import me.kvalbrus.multibans.api.punishment.PunishmentType;
import me.kvalbrus.multibans.api.punishment.TemporaryPunishment;
import me.kvalbrus.multibans.common.managers.PluginManager;
import me.kvalbrus.multibans.common.storage.DataProvider;
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

    public MultiTemporaryPunishment(@NotNull PluginManager pluginManager,
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
        super(pluginManager, type, id, targetIp, targetName, targetUUID, creatorName,
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
        this.cancelled = false;

        List<? extends MultiTemporaryPunishment> activePunishments = this.getPluginManager()
            .getPunishmentManager().getActivePunishments(this.getTargetUniqueId(), this.getClass());

        final int size = activePunishments.size();
        if (size > 0) {
            activePunishments.sort(null);

            MultiTemporaryPunishment last = activePunishments.get(size - 1);

            this.startedDate = last.startedDate + last.duration;

        } else {
            this.startedDate = this.getCreatedDate();
        }

        this.getPluginManager().activatePunishment(this);
        this.updateData();
    }

    @Override
    public synchronized void deactivate() {
        this.deactivate(null, System.currentTimeMillis(), null);
    }

    @Override
    public synchronized void deactivate(@Nullable String cancellationCreator,
                                        long cancellationDate,
                                        @Nullable String cancellationReason) {
        if (this.cancelled) {
            return;
        }

        List<? extends MultiTemporaryPunishment> history = this.getPluginManager()
            .getPunishmentManager().getPlayerHistory(this.getTargetUniqueId(), this.getClass());

        if(history.stream().noneMatch(punishment -> punishment.getId().equals(this.getId()))) {
            return;
        }

        history.removeIf(MultiTemporaryPunishment::isCancelled);
        history.sort(null);

        int size = history.size();
        if (size > 0) {
            MultiTemporaryPunishment prev = null, curr = null;
            int index = -1;
            for (MultiTemporaryPunishment punishment : history) {
                if (punishment.getId().equals(this.getId())) {
                    index = history.indexOf(punishment);
                    break;
                }
            }

            if (index == 0) {
                while (index < size - 1) {
                    curr = history.get(index + 1);
                    if (prev == null) {
                        curr.startedDate = history.get(0).getStartedDate();
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

        this.cancelled = true;
        this.cancellationCreator = cancellationCreator;
        this.cancellationDate = cancellationDate;
        this.cancellationReason = cancellationReason;

        this.getPluginManager().deactivatePunishment(this);
        this.updateData();
    }

    @Override
    public synchronized void delete() {
        this.deactivate();
        this.deleteData();
    }

    @Override
    public synchronized void updateData() {
        DataProvider dataProvider = this.getPluginManager().getDataProvider();
        if (dataProvider != null) {
            if (dataProvider.hasPunishment(this.getId())) {
                dataProvider.updatePunishment(this);
            } else {
                dataProvider.createPunishment(this);
            }
        }
    }

    @Override
    public synchronized void deleteData() {
        DataProvider dataProvider = this.getPluginManager().getDataProvider();
        if (dataProvider != null && dataProvider.hasPunishment(this.getId())) {
            dataProvider.deletePunishment(this);
        }
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

    @Override
    public final boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public final long getDuration() {
        return this.duration;
    }

    @Override
    public final long getStartedDate() {
        return this.startedDate;
    }

    @Override
    public final synchronized void setCancellationCreator(@Nullable String cancellationCreator) {
        this.cancellationCreator = cancellationCreator;
        this.updateData();
    }

    @Override
    public final synchronized void setCancellationDate(long cancellationDate) {
        this.cancellationDate = cancellationDate;
        this.updateData();
    }

    @Override
    public final synchronized void setCancellationReason(@Nullable String cancellationReason) {
        this.cancellationReason = cancellationReason;
        this.updateData();
    }

    @Override
    public final synchronized void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
        this.updateData();
    }

    @Override
    public final synchronized void setDuration(long duration) {
        this.duration = duration;
        this.updateData();
    }

    @Override
    public final synchronized void setStartedDate(long startedDate) {
        this.startedDate = startedDate;
        this.updateData();
    }
}