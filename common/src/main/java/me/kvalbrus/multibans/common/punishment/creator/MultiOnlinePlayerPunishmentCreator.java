package me.kvalbrus.multibans.common.punishment.creator;

import java.util.UUID;
import me.kvalbrus.multibans.api.OnlinePlayer;
import me.kvalbrus.multibans.api.punishment.creator.OnlinePlayerPunishmentCreator;
import org.jetbrains.annotations.NotNull;

public class MultiOnlinePlayerPunishmentCreator extends MultiOnlinePunishmentCreator
    implements OnlinePlayerPunishmentCreator {

    private OnlinePlayer player;

    public MultiOnlinePlayerPunishmentCreator(OnlinePlayer player) {
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
