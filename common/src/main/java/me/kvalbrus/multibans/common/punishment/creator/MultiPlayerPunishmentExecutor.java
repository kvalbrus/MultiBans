package me.kvalbrus.multibans.common.punishment.creator;

import java.util.UUID;
import me.kvalbrus.multibans.api.OnlinePlayer;
import me.kvalbrus.multibans.api.Player;
import me.kvalbrus.multibans.api.punishment.executor.PlayerPunishmentExecutor;
import org.jetbrains.annotations.NotNull;

public class MultiPlayerPunishmentExecutor extends MultiPunishmentExecutor
    implements PlayerPunishmentExecutor {

    private final Player player;

    public MultiPlayerPunishmentExecutor(Player player) {
        super();

        if (player instanceof OnlinePlayer onlinePlayer) {
            new MultiOnlinePlayerPunishmentExecutor(onlinePlayer);
        }

        this.player = player;
    }

    @NotNull
    @Override
    public String getName() {
        return this.player.getName();
    }

    @Override
    public UUID getUniqueId() {
        return this.player.getUniqueId();
    }
}