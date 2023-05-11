package me.kvalbrus.multibans.common.punishment.punishments;

import java.util.List;
import me.kvalbrus.multibans.api.OnlinePlayer;
import me.kvalbrus.multibans.api.punishment.creator.OnlinePunishmentCreator;
import me.kvalbrus.multibans.api.punishment.target.OnlinePunishmentTarget;
import me.kvalbrus.multibans.api.punishment.creator.PunishmentCreator;
import me.kvalbrus.multibans.api.punishment.target.PunishmentTarget;
import me.kvalbrus.multibans.api.punishment.PunishmentType;
import me.kvalbrus.multibans.api.punishment.punishments.TemporaryChatMute;
import me.kvalbrus.multibans.common.managers.MultiBansPluginManager;
import me.kvalbrus.multibans.common.managers.PluginManager;
import me.kvalbrus.multibans.common.permissions.Permission;
import me.kvalbrus.multibans.common.punishment.MultiTemporaryPunishment;
import me.kvalbrus.multibans.common.utils.Message;
import me.kvalbrus.multibans.common.utils.ReplacedString;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MultiTemporaryChatMute extends MultiTemporaryPunishment implements TemporaryChatMute {

    public MultiTemporaryChatMute(@NotNull PluginManager pluginManager,
                                  @NotNull String id,
                                  @NotNull PunishmentTarget target,
                                  @NotNull PunishmentCreator creator,
                                  long createdDate,
                                  long startedDate,
                                  long duration,
                                  @Nullable String reason,
                                  @Nullable String comment,
                                  @Nullable PunishmentCreator cancellationCreator,
                                  long cancellationDate,
                                  @Nullable String cancellationReason,
                                  @NotNull List<String> servers, boolean cancelled) {
        super(pluginManager, PunishmentType.TEMP_MUTE, id, target, creator, createdDate, startedDate,
            duration, reason, comment, cancellationCreator, cancellationDate, cancellationReason,
            servers, cancelled);
    }

    @Override
    public synchronized void activate() {
        super.activate();

        // Sends a message to all players with permission
        ReplacedString listenMessage = new ReplacedString(Message.TEMPMUTECHAT_ACTIVATE_LISTEN.getMessage())
            .replacePunishment(this);

        for (OnlinePlayer p : this.getPluginManager().getOnlinePlayers()) {
            if (p.hasPermission(Permission.PUNISHMENT_TEMPMUTECHAT_LISTEN.getName())) {
                p.sendMessage(listenMessage.string());
            }
        }

        // Sends a message to console
        if (this.getPluginManager() instanceof MultiBansPluginManager multiBansPluginManager) {
            if (multiBansPluginManager.getSettings().isConsoleLog()) {
                this.getPluginManager().getConsole().sendMessage(listenMessage.string());
            }
        }

        // Sends a message to target if target is online
        if (this.getTarget() instanceof OnlinePunishmentTarget onlineTarget) {
            ReplacedString targetMessage = new ReplacedString(Message.TEMPMUTECHAT_ACTIVATE_TARGET.getMessage())
                .replacePunishment(this);
            onlineTarget.sendMessage(targetMessage.string());
        }

        // Sends a message to the creator
        if (this.getCreator() instanceof OnlinePunishmentCreator creator) {
            ReplacedString creatorMessage = new ReplacedString(Message.TEMPMUTECHAT_ACTIVATE_EXECUTOR.getMessage())
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
        ReplacedString listenMessage = new ReplacedString(Message.TEMPMUTECHAT_DEACTIVATE_LISTEN.getMessage())
            .replacePunishment(this);

        for (OnlinePlayer p : this.getPluginManager().getOnlinePlayers()) {
            if (p.hasPermission(Permission.PUNISHMENT_TEMPMUTECHAT_LISTEN.getName())) {
                p.sendMessage(listenMessage.string());
            }
        }

        // Sends a message to console
        if (this.getPluginManager() instanceof MultiBansPluginManager multiBansPluginManager) {
            if (multiBansPluginManager.getSettings().isConsoleLog()) {
                this.getPluginManager().getConsole().sendMessage(listenMessage.string());
            }
        }

        // Sends a message to target if target is online
        if (this.getTarget() instanceof OnlinePunishmentTarget onlineTarget) {
            ReplacedString targetMessage = new ReplacedString(Message.TEMPMUTECHAT_DEACTIVATE_TARGET.getMessage())
                .replacePunishment(this);
            onlineTarget.sendMessage(targetMessage.string());
        }

        // Sends a message to the creator
        if (this.getCreator() instanceof OnlinePunishmentCreator creator) {
            ReplacedString creatorMessage = new ReplacedString(Message.TEMPMUTECHAT_DEACTIVATE_EXECUTOR.getMessage())
                .replacePunishment(this);
            creator.sendMessage(creatorMessage.string());
        }
    }

    @Override
    public synchronized void delete() {
        super.delete();

        // Sends a message to all players with permission
        ReplacedString listenMessage = new ReplacedString(Message.TEMPMUTECHAT_DELETE_LISTEN.getMessage())
            .replacePunishment(this);

        for (OnlinePlayer p : this.getPluginManager().getOnlinePlayers()) {
            if (p.hasPermission(Permission.PUNISHMENT_TEMPMUTECHAT_LISTEN.getName())) {
                p.sendMessage(listenMessage.string());
            }
        }

        // Sends a message to console
        if (this.getPluginManager() instanceof MultiBansPluginManager multiBansPluginManager) {
            if (multiBansPluginManager.getSettings().isConsoleLog()) {
                this.getPluginManager().getConsole().sendMessage(listenMessage.string());
            }
        }

        // Sends a message to target if target is online
        if (this.getTarget() instanceof OnlinePunishmentTarget onlineTarget) {
            ReplacedString targetMessage = new ReplacedString(Message.TEMPMUTECHAT_DELETE_TARGET.getMessage())
                .replacePunishment(this);
            onlineTarget.sendMessage(targetMessage.string());
        }

        // Sends a message to the creator
        if (this.getCreator() instanceof OnlinePunishmentCreator creator) {
            ReplacedString creatorMessage = new ReplacedString(Message.TEMPMUTECHAT_DELETE_EXECUTOR.getMessage())
                .replacePunishment(this);
            creator.sendMessage(creatorMessage.string());
        }
    }
}