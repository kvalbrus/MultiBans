package me.kvalbrus.multibans.common.punishment.target;

import me.kvalbrus.multibans.api.OnlinePlayer;
import me.kvalbrus.multibans.api.punishment.target.OnlinePunishmentTarget;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

public class MultiOnlinePunishmentTarget extends MultiPunishmentTarget
    implements OnlinePunishmentTarget {

    private final OnlinePlayer onlinePlayer;

    public MultiOnlinePunishmentTarget(@NotNull OnlinePlayer onlinePlayer) {
        super(onlinePlayer);
        this.onlinePlayer = onlinePlayer;
    }

    @Override
    public void sendMessage(String message) {
        this.onlinePlayer.sendMessage(message);
    }

    @Override
    public void sendMessage(String... messages) {
        this.onlinePlayer.sendMessage(messages);
    }

    @Override
    public void sendMessage(Component component) {
        this.onlinePlayer.sendMessage(component);
    }

    @Override
    public boolean hasPermission(String permission) {
        return this.onlinePlayer.hasPermission(permission);
    }

    @Override
    public @NotNull String getHostAddress() {
        return this.onlinePlayer.getHostAddress();
    }
}