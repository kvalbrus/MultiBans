package me.kvalbrus.multibans.api;

public interface Player extends CommandSender {

    String getHostAddress();

    int getPort();
}