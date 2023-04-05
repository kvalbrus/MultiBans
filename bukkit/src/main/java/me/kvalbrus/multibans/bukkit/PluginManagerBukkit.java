package me.kvalbrus.multibans.bukkit;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import me.kvalbrus.multibans.api.MultiBans;
import me.kvalbrus.multibans.api.punishment.Punishment;
import me.kvalbrus.multibans.common.managers.PluginManager;
import me.kvalbrus.multibans.common.managers.PunishmentManager;
import me.kvalbrus.multibans.common.storage.DataProvider;
import me.kvalbrus.multibans.common.storage.StorageData;
import me.kvalbrus.multibans.common.storage.mysql.MySqlProvider;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.plugin.ServicePriority;
import org.jetbrains.annotations.NotNull;
import me.kvalbrus.multibans.bukkit.events.ActivatePunishmentEvent;
import me.kvalbrus.multibans.bukkit.events.DeactivatePunishmentEvent;
import me.kvalbrus.multibans.api.punishment.PunishmentType;

public class PluginManagerBukkit implements PluginManager {

    private final BukkitPlugin plugin;

    private final PunishmentManager punishmentManager;

    private MultiBans multiBandProvider;

    private DataProvider dataProvider;

    public PluginManagerBukkit(@NotNull BukkitPlugin plugin) {
        this.plugin = plugin;
        this.punishmentManager = new PunishmentManager(this);
    }

    @Override
    public void onLoad() {
        this.loadConfiguration();
        this.loadCommands();

        try {
            this.dataProvider.initialization();
            this.dataProvider.createPunishmentsTable();
        } catch (Exception exception) {
            exception.printStackTrace();
            // TODO: logger
        }

        this.multiBandProvider = new MultiBansBukkit(this);
        this.plugin.getServer().getServicesManager()
            .register(MultiBans.class, this.multiBandProvider, this.plugin, ServicePriority.Normal);
    }

    @Override
    public void onEnable() {
        this.registerCommands();
        this.registerListeners();
    }

    @Override
    public void onDisable() {
        if (this.dataProvider != null) {
            this.dataProvider.shutdown();
        }
    }

    @Override
    public void reload() {

    }

    @Override
    public DataProvider getDataProvider() {
        return this.dataProvider;
    }

    @NotNull
    @Override
    public PunishmentManager getPunishmentManager() {
        return this.punishmentManager;
    }

    @NotNull
    @Override
    public final File getDataFolder() {
        return this.plugin.getDataFolder();
    }

    @Override
    public void saveResource(String path, boolean replace) {
        this.plugin.saveResource(path, replace);
    }

    @NotNull
    @Override
    public List<String> getPlayers() {
        OfflinePlayer[] offlinePlayers = this.plugin.getServer().getOfflinePlayers();
        List<String> players = new ArrayList<>();
        for (OfflinePlayer offlinePlayer : offlinePlayers) {
            players.add(offlinePlayer.getName());
        }

        return players;
    }

    @Override
    public me.kvalbrus.multibans.api.Player getPlayer(UUID uuid) {
        return null;
    }

    @Override
    public me.kvalbrus.multibans.api.Player getPlayer(String name) {
        return null;
    }

    @Override
    public void activatePunishment(@NotNull Punishment punishment) {
        if (punishment.getType() == PunishmentType.BAN ||
            punishment.getType() == PunishmentType.BAN_IP) {
            Player player = this.plugin.getServer().getPlayer(punishment.getTargetUniqueId());
            if (player != null) {
                player.kickPlayer("");
            }
        }

        Event event = new ActivatePunishmentEvent(punishment);
        this.plugin.getServer().getPluginManager().callEvent(event);
    }

    @Override
    public void deactivatePunishment(@NotNull Punishment punishment) {
        Event event = new DeactivatePunishmentEvent(punishment);
        this.plugin.getServer().getPluginManager().callEvent(event);
    }

    private void registerCommands() {
    }

    private void registerListeners() {
    }

    private void loadConfiguration() {
        File file = new File(this.plugin.getDataFolder(), "settings.yml");
        if (!file.exists()) {
            this.plugin.saveResource("settings.yml", false);
        }

        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

        if (config.contains("database")) {
            String address = config.getString("database.address", "");
            int port = config.getInt("database.port");
            String databaseName = config.getString("database.database_name", "");
            String username = config.getString("database.username", "");
            String password = config.getString("database.password", "");
            String properties = config.getString("database.properties", "");

            this.dataProvider = new MySqlProvider(
                this, new StorageData(databaseName, address, port, username, password, properties));
        }
    }

    private void loadCommands() {

    }
}
