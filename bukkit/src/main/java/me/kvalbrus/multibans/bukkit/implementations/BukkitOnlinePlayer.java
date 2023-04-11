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
        this.player.sendMessage(message);
    }

    @Override
    public void sendMessage(String... messages) {
        this.player.sendMessage(messages);
    }

    @Override
    public boolean hasPermission(String permission) {
        return this.player.hasPermission(permission);
    }
}