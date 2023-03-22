package me.kvalbrus.multibans.common.managers;

import java.util.List;
import java.util.Random;
import java.util.UUID;
import lombok.Getter;
import me.kvalbrus.multibans.common.punishment.MultiPunishment;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import me.kvalbrus.multibans.api.punishment.PunishmentStatus;
import me.kvalbrus.multibans.api.punishment.PunishmentType;
import me.kvalbrus.multibans.common.punishment.Punishment;

public class PunishmentManager {

    @Getter
    @NotNull
    private final PluginManager pluginManager;

    public PunishmentManager(@NotNull final PluginManager pluginManager) {
        this.pluginManager = pluginManager;
    }

    @NotNull
    public <T extends Punishment> List<T> getPlayerHistory(String target) {
        return this.pluginManager.getDataProvider().getTargetHistory(target);
    }

    @NotNull
    public <T extends Punishment> List<T> getPlayerHistory(UUID uuid) {
        return this.pluginManager.getDataProvider().getTargetHistory(uuid);
    }

    @NotNull
    public <T extends Punishment> List<T> getPlayerHistory(UUID uuid, @NotNull Class<T> clazz) {
        List<T> punishments = this.getActivePunishments(uuid);

        punishments.removeIf(punishment -> !clazz.isInstance(punishment));

        return punishments;
    }

    @NotNull
    public <T extends Punishment> List<T> getCreatorHistory(String creator) {
        return this.pluginManager.getDataProvider().getCreatorHistory(creator);
    }

    @NotNull
    public <T extends Punishment> List<T> getActivePunishments(String target) {
        List<T> punishments = this.getPlayerHistory(target);

        punishments.removeIf(punishment -> punishment.getStatus() != PunishmentStatus.ACTIVE);

        return punishments;
    }

    @NotNull
    public <T extends Punishment> List<T> getActivePunishments(UUID uuid) {
        List<T> punishments = this.getPlayerHistory(uuid);

        punishments.removeIf(punishment -> punishment.getStatus() != PunishmentStatus.ACTIVE);

        return punishments;
    }

    @NotNull
    public <T extends Punishment> List<T> getActivePunishments(UUID uuid, @NotNull Class<T> clazz) {
        List<T> punishments = this.getActivePunishments(uuid);
        punishments.removeIf(punishment -> !clazz.isInstance(punishment));

        return punishments;
    }

//    @NotNull
//    public Map<PunishmentType, List<Punishment>> getMapActivePunishments(String target) {
//        Map<PunishmentType, List<Punishment>> activePunishments = new HashMap<>();
//
//        Arrays.stream(PunishmentType.values()).toList()
//            .forEach(type -> activePunishments.put(type, new ArrayList<>()));
//        if (target == null) {
//            return activePunishments;
//        }
//
//        List<Punishment> punishments = this.getPluginManager().getDataProvider()
//            .getTargetHistory(target);
//        punishments.removeIf(Punishment::isCancelled);
//
//        long realTime = System.currentTimeMillis();
//
//        List<Punishment> temporary = new ArrayList<>();
//        for (Punishment punishment : punishments) {
//            if (!punishment.isCancelled()) {
//                temporary.add(punishment);
//            }
//        }
//
//        temporary.sort(null);
//
//        long sumDurations = 0L;
//        for (Punishment punishment : temporary) {
//            if (punishment.getDuration() > 0) {
//                sumDurations += punishment.getDuration();
//
//                if (punishment.getCreatedDate() + sumDurations >= realTime) {
//                    activePunishments.get(punishment.getType()).add(punishment);
//                }
//            } else {
//                activePunishments.get(punishment.getType()).add(punishment);
//            }
//        }
//
//        return activePunishments;
//    }

//    @NotNull
//    public Map<PunishmentType, List<Punishment>> getMapActivePunishments(UUID uuid) {
//        Map<PunishmentType, List<Punishment>> activePunishments = new HashMap<>();
//
//        Arrays.stream(PunishmentType.values()).toList()
//            .forEach(type -> activePunishments.put(type, new ArrayList<>()));
//        if (uuid == null) {
//            return activePunishments;
//        }
//
//        List<Punishment> punishments = this.getPluginManager().getDataProvider()
//            .getTargetHistory(uuid);
//        punishments.removeIf(Punishment::isCancelled);
//
//        long realTime = System.currentTimeMillis();
//
//        List<Punishment> temporary = new ArrayList<>();
//        for (Punishment punishment : punishments) {
//            if (!punishment.isCancelled()) {
//                temporary.add(punishment);
//            }
//        }
//
//        temporary.sort(null);
//
//        long sumDurations = 0L;
//        for (Punishment punishment : temporary) {
//            if (punishment.getDuration() > 0) {
//                sumDurations += punishment.getDuration();
//
//                if (punishment.getCreatedDate() + sumDurations >= realTime) {
//                    activePunishments.get(punishment.getType()).add(punishment);
//                }
//            } else {
//                activePunishments.get(punishment.getType()).add(punishment);
//            }
//        }
//
//        return activePunishments;
//    }

    public boolean hasPunishment(@NotNull String id) {
        return this.pluginManager.getDataProvider().hasPunishment(id);
    }

    public Punishment getPunishment(String id) {
        return this.pluginManager.getDataProvider().getPunishment(id);
    }

    @NotNull
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