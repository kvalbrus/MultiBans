package me.kvalbrus.multibans.common.punishment.creator;

import java.util.UUID;
import me.kvalbrus.multibans.api.OnlinePlayer;
import me.kvalbrus.multibans.api.Player;
import me.kvalbrus.multibans.api.punishment.creator.PlayerPunishmentCreator;
import org.jetbrains.annotations.NotNull;

public class MultiPlayerPunishmentCreator extends MultiPunishmentCreator
    implements PlayerPunishmentCreator {

    private Player player;

    public MultiPlayerPunishmentCreator(Player player) {
        super();

        if (player instanceof OnlinePlayer onlinePlayer) {
            new MultiOnlinePlayerPunishmentCreator(onlinePlayer);
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