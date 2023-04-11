package me.kvalbrus.multibans.common.permissions;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;

public enum Permission {

//    public static final Permission PUNISHMENT_BAN = new Permission("multibans.punishment.ban");
//
//    public static final Permission PUNISHMENT_TEMPBAN = new Permission("multibans.punishment.tempban");
//
//    public static final Permission PUNISHMENT_BANIP = new Permission("multibans.punishment.banip");
//
//    public static final Permission PUNISHMENT_TEMPBANIP = new Permission("multibans.punishment.tempbanip");
//
//    public static final Permission PUNISHMENT_CHATMUTE = new Permission("multibans.punishment.chatmute");
//
//    public static final Permission PUNISHMENT_TEMPCHATMUTE = new Permission("multibans.punishment.tempchatmute");

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
    PUNISHMENT_TEMPMUTECHAT_LISTEN("multibans.punishment.tempmutechat.listen");

    @Getter
    private final String name;

    Permission(@NotNull String name) {
        this.name = name;
    }
}