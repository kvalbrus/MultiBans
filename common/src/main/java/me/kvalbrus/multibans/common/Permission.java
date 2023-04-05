package me.kvalbrus.multibans.common;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;

public class Permission {

    public static final Permission PUNISHMENT_BAN = new Permission("multibans.punishment.ban");

    public static final Permission PUNISHMENT_TEMPBAN = new Permission("multibans.punishment.tempban");

    public static final Permission PUNISHMENT_BANIP = new Permission("multibans.punishment.banip");

    public static final Permission PUNISHMENT_TEMPBANIP = new Permission("multibans.punishment.tempbanip");

    public static final Permission PUNISHMENT_CHATMUTE = new Permission("multibans.punishment.chatmute");

    public static final Permission PUNISHMENT_TEMPCHATMUTE = new Permission("multibans.punishment.tempchatmute");

    @Getter
    private final String name;

    public Permission(@NotNull String name) {
        this.name = name;
    }
}