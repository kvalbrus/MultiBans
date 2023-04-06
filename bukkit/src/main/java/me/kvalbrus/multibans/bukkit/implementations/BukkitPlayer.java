package me.kvalbrus.multibans.bukkit.implementations;

import java.util.Objects;
import java.util.UUID;
import me.kvalbrus.multibans.api.Player;

public class BukkitPlayer extends BukkitCommandSender implements Player {

    private final org.bukkit.entity.Player player;

    public BukkitPlayer(org.bukkit.entity.Player player) {
        super(player);
        this.player = player;
    }

    @Override
    public UUID getUniqueId() {
        return this.player.getUniqueId();
    }

    @Override
    public String getHostAddress() {
        return Objects.requireNonNull(this.player.getAddress()).getHostString();
    }

    @Override
    public int getPort() {
        return Objects.requireNonNull(this.player.getAddress()).getPort();
    }
}
