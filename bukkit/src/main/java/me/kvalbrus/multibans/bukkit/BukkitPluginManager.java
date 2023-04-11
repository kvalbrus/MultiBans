package me.kvalbrus.multibans.bukkit;

import java.io.File;
import java.util.Collection;
import java.util.UUID;
import me.kvalbrus.multibans.api.Console;
import me.kvalbrus.multibans.api.MultiBans;
import me.kvalbrus.multibans.api.OnlinePlayer;
import me.kvalbrus.multibans.api.punishment.Punishment;
import me.kvalbrus.multibans.bukkit.commands.BanBukkit;
import me.kvalbrus.multibans.bukkit.commands.BanIpBukkit;
import me.kvalbrus.multibans.bukkit.commands.MuteChatBukkit;
import me.kvalbrus.multibans.bukkit.implementations.BukkitConsole;
import me.kvalbrus.multibans.bukkit.implementations.BukkitPlayer;
import me.kvalbrus.multibans.bukkit.implementations.BukkitOnlinePlayer;
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
import org.jetbrains.annotations.Nullable;

public class BukkitPluginManager extends MultiBansPluginManager {

    private final BukkitPlugin plugin;

    public BukkitPluginManager(@NotNull BukkitPlugin plugin) {
        super();
        this.plugin = plugin;
    }

    @Override
    public void onEnable() {
        MultiBans multiBansProvider = new MultiBansBukkit(this);
        this.plugin.getServer().getServicesManager()
            .register(MultiBans.class, multiBansProvider, this.plugin, ServicePriority.Normal);
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
    public me.kvalbrus.multibans.api.Player[] getOfflinePlayers() {
        OfflinePlayer[] players = this.plugin.getServer().getOfflinePlayers();
        me.kvalbrus.multibans.api.Player[] offlinePlayers = new me.kvalbrus.multibans.api.Player[players.length];
        for (int i = 0; i < players.length; ++i) {
            offlinePlayers[i] = new BukkitPlayer(players[i]);
        }

        return offlinePlayers;
    }

    @NotNull
    @Override
    public OnlinePlayer[] getOnlinePlayers() {
        Collection<? extends Player> players = this.plugin.getServer().getOnlinePlayers();
        OnlinePlayer[] onlineOnlinePlayers = new OnlinePlayer[players.size()];
        int i = 0;
        for (Player player : players) {
            onlineOnlinePlayers[i++] = new BukkitOnlinePlayer(player);
        }

        return onlineOnlinePlayers;
    }

    @Nullable
    @Override
    public OnlinePlayer getPlayer(UUID uuid) {
        Player player = this.plugin.getServer().getPlayer(uuid);
        if (player == null) {
            return null;
        }

        return new BukkitOnlinePlayer(player);
    }

    @Nullable
    @Override
    public OnlinePlayer getPlayer(String name) {
        Player player = this.plugin.getServer().getPlayer(name);
        if (player == null) {
            return null;
        }

        return new BukkitOnlinePlayer(player);
    }

    @NotNull
    @Override
    public me.kvalbrus.multibans.api.Player getOfflinePlayer(String name) {
        OnlinePlayer onlinePlayer = this.getPlayer(name);
        if (onlinePlayer != null) {
            return onlinePlayer;
        } else {
            OfflinePlayer bukkitOfflinePlayer = this.plugin.getServer().getOfflinePlayer(name);
            return new BukkitPlayer(bukkitOfflinePlayer);
        }
    }

    @Override
    public Console getConsole() {
        return new BukkitConsole(this.plugin.getServer().getConsoleSender());
    }

    @NotNull
    @Override
    public me.kvalbrus.multibans.api.Player getOfflinePlayer(UUID uuid) {
        OnlinePlayer onlinePlayer = this.getPlayer(uuid);
        if (onlinePlayer != null) {
            return onlinePlayer;
        } else {
            OfflinePlayer bukkitOfflinePlayer = this.plugin.getServer().getOfflinePlayer(uuid);
            return new BukkitPlayer(bukkitOfflinePlayer);
        }
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
}