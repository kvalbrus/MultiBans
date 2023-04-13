package me.kvalbrus.multibans.common.utils;

import java.util.regex.Pattern;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

public enum Message {

    PREFIX(
        "prefix",
        "&7[&eMulti&6Bans&7]",
        "Plugin prefix"
    ),

    DATE_FORMAT(
        "date.format",
        "HH:MM mm/dd/yyyy",
        "Date format"
    ),

    PERMANENTLY(
        "punishment.duration.permanently",
        "Permanently",
        ""
    ),

    BAN_LISTEN(
        "ban.listen",
        "Player %player_name% was permanently banned by %executor_name%!",
        "Sends to all players with permission 'multibans.punishment.ban.listen' " +
            "when player is permanently banned"
    ),

    BAN_EXECUTOR(
        "ban.executor",
        "You permanently banned player %player_name%",
        "Sends to the executor of the ban"
    ),

    BAN_TRY(
        "ban.try",
        "Player %player_name% tried joining, but he was banned",
        "Sends to all player with permission 'multibans.punishment.ban.try' when " +
            "a player tries to join to the server, but he was banned"
    ),

    TEMPBAN_LISTEN(
        "tempban.listen",
        "Player %player_name% was banned for %dutarion% by %executor_name%!",
        "Sends to all players with permission 'multibans.punishment.tempban.listen' " +
            "when player is temporarily banned"
    ),

    TEMPBAN_EXECUTOR(
        "tempban.executor",
        "You temporarily banned player %player_name%",
        "Sends to the executor of the ban"
    ),

    TEMPBAN_TRY(
        "ban.try",
        "Player %player_name% tried joining, but he was banned",
        "Sends to all player with permission 'multibans.punishment.tempban.try' when " +
            "a player tries to join to the server, but he was banned"),

    BANIP_LISTEN(
        "banip.listen",
        "Player %player_name% was permanently ip-banned by %executor_name%!",
        ""
    ),

    BANIP_EXECUTOR(
        "banip.executor",
        "You permanently banned ip player %player_name%",
        ""
    ),

    BANIP_TRY(
        "banip.try",
        "Player %player_name% tried joining, but he was banned",
        ""
    ),

    TEMPBANIP_LISTEN(
        "tempbanip.listen",
        "Player %player_name% was ip-banned for %dutarion% by %executor_name%!",
        ""
    ),

    TEMPBANIP_EXECUTOR(
        "tempbanip.executor",
        "You banned ip player %player_name%",
        ""
    ),

    TEMPBANIP_TRY(
        "tempbanip.try",
        "You permanently banned ip player %player_name%",
        ""
    ),

    MUTECHAT_LISTEN(
        "mutechat.listen",
        "Player %player_name% was permanently muted by %executor_name%!",
        ""
    ),

    MUTECHAT_EXECUTOR(
        "mutechat.executor",
        "You permanently muted player %player_name%",
        ""
    ),

    MUTECHAT_PLAYER(
        "mutechat.player",
        "Player %player_name% was muted by %executor_name%!",
        ""
    ),

    MUTECHAT_TRY(
        "mutechat.try",
        "a",
        ""
    ),

    TEMPMUTECHAT_LISTEN(
        "tempmutechat.listen",
        "Player %player_name% was muted by %executor_name%!",
        ""
    ),

    TEMPMUTECHAT_EXECUTOR(
        "tempmutechat.executor",
        "You muted player %player_name%",
        ""
    ),

    TEMPMUTECHAT_PLAYER(
        "tempmutechat.player",
        "You was muted by %executor_name% for %duration%!",
        ""
    ),

    TEMPMUTECHAT_TRY(
        "tempmutechat.try",
        "a",
        ""
    ),

    KICK_LISTEN(
        "kick.listen",
        "a",
        ""
    ),

    KICK_EXECUTOR(
        "kick.executor",
        "a",
        ""
    ),

    UNBAN_LISTEN(
        "unban.listen",
        "a",
        ""
    ),

    UNBAN_EXECUTOR(
        "unban.executor",
        "a",
        ""
    ),

    UNBANIP_LISTEN(
        "unbanip.listen",
        "a",
        ""
    ),

    UNBANIP_EXECUTOR(
        "unbanip.executor",
        "a",
        ""
    ),

    NOT_PERMISSION_BAN_EXECUTE(
        "error.permission.ban.execute",
        "You don't have permission",
        ""
    ),

    NOT_PERMISSION_TEMPBAN_EXECUTE(
        "error.permission.tempban.execute",
        "You don't have permission",
        ""
    ),

    NOT_PERMISSION_BANIP_EXECUTE(
        "error.permission.banip.execute",
        "You don't have permission",
        ""
    ),

    NOT_PERMISSION_TEMPBANIP_EXECUTE(
        "error.permission.tempbanip.execute",
        "You don't have permission",
        ""
    ),

    NOT_PERMISSION_MUTECHAT_EXECUTE(
        "error.permission.mutechat.execute",
        "You don't have permission",
        ""
    ),

    NOT_PERMISSION_TEMPMUTECHAT_EXECUTE(
        "error.permission.tempmutechat.execute",
        "You don't have permission",
        ""
    ),

    NOT_PERMISSION_UNBAN_EXECUTE(
        "error.permission.unban.execute",
        "You don't have permission",
        ""
    ),

    NOT_PERMISSION_UNMUTECHAT_EXECUTE(
        "error.permission.unmutechat.execute",
        "You don't have permission",
        ""
    ),

    NOT_ENOUGH_ARGUMENTS(
        "error.command.arguments",
        "Not enough arguments in the command",
        "Sends to the sender if the command contains not enough arguments"
    ),

    NOT_FOUND_PLAYER(
        "error.command.player",
        "Player not found",
        "Sends to the sender if player isn't found"
    ),

    TITLE_HEADER(
        "title.header",
        "<red>You are banned from this server!</red>\n\n",
        ""
    ),

    TITLE_BANIP("title.banip",
        """
            Permanently ban
            
            <gray>Banned on:</gray> %punishment_date_created%
            <gray>Banned by:</gray> %punishment_creator%
            <gray>Reason:</gray> %punishment_reason%
            
            <gray>BAN ID:</gray> #%punishment_id%
            
            
            """,
        ""),

    TITLE_TEMPBANIP("title.tempbanip",
        """
            Temporary ban-ip
            
            <gray>Banned on:</gray> %punishment_date_created%
            <gray>Banned by:</gray> %punishment_creator%
            <gray>Reason:</gray> %punishment_reason%
            
            <gray>Banned started:</gray> %punishment_date_started%
            <gray>Duration:</gray> %punishment_duration%
            <gray>Expires in:</gray> %punishment_duration_left%
            
            <gray>BAN ID:</gray> #%punishment_id%
            
            
            """,
        ""),

    TITLE_BAN("title.ban",
        """
            Permanently ban
            
            <gray>Banned on:</gray> %punishment_date_created%
            <gray>Banned by:</gray> %punishment_creator%
            <gray>Reason:</gray> %punishment_reason%
            
            <gray>BAN ID:</gray> #%punishment_id%
            
            
            """,
        ""),

    TITLE_TEMPBAN(
        "title.tempban",
        """
            Temporary ban
            
            <gray>Banned on:</gray> %punishment_date_created%
            <gray>Banned by:</gray> %punishment_creator%
            <gray>Reason:</gray> %punishment_reason%
            
            <gray>Banned started:</gray> %punishment_date_started%
            <gray>Duration:</gray> %punishment_duration%
            <gray>Expires in:</gray> %punishment_duration_left%
            
            <gray>BAN ID:</gray> #%punishment_id%
            
            
            """,
        ""
    ),

    TITLE_FOOTER(
        "title.footer",
        "<gray>Our site: www.site.com</gray>",
        ""
    );

    @Getter
    private final String key;

    private String message;

    @Getter
    private final String description;

    Message(final String key, final String message, final String description) {
        this.key = key;
        this.message = message;
        this.description = description;
    }

    private static final Pattern LEGACY_FORMATS_PATTERN = Pattern.compile("§[\\da-fk-or]");
    private static final Pattern LEGACY_HEX_COLOR_PATTERN = Pattern.compile("§x(§[\\da-fA-F]){6}");

    public String getMessage() {
        String message = this.message;
        message = LEGACY_FORMATS_PATTERN.matcher(LEGACY_HEX_COLOR_PATTERN.matcher(message)
            .replaceAll("")).replaceAll("").replace("§", "");

        return LegacyComponentSerializer.legacySection().serialize(MiniMessage.miniMessage().deserialize(message));
    }

    public void setMessage(String message) {
        this.message = message;
    }
}