package me.kvalbrus.multibans.common.api;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import me.kvalbrus.multibans.common.managers.PunishmentManager;
import me.kvalbrus.multibans.common.punishment.Punishment;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import me.kvalbrus.multibans.api.punishment.PunishmentType;

public class PunishmentManagerAPI implements
    me.kvalbrus.multibans.api.punishment.PunishmentManager {

    private final PunishmentManager punishmentManager;

    public PunishmentManagerAPI(
        @NotNull final PunishmentManager punishmentManager) {
        this.punishmentManager = punishmentManager;
    }

    @NotNull
    @Override
    public List<me.kvalbrus.multibans.api.punishment.Punishment> getPlayerHistory(String target) {
        List<Punishment> history = this.punishmentManager.getPlayerHistory(
            target);
        List<me.kvalbrus.multibans.api.punishment.Punishment> copyHistory = new ArrayList<>();
        for (Punishment punishment : history) {
            copyHistory.add(new PunishmentAPI(punishment));
        }

        return copyHistory;
    }

    @NotNull
    @Override
    public List<me.kvalbrus.multibans.api.punishment.Punishment> getPlayerHistory(UUID uuid) {
        List<Punishment> history = this.punishmentManager.getPlayerHistory(
            uuid);
        List<me.kvalbrus.multibans.api.punishment.Punishment> copyHistory = new ArrayList<>();
        for (Punishment punishment : history) {
            copyHistory.add(new PunishmentAPI(punishment));
        }

        return copyHistory;
    }

    @NotNull
    @Override
    public List<me.kvalbrus.multibans.api.punishment.Punishment> getPlayerHistory(String target,
                                                                                  Comparator<me.kvalbrus.multibans.api.punishment.Punishment> comparator) {
        List<me.kvalbrus.multibans.api.punishment.Punishment> history = this.getPlayerHistory(target);
        history.sort(comparator);

        return history;
    }

    @NotNull
    @Override
    public List<me.kvalbrus.multibans.api.punishment.Punishment> getPlayerHistory(UUID uuid,
                                                                                  Comparator<me.kvalbrus.multibans.api.punishment.Punishment> comparator) {
        List<me.kvalbrus.multibans.api.punishment.Punishment> history = this.getPlayerHistory(uuid);
        history.sort(comparator);

        return history;
    }

    @NotNull
    @Override
    public List<me.kvalbrus.multibans.api.punishment.Punishment> getCreatorHistory(String creator) {
        List<Punishment> history = this.punishmentManager.getCreatorHistory(
            creator);
        List<me.kvalbrus.multibans.api.punishment.Punishment> copyHistory = new ArrayList<>();
        for (Punishment punishment : history) {
            copyHistory.add(new PunishmentAPI(punishment));
        }

        return copyHistory;
    }

    @NotNull
    @Override
    public List<me.kvalbrus.multibans.api.punishment.Punishment> getActivePunishments(String target) {
        List<Punishment> history = this.punishmentManager.getActivePunishments(
            target);
        List<me.kvalbrus.multibans.api.punishment.Punishment> copyHistory = new ArrayList<>();
        for (Punishment punishment : history) {
            copyHistory.add(new PunishmentAPI(punishment));
        }

        return copyHistory;
    }

    @NotNull
    @Override
    public List<me.kvalbrus.multibans.api.punishment.Punishment> getActivePunishments(String target,
                                                                                      Comparator<me.kvalbrus.multibans.api.punishment.Punishment> comparator) {
        List<me.kvalbrus.multibans.api.punishment.Punishment> activePunishments = this.getActivePunishments(target);
        activePunishments.sort(comparator);

        return activePunishments;
    }

    @NotNull
    @Override
    public List<me.kvalbrus.multibans.api.punishment.Punishment> getActivePunishments(UUID uuid) {
        List<Punishment> history = this.punishmentManager.getActivePunishments(
            uuid);
        List<me.kvalbrus.multibans.api.punishment.Punishment> copyHistory = new ArrayList<>();
        for (Punishment punishment : history) {
            copyHistory.add(new PunishmentAPI(punishment));
        }

        return copyHistory;
    }

    @NotNull
    @Override
    public List<me.kvalbrus.multibans.api.punishment.Punishment> getActivePunishments(UUID uuid,
                                                                                      Comparator<me.kvalbrus.multibans.api.punishment.Punishment> comparator) {
        List<me.kvalbrus.multibans.api.punishment.Punishment> activePunishments = this.getActivePunishments(uuid);
        activePunishments.sort(comparator);

        return activePunishments;
    }

    @NotNull
    @Override
    @Deprecated
    public Map<PunishmentType, List<me.kvalbrus.multibans.api.punishment.Punishment>> getMapActivePunishments(String target) {
        Map<PunishmentType, List<Punishment>> activePunishments =
            this.punishmentManager.getMapActivePunishments(target);
        Map<PunishmentType, List<me.kvalbrus.multibans.api.punishment.Punishment>> copyMap = new HashMap<>();
        for (Map.Entry<PunishmentType, List<Punishment>> entry : activePunishments.entrySet()) {
            copyMap.put(entry.getKey(), new ArrayList<>());
            for (Punishment punishment : entry.getValue()) {
                copyMap.get(entry.getKey()).add(new PunishmentAPI(punishment));
            }
        }

        return copyMap;
    }

    @NotNull
    @Override
    @Deprecated
    public Map<PunishmentType, List<me.kvalbrus.multibans.api.punishment.Punishment>> getMapActivePunishments(UUID uuid) {
        Map<PunishmentType, List<Punishment>> activePunishments =
            this.punishmentManager.getMapActivePunishments(uuid);
        Map<PunishmentType, List<me.kvalbrus.multibans.api.punishment.Punishment>> copyMap = new HashMap<>();
        for (Map.Entry<PunishmentType, List<Punishment>> entry : activePunishments.entrySet()) {
            copyMap.put(entry.getKey(), new ArrayList<>());
            for (Punishment punishment : entry.getValue()) {
                copyMap.get(entry.getKey()).add(new PunishmentAPI(punishment));
            }
        }

        return copyMap;
    }

    @Override
    public boolean hasPunishments(String id) {
        return this.punishmentManager.hasPunishment(id);
    }

    @Nullable
    @Override
    public me.kvalbrus.multibans.api.punishment.Punishment getPunishment(String id) {
        Punishment punishment = this.punishmentManager.getPunishment(id);
        if(punishment != null) {
            return new PunishmentAPI(punishment);
        }

        return null;
    }

    @NotNull
    @Override
    public me.kvalbrus.multibans.api.punishment.Punishment generatePunishment(@NotNull PunishmentType type,
                                                                              @NotNull String targetIp,
                                                                              @NotNull String targetName,
                                                                              @NotNull UUID targetUUID,
                                                                              @NotNull String creatorName,
                                                                              long duration,
                                                                              @NotNull String reason) {
        return this.generatePunishment(type, targetIp, targetName, targetUUID, creatorName,
            duration, reason, null, new ArrayList<>());
    }

    @NotNull
    @Override
    public me.kvalbrus.multibans.api.punishment.Punishment generatePunishment(@NotNull PunishmentType type,
                                                                              @NotNull String targetIp,
                                                                              @NotNull String targetName,
                                                                              @NotNull UUID targetUUID,
                                                                              @NotNull String creatorName,
                                                                              long duration,
                                                                              @NotNull String reason,
                                                                              @Nullable String comment,
                                                                              @NotNull List<String> servers) {

        Punishment punishment =
            this.punishmentManager.generatePunishment(type, targetIp, targetName, targetUUID,
                creatorName, duration, reason, comment, servers);
        return new PunishmentAPI(punishment);
    }
}
