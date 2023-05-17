package me.kvalbrus.multibans.common.punishment;

import java.util.List;
import me.kvalbrus.multibans.api.punishment.PermanentlyPunishment;
import me.kvalbrus.multibans.api.punishment.creator.PunishmentCreator;
import me.kvalbrus.multibans.api.punishment.PunishmentStatus;
import me.kvalbrus.multibans.api.punishment.target.PunishmentTarget;
import me.kvalbrus.multibans.api.punishment.PunishmentType;
import me.kvalbrus.multibans.common.managers.PluginManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class MultiPermanentlyPunishment extends MultiPunishment
    implements PermanentlyPunishment {

    private PunishmentCreator cancellationCreator;

    private long cancellationDate;

    private String cancellationReason;

    private boolean cancelled;

    public MultiPermanentlyPunishment(@NotNull PluginManager pluginManager,
                                      @NotNull PunishmentType type,
                                      @NotNull String id,
                                      @NotNull PunishmentTarget target,
                                      @NotNull PunishmentCreator creator,
                                      long dateCreated,
                                      @Nullable String reason,
                                      @Nullable String comment,
                                      @Nullable PunishmentCreator cancellationCreator,
                                      long cancellationDate,
                                      @Nullable String cancellationReason,
                                      @NotNull List<String> servers,
                                      boolean cancelled) {
        super(pluginManager, type, id, target, creator, dateCreated, reason, comment, servers);
        this.cancellationCreator = cancellationCreator;
        this.cancellationDate = cancellationDate;
        this.cancellationReason = cancellationReason;
        this.cancelled = cancelled;
    }

    @Override
    public final synchronized void activate() {
        this.cancelled = false;
        super.activate();
    }

    @Override
    public final synchronized void deactivate(@Nullable PunishmentCreator cancellationCreator,
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
        this.sendMessageAboutDeactivate();
    }

    @Override
    public final synchronized void delete() {
        super.delete();
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
    public PunishmentCreator getCancellationCreator() {
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
    public synchronized void setCancellationCreator(@Nullable PunishmentCreator cancellationCreator) {
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

    @NotNull
    public abstract String getDeactivateMessageForListener();

    @NotNull
    public abstract String getDeactivateMessageForExecutor();

    @Nullable
    public abstract String getDeactivateMessageForTarget();

    private void sendMessageAboutDeactivate() {
        this.sendMessageToListeners(this.getDeactivateMessageForListener());
        this.sendMessageToCreator(this.getDeactivateMessageForExecutor());
        this.sendMessageToTarget(this.getDeactivateMessageForTarget());

    }
}