package me.kvalbrus.multibans.common.managers;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import lombok.Getter;
import me.kvalbrus.multibans.api.punishment.Punishment;
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

    @Deprecated
    @NotNull
    @Override
    public <T extends Punishment> List<T> getPlayerHistory(String target) {
        return this.pluginManager.getDataProvider().getTargetHistory(target);
    }

    @NotNull
    @Override
    public <T extends Punishment> List<T> getPlayerHistory(UUID uuid) {
        return this.pluginManager.getDataProvider().getTargetHistory(uuid);
    }

    @NotNull
    @Override
    public <T extends Punishment> List<T> getPlayerHistory(UUID uuid, @NotNull Class<T> clazz) {
        List<T> punishments = this.getActivePunishments(uuid);

        punishments.removeIf(punishment -> !clazz.isInstance(punishment));

        return punishments;
    }

    @NotNull
    @Override
    public <T extends Punishment> List<T> getCreatorHistory(String creator) {
        return this.pluginManager.getDataProvider().getCreatorHistory(creator);
    }

    @Deprecated
    @NotNull
    @Override
    public <T extends Punishment> List<T> getActivePunishments(String target) {
        List<T> punishments = this.getPlayerHistory(target);

        punishments.removeIf(punishment -> punishment.getStatus() != PunishmentStatus.ACTIVE);

        return punishments;
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

    @NotNull
    @Override
    public synchronized Punishment generatePunishment(@NotNull final PunishmentType type,
                                                      @NotNull final String targetIp,
                                                      @NotNull final String targetName,
                                                      @NotNull final UUID targetUUID,
                                                      @NotNull final String creatorName,
                                                      long duration,
                                                      @NotNull String reason) {
        return this.generatePunishment(type, targetIp, targetName, targetUUID, creatorName,
            duration, reason, null, new ArrayList<>());
    }

    @NotNull
    @Override
    public synchronized Punishment generatePunishment(@NotNull final PunishmentType type,
                                                      @NotNull final String targetIp,
                                                      @NotNull final String targetName,
                                                      @NotNull final UUID targetUUID,
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

        return MultiPunishment.constructPunishment(this, type, id, targetIp, targetName, targetUUID,
            creatorName, realTime, realTime, duration, reason, comment, null, -1, null, servers, false);
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