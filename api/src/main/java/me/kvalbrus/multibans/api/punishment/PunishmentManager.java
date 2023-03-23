package me.kvalbrus.multibans.api.punishment;

import java.util.List;
import java.util.UUID;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface PunishmentManager {

    @Deprecated
    @NotNull
    <T extends Punishment> List<T> getPlayerHistory(String target);

    @NotNull
    <T extends Punishment> List<T> getPlayerHistory(UUID uuid);

    @NotNull
    <T extends Punishment> List<T> getPlayerHistory(UUID uuid, Class<T> clazz);

    @NotNull
    <T extends Punishment> List<T> getCreatorHistory(String creator);

    @Deprecated
    @NotNull
    <T extends Punishment> List<T> getActivePunishments(String target);

    @NotNull
    <T extends Punishment> List<T> getActivePunishments(UUID uuid);

    @NotNull
    <T extends Punishment> List<T> getActivePunishments(UUID uuid, Class<T> clazz);

    boolean hasPunishment(String id);

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