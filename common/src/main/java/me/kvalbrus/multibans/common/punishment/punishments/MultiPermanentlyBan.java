package me.kvalbrus.multibans.common.punishment.punishments;

import java.util.List;
import me.kvalbrus.multibans.api.OnlinePlayer;
import me.kvalbrus.multibans.api.punishment.creator.OnlinePunishmentCreator;
import me.kvalbrus.multibans.api.punishment.creator.PunishmentCreator;
import me.kvalbrus.multibans.api.punishment.target.PunishmentTarget;
import me.kvalbrus.multibans.api.punishment.PunishmentType;
import me.kvalbrus.multibans.api.punishment.punishments.PermanentlyBan;
import me.kvalbrus.multibans.common.managers.PluginManager;
import me.kvalbrus.multibans.common.permissions.Permission;
import me.kvalbrus.multibans.common.punishment.MultiPermanentlyPunishment;
import me.kvalbrus.multibans.common.utils.Message;
import me.kvalbrus.multibans.common.utils.ReplacedString;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MultiPermanentlyBan extends MultiPermanentlyPunishment implements PermanentlyBan {

    public MultiPermanentlyBan(@NotNull PluginManager pluginManager,
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
        super(pluginManager, PunishmentType.BAN, id, target, creator, dateCreated, reason, comment,
            cancellationCreator, cancellationDate, cancellationReason, servers, cancelled);
    }

    @Override
    public synchronized void activate() {
        super.activate();

        // Sends a message to all players with permission
        ReplacedString listenMessage = new ReplacedString(Message.BAN_ACTIVATE_LISTEN.getMessage())
            .replacePunishment(this);

        for (OnlinePlayer p : this.getPluginManager().getOnlinePlayers()) {
            if (p.hasPermission(Permission.PUNISHMENT_BAN_LISTEN.getName())) {
                p.sendMessage(listenMessage.string());
            }
        }

        // Sends a message to console
        this.getPluginManager().getConsole().sendMessage(listenMessage.string());

        // Sends a message to the creator
        if (this.getCreator() instanceof OnlinePunishmentCreator creator) {
            ReplacedString creatorMessage = new ReplacedString(Message.BAN_ACTIVATE_EXECUTOR.getMessage())
                .replacePunishment(this);
            creator.sendMessage(creatorMessage.string());
        }
    }

    @Override
    public synchronized void deactivate(@Nullable PunishmentCreator cancellationCreator,
                                        long cancellationDate,
                                        @Nullable String cancellationReason) {
        super.deactivate(cancellationCreator, cancellationDate, cancellationReason);

        // Sends a message to all players with permission
        ReplacedString listenMessage = new ReplacedString(Message.BAN_DEACTIVATE_LISTEN.getMessage())
            .replacePunishment(this);

        for (OnlinePlayer p : this.getPluginManager().getOnlinePlayers()) {
            if (p.hasPermission(Permission.PUNISHMENT_BAN_LISTEN.getName())) {
                p.sendMessage(listenMessage.string());
            }
        }

        // Sends a message to console
        this.getPluginManager().getConsole().sendMessage(listenMessage.string());

        // Sends a message to the creator
        if (this.getCreator() instanceof OnlinePunishmentCreator creator) {
            ReplacedString creatorMessage = new ReplacedString(Message.BANIP_DEACTIVATE_EXECUTOR.getMessage())
                .replacePunishment(this);
            creator.sendMessage(creatorMessage.string());
        }
    }

    @Override
    public synchronized void delete() {
        super.delete();

        // Sends a message to all players with permission
        ReplacedString listenMessage = new ReplacedString(Message.BAN_DELETE_LISTEN.getMessage())
            .replacePunishment(this);

        for (OnlinePlayer p : this.getPluginManager().getOnlinePlayers()) {
            if (p.hasPermission(Permission.PUNISHMENT_BAN_LISTEN.getName())) {
                p.sendMessage(listenMessage.string());
            }
        }

        // Sends a message to console
        this.getPluginManager().getConsole().sendMessage(listenMessage.string());

        // Sends a message to the creator
        if (this.getCreator() instanceof OnlinePunishmentCreator creator) {
            ReplacedString creatorMessage = new ReplacedString(Message.BANIP_DELETE_EXECUTOR.getMessage())
                .replacePunishment(this);
            creator.sendMessage(creatorMessage.string());
        }
    }
}