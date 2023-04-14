package me.kvalbrus.multibans.common.punishment;

import java.util.List;
import java.util.UUID;
import me.kvalbrus.multibans.api.DataProvider;
import me.kvalbrus.multibans.api.OnlinePlayer;
import me.kvalbrus.multibans.api.punishment.Punishment;
import me.kvalbrus.multibans.api.punishment.creator.PunishmentCreator;
import me.kvalbrus.multibans.api.punishment.target.PunishmentTarget;
import me.kvalbrus.multibans.api.punishment.PunishmentType;
import me.kvalbrus.multibans.api.punishment.TemporaryPunishment;
import me.kvalbrus.multibans.common.managers.PluginManager;
import me.kvalbrus.multibans.common.punishment.punishments.MultiPermanentlyBan;
import me.kvalbrus.multibans.common.punishment.punishments.MultiPermanentlyBanIp;
import me.kvalbrus.multibans.common.punishment.punishments.MultiPermanentlyChatMute;
import me.kvalbrus.multibans.common.punishment.punishments.MultiTemporaryBan;
import me.kvalbrus.multibans.common.punishment.punishments.MultiTemporaryBanIp;
import me.kvalbrus.multibans.common.punishment.punishments.MultiTemporaryChatMute;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class MultiPunishment implements Punishment {

    private final PluginManager pluginManager;

    private final PunishmentType type;

    private final String id;

    private final PunishmentTarget target;

    private final PunishmentCreator creator;

    private final long createdDate;

    private String reason;

    private String comment;

    private List<String> servers;

    public MultiPunishment(@NotNull PluginManager pluginManager,
                           @NotNull final PunishmentType type,
                           @NotNull final String id,
                           @NotNull final PunishmentTarget target,
                           @NotNull final PunishmentCreator creator,
                           final long createdDate,
                           @Nullable String reason,
                           @Nullable String comment,
                           @NotNull List<String> servers) {
        this.pluginManager = pluginManager;
        this.type = type;
        this.id = id;

        this.target = target;
        this.target.setPunishment(this);

        this.creator = creator;
        this.creator.setPunishment(this);

        this.createdDate = createdDate;
        this.reason = reason;
        this.comment = comment;
        this.servers = servers;
    }

    @NotNull
    public static <T extends Punishment> T constructPunishment(
        @NotNull PluginManager pluginManager,
        @NotNull PunishmentType type,
        @NotNull String id,
        @NotNull PunishmentTarget target,
        @NotNull PunishmentCreator creator,
        long createdDate,
        long startedDate,
        long duration,
        @Nullable String createdReason,
        @Nullable String comment,
        @Nullable PunishmentCreator cancellationCreator,
        long cancellationDate,
        @Nullable String cancellationReason,
        @NotNull List<String> servers,
        boolean cancelled) {
        Punishment punishment;
        switch (type) {
            case BAN:
                punishment = new MultiPermanentlyBan(pluginManager, id, target, creator, createdDate,
                    createdReason, comment, cancellationCreator, cancellationDate,
                    cancellationReason, servers, cancelled);
                break;

            case TEMP_BAN:
                punishment = new MultiTemporaryBan(pluginManager, id, target, creator, createdDate,
                    startedDate, duration, createdReason, comment,
                    cancellationCreator, cancellationDate, cancellationReason, servers, cancelled);
                break;

            case BAN_IP:
                punishment = new MultiPermanentlyBanIp(pluginManager, id, target, creator,
                    createdDate, createdReason, comment, cancellationCreator, cancellationDate,
                    cancellationReason, servers, cancelled);
                break;

            case TEMP_BAN_IP:
                punishment = new MultiTemporaryBanIp(pluginManager, id, target, creator, createdDate,
                    startedDate, duration, createdReason, comment, cancellationCreator,
                    cancellationDate, cancellationReason, servers, cancelled);
                break;

            case MUTE:
                punishment = new MultiPermanentlyChatMute(pluginManager, id, target, creator,
                    createdDate, createdReason, comment, cancellationCreator, cancellationDate,
                    cancellationReason, servers, cancelled);
                break;

            case TEMP_MUTE:
                punishment = new MultiTemporaryChatMute(pluginManager, id, target, creator,
                    createdDate, startedDate, duration, createdReason, comment, cancellationCreator,
                    cancellationDate, cancellationReason, servers, cancelled);
                break;

            default:
                throw new IllegalArgumentException("Punishment type hasn't its constructor");
        }

        return (T) punishment;
    }

    @Override
    public synchronized void activate() {
        this.pluginManager.activatePunishment(this);
        this.updateData();
    }

    @Override
    public synchronized void delete() {
        this.pluginManager.getDataProvider().deletePunishment(this);
        this.deleteData();
    }

    @NotNull
    public final PluginManager getPluginManager() {
        return this.pluginManager;
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
    @Deprecated
    public final String getTargetIp() {
        return this.target instanceof OnlinePlayer onlinePlayer ? onlinePlayer.getHostAddress() : "";
    }

    @NotNull
    @Override
    @Deprecated
    public final String getTargetName() {
        return this.target.getName();
    }

    @NotNull
    @Override
    @Deprecated
    public final UUID getTargetUniqueId() {
        return this.target.getUniqueId();
    }

    @NotNull
    @Override
    @Deprecated
    public final String getCreatorName() {
        return this.creator.getName();
    }

    @Override
    public final PunishmentTarget getTarget() {
        return this.target;
    }

    @Override
    public final PunishmentCreator getCreator() {
        return this.creator;
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

    public synchronized void deleteData() {
        DataProvider dataProvider = this.getPluginManager().getDataProvider();
        if (dataProvider != null && dataProvider.hasPunishment(this.getId())) {
            dataProvider.deletePunishment(this);
        }
    }
}