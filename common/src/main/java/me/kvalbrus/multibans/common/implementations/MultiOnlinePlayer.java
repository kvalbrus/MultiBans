package me.kvalbrus.multibans.common.implementations;

import java.util.UUID;
import me.kvalbrus.multibans.api.OnlinePlayer;
import org.jetbrains.annotations.NotNull;

public abstract class MultiOnlinePlayer extends MultiPlayer implements OnlinePlayer {

    private final String hostAddress;

    public MultiOnlinePlayer(String name, @NotNull UUID uuid, @NotNull String hostAddress) {
        super(name, uuid);
        this.hostAddress = hostAddress;
    }

    @NotNull
    @Override
    public String getHostAddress() {
        return this.hostAddress;
    }
}