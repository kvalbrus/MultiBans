package me.kvalbrus.multibans.common.api;

import java.util.List;
import java.util.UUID;
import me.kvalbrus.multibans.common.punishment.Punishment;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import me.kvalbrus.multibans.api.punishment.PunishmentStatus;
import me.kvalbrus.multibans.api.punishment.PunishmentType;

public class PunishmentAPI implements me.kvalbrus.multibans.api.punishment.Punishment {

    private Punishment punishment;

    public PunishmentAPI(Punishment punishment) {
        this.punishment = punishment;
    }

    @Override
    public synchronized void activate() {
        this.punishment.activate();
    }

    @Override
    public synchronized void deactivate() {
        this.punishment.deactivate();
    }

    @Override
    public synchronized void deactivate(String creator, String reason) {
        this.punishment.deactivate(creator, reason);
    }

    @Override
    public synchronized void delete() {
        this.punishment.delete();
    }

    @NotNull
    @Override
    public PunishmentStatus getStatus() {
        return this.punishment.getStatus();
    }

    @NotNull
    @Override
    public PunishmentType getType() {
        return this.punishment.getType();
    }

    @NotNull
    @Override
    public String getId() {
        return this.punishment.getId();
    }

    @Nullable
    @Override
    public String getTargetIp() {
        return this.punishment.getTargetIp();
    }

    @NotNull
    @Override
    public String getTargetName() {
        return this.punishment.getTargetName();
    }

    @NotNull
    @Override
    public UUID getTargetUniqueId() {
        return this.punishment.getTargetUniqueId();
    }

    @NotNull
    @Override
    public String getCreatorName() {
        return this.punishment.getCreatorName();
    }

    @Override
    public long getDateCreated() {
        return this.punishment.getDateCreated();
    }

    @Override
    public long getDateStart() {
        return this.punishment.getDateStart();
    }

    @Override
    public long getDuration() {
        return this.punishment.getDuration();
    }

    @NotNull
    @Override
    public String getReason() {
        return this.punishment.getReason();
    }

    @Nullable
    @Override
    public String getComment() {
        return this.punishment.getComment();
    }

    @Override
    public long getCancellationDate() {
        return this.punishment.getCancellationDate();
    }

    @Nullable
    @Override
    public String getCancellationCreator() {
        return this.punishment.getCancellationCreator();
    }

    @Nullable
    @Override
    public String getCancellationReason() {
        return this.punishment.getCancellationReason();
    }

    @NotNull
    @Override
    public List<String> getServers() {
        return this.punishment.getServers();
    }

    @Override
    public boolean isCancelled() {
        return this.punishment.isCancelled();
    }

    @Override
    public synchronized void setDateStart(long dateStart) {
        this.punishment.setDateStart(dateStart);
    }

    @Override
    public synchronized void setDuration(long duration) {
        this.punishment.setDuration(duration);
    }

    @Override
    public synchronized void setReason(String reason) {
        this.punishment.setReason(reason);
    }

    @Override
    public synchronized void setComment(String comment) {
        this.punishment.setComment(comment);
    }

    @Override
    public synchronized void setCancellationDate(long date) {
        this.punishment.setCancellationDate(date);
    }

    @Override
    public synchronized void setCancellationCreator(String creator) {
        this.punishment.setCancellationCreator(creator);
    }

    @Override
    public synchronized void setCancellationReason(String reason) {
        this.punishment.setCancellationReason(reason);
    }

    @Override
    public synchronized void setServers(@NotNull List<String> servers) {
        this.punishment.setServers(servers);
    }

    @Override
    public synchronized void setCancelled(boolean cancelled) {
        this.punishment.setCancelled(cancelled);
    }
}
