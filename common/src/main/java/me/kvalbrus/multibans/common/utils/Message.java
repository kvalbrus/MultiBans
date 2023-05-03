package me.kvalbrus.multibans.common.utils;

import lombok.Getter;

public enum Message {

    PREFIX(
        "prefix",
        "[MultiBans]",
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

    BAN_ACTIVATE_LISTEN(
        "ban.activate.listen",
        "Player %punishment_target% was permanently banned by %punishment_creator%!",
        "Sends to all players with permission 'multibans.punishment.ban.listen' " +
            "when player is permanently banned"
    ),

    BAN_ACTIVATE_EXECUTOR(
        "ban.activate.executor",
        "You permanently banned player %punishment_target%",
        "Sends to the executor of the ban"
    ),

    BAN_DEACTIVATE_LISTEN("ban.deactivate.listen", "",""),
    BAN_DEACTIVATE_EXECUTOR("ban.deactivate.executor", "", ""),
    BAN_DELETE_LISTEN("ban.delete.listen", "", ""),
    BAN_DELETE_EXECUTOR("ban.delete.executor", "", ""),

    BAN_TRY_LISTEN(
        "ban.try.listen",
        "Player %punishment_target% tried joining, but he was banned",
        "Sends to all player with permission 'multibans.punishment.ban.try' when " +
            "a player tries to join to the server, but he was banned"
    ),

    TEMPBAN_ACTIVATE_LISTEN(
        "tempban.activate.listen",
        "Player %punishment_target% was banned for %punishment_duration% by %punishment_creator%!",
        "Sends to all players with permission 'multibans.punishment.tempban.listen' " +
            "when player is temporarily banned"
    ),

    TEMPBAN_ACTIVATE_EXECUTOR(
        "tempban.activate.executor",
        "You temporarily banned player %punishment_target%",
        "Sends to the executor of the ban"
    ),

    TEMPBAN_DEACTIVATE_LISTEN("tempban.deactivate.listen", "", ""),
    TEMPBAN_DEACTIVATE_EXECUTOR("tempban.deactivate.executor", "", ""),
    TEMPBAN_DELETE_LISTEN("tempban.delete.listen", "", ""),
    TEMPBAN_DELETE_EXECUTOR("tempban.delete.executor", "", ""),

    TEMPBAN_TRY_LISTEN(
        "tempban.try.listen",
        "Player %punishment_target% tried joining, but he was banned",
        "Sends to all player with permission 'multibans.punishment.tempban.try' when " +
            "a player tries to join to the server, but he was banned"),

    TEMPBAN_HAS_PASSED_LISTEN("tempban.has-passed.listen", "", ""),

    BANIP_ACTIVATE_LISTEN(
        "banip.activate.listen",
        "Player %punishment_target% was permanently ip-banned by %punishment_creator%!",
        ""
    ),

    BANIP_ACTIVATE_EXECUTOR(
        "banip.activate.executor",
        "You permanently banned ip player %punishment_target%",
        ""
    ),

    BANIP_DEACTIVATE_LISTEN("banip.deactivate.listen", "", ""),
    BANIP_DEACTIVATE_EXECUTOR("banip.deactivate.executor", "", ""),
    BANIP_DELETE_LISTEN("banip.delete.listen", "", ""),
    BANIP_DELETE_EXECUTOR("banip.delete.executor", "", ""),

    BANIP_TRY_LISTEN(
        "banip.try.listen",
        "Player %punishment_target% tried joining, but he was banned",
        ""
    ),

    TEMPBANIP_ACTIVATE_LISTEN(
        "tempbanip.activate.listen",
        "Player %punishment_target% was ip-banned for %punishment_duration% by %punishment_creator%!",
        ""
    ),

    TEMPBANIP_ACTIVATE_EXECUTOR(
        "tempbanip.activate.executor",
        "You banned ip player %punishment_target%",
        ""
    ),

    TEMPBANIP_DEACTIVATE_LISTEN("tempbanip.deactivate.listen", "", ""),
    TEMPBANIP_DEACTIVATE_EXECUTOR("tempbanip.deactivate.executor", "", ""),
    TEMPBANIP_DELETE_LISTEN("tempbanip.delete.listen", "", ""),
    TEMPBANIP_DELETE_EXECUTOR("tempbanip.delete.executor", "", ""),

    TEMPBANIP_TRY_LISTEN(
        "tempbanip.try.listen",
        "You permanently banned ip player %punishment_target%",
        ""
    ),

    TEMPBANIP_HAS_PASSED_LISTEN("tempbanip.has-passed.listen", "", ""),

    MUTECHAT_ACTIVATE_LISTEN(
        "mutechat.activate.listen",
        "Player %punishment_target% was permanently muted by %punishment_creator%!",
        ""
    ),

    MUTECHAT_ACTIVATE_EXECUTOR(
        "mutechat.activate.executor",
        "You permanently muted player %punishment_target%",
        ""
    ),

    MUTECHAT_ACTIVATE_TARGET(
        "mutechat.activate.target",
        "Player %punishment_target% was muted by %punishment_creator%!",
        ""
    ),

    MUTECHAT_DEACTIVATE_LISTEN("mutechat.deactivate.listen", "", ""),
    MUTECHAT_DEACTIVATE_EXECUTOR("mutechat.deactivate.executor", "", ""),
    MUTECHAT_DEACTIVATE_TARGET("mutechat.deactivate.target", "", ""),

    MUTECHAT_DELETE_LISTEN("mutechat.delete.listen", "", ""),
    MUTECHAT_DELETE_EXECUTOR("mutechat.delete.executor", "", ""),
    MUTECHAT_DELETE_TARGET("mutechat.delete.target", "", ""),

    MUTECHAT_TRY_LISTEN("mutechat.try.listen", "", ""),
    MUTECHAT_TRY_TARGET("mutechat.try.target", "", ""),

    TEMPMUTECHAT_ACTIVATE_LISTEN(
        "tempmutechat.activate.listen",
        "Player %punishment_target% was muted by %punishment_creator%!",
        ""
    ),

    TEMPMUTECHAT_ACTIVATE_EXECUTOR(
        "tempmutechat.activate.executor",
        "You muted player %punishment_target%",
        ""
    ),

    TEMPMUTECHAT_ACTIVATE_TARGET(
        "tempmutechat.activate.player",
        "You was muted by %punishment_creator% for %punishment_duration%!",
        ""
    ),

    TEMPMUTECHAT_TRY_LISTEN("tempmutechat.try.listen", "", ""),
    TEMPMUTECHAT_TRY_TARGET("tempmutechat.try.target", "", ""),

    TEMPMUTECHAT_HAS_PASSED_LISTEN(
        "tempmutechat.has-passed.listen",
        "The tempmute of the %punishment_target% player has been passed",
        ""),
    TEMPMUTECHAT_HAS_PASSED_TARGET("tempmutechat.has-passed.target", "", ""),

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

    public String getMessage() {
        return this.message.replaceAll("&", "§");
    }

    public void setMessage(String message) {
        this.message = message;
    }
}