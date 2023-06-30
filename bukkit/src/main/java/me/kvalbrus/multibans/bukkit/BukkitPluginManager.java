package me.kvalbrus.multibans.bukkit;

import java.io.File;
import java.util.Collection;
import java.util.UUID;
import me.kvalbrus.multibans.api.Console;
import me.kvalbrus.multibans.api.MultiBansAPI;
import me.kvalbrus.multibans.api.OnlinePlayer;
import me.kvalbrus.multibans.bukkit.commands.HistoryBukkit;
import me.kvalbrus.multibans.bukkit.commands.IPCheckBukkit;
import me.kvalbrus.multibans.bukkit.commands.IPHistoryBukkit;
import me.kvalbrus.multibans.bukkit.events.ActivatePunishmentEvent;
import me.kvalbrus.multibans.bukkit.events.CreatePunishmentEvent;
import me.kvalbrus.multibans.bukkit.events.DeactivatePunishmentEvent;
import me.kvalbrus.multibans.api.punishment.Punishment;
import me.kvalbrus.multibans.api.punishment.action.ActivationAction;
import me.kvalbrus.multibans.api.punishment.action.CreationAction;
import me.kvalbrus.multibans.api.punishment.action.DeactivationAction;
import me.kvalbrus.multibans.bukkit.commands.BanBukkit;
import me.kvalbrus.multibans.bukkit.commands.BanIpBukkit;
import me.kvalbrus.multibans.bukkit.commands.KickBukkit;
import me.kvalbrus.multibans.bukkit.commands.MuteChatBukkit;
import me.kvalbrus.multibans.bukkit.commands.PunishBukkit;
import me.kvalbrus.multibans.bukkit.commands.UnbanBukkit;
import me.kvalbrus.multibans.bukkit.commands.UnmuteChatBukkit;
import me.kvalbrus.multibans.bukkit.events.DeletePunishmentEvent;
import me.kvalbrus.multibans.bukkit.implementations.BukkitConsole;
import me.kvalbrus.multibans.bukkit.implementations.BukkitPlayer;
import me.kvalbrus.multibans.bukkit.implementations.BukkitOnlinePlayer;
import me.kvalbrus.multibans.bukkit.listeners.PlayerListener;
import me.kvalbrus.multibans.common.managers.MultiBansPluginManager;
import me.kvalbrus.multibans.common.utils.Message;
import me.kvalbrus.multibans.common.utils.ReplacedString;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import me.kvalbrus.multibans.api.punishment.punishments.PunishmentType;
import org.jetbrains.annotations.Nullable;

public class BukkitPluginManager extends MultiBansPluginManager {

    private final BukkitPlugin plugin;

    public BukkitPluginManager(@NotNull BukkitPlugin plugin) {
        super();
        this.plugin = plugin;
    }

    @Override
    public void enable() {
        this.plugin.getServer().getPluginManager().enablePlugin(this.plugin);
    }

    @Override
    public void disable() {
        this.plugin.getServer().getPluginManager().disablePlugin(this.plugin);
    }

    @Override
    public void onEnable() {
        MultiBansAPI multiBansAPIProvider = new MultiBansAPIBukkit(this);
        this.plugin.getServer().getServicesManager()
            .register(MultiBansAPI.class, multiBansAPIProvider, this.plugin, ServicePriority.Normal);
        this.registerCommands();
        this.registerListeners();

        audiences = BukkitAudiences.create(this.plugin);
        super.onEnable();
    }

    @Override
    public void onDisable() {
        super.onDisable();

        if (this.getDataProvider() != null) {
            this.getDataProvider().shutdown();
            // TODO: this.dataProvider = null;
        }

        if (audiences != null) {
            audiences.close();
            audiences = null;
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
    public @NotNull Console getConsole() {
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
    public void createPunishment(@NotNull Punishment punishment, @NotNull CreationAction action) {
        var player = this.plugin.getServer().getPlayer(punishment.getTarget().getUniqueId());
        if (player != null) {
            if (punishment.getType() == PunishmentType.BAN || punishment.getType() == PunishmentType.TEMP_BAN ||
                punishment.getType() == PunishmentType.BAN_IP || punishment.getType() == PunishmentType.TEMP_BAN_IP) {
                player.kickPlayer(this.getPunishmentManager()
                    .getBanMessage(punishment.getTarget().getUniqueId()));

            } else if (punishment.getType() == PunishmentType.KICK) {
                ReplacedString punishmentMessage = new ReplacedString(Message.KICK_ACTIVATE_TARGET.getText()).replacePunishment(punishment);
                player.kickPlayer(punishmentMessage.string());
            }
        }

        Event event = new CreatePunishmentEvent(punishment, action);

        this.plugin.getServer().getScheduler().runTask(this.plugin, () ->
            this.plugin.getServer().getPluginManager().callEvent(event)
        );
    }

    @Override
    public void activatePunishment(@NotNull Punishment punishment, @NotNull ActivationAction action) {
        var player = this.plugin.getServer().getPlayer(punishment.getTarget().getUniqueId());
        if (player != null) {
            if (punishment.getType() == PunishmentType.BAN || punishment.getType() == PunishmentType.TEMP_BAN ||
                punishment.getType() == PunishmentType.BAN_IP || punishment.getType() == PunishmentType.TEMP_BAN_IP) {
                player.kickPlayer(this.getPunishmentManager()
                    .getBanMessage(punishment.getTarget().getUniqueId()));

            } else if (punishment.getType() == PunishmentType.KICK) {
                ReplacedString punishmentMessage = new ReplacedString(Message.KICK_ACTIVATE_TARGET.getText()).replacePunishment(punishment);
                player.kickPlayer(punishmentMessage.string());
            }
        }

        Event event = new ActivatePunishmentEvent(punishment, action);

        this.plugin.getServer().getScheduler().runTask(this.plugin, () ->
            this.plugin.getServer().getPluginManager().callEvent(event)
        );
    }

    @Override
    public void deactivatePunishment(@NotNull Punishment punishment, @NotNull DeactivationAction action) {
        Event event = new DeactivatePunishmentEvent(punishment, action);

        this.plugin.getServer().getScheduler().runTask(this.plugin, () ->
            this.plugin.getServer().getPluginManager().callEvent(event)
        );
    }

    @Override
    public void deletePunishment(@NotNull Punishment punishment) {
        Event event = new DeletePunishmentEvent(punishment);

        this.plugin.getServer().getScheduler().runTask(this.plugin, () ->
            this.plugin.getServer().getPluginManager().callEvent(event)
        );
    }

    @NotNull
    public JavaPlugin getPlugin() {
        return this.plugin;
    }

    private void registerCommands() {
        new BanBukkit(this, this.plugin);
        new BanIpBukkit(this, this.plugin);
        new MuteChatBukkit(this, this.plugin);
        new KickBukkit(this, this.plugin);
        new UnbanBukkit(this, this.plugin);
        new UnmuteChatBukkit(this, this.plugin);
        new PunishBukkit(this, this.plugin);
        new me.kvalbrus.multibans.bukkit.commands.MultiBansBukkit(this, this.plugin);
        new HistoryBukkit(this, this.plugin);
        new IPHistoryBukkit(this, this.plugin);
        new IPCheckBukkit(this, this.plugin);
    }

    private void registerListeners() {
        this.plugin.getServer().getPluginManager().registerEvents(new PlayerListener(this), this.plugin);
    }

    private static BukkitAudiences audiences;
    public static BukkitAudiences getAudiences() {
        return audiences;
    }
}