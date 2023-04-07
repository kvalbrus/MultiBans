package me.kvalbrus.multibans.bukkit;

import java.io.File;
import java.util.Collection;
import java.util.UUID;
import me.kvalbrus.multibans.api.MultiBans;
import me.kvalbrus.multibans.api.punishment.Punishment;
import me.kvalbrus.multibans.bukkit.commands.BanBukkit;
import me.kvalbrus.multibans.bukkit.commands.BanIpBukkit;
import me.kvalbrus.multibans.bukkit.commands.MuteChatBukkit;
import me.kvalbrus.multibans.bukkit.implementations.BukkitOfflinePlayer;
import me.kvalbrus.multibans.bukkit.implementations.BukkitPlayer;
import me.kvalbrus.multibans.bukkit.listeners.PlayerJoinListener;
import me.kvalbrus.multibans.common.managers.MultiBansPluginManager;
import me.kvalbrus.multibans.common.managers.PunishmentManager;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.plugin.ServicePriority;
import org.jetbrains.annotations.NotNull;
import me.kvalbrus.multibans.bukkit.events.ActivatePunishmentEvent;
import me.kvalbrus.multibans.bukkit.events.DeactivatePunishmentEvent;
import me.kvalbrus.multibans.api.punishment.PunishmentType;

public class BukkitPluginManager extends MultiBansPluginManager {

    private final BukkitPlugin plugin;

    private MultiBans multiBansProvider;

    public BukkitPluginManager(@NotNull BukkitPlugin plugin) {
        super();
        this.plugin = plugin;
    }

//    @Override
//    public void onLoad() {
//        this.loadConfiguration();
//
//        try {
//            this.dataProvider.initialization();
//            this.dataProvider.createPunishmentsTable();
//        } catch (Exception exception) {
//            exception.printStackTrace();
//            // TODO: logger
//        }
//
//
//
//    }

    @Override
    public void onEnable() {
        this.multiBansProvider = new MultiBansBukkit(this);
        this.plugin.getServer().getServicesManager()
            .register(MultiBans.class, this.multiBansProvider, this.plugin, ServicePriority.Normal);
        this.registerCommands();
        this.registerListeners();
    }

    @Override
    public void onDisable() {
        if (this.getDataProvider() != null) {
            this.getDataProvider().shutdown();
        }
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
    public me.kvalbrus.multibans.api.OfflinePlayer[] getOfflinePlayers() {
        OfflinePlayer[] players = this.plugin.getServer().getOfflinePlayers();
        me.kvalbrus.multibans.api.OfflinePlayer[] offlinePlayers = new me.kvalbrus.multibans.api.OfflinePlayer[players.length];
        for (int i = 0; i < players.length; ++i) {
            offlinePlayers[i] = new BukkitOfflinePlayer(players[i]);
        }

        return offlinePlayers;
    }

    @NotNull
    @Override
    public me.kvalbrus.multibans.api.Player[] getOnlinePlayers() {
        Collection<? extends Player> players = this.plugin.getServer().getOnlinePlayers();
        me.kvalbrus.multibans.api.Player[] onlinePlayers = new me.kvalbrus.multibans.api.Player[players.size()];
        int i = 0;
        for (Player player : players) {
            onlinePlayers[i++] = new BukkitPlayer(player);
        }

        return onlinePlayers;
    }

    @Override
    public me.kvalbrus.multibans.api.Player getPlayer(UUID uuid) {
        return new BukkitPlayer(this.plugin.getServer().getPlayer(uuid));
    }

    @Override
    public me.kvalbrus.multibans.api.Player getPlayer(String name) {
        return new BukkitPlayer(this.plugin.getServer().getPlayer(name));
    }

    @Override
    public void activatePunishment(@NotNull Punishment punishment) {
        if (punishment.getType() == PunishmentType.BAN ||
            punishment.getType() == PunishmentType.TEMP_BAN ||
            punishment.getType() == PunishmentType.BAN_IP ||
            punishment.getType() == PunishmentType.TEMP_BAN_IP) {
            Player player = this.plugin.getServer().getPlayer(punishment.getTargetUniqueId());
            if (player != null) {
                player.kickPlayer(((PunishmentManager) this.getPunishmentManager()).getPlayerTitle(player.getUniqueId()));
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
        new BanBukkit(this, this.plugin);
        new BanIpBukkit(this, this.plugin);
        new MuteChatBukkit(this, this.plugin);
    }

    private void registerListeners() {
        this.plugin.getServer().getPluginManager().registerEvents(new PlayerJoinListener(this), this.plugin);
    }

//    private void loadConfiguration() {
////        File file = new File(this.plugin.getDataFolder(), "settings.yml");
////        if (!file.exists()) {
////            this.plugin.saveResource("settings.yml", false);
////        }
////
////        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
////
////        if (config.contains("database")) {
////            String address = config.getString("database.address", "");
////            int port = config.getInt("database.port");
////            String databaseName = config.getString("database.database_name", "");
////            String username = config.getString("database.username", "");
////            String password = config.getString("database.password", "");
////            String properties = config.getString("database.properties", "");
////
////            this.getDataProvider() = new MySqlProvider(
////                this, new DataProviderSettings(databaseName, address, port, username, password, properties));
////        }
//
//
//    }
}