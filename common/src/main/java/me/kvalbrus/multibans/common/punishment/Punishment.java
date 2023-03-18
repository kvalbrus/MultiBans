package me.kvalbrus.multibans.common.punishment;

import java.util.List;
import java.util.UUID;
import me.kvalbrus.multibans.common.managers.PunishmentManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import me.kvalbrus.multibans.api.punishment.PunishmentStatus;
import me.kvalbrus.multibans.api.punishment.PunishmentType;
import me.kvalbrus.multibans.common.storage.DataProvider;

public class Punishment implements Comparable<Punishment> {

    private final PunishmentManager punishmentManager;

    private final PunishmentType type;

    private final String id;

    private final String targetIp;

    private final String targetName;

    private final UUID targetUUID;

    private final String creatorName;

    private final long dateCreated;

    private long dateStart;

    private long duration;

    private String reason;

    private String comment;

    private String cancellationCreator;

    private long cancellationDate;

    private String cancellationReason;

    private List<String> servers;

    private boolean cancelled;

    public Punishment(@NotNull final PunishmentManager punishmentManager,
                      @NotNull final PunishmentType type,
                      @NotNull final String id,
                      @NotNull final String targetIp,
                      @NotNull final String targetName,
                      @NotNull final UUID targetUUID,
                      @NotNull final String creatorName,
                      final long dateCreated,
                      long dateStart,
                      long duration,
                      @NotNull String reason,
                      @Nullable String comment,
                      @NotNull List<String> servers,
                      boolean cancelled) {
        this.punishmentManager = punishmentManager;
        this.type = type;
        this.id = id;
        this.targetIp = targetIp;
        this.targetName = targetName;
        this.creatorName = creatorName;
        this.targetUUID = targetUUID;
        this.dateCreated = dateCreated;
        this.dateStart = dateStart;
        this.duration = duration;
        this.reason = reason;
        this.comment = comment;
        this.servers = servers;
        this.cancelled = cancelled;
    }

    public Punishment(@NotNull final PunishmentManager punishmentManager,
                      @NotNull final PunishmentType type,
                      @NotNull final String id,
                      @NotNull final String targetIp,
                      @NotNull final String targetName,
                      @NotNull final UUID targetUUID,
                      @NotNull final String creatorName,
                      final long dateCreated,
                      long dateStart,
                      long duration,
                      @NotNull String reason,
                      @Nullable String comment,
                      @Nullable String cancellationCreator,
                      long cancellationDate,
                      @Nullable String cancellationReason,
                      @NotNull List<String> servers,
                      boolean cancelled) {
        this(punishmentManager, type, id, targetIp, targetName, targetUUID, creatorName, dateCreated, dateStart, duration,
            reason, comment, servers, cancelled);

        this.cancellationCreator = cancellationCreator;
        this.cancellationDate = cancellationDate;
        this.cancellationReason = cancellationReason;
    }

    public synchronized void activate() {
        // Start date calculation
        // P_1,..., P_n - active punishments;
        // P_1,..., P_k - active punishments, but P_i.duration > 0 (i=1,...,k)
        // P_s(1),..., P_s(k) - sorted active punishments.
        //   (P_s(1).dateStart <= ... <= P_s(k).dateStart. If P_s(i).dateStart == P_s(j).dateStart,
        //   then sort(P_s(i).id, P_s(j).id))
        //
        // Supposing, P_s(1).dateStart, ... , P_s(k).dateStart are correctly
        //   (P_s(i).dateStart = P_s(i-1).dateStart + P_s(i-1).duration (i = s(2),...,s(k)),
        // then
        // this.dateStart = this.dateCreate, if n == 0 || this.duration <= 0 (permanently).
        // this.dateStart = P_s(k).dateStart + P_s(k).duration, else.

        List<Punishment> activePunishments = this.punishmentManager.getActivePunishments(this.targetName);
        activePunishments.removeIf(p -> p.getDuration() <= 0 || p.getType() != this.type);

        if (this.duration > 0) {
            if(activePunishments.size() > 0) {
                activePunishments.sort(null);

                Punishment lastPunishment = activePunishments.get(activePunishments.size() - 1);
                this.dateStart = lastPunishment.getDateStart() + lastPunishment.getDuration();
            } else {
                this.dateStart = this.dateCreated;
            }
        }

        this.cancelled = false;
        this.updateData();
    }

    public synchronized void deactivate() {
        this.deactivate(null, null);
    }

    public synchronized void deactivate(String cancellationCreator, String cancellationReason) {
        if(this.cancelled) {
            return;
        }

        List<Punishment> history = this.punishmentManager.getPlayerHistory(this.targetName);
        history.sort(null);

        if (history.stream().noneMatch(p -> p.id.equals(this.id))) {
            return;
        }

        history.removeIf(p -> p.duration <= 0 || p.type != this.type || p.cancelled);
        int size = history.size();
        if (this.duration > 0 && size > 0) {
            Punishment prev = null, curr = null;
            int index = -1;
            for (Punishment punishment : history) {
                if (punishment.getId().equals(this.id)) {
                    index = history.indexOf(punishment);
                    break;
                }
            }

            if (index != -1) {
                if (index == 0) {
                    while (index < size - 1) {
                        curr = history.get(index + 1);
                        if (prev == null) {
                            curr.dateStart = curr.dateCreated;
                        } else {
                            curr.dateStart = prev.dateStart + prev.duration;
                        }

                        prev = curr;
                        index++;

                        curr.updateData();
                    }
                } else if (index != size - 1) {
                    prev = history.get(index - 1);
                    while (index < size - 1) {
                        curr = history.get(index + 1);
                        curr.dateStart = prev.dateStart + prev.duration;

                        prev = curr;
                        index++;

                        curr.updateData();
                    }
                }
            }
        }

        this.cancellationCreator = cancellationCreator;
        this.cancellationDate = System.currentTimeMillis();
        this.cancellationReason = cancellationReason;
        this.cancelled = true;

        this.updateData();
    }
    
    public synchronized void delete() {
        this.deleteData();
    }

    @NotNull
    public PunishmentStatus getStatus() {
        return this.cancelled ? PunishmentStatus.CANCELLED :
            (this.dateStart + this.duration > System.currentTimeMillis() ?
                PunishmentStatus.ACTIVE : PunishmentStatus.PASSED);
    }

    @NotNull
    public final PunishmentType getType() {
        return this.type;
    }

    @NotNull
    public final String getId() {
        return this.id;
    }

    @Nullable
    public String getTargetIp() {
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
    public String getCreatorName() {
        return this.creatorName;
    }

    public final long getDateCreated() {
        return this.dateCreated;
    }

    public long getDateStart() {
        return this.dateStart;
    }

    public long getDuration() {
        return this.duration;
    }

    @NotNull
    public String getReason() {
        return this.reason;
    }

    @Nullable
    public String getComment() {
        return this.comment;
    }

    public long getCancellationDate() {
        return this.cancellationDate;
    }

    @Nullable
    public String getCancellationCreator() {
        return this.cancellationCreator;
    }

    @Nullable
    public String getCancellationReason() {
        return this.cancellationReason;
    }

    @NotNull
    public List<String> getServers() {
        return this.servers;
    }

    public boolean isCancelled() {
        return this.cancelled;
    }
    
    public synchronized void setDateStart(long dateStart) {
        this.dateStart = dateStart;
        this.updateData();
    }

    public synchronized void setDuration(long duration) {
        this.duration = duration;
        this.updateData();
    }

    public synchronized void setReason(String reason) {
        this.reason = reason;
        this.updateData();
    }
    
    public synchronized void setComment(String comment) {
        this.comment = comment;
        this.updateData();
    }

    public synchronized void setCancellationDate(long date) {
        this.cancellationDate = date;
        this.updateData();
    }
    
    public synchronized void setCancellationCreator(String creator) {
        this.cancellationCreator = creator;
        this.updateData();
    }

    public synchronized void setCancellationReason(String reason) {
        this.cancellationReason = reason;
        this.updateData();
    }

    public synchronized void setServers(@NotNull List<String> servers) {
        this.servers = servers;
        this.updateData();
    }

    public synchronized void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
        this.updateData();
    }

    public int compareTo(@NotNull Punishment o) {
        if (this.type == o.type) {
            if (this.dateStart == o.dateStart) {
                return this.id.compareTo(o.id);
            } else if (this.dateStart < o.dateStart) {
                return -1;
            } else {
                return 1;
            }
        } else {
            return this.type.compareTo(o.type);
        }
    }

    private synchronized void updateData() {
        DataProvider dataProvider = this.punishmentManager.getPluginManager().getDataProvider();
        if (dataProvider != null) {
            if (dataProvider.hasPunishment(this.id)) {
                dataProvider.updatePunishment(this);
            } else {
                dataProvider.createPunishment(this);
            }
        }
    }

    private synchronized void deleteData() {
        DataProvider dataProvider = this.punishmentManager.getPluginManager().getDataProvider();
        if (dataProvider != null && dataProvider.hasPunishment(this.id)) {
            dataProvider.deletePunishment(this);
        }
    }
}