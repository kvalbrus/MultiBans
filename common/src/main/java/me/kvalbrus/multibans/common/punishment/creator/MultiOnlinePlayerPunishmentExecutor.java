package me.kvalbrus.multibans.common.punishment.creator;

import java.util.UUID;
import me.kvalbrus.multibans.api.OnlinePlayer;
import me.kvalbrus.multibans.api.punishment.executor.OnlinePlayerPunishmentExecutor;
import org.jetbrains.annotations.NotNull;

public class MultiOnlinePlayerPunishmentExecutor extends MultiOnlinePunishmentExecutor
    implements OnlinePlayerPunishmentExecutor {

    private final OnlinePlayer player;

    public MultiOnlinePlayerPunishmentExecutor(OnlinePlayer player) {
        super(player);
        this.player = player;
    }

    @NotNull
    @Override
    public String getHostAddress() {
        return this.player.getHostAddress();
    }

    @Override
    public UUID getUniqueId() {
        return this.player.getUniqueId();
    }
}