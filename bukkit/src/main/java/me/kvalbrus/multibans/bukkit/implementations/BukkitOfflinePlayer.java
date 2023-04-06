package me.kvalbrus.multibans.bukkit.implementations;

import me.kvalbrus.multibans.api.OfflinePlayer;

public class BukkitOfflinePlayer implements OfflinePlayer {

    private final org.bukkit.OfflinePlayer offlinePlayer;

    public BukkitOfflinePlayer(org.bukkit.OfflinePlayer offlinePlayer) {
        this.offlinePlayer = offlinePlayer;
    }

    @Override
    public String getName() {
        return this.offlinePlayer.getName();
    }
}
