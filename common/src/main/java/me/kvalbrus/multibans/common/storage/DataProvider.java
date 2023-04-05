package me.kvalbrus.multibans.common.storage;

import java.util.List;
import java.util.UUID;
import me.kvalbrus.multibans.api.punishment.Punishment;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface DataProvider {

    String getName();

    void initialization();

    void shutdown();

    @Deprecated
    boolean createPunishmentsTable();

    @Deprecated
    boolean deletePunishmentTable();

    void wipe();

    boolean createPunishment(@NotNull Punishment punishment);

    boolean updatePunishment(@NotNull Punishment punishment);

    boolean deletePunishment(@NotNull Punishment punishment);

    boolean hasPunishment(String id);

    @Nullable
    Punishment getPunishment(String id);

    @NotNull
    <T extends Punishment> List<T> getTargetHistory(UUID uuid);

    @NotNull
    <T extends Punishment> List<T> getCreatorHistory(String creator);
}