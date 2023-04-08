package me.kvalbrus.multibans.api.managers;

import java.util.List;
import java.util.UUID;
import me.kvalbrus.multibans.api.Player;
import me.kvalbrus.multibans.api.punishment.Punishment;
import me.kvalbrus.multibans.api.punishment.PunishmentType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface PunishmentManager {

    @NotNull
    <T extends Punishment> List<T> getPlayerHistory(UUID uuid);

    @NotNull
    <T extends Punishment> List<T> getPlayerHistory(UUID uuid, Class<T> clazz);

    @NotNull
    <T extends Punishment> List<T> getCreatorHistory(String creator);

    @NotNull
    <T extends Punishment> List<T> getActivePunishments(UUID uuid);

    @NotNull
    <T extends Punishment> List<T> getActivePunishments(UUID uuid, Class<T> clazz);

    boolean hasPunishment(String id);

    @Nullable
    Punishment getPunishment(String id);

    boolean hasActiveBan(UUID uuid);

    boolean hasActiveBan(String name);

    boolean hasActiveBanIp(UUID uuid);

    boolean hasActiveBanIp(String name);

    boolean hasActiveChatMute(UUID uuid);

    boolean hasActiveChatMute(String name);

    @NotNull
    Punishment generatePunishment(@NotNull final PunishmentType type,
                                  @NotNull final Player target,
                                  @NotNull final String creator,
                                  long duration,
                                  @NotNull final String reason);

    @NotNull
    Punishment generatePunishment(@NotNull final PunishmentType type,
                                  @NotNull final Player target,
                                  @NotNull final String creator,
                                  long duration,
                                  @NotNull final String reason,
                                  @Nullable String comment,
                                  @NotNull List<String> servers);
}