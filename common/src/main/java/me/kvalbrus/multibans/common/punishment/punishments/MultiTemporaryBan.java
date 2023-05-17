package me.kvalbrus.multibans.common.punishment.punishments;

import java.util.List;
import me.kvalbrus.multibans.api.punishment.creator.PunishmentCreator;
import me.kvalbrus.multibans.api.punishment.target.PunishmentTarget;
import me.kvalbrus.multibans.api.punishment.PunishmentType;
import me.kvalbrus.multibans.api.punishment.punishments.TemporaryBan;
import me.kvalbrus.multibans.common.managers.PluginManager;
import me.kvalbrus.multibans.common.permissions.Permission;
import me.kvalbrus.multibans.common.punishment.MultiTemporaryPunishment;
import me.kvalbrus.multibans.common.utils.Message;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MultiTemporaryBan extends MultiTemporaryPunishment implements TemporaryBan {

    public MultiTemporaryBan(@NotNull PluginManager pluginManager,
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
                             @NotNull List<String> servers,
                             boolean cancelled) {
        super(pluginManager, PunishmentType.TEMP_BAN, id, target, creator, createdDate, startedDate,
            duration, reason, comment, cancellationCreator, cancellationDate, cancellationReason,
            servers, cancelled);
    }

    @NotNull
    @Override
    public String getActivateMessageForListener() {
        return Message.TEMPBAN_ACTIVATE_LISTEN.getMessage();
    }

    @NotNull
    @Override
    public String getActivateMessageForExecutor() {
        return Message.TEMPBAN_ACTIVATE_EXECUTOR.getMessage();
    }

    @Nullable
    @Override
    public String getActivateMessageForTarget() {
        return null;
    }

    @NotNull
    @Override
    public String getDeactivateMessageForListener() {
        return Message.TEMPBAN_DEACTIVATE_LISTEN.getMessage();
    }

    @NotNull
    @Override
    public String getDeactivateMessageForExecutor() {
        return Message.TEMPBAN_DEACTIVATE_EXECUTOR.getMessage();
    }

    @Nullable
    @Override
    public String getDeactivateMessageForTarget() {
        return null;
    }

    @NotNull
    @Override
    public String getDeleteMessageForListener() {
        return Message.TEMPBAN_DELETE_LISTEN.getMessage();
    }

    @NotNull
    @Override
    public String getDeleteMessageForExecutor() {
        return Message.TEMPBAN_DELETE_EXECUTOR.getMessage();
    }

    @Nullable
    @Override
    public String getDeleteMessageForTarget() {
        return null;
    }

    @NotNull
    @Override
    public  String getReasonChangeMessageForExecutor() {
        return Message.TEMPBAN_REASON_CREATE_CHANGE_EXECUTOR.getMessage();
    }

    @NotNull
    @Override
    public String getReasonChangeMessageForListener() {
        return Message.TEMPBAN_REASON_CREATE_CHANGE_LISTEN.getMessage();
    }

    @NotNull
    @Override
    public String getCommentChangeMessageForExecutor() {
        return Message.TEMPBAN_REASON_CREATE_CHANGE_EXECUTOR.getMessage();
    }

    @NotNull
    @Override
    public String getCommentChangeMessageForListener() {
        return Message.TEMPBAN_COMMENT_CHANGE_EXECUTOR.getMessage();
    }

    @NotNull
    @Override
    public Permission getPermissionForListener() {
        return Permission.PUNISHMENT_TEMPBAN_LISTEN;
    }
}