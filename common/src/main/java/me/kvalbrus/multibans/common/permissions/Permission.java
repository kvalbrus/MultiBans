package me.kvalbrus.multibans.common.permissions;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;

public enum Permission {

    PUNISHMENT_BAN_EXECUTE("multibans.punishment.ban.execute"),
    PUNISHMENT_BAN_LISTEN("multibans.punishment.ban.listen"),

    PUNISHMENT_TEMPBAN_EXECUTE("multibans.punishment.tempban.execute"),
    PUNISHMENT_TEMPBAN_LISTEN("multibans.punishment.tempban.listen"),

    PUNISHMENT_BANIP_EXECUTE("multibans.punishment.banip.execute"),
    PUNISHMENT_BANIP_LISTEN("multibans.punishment.banip.listen"),

    PUNISHMENT_TEMPBANIP_EXECUTE("multibans.punishmnet.tempbanip.execute"),
    PUNISHMENT_TEMPBANIP_LISTEN("multibans.punishment.tempbanip.listen"),

    PUNISHMENT_MUTECHAT_EXECUTE("multibans.punishment.mutechat.execute"),
    PUNISHMENT_MUTECHAT_LISTEN("multibans.punishment.mutechat.listen"),

    PUNISHMENT_TEMPMUTECHAT_EXECUTE("multibans.punishment.tempmutechat.execute"),
    PUNISHMENT_TEMPMUTECHAT_LISTEN("multibans.punishment.tempmutechat.listen"),

    PUNISHMENT_KICK_EXECUTOR("multibans.punishment.kick.executor"),
    PUNISHMENT_KICK_LISTEN("multibans.punishment.kick.listen"),

    PUNISHMENT_UNBAN_EXECUTE("multibans.punishment.unban.execute"),
    PUNISHMENT_UNBAN_LISTEN("multibans.punishment.unban.listen"),

    PUNISHMENT_UNMUTECHAT_EXECUTE("multibans.punishment.unmutechat.execute"),
    PUNISHMENT_UNMUTECHAT_LISTEN("multibans.punishment.unmutechat.listen"),

    COMMAND_RELOAD("multibans.command.reload");

    @Getter
    private final String name;

    Permission(@NotNull String name) {
        this.name = name;
    }
}