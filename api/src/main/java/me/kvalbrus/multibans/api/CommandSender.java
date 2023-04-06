package me.kvalbrus.multibans.api;

import org.jetbrains.annotations.NotNull;

public interface CommandSender {

    @NotNull
    String getName();

    void sendMessage(String... messages);

    boolean hasPermission(@NotNull String permission);
}
