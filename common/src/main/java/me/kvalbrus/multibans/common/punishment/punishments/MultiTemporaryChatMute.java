package me.kvalbrus.multibans.common.punishment.punishments;

import java.util.List;
import java.util.UUID;
import me.kvalbrus.multibans.api.punishment.PunishmentType;
import me.kvalbrus.multibans.api.punishment.punishments.TemporaryChatMute;
import me.kvalbrus.multibans.common.managers.PunishmentManager;
import me.kvalbrus.multibans.common.punishment.MultiTemporaryPunishment;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MultiTemporaryChatMute extends MultiTemporaryPunishment implements TemporaryChatMute {

    public MultiTemporaryChatMute(@NotNull PunishmentManager punishmentManager,
                                  @NotNull String id,
                                  @NotNull String targetIp,
                                  @NotNull String targetName,
                                  @NotNull UUID targetUUID,
                                  @NotNull String creatorName,
                                  long createdDate,
                                  long startedDate,
                                  long duration,
                                  @Nullable String reason,
                                  @Nullable String comment,
                                  @Nullable String cancellationCreator,
                                  long cancellationDate,
                                  @Nullable String cancellationReason,
                                  @NotNull List<String> servers, boolean cancelled) {
        super(punishmentManager, PunishmentType.TEMP_MUTE, id, targetIp, targetName, targetUUID,
            creatorName, createdDate, startedDate, duration, reason, comment, cancellationCreator,
            cancellationDate, cancellationReason, servers, cancelled);
    }
}