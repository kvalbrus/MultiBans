package me.kvalbrus.multibans.common.punishment.punishments;

import java.util.UUID;
import me.kvalbrus.multibans.api.punishment.PunishmentType;
import me.kvalbrus.multibans.common.managers.PunishmentManager;
import org.jetbrains.annotations.NotNull;

public class TemporaryChatMute extends TemporaryPunishment {

    public TemporaryChatMute(@NotNull PunishmentManager punishmentManager,
                        @NotNull String id,
                        @NotNull String targetIp,
                        @NotNull String targetName,
                        @NotNull UUID targetUUID,
                        @NotNull String creatorName,
                        long dateCreated,
                        long duration) {
        super(punishmentManager, PunishmentType.TEMP_BAN, id, targetIp, targetName, targetUUID,
            creatorName, dateCreated, duration);
    }
}
