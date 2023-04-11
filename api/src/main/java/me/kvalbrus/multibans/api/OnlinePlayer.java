package me.kvalbrus.multibans.api;

import org.jetbrains.annotations.NotNull;

public interface OnlinePlayer extends Player, CommandSender {

    @NotNull
    String getHostAddress();
}