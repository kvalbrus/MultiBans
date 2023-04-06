package me.kvalbrus.multibans.api;

import java.util.UUID;

public interface Player extends CommandSender, OfflinePlayer {

    UUID getUniqueId();

    String getHostAddress();

    int getPort();
}