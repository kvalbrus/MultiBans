package me.kvalbrus.multibans.bukkit.implementations;

import me.kvalbrus.multibans.common.implementations.MultiPlayer;

public class BukkitPlayer extends MultiPlayer {

    private final org.bukkit.OfflinePlayer offlinePlayer;

    public BukkitPlayer(org.bukkit.OfflinePlayer offlinePlayer) {
        super(offlinePlayer.getName() != null ? offlinePlayer.getName() : "", offlinePlayer.getUniqueId());
        this.offlinePlayer = offlinePlayer;
    }
}