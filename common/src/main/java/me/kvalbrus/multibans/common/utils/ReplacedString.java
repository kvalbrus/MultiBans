package me.kvalbrus.multibans.common.utils;

import me.kvalbrus.multibans.api.Player;
import me.kvalbrus.multibans.api.punishment.PermanentlyPunishment;
import me.kvalbrus.multibans.api.punishment.Punishment;
import me.kvalbrus.multibans.api.punishment.TemporaryPunishment;
import me.kvalbrus.multibans.api.punishment.executor.PunishmentExecutor;

public class ReplacedString {

    private String string;

    public ReplacedString(String string) {
        this.string = string;
    }

    public ReplacedString replacePlayerName(Player player) {
        return this.replace("%player_name%", player.getName());
    }

    public ReplacedString replaceExecutorName(PunishmentExecutor creator) {
        return this.replace("%executor_name%", creator.getName());
    }

    public ReplacedString replacePunishment(Punishment punishment) {
        this.replacePunishmentId(punishment)
            .replacePunishmentType(punishment)
            .replacePunishmentTargetName(punishment)
            .replacePunishmentCreatorName(punishment)
            .replacePunishmentReason(punishment)
            .replacePunishmentCreatedDate(punishment);

        if (punishment instanceof TemporaryPunishment temporary) {
            this.replacePunishmentStartedDate(temporary)
                .replacePunishmentDuration(temporary)
                .replacePunishmentEndDate(temporary);
            this.replace("%punishment_duration_left%", StringUtil.getDuration(Math.min(
                temporary.getDuration(),
                temporary.getStartedDate() + temporary.getDuration() - System.currentTimeMillis())));
        } else if (punishment instanceof PermanentlyPunishment) {
            this.replace("%punishment_duration%", Message.PERMANENTLY.getMessage());
        }

        return this;
    }

    public ReplacedString replacePunishmentId(Punishment punishment) {
        return this.replace("%punishment_id%", punishment.getId());
    }

    public ReplacedString replacePunishmentType(Punishment punishment) {
        return this.replace("%punishment_type%", punishment.getType().getPrefix());
    }

    public ReplacedString replacePunishmentCreatorName(Punishment punishment) {
        return this.replace("%punishment_creator%", punishment.getCreator().getName());
    }

    public ReplacedString replacePunishmentReason(Punishment punishment) {
        return this.replace("%punishment_reason%", punishment.getCreatedReason());
    }

    public ReplacedString replacePunishmentTargetName(Punishment punishment) {
        return this.replace("%punishment_target%", punishment.getTarget().getName());
    }

    public ReplacedString replacePunishmentCreatedDate(Punishment punishment) {
        return this.replace("%punishment_date_created%",
            StringUtil.getStringDate(punishment.getCreatedDate(), Message.DATE_FORMAT.getMessage()));
    }

    public ReplacedString replacePunishmentStartedDate(TemporaryPunishment punishment) {
        return this.replace("%punishment_date_started%",
            StringUtil.getStringDate(punishment.getStartedDate(), Message.DATE_FORMAT.getMessage()));
    }

    public ReplacedString replacePunishmentEndDate(TemporaryPunishment punishment) {
        return this.replace("%punishment_date_end%",
            StringUtil.getStringDate(punishment.getStartedDate() + punishment.getDuration(), Message.DATE_FORMAT.getMessage()));
    }

    public ReplacedString replacePunishmentDuration(TemporaryPunishment punishment) {
        return this.replace("%punishment_duration%", StringUtil.getDuration(punishment.getDuration()));
    }

    private ReplacedString replace(String key, String string) {
        this.string = this.string.replaceAll(key, string);
        return this;
    }

    public String string() {
        return this.string;
    }
}