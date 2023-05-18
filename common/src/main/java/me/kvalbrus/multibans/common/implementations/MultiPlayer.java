package me.kvalbrus.multibans.common.implementations;

import java.util.UUID;
import me.kvalbrus.multibans.api.Player;
import org.jetbrains.annotations.NotNull;

public abstract class MultiPlayer implements Player {

    private final String name;

    private final UUID uuid;

    public MultiPlayer(@NotNull String name, @NotNull final UUID uuid) {
        this.name = name;
        this.uuid = uuid;
    }

    @NotNull
    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public UUID getUniqueId() {
        return this.uuid;
    }
}