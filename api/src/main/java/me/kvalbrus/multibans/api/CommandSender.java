package me.kvalbrus.multibans.api;

import java.util.UUID;
import org.jetbrains.annotations.NotNull;

public interface CommandSender {

    UUID getUniqueId();

    @NotNull
    String getName();

    void sendMessage(String... messages);

    boolean hasPermission(@NotNull String permission);
}
