package me.kvalbrus.multibans.common.punishment;

import java.util.List;
import java.util.UUID;
import me.kvalbrus.multibans.api.punishment.PermanentlyPunishment;
import me.kvalbrus.multibans.api.punishment.PunishmentStatus;
import me.kvalbrus.multibans.api.punishment.PunishmentType;
import me.kvalbrus.multibans.common.managers.PluginManager;
import me.kvalbrus.multibans.api.DataProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class MultiPermanentlyPunishment
    extends MultiPunishment implements PermanentlyPunishment {

    private String cancellationCreator;

    private long cancellationDate;

    private String cancellationReason;

    private boolean cancelled;

    public MultiPermanentlyPunishment(@NotNull PluginManager pluginManager,
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
        super(pluginManager, type, id, targetIp, targetName, targetUUID, creatorName,
            dateCreated, reason, comment, servers);
        this.cancellationCreator = cancellationCreator;
        this.cancellationDate = cancellationDate;
        this.cancellationReason = cancellationReason;
        this.cancelled = cancelled;
    }

    @Override
    public synchronized void activate() {
        this.cancelled = false;
        super.activate();
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

        this.cancelled = true;
        this.cancellationCreator = cancellationCreator;
        this.cancellationDate = cancellationDate;
        this.cancellationReason = cancellationReason;

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
    public synchronized void setCancellationCreator(@Nullable String cancellationCreator) {
        this.cancellationCreator = cancellationCreator;
        this.updateData();
    }

    @Override
    public synchronized void setCancellationDate(long cancellationDate) {
        this.cancellationDate = cancellationDate;
        this.updateData();
    }

    @Override
    public synchronized void setCancellationReason(@NotNull String cancellationReason) {
        this.cancellationReason = cancellationReason;
        this.updateData();
    }

    @Override
    public synchronized void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
        this.updateData();
    }
}