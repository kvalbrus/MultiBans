package me.kvalbrus.multibans.common.utils;

import me.kvalbrus.multibans.api.Player;
import me.kvalbrus.multibans.api.punishment.PermanentlyPunishment;
import me.kvalbrus.multibans.api.punishment.Punishment;
import me.kvalbrus.multibans.api.punishment.TemporaryPunishment;
import me.kvalbrus.multibans.api.punishment.creator.PunishmentCreator;

public class ReplacedString {

    private String string;

    public ReplacedString(String string) {
        this.string = string;
    }

    public ReplacedString replacePlayerName(Player player) {
        return this.replace("%player_name%", player.getName());
    }

    public ReplacedString replaceExecutorName(PunishmentCreator creator) {
        return this.replace("%executor_name%", creator.getName());
    }

    public ReplacedString replacePunishment(Punishment punishment) {
        this.replace("%punishment_id%", punishment.getId());
        this.replace("%punishment_type%", punishment.getType().getPrefix());
        this.replace("%punishment_creator%", punishment.getCreator().getName());
        this.replace("%punishment_reason%", punishment.getCreatedReason());
        this.replace("%punishment_target%", punishment.getTarget().getName());
        this.replace("%punishment_date_created%", StringUtil.getStringDate(punishment.getCreatedDate(), Message.DATE_FORMAT.getMessage()));

        if (punishment instanceof TemporaryPunishment temporary) {
            this.replace("%punishment_date_started%", StringUtil.getStringDate(temporary.getStartedDate(), Message.DATE_FORMAT.getMessage()));
            this.replace("%punishment_duration%", StringUtil.getStringDate(temporary.getDuration(), StringUtil.getDuration(temporary.getDuration())));
            this.replace("%punishment_duration_left%", StringUtil.getDuration(Math.min(temporary.getDuration(),
                temporary.getStartedDate() + temporary.getDuration() - System.currentTimeMillis())));
            this.replace("%punishment_date_end%", StringUtil.getStringDate(temporary.getStartedDate() + temporary.getDuration(), Message.DATE_FORMAT.getMessage()));
        } else if (punishment instanceof PermanentlyPunishment) {
            this.replace("%punishment_duration%", Message.PERMANENTLY.getMessage());
        }

        return this;
    }

    private ReplacedString replace(String key, String string) {
        this.string = this.string.replaceAll(key, string);
        return this;
    }

    public String string() {
        return this.string;
    }
}