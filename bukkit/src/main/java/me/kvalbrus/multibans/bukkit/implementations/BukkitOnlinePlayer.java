package me.kvalbrus.multibans.bukkit.implementations;

import me.kvalbrus.multibans.api.OnlinePlayer;
import me.kvalbrus.multibans.bukkit.BukkitPluginManager;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

public class BukkitOnlinePlayer extends BukkitPlayer implements OnlinePlayer {

    private final org.bukkit.entity.Player player;

    public BukkitOnlinePlayer(org.bukkit.entity.Player player) {
        super(player);
        //        super(player.getName(), player.getUniqueId(),
//            player.getAddress() != null ? player.getAddress().getHostString() : "");
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
    public void sendMessage(Component component) {
        BukkitPluginManager.getAudiences().player(this.player).sendMessage(component);
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