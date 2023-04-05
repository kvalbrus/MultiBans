package me.kvalbrus.multibans.common.managers;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import lombok.Getter;
import me.kvalbrus.multibans.api.punishment.Punishment;
import me.kvalbrus.multibans.api.Player;
import me.kvalbrus.multibans.common.punishment.MultiPunishment;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import me.kvalbrus.multibans.api.punishment.PunishmentStatus;
import me.kvalbrus.multibans.api.punishment.PunishmentType;

public class PunishmentManager implements me.kvalbrus.multibans.api.punishment.PunishmentManager {

    @Getter
    @NotNull
    private final PluginManager pluginManager;

    public PunishmentManager(@NotNull final PluginManager pluginManager) {
        this.pluginManager = pluginManager;
    }

    @NotNull
    @Override
    public <T extends Punishment> List<T> getPlayerHistory(UUID uuid) {
        return this.pluginManager.getDataProvider().getTargetHistory(uuid);
    }

    @NotNull
    @Override
    public <T extends Punishment> List<T> getPlayerHistory(UUID uuid, @NotNull Class<T> clazz) {
        List<T> punishments = this.getPlayerHistory(uuid);

        punishments.removeIf(punishment -> !clazz.isInstance(punishment));

        return punishments;
    }

    @NotNull
    @Override
    public <T extends Punishment> List<T> getCreatorHistory(String creator) {
        return this.pluginManager.getDataProvider().getCreatorHistory(creator);
    }

    @NotNull
    @Override
    public <T extends Punishment> List<T> getActivePunishments(UUID uuid) {
        List<T> punishments = this.getPlayerHistory(uuid);

        punishments.removeIf(punishment -> punishment.getStatus() != PunishmentStatus.ACTIVE);

        return punishments;
    }

    @NotNull
    @Override
    public <T extends Punishment> List<T> getActivePunishments(UUID uuid, @NotNull Class<T> clazz) {
        List<T> punishments = this.getActivePunishments(uuid);
        punishments.removeIf(punishment -> !clazz.isInstance(punishment));

        return punishments;
    }

    @Override
    public boolean hasPunishment(@NotNull String id) {
        return this.pluginManager.getDataProvider().hasPunishment(id);
    }

    @Override
    public Punishment getPunishment(String id) {
        return this.pluginManager.getDataProvider().getPunishment(id);
    }

    @Override
    public boolean hasActiveBan(UUID uuid) {
        List<Punishment> punishments = this.getPlayerHistory(uuid);
        for (Punishment punishment : punishments) {
            if (punishment.getType() == PunishmentType.BAN || punishment.getType() == PunishmentType.TEMP_BAN) {
                if (punishment.getStatus() == PunishmentStatus.ACTIVE) {
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public boolean hasActiveBanIp(UUID uuid) {
        List<Punishment> punishments = this.getPlayerHistory(uuid);
        for (Punishment punishment : punishments) {
            if (punishment.getType() == PunishmentType.BAN_IP || punishment.getType() == PunishmentType.TEMP_BAN_IP) {
                if (punishment.getStatus() == PunishmentStatus.ACTIVE) {
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public boolean hasActiveChatMute(UUID uuid) {
        List<Punishment> punishments = this.getPlayerHistory(uuid);
        for (Punishment punishment : punishments) {
            if (punishment.getType() == PunishmentType.MUTE || punishment.getType() == PunishmentType.TEMP_MUTE) {
                if (punishment.getStatus() == PunishmentStatus.ACTIVE) {
                    return true;
                }
            }
        }

        return false;
    }

    @NotNull
    @Override
    public synchronized Punishment generatePunishment(@NotNull final PunishmentType type,
                                                      @NotNull final Player target,
                                                      @NotNull final String creatorName,
                                                      long duration,
                                                      @NotNull String reason) {
        return this.generatePunishment(type, target, creatorName, duration, reason, null, new ArrayList<>());
    }

    @NotNull
    @Override
    public synchronized Punishment generatePunishment(@NotNull final PunishmentType type,
                                                      @NotNull final Player target,
                                                      @NotNull final String creatorName,
                                                      long duration,
                                                      @NotNull String reason,
                                                      @Nullable String comment,
                                                      @NotNull List<String> servers) {
        String id = generateId();
        while (this.pluginManager.getDataProvider().hasPunishment(id)) {
            id = generateId();
        }

        long realTime = System.currentTimeMillis();

        return MultiPunishment.constructPunishment(this, type, id,
            target.getHostAddress(), target.getName(), target.getUniqueId(), creatorName, realTime,
            realTime, duration, reason, comment, null, -1, null, servers, false);
    }

    @NotNull
    private synchronized String generateId() {
        StringBuilder builder = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < 7; ++i) {
            builder.append(random.nextInt(10));
        }

        return builder.toString();
    }
}