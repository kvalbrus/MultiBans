package me.kvalbrus.multibans.common.utils;

import me.kvalbrus.multibans.api.Player;
import me.kvalbrus.multibans.api.punishment.creator.PunishmentCreator;

public class ReplacedString {

    private String string;

    public ReplacedString(String string) {
        this.string = string;
    }

    public ReplacedString replacePlayerName(Player player) {
        return this.replace("%player_name%", player.getName());
    }

    public ReplacedString replaceCreatorName(PunishmentCreator creator) {
        return this.replace("%creator_name%", creator.getName());
    }

    private ReplacedString replace(String key, String string) {
        this.string = this.string.replaceAll(key, string);
        return this;
    }

    public String string() {
        return this.string;
    }
}
