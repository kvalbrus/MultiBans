package me.kvalbrus.multibans.bukkit.implementations;

import java.util.UUID;
import me.kvalbrus.multibans.api.Player;
import org.jetbrains.annotations.NotNull;

public class BukkitPlayer implements Player {

    private final org.bukkit.OfflinePlayer offlinePlayer;

    public BukkitPlayer(org.bukkit.OfflinePlayer offlinePlayer) {
//        super(offlinePlayer.getName() != null ? offlinePlayer.getName() : "", offlinePlayer.getUniqueId());
        this.offlinePlayer = offlinePlayer;
    }

    @NotNull
    @Override
    public String getName() {
        return this.offlinePlayer.getName() != null ? this.offlinePlayer.getName() : "";
    }

    @Override
    public UUID getUniqueId() {
        return this.offlinePlayer.getUniqueId();
    }
}