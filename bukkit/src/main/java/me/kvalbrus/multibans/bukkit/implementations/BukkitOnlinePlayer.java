package me.kvalbrus.multibans.bukkit.implementations;

import me.kvalbrus.multibans.common.implementations.MultiOnlinePlayer;

public class BukkitOnlinePlayer extends MultiOnlinePlayer {

    private final org.bukkit.entity.Player player;

    public BukkitOnlinePlayer(org.bukkit.entity.Player player) {
        super(player.getName(), player.getUniqueId(),
            player.getAddress() != null ? player.getAddress().getHostString() : "");
        this.player = player;
    }

    @Override
    public void sendMessage(String message) {
        if (message != null && message.length() > 0) {
            this.player.sendMessage(message);
        }
    }

    @Override
    public void sendMessage(String... messages) {
        for (String message : messages) {
            this.sendMessage(message);
        }
    }

    @Override
    public boolean hasPermission(String permission) {
        return this.player.hasPermission(permission);
    }
}