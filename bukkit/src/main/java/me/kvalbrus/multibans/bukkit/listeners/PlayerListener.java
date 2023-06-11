package me.kvalbrus.multibans.bukkit.listeners;

import java.util.ArrayList;
import java.util.List;
import me.kvalbrus.multibans.api.OnlinePlayer;
import me.kvalbrus.multibans.api.punishment.Punishment;
import me.kvalbrus.multibans.api.punishment.punishments.PunishmentType;
import me.kvalbrus.multibans.api.punishment.punishments.PermanentlyBan;
import me.kvalbrus.multibans.api.punishment.punishments.PermanentlyBanIp;
import me.kvalbrus.multibans.api.punishment.punishments.PermanentlyChatMute;
import me.kvalbrus.multibans.api.punishment.punishments.TemporaryBan;
import me.kvalbrus.multibans.api.punishment.punishments.TemporaryBanIp;
import me.kvalbrus.multibans.api.punishment.punishments.TemporaryChatMute;
import me.kvalbrus.multibans.bukkit.implementations.BukkitOnlinePlayer;
import me.kvalbrus.multibans.common.managers.MultiBansPluginManager;
import me.kvalbrus.multibans.common.managers.PluginManager;
import me.kvalbrus.multibans.common.managers.PunishmentManager;
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

    private final PluginManager pluginManager;

    public PlayerListener(@NotNull PluginManager pluginManager) {
        this.pluginManager = pluginManager;
    }

    @EventHandler
    public void onJoinEvent(AsyncPlayerPreLoginEvent event) {
        PunishmentManager punManager = (PunishmentManager) pluginManager.getPunishmentManager();

        try {
            if (punManager.hasActiveBan(event.getUniqueId()) ||
                punManager.hasActiveBanIp(event.getUniqueId())) {
                List<Punishment> activeBans = new ArrayList<>();

                activeBans.addAll(this.pluginManager.getPunishmentManager()
                    .getActivePunishments(event.getUniqueId(), PermanentlyBanIp.class));

                activeBans.addAll(this.pluginManager.getPunishmentManager()
                    .getActivePunishments(event.getUniqueId(), PermanentlyBan.class));

                activeBans.addAll(this.pluginManager.getPunishmentManager()
                    .getActivePunishments(event.getUniqueId(), TemporaryBanIp.class));

                activeBans.addAll(this.pluginManager.getPunishmentManager()
                    .getActivePunishments(event.getUniqueId(), TemporaryBan.class));

                StringBuilder punishmentsString = new StringBuilder();
                for (Punishment punishment : activeBans) {
                    ReplacedString listenMessage = null;

                    if (punishment.getType() == PunishmentType.BAN_IP) {
                        punishmentsString.append(
                            new ReplacedString(Message.TITLE_BANIP.getMessage()).replacePunishment(
                                punishment).string());
                        listenMessage = new ReplacedString(Message.BANIP_TRY_LISTEN.getMessage())
                            .replacePunishment(punishment);
                    } else if (punishment.getType() == PunishmentType.BAN) {
                        punishmentsString.append(
                            new ReplacedString(Message.TITLE_BAN.getMessage()).replacePunishment(
                                punishment).string());
                        listenMessage = new ReplacedString(Message.BAN_TRY_LISTEN.getMessage())
                            .replacePunishment(punishment);
                    } else if (punishment.getType() == PunishmentType.TEMP_BAN_IP) {
                        punishmentsString.append(
                            new ReplacedString(
                                Message.TITLE_TEMPBANIP.getMessage()).replacePunishment(
                                punishment).string());
                        listenMessage = new ReplacedString(
                            Message.TEMPBANIP_TRY_LISTEN.getMessage())
                            .replacePunishment(punishment);
                    } else if (punishment.getType() == PunishmentType.TEMP_BAN) {
                        punishmentsString.append(
                            new ReplacedString(
                                Message.TITLE_TEMPBAN.getMessage()).replacePunishment(
                                punishment).string());
                        listenMessage = new ReplacedString(Message.TEMPBAN_TRY_LISTEN.getMessage())
                            .replacePunishment(punishment);
                    }

                    if (listenMessage != null) {
                        for (var listener : this.pluginManager.getOnlinePlayers()) {
                            listener.sendMessage(listenMessage.string());
                        }

                        this.pluginManager.getConsole().sendMessage(listenMessage.string());
                    }
                }

                ReplacedString title = new ReplacedString(Message.TITLE_HEADER.getMessage() +
                    punishmentsString + Message.TITLE_FOOTER.getMessage());

                event.disallow(Result.KICK_BANNED, LegacyComponentSerializer.legacySection()
                    .serialize(Component.text(title.string())));
            }
        } catch (Exception exception) {
            // TODO:
        }
    }

    @EventHandler
    public void onCmdEvent(PlayerCommandPreprocessEvent event) {
        var message = event.getMessage();
        var player = event.getPlayer();

        try {
            for (var command : ((MultiBansPluginManager) this.pluginManager).getSettings()
                .getChatMuteCommands()) {
                if (message.startsWith("/" + command)) {
                    if (this.pluginManager.getPunishmentManager()
                        .hasActiveChatMute(player.getUniqueId())) {
                        event.setCancelled(true);

                        synchronized (Punishment.class) {
                            List<PermanentlyChatMute> permChatMutes = this.pluginManager.getPunishmentManager()
                                .getActivePunishments(player.getUniqueId(),
                                    PermanentlyChatMute.class);

                            if (!permChatMutes.isEmpty()) {
                                for (Punishment mute : permChatMutes) {
                                    try {
                                        ReplacedString playerMessage = new ReplacedString(
                                            Message.MUTECHAT_TRY_TARGET.getMessage())
                                            .replacePunishment(mute);

                                        new BukkitOnlinePlayer(player).sendMessage(
                                            playerMessage.string());

                                        ReplacedString listenMessage = new ReplacedString(
                                            Message.MUTECHAT_TRY_LISTEN.getMessage())
                                            .replacePunishment(mute);

                                        for (var listener : this.pluginManager.getOnlinePlayers()) {
                                            listener.sendMessage(listenMessage.string());
                                        }

                                        this.pluginManager.getConsole()
                                            .sendMessage(listenMessage.string());
                                    } catch (ArrayIndexOutOfBoundsException exception) {
                                        // This exception calls, if punishment was cancelled or deleted.
                                        return;
                                    }
                                }

                                return;
                            }

                            List<TemporaryChatMute> tempChatMutes = this.pluginManager.getPunishmentManager()
                                .getActivePunishments(player.getUniqueId(),
                                    TemporaryChatMute.class);

                            if (!tempChatMutes.isEmpty()) {
                                for (Punishment tempMute : tempChatMutes) {
                                    try {
                                        ReplacedString playerMessage = new ReplacedString(
                                            Message.TEMPMUTECHAT_TRY_TARGET.getMessage())
                                            .replacePunishment(tempMute);

                                        new BukkitOnlinePlayer(player).sendMessage(
                                            playerMessage.string());

                                        ReplacedString listenMessage = new ReplacedString(
                                            Message.TEMPMUTECHAT_TRY_LISTEN.getMessage())
                                            .replacePunishment(tempMute);

                                        for (var listener : this.pluginManager.getOnlinePlayers()) {
                                            listener.sendMessage(listenMessage.string());
                                        }

                                        this.pluginManager.getConsole()
                                            .sendMessage(listenMessage.string());
                                    } catch (ArrayIndexOutOfBoundsException exception) {
                                        // This exception calls, if punishment was cancelled or deleted.
                                        return;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception exception) {
            // TODO:
        }
    }

    @EventHandler
    public void onChatEvent(AsyncPlayerChatEvent event) {
        var player = event.getPlayer();

        try {
            if (this.pluginManager.getPunishmentManager().hasActiveChatMute(player.getUniqueId())) {
                event.setCancelled(true);

                synchronized (Punishment.class) {
                    List<PermanentlyChatMute> permChatMutes = this.pluginManager.getPunishmentManager()
                        .getActivePunishments(player.getUniqueId(), PermanentlyChatMute.class);

                    if (!permChatMutes.isEmpty()) {
                        for (Punishment mute : permChatMutes) {
                            try {
                                ReplacedString playerMessage = new ReplacedString(
                                    Message.MUTECHAT_TRY_TARGET.getMessage())
                                    .replacePunishment(mute);

                                new BukkitOnlinePlayer(player).sendMessage(
                                    playerMessage.string());

                                ReplacedString listenMessage = new ReplacedString(
                                    Message.MUTECHAT_TRY_LISTEN.getMessage())
                                    .replacePunishment(mute);

                                for (var listener : this.pluginManager.getOnlinePlayers()) {
                                    listener.sendMessage(listenMessage.string());
                                }

                                this.pluginManager.getConsole().sendMessage(listenMessage.string());
                            } catch (ArrayIndexOutOfBoundsException exception) {
                                // This exception calls, if punishment was cancelled or deleted.
                                return;
                            }
                        }

                        return;
                    }

                    List<TemporaryChatMute> tempChatMutes = this.pluginManager.getPunishmentManager()
                        .getActivePunishments(player.getUniqueId(), TemporaryChatMute.class);

                    if (!tempChatMutes.isEmpty()) {
                        for (Punishment tempMute : tempChatMutes) {
                            try {
                                ReplacedString playerMessage = new ReplacedString(
                                    Message.TEMPMUTECHAT_TRY_TARGET.getMessage())
                                    .replacePunishment(tempMute);

                                new BukkitOnlinePlayer(player).sendMessage(
                                    playerMessage.string());

                                ReplacedString listenMessage = new ReplacedString(
                                    Message.TEMPMUTECHAT_TRY_LISTEN.getMessage())
                                    .replacePunishment(tempMute);

                                for (var listener : this.pluginManager.getOnlinePlayers()) {
                                    listener.sendMessage(listenMessage.string());
                                }

                                this.pluginManager.getConsole().sendMessage(listenMessage.string());
                            } catch (ArrayIndexOutOfBoundsException exception) {
                                // This exception calls, if punishment was cancelled or deleted.
                                return;
                            }
                        }
                    }
                }
            }
        } catch (Exception exception) {
            // TODO:
        }
    }

    @EventHandler
    public void onJoinEvent(PlayerJoinEvent event) {
        OnlinePlayer player = new BukkitOnlinePlayer(event.getPlayer());
        ((MultiBansPluginManager) this.pluginManager).playerJoin(player);
    }

    @EventHandler
    public void onQuitEvent(PlayerQuitEvent event) {
        OnlinePlayer player = new BukkitOnlinePlayer(event.getPlayer());
        ((MultiBansPluginManager) this.pluginManager).playerQuit(player);
    }
}