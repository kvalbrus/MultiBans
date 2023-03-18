package me.kvalbrus.multibans.common.storage;

import java.util.List;
import java.util.UUID;
import me.kvalbrus.multibans.common.punishment.Punishment;
import org.jetbrains.annotations.NotNull;

public interface DataProvider {

    void connect() throws Exception;

    void disconnect();

    boolean createPunishmentsTable();

    boolean deletePunishmentTable();

    boolean createPunishment(@NotNull Punishment punishment);

    boolean updatePunishment(@NotNull Punishment punishment);

    boolean deletePunishment(@NotNull Punishment punishment);

    boolean hasPunishment(String id);

    @NotNull
    List<Punishment> getTargetHistory(String target);

    @NotNull
    List<Punishment> getTargetHistory(UUID uuid);

    @NotNull
    List<Punishment> getCreatorHistory(String creator);
}