package me.kvalbrus.multibans.common.punishment.punishments;

import java.util.List;
import me.kvalbrus.multibans.api.punishment.creator.PunishmentCreator;
import me.kvalbrus.multibans.api.punishment.target.PunishmentTarget;
import me.kvalbrus.multibans.api.punishment.PunishmentType;
import me.kvalbrus.multibans.api.punishment.punishments.PermanentlyBan;
import me.kvalbrus.multibans.common.managers.PluginManager;
import me.kvalbrus.multibans.common.permissions.Permission;
import me.kvalbrus.multibans.common.punishment.MultiPermanentlyPunishment;
import me.kvalbrus.multibans.common.utils.Message;
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

    @NotNull
    @Override
    public String getActivateMessageForListener() {
        return Message.BAN_ACTIVATE_LISTEN.getMessage();
    }

    @NotNull
    @Override
    public String getActivateMessageForExecutor() {
        return Message.BAN_ACTIVATE_EXECUTOR.getMessage();
    }

    @Nullable
    @Override
    public String getActivateMessageForTarget() {
        return null;
    }

    @NotNull
    @Override
    public String getDeactivateMessageForListener() {
        return Message.BAN_DEACTIVATE_LISTEN.getMessage();
    }

    @NotNull
    @Override
    public String getDeactivateMessageForExecutor() {
        return Message.BAN_DEACTIVATE_EXECUTOR.getMessage();
    }

    @Nullable
    @Override
    public String getDeactivateMessageForTarget() {
        return null;
    }

    @NotNull
    @Override
    public String getDeleteMessageForListener() {
        return Message.BAN_DELETE_LISTEN.getMessage();
    }

    @NotNull
    @Override
    public String getDeleteMessageForExecutor() {
        return Message.BAN_DELETE_EXECUTOR.getMessage();
    }

    @Nullable
    @Override
    public String getDeleteMessageForTarget() {
        return null;
    }

    @NotNull
    @Override
    public String getReasonChangeMessageForExecutor() {
        return Message.BAN_REASON_CREATE_CHANGE_EXECUTOR.getMessage();
    }

    @NotNull
    @Override
    public String getReasonChangeMessageForListener() {
        return Message.BAN_REASON_CREATE_CHANGE_LISTEN.getMessage();
    }

    @NotNull
    @Override
    public String getCommentChangeMessageForExecutor() {
        return Message.BAN_COMMENT_CHANGE_EXECUTOR.getMessage();
    }

    @NotNull
    @Override
    public String getCommentChangeMessageForListener() {
        return Message.BAN_COMMENT_CHANGE_LISTEN.getMessage();
    }

    @NotNull
    @Override
    public Permission getPermissionForListener() {
        return Permission.PUNISHMENT_BAN_LISTEN;
    }

}