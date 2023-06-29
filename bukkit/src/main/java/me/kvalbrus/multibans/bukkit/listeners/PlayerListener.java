package me.kvalbrus.multibans.bukkit.listeners;

import me.kvalbrus.multibans.api.OnlinePlayer;
import me.kvalbrus.multibans.bukkit.implementations.BukkitOnlinePlayer;
import me.kvalbrus.multibans.common.managers.MultiBansPluginManager;
import me.kvalbrus.multibans.common.permissions.Permission;
import me.kvalbrus.multibans.common.utils.Message;
import me.kvalbrus.multibans.common.utils.ReplacedString;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent.Result;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;

public class PlayerListener implements Listener {

    private final MultiBansPluginManager pluginManager;

    public PlayerListener(@NotNull MultiBansPluginManager pluginManager) {
        this.pluginManager = pluginManager;
    }

    @EventHandler
    public void onJoinEvent(AsyncPlayerPreLoginEvent event) {
        if (this.pluginManager.getPunishmentManager().hasActiveBan(event.getUniqueId())) {
            var message = this.pluginManager.getPunishmentManager().getBanMessage(event.getUniqueId());
            event.disallow(Result.KICK_BANNED, LegacyComponentSerializer.legacySection().serialize(Component.text(message)));
        } else if (this.pluginManager.getPunishmentManager().hasActiveBanIp(event.getAddress().getHostAddress())) {
            var message = this.pluginManager.getPunishmentManager().getBanIPMessage(event.getAddress().getHostAddress());
            event.disallow(Result.KICK_BANNED, LegacyComponentSerializer.legacySection().serialize(Component.text(message)));
        }
    }

    @EventHandler
    public void onCmdEvent(PlayerCommandPreprocessEvent event) {
        var message = event.getMessage();
        var player = event.getPlayer();

        if (this.pluginManager.getPunishmentManager().hasActiveChatMute(player.getUniqueId())) {
            for (var command : this.pluginManager.getSettings().getChatMuteCommands()) {
                if (message.startsWith("/" + command)) {
                    event.setCancelled(true);

                    player.sendMessage(this.pluginManager.getPunishmentManager().getMuteMessage(player.getUniqueId()));

                    ReplacedString listenMessage = new ReplacedString(Message.MUTECHAT_TRY_LISTEN.getText());

                    for (var listener : this.pluginManager.getOnlinePlayers()) {
                        if (listener.hasPermission(Permission.PUNISHMENT_MUTECHAT_LISTEN.getPerm())) {
                            listener.sendMessage(listenMessage.string());
                        }
                    }

                    this.pluginManager.getConsole().sendMessage(listenMessage.string());
                    break;
                }
            }
        }
    }

    @EventHandler
    public void onChatEvent(AsyncPlayerChatEvent event) {
        var player = event.getPlayer();

        if (this.pluginManager.getPunishmentManager().hasActiveChatMute(player.getUniqueId())) {
            event.setCancelled(true);

            player.sendMessage(this.pluginManager.getPunishmentManager().getMuteMessage(player.getUniqueId()));

            ReplacedString listenMessage = new ReplacedString(Message.MUTECHAT_TRY_LISTEN.getText());

            for (var listener : this.pluginManager.getOnlinePlayers()) {
                if (listener.hasPermission(Permission.PUNISHMENT_MUTECHAT_LISTEN.getPerm())) {
                    listener.sendMessage(listenMessage.string());
                }
            }

            this.pluginManager.getConsole().sendMessage(listenMessage.string());
        }
    }

    @EventHandler
    public void onJoinEvent(PlayerJoinEvent event) {
        OnlinePlayer player = new BukkitOnlinePlayer(event.getPlayer());
        this.pluginManager.playerJoin(player);
    }

    @EventHandler
    public void onQuitEvent(PlayerQuitEvent event) {
        OnlinePlayer player = new BukkitOnlinePlayer(event.getPlayer());
        this.pluginManager.playerQuit(player);
    }
}