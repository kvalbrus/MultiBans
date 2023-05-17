package me.kvalbrus.multibans.common.punishment.punishments;

import java.util.List;
import me.kvalbrus.multibans.api.punishment.creator.PunishmentCreator;
import me.kvalbrus.multibans.api.punishment.target.PunishmentTarget;
import me.kvalbrus.multibans.api.punishment.PunishmentType;
import me.kvalbrus.multibans.api.punishment.punishments.PermanentlyChatMute;
import me.kvalbrus.multibans.common.managers.PluginManager;
import me.kvalbrus.multibans.common.permissions.Permission;
import me.kvalbrus.multibans.common.punishment.MultiPermanentlyPunishment;
import me.kvalbrus.multibans.common.utils.Message;
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
                                    @Nullable PunishmentCreator cancellationCreator,
                                    long cancellationDate,
                                    @Nullable String cancellationReason,
                                    @NotNull List<String> servers,
                                    boolean cancelled) {
        super(pluginManager, PunishmentType.MUTE, id, target, creator, dateCreated, reason, comment,
            cancellationCreator, cancellationDate, cancellationReason, servers, cancelled);
    }

    @NotNull
    @Override
    public String getActivateMessageForListener() {
        return Message.MUTECHAT_ACTIVATE_LISTEN.getMessage();
    }

    @NotNull
    @Override
    public String getActivateMessageForExecutor() {
        return Message.MUTECHAT_ACTIVATE_EXECUTOR.getMessage();
    }

    @Nullable
    @Override
    public String getActivateMessageForTarget() {
        return Message.MUTECHAT_ACTIVATE_EXECUTOR.getMessage();
    }

    @NotNull
    @Override
    public String getDeactivateMessageForListener() {
        return Message.MUTECHAT_DEACTIVATE_LISTEN.getMessage();
    }

    @NotNull
    @Override
    public String getDeactivateMessageForExecutor() {
        return Message.MUTECHAT_DEACTIVATE_EXECUTOR.getMessage();
    }

    @Nullable
    @Override
    public String getDeactivateMessageForTarget() {
        return Message.MUTECHAT_DEACTIVATE_TARGET.getMessage();
    }

    @NotNull
    @Override
    public String getDeleteMessageForListener() {
        return Message.MUTECHAT_DELETE_LISTEN.getMessage();
    }

    @NotNull
    @Override
    public String getDeleteMessageForExecutor() {
        return Message.MUTECHAT_DELETE_EXECUTOR.getMessage();
    }

    @Nullable
    @Override
    public String getDeleteMessageForTarget() {
        return Message.MUTECHAT_DELETE_EXECUTOR.getMessage();
    }

    @NotNull
    @Override
    public String getReasonChangeMessageForExecutor() {
        return Message.MUTECHAT_REASON_CREATE_CHANGE_EXECUTOR.getMessage();
    }

    @NotNull
    @Override
    public String getReasonChangeMessageForListener() {
        return Message.MUTECHAT_REASON_CREATE_CHANGE_LISTEN.getMessage();
    }

    @NotNull
    @Override
    public String getCommentChangeMessageForExecutor() {
        return Message.MUTECHAT_COMMENT_CHANGE_EXECUTOR.getMessage();
    }

    @NotNull
    @Override
    public String getCommentChangeMessageForListener() {
        return Message.MUTECHAT_COMMENT_CHANGE_LISTEN.getMessage();
    }

    @NotNull
    @Override
    public Permission getPermissionForListener() {
        return Permission.PUNISHMENT_MUTECHAT_LISTEN;
    }
}