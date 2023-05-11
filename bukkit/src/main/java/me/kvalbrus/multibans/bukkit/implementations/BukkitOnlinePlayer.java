package me.kvalbrus.multibans.bukkit.implementations;

import me.kvalbrus.multibans.api.OnlinePlayer;
import me.kvalbrus.multibans.bukkit.BukkitPluginManager;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.jetbrains.annotations.NotNull;

public class BukkitOnlinePlayer extends BukkitPlayer implements OnlinePlayer {

    private final org.bukkit.entity.Player player;

    public BukkitOnlinePlayer(org.bukkit.entity.Player player) {
        super(player);
        this.player = player;
    }

    @Override
    public void sendMessage(String message) {
        if (message != null && message.length() > 0) {
            BukkitPluginManager.getAudiences().sender(this.player).sendMessage(MiniMessage.miniMessage()
                .deserialize(message));
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

    @NotNull
    @Override
    public String getHostAddress() {
        return this.player.getAddress() != null ? this.player.getAddress().getHostString() : "";
    }
}