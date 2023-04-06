package me.kvalbrus.multibans.common.utils;

import me.kvalbrus.multibans.api.OfflinePlayer;

public class ReplacedString {

    private String string;

    public ReplacedString(String string) {
        this.string = string;
    }

    public ReplacedString replacePlayerName(OfflinePlayer player) {
        return this.replace("%player_name%", player.getName());
    }

    public ReplacedString replaceCreatorName(OfflinePlayer player) {
        return this.replace("%creator_name%", player.getName());
    }

    private ReplacedString replace(String key, String string) {
        this.string = this.string.replaceAll(key, string);
        return this;
    }

    public String string() {
        return this.string;
    }
}
