package me.kvalbrus.multibans.common.punishment.target;

import java.util.UUID;
import me.kvalbrus.multibans.api.OnlinePlayer;
import me.kvalbrus.multibans.api.Player;
import me.kvalbrus.multibans.api.punishment.Punishment;
import me.kvalbrus.multibans.api.punishment.target.PunishmentTarget;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MultiPunishmentTarget implements PunishmentTarget {

    private final Player player;

    private Punishment punishment;

    public MultiPunishmentTarget(@NotNull final Player player) {
        if (player instanceof OnlinePlayer onlinePlayer) {
            new MultiOnlinePunishmentTarget(onlinePlayer);
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

    @Nullable
    @Override
    public Punishment getPunishment() {
        return this.punishment;
    }

    @Override
    public void setPunishment(@NotNull Punishment punishment) {
        this.punishment = punishment;
    }
}