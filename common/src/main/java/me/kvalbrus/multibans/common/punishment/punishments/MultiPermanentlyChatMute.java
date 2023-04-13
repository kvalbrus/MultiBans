package me.kvalbrus.multibans.common.punishment.punishments;

import java.util.List;
import me.kvalbrus.multibans.api.OnlinePlayer;
import me.kvalbrus.multibans.api.punishment.creator.OnlinePunishmentCreator;
import me.kvalbrus.multibans.api.punishment.target.OnlinePunishmentTarget;
import me.kvalbrus.multibans.api.punishment.creator.PunishmentCreator;
import me.kvalbrus.multibans.api.punishment.target.PunishmentTarget;
import me.kvalbrus.multibans.api.punishment.PunishmentType;
import me.kvalbrus.multibans.api.punishment.punishments.PermanentlyChatMute;
import me.kvalbrus.multibans.common.managers.PluginManager;
import me.kvalbrus.multibans.common.permissions.Permission;
import me.kvalbrus.multibans.common.punishment.MultiPermanentlyPunishment;
import me.kvalbrus.multibans.common.utils.Message;
import me.kvalbrus.multibans.common.utils.ReplacedString;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MultiPermanentlyChatMute extends MultiPermanentlyPunishment implements
    PermanentlyChatMute {

    public MultiPermanentlyChatMute(@NotNull PluginManager pluginManager,
                                    @NotNull String id,
                                    @NotNull PunishmentTarget target,
                                    @NotNull PunishmentCreator creator,
                                    long dateCreated,
                                    @Nullable String reason,
                                    @Nullable String comment,
                                    @Nullable String cancellationCreator,
                                    long cancellationDate,
                                    @Nullable String cancellationReason,
                                    @NotNull List<String> servers,
                                    boolean cancelled) {
        super(pluginManager, PunishmentType.MUTE, id, target, creator, dateCreated, reason, comment,
            cancellationCreator, cancellationDate, cancellationReason, servers, cancelled);
    }

    @Override
    public synchronized void activate() {
        super.activate();

        // Sends a message to all players with permission
        ReplacedString listenMessage = new ReplacedString(Message.MUTECHAT_LISTEN.getMessage())
            .replacePlayerName(this.getTarget())
            .replaceExecutorName(this.getCreator());

        for (OnlinePlayer p : this.getPluginManager().getOnlinePlayers()) {
            if (p.hasPermission(Permission.PUNISHMENT_MUTECHAT_LISTEN.getName())) {
                p.sendMessage(listenMessage.string());
            }
        }

        // Sends a message to target if target is online
        if (this.getTarget() instanceof OnlinePunishmentTarget onlineTarget) {
            ReplacedString targetMessage = new ReplacedString(Message.MUTECHAT_PLAYER.getMessage())
                .replacePlayerName(onlineTarget)
                .replaceExecutorName(this.getCreator());
            onlineTarget.sendMessage(targetMessage.string());
        }

        // Sends a message to the creator
        if (this.getCreator() instanceof OnlinePunishmentCreator creator) {
            ReplacedString creatorMessage = new ReplacedString(Message.MUTECHAT_EXECUTOR.getMessage())
                .replacePlayerName(this.getTarget());
            creator.sendMessage(creatorMessage.string());
        }
    }
}