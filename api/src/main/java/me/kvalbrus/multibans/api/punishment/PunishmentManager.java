package me.kvalbrus.multibans.api.punishment;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface PunishmentManager {

    @NotNull
    List<Punishment> getPlayerHistory(String target);

    @NotNull
    List<Punishment> getPlayerHistory(String target, Comparator<Punishment> comparator);

    @NotNull
    List<Punishment> getPlayerHistory(UUID uuid);

    @NotNull
    List<Punishment> getPlayerHistory(UUID uuid, Comparator<Punishment> comparator);

    @NotNull
    List<Punishment> getCreatorHistory(String creator);

    @NotNull
    List<Punishment> getActivePunishments(String target);

    @NotNull
    List<Punishment> getActivePunishments(String target, Comparator<Punishment> comparator);

    @NotNull
    List<Punishment> getActivePunishments(UUID uuid);

    @NotNull
    List<Punishment> getActivePunishments(UUID uuid, Comparator<Punishment> comparator);

    @Deprecated
    @NotNull
    Map<PunishmentType, List<Punishment>> getMapActivePunishments(String target);

    @Deprecated
    @NotNull
    Map<PunishmentType, List<Punishment>> getMapActivePunishments(UUID uuid);

    boolean hasPunishments(String id);

    @Nullable
    Punishment getPunishment(String id);

    @NotNull
    Punishment generatePunishment(@NotNull final PunishmentType type,
                                  @NotNull final String ip,
                                  @NotNull final String target,
                                  @NotNull final UUID targetUUID,
                                  @NotNull final String creator,
                                  long duration,
                                  @NotNull final String reason);

    @NotNull
    Punishment generatePunishment(@NotNull final PunishmentType type,
                                  @NotNull final String ip,
                                  @NotNull final String target,
                                  @NotNull final UUID targetUUID,
                                  @NotNull final String creator,
                                  long duration,
                                  @NotNull final String reason,
                                  @Nullable String comment,
                                  @NotNull List<String> servers);
}