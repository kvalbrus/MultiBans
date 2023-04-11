package me.kvalbrus.multibans.common.implementations;

import java.util.UUID;
import lombok.NonNull;
import me.kvalbrus.multibans.api.OnlinePlayer;
import org.jetbrains.annotations.NotNull;

public abstract class MultiOnlinePlayer extends MultiPlayer implements OnlinePlayer {

    private String hostAddress;

    public MultiOnlinePlayer(String name, @NotNull UUID uuid, @NotNull @NonNull String hostAddress) {
        super(name, uuid);
        this.hostAddress = hostAddress;
    }

    @NotNull
    @Override
    public String getHostAddress() {
        return this.hostAddress;
    }
}