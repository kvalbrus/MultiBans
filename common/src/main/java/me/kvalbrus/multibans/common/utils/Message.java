package me.kvalbrus.multibans.common.utils;

import lombok.Getter;

public enum Message {

    PREFIX(
        "prefix",
        "&7[&eMulti&6Bans&7]",
        "Plugin prefix"
    ),

    BAN_LISTEN(
        "ban.listen",
        "Player %player_name% was permanently banned by %creator_name%!",
        "Sends to all players with permission 'multibans.punishment.ban.listen' " +
            "when player is permanently banned"
    ),

    BAN_CREATOR(
        "ban.creator",
        "You permanently banned player %player_name%",
        "Sends to the creator of the ban"
    ),

    BAN_TRY(
        "ban.try",
        "Player %player_name% tried joining, but he was banned",
        "Sends to all player with permission 'multibans.punishment.ban.try' when " +
            "a player tries to join to the server, but he was banned"
    ),

    TEMPBAN_LISTEN(
        "tempban.listen",
        "Player %player_name% was banned for %dutarion% by %creator_name%!",
        "Sends to all players with permission 'multibans.punishment.tempban.listen' " +
            "when player is temporarily banned"
    ),

    TEMPBAN_CREATOR(
        "tempban.creator",
        "You temporarily banned player %player_name%",
        "Sends to the creator of the ban"
    ),

    TEMPBAN_TRY(
        "ban.try",
        "Player %player_name% tried joining, but he was banned",
        "Sends to all player with permission 'multibans.punishment.tempban.try' when " +
            "a player tries to join to the server, but he was banned"),

    BANIP_LISTEN(
        "banip.listen",
        "Player %player_name% was permanently ip-banned by %creator_name%!",
        ""
    ),

    BANIP_CREATOR(
        "banip.creator",
        "You ip banned player %player_name%",
        ""
    ),

    BANIP_TRY(
        "banip.try",
        "Player %player_name% tried joining, but he was banned",
        ""
    ),

    TEMPBANIP_LISTEN(
        "tempbanip.listen",
        "Player %player_name% was ip-banned for %dutarion% by %creator_name%!",
        ""
    ),

    TEMPBANIP_CREATOR(
        "tempbanip.creator",
        "",
        ""
    ),

    TEMPBANIP_TRY(
        "tempbanip.try",
        "",
        ""
    ),

    MUTECHAT_LISTEN(
        "mutechat.listen",
        "Player %player_name% was permanently muted by %creator_name%!",
        ""
    ),

    MUTECHAT_CREATOR(
        "mutechat.creator",
        "",
        ""
    ),

    MUTECHAT_PLAYER(
        "mutechat.player",
        "You was muted by %creator_name%!",
        ""
    ),

    MUTECHAT_TRY(
        "mutechat.try",
        "",
        ""
    ),

    TEMPMUTECHAT_LISTEN(
        "tempmutechat.listen",
        "",
        ""
    ),

    TEMPMUTECHAT_CREATOR(
        "tempmutechat.creator",
        "",
        ""
    ),

    TEMPMUTECHAT_PLAYER(
        "tempmutechat.player",
        "You was muted by %creator_name% for %duration%!",
        ""
    ),

    TEMPMUTECHAT_TRY(
        "tempmutechat.try",
        "",
        ""
    ),

    KICK_LISTEN(
        "kick.listen",
        "",
        ""
    ),

    KICK_CREATOR(
        "kick.creator",
        "",
        ""
    ),

    UNBAN_LISTEN(
        "unban.listen",
        "",
        ""
    ),

    UNBAN_CREATOR(
        "unban.creator",
        "",
        ""
    ),

    UNBANIP_LISTEN(
        "unbanip.listen",
        "",
        ""
    ),

    UNBANIP_CREATOR(
        "unbanip.creator",
        "",
        ""
    ),

    NOT_PERMISSION_BAN_EXECUTE(
        "error.permission.ban.execute",
        "",
        ""
    ),

    NOT_PERMISSION_TEMPBAN_EXECUTE(
        "error.permission.tempban.execute",
        "",
        ""
    ),

    NOT_PERMISSION_BANIP_EXECUTE(
        "error.permission.banip.execute",
        "",
        ""
    ),

    NOT_PERMISSION_TEMPBANIP_EXECUTE(
        "error.permission.tempbanip.execute",
        "",
        ""
    ),

    NOT_PERMISSION_MUTECHAT_EXECUTE(
        "error.permission.mutechat.execute",
        "",
        ""
    ),

    NOT_PERMISSION_TEMPMUTECHAT_EXECUTE(
        "error.permission.tempmutechat.execute",
        "",
        ""
    );

    @Getter
    private final String key;

    public String message;

    @Getter
    private final String description;

    Message(final String key, final String message, final String description) {
        this.key = key;
        this.message = message;
        this.description = description;
    }
}