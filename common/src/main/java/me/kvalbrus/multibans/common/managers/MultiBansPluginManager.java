package me.kvalbrus.multibans.common.managers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import me.kvalbrus.multibans.api.DataProvider;
import me.kvalbrus.multibans.api.OnlinePlayer;
import me.kvalbrus.multibans.api.managers.SessionManager;
import me.kvalbrus.multibans.common.session.MultiSession;
import me.kvalbrus.multibans.common.storage.DataProviderSettings;
import me.kvalbrus.multibans.common.storage.DataProviderType;
import me.kvalbrus.multibans.common.storage.mysql.MySqlProvider;
import me.kvalbrus.multibans.common.utils.Message;
import me.kvalbrus.multibans.common.utils.MultiBansSettings;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class MultiBansPluginManager implements PluginManager {

    private final MultiPunishmentManager punishmentManager;

    private final SessionManager sessionManager;

    private DataProvider dataProvider;

    private MultiBansSettings settings;

    private Map<UUID, MultiSession> playerSessions = new HashMap<>();

    public MultiBansPluginManager() {
        this.punishmentManager = new MultiPunishmentManager(this);
        this.sessionManager = new MultiSessionManager(this);
    }

    @Override
    public final void onLoad() {
        if (!this.getDataFolder().exists()) {
            this.getDataFolder().mkdirs();
        }

        this.loadSettings();
        this.loadMessages();
    }

    @Override
    public void onEnable() {
        if (!this.loadDataProvider()) {
            this.disable();
        }
    }

    @Override
    public void onDisable() {
        for (var session : this.playerSessions.values()) {
            session.quit();

            try {
                this.dataProvider.createPlayerSession(session);
            } catch (Exception exception) {}
        }

        this.playerSessions = new HashMap<>();
    }

    @Override
    public final void reload() {
        this.onDisable();
        this.onLoad();
        this.onEnable();
    }

    @Nullable
    public final DataProvider getDataProvider() {
        return this.dataProvider;
    }

    @NotNull
    public final MultiBansSettings getSettings() {
        return this.settings;
    }

    @NotNull
    public final MultiPunishmentManager getPunishmentManager() {
        return this.punishmentManager;
    }

    @NotNull
    @Override
    public SessionManager getSessionManager() {
        return this.sessionManager;
    }

    public void playerJoin(OnlinePlayer player) {
        MultiSession session = new MultiSession(player);
        session.join();
        this.playerSessions.put(player.getUniqueId(), session);
    }

    public void playerQuit(OnlinePlayer player) {
        MultiSession session = this.playerSessions.get(player.getUniqueId());
        if (session != null) {
            session.quit();
            this.playerSessions.remove(player.getUniqueId());

            try {
                this.dataProvider.createPlayerSession(session);
            } catch (Exception e) {
            }
        }
    }

    private boolean loadDataProvider() {
        DataProviderSettings dataProviderSettings = new DataProviderSettings(this).load();

        if (dataProviderSettings.getType() == DataProviderType.MYSQL || dataProviderSettings.getType() == DataProviderType.MARIADB) {
            this.dataProvider = new MySqlProvider(this, dataProviderSettings);
            try {
                this.dataProvider.initialization();
            } catch (Exception exception) {
                exception.printStackTrace();
                this.dataProvider = null;
            }
        }

        return this.dataProvider != null;
    }

    private void loadSettings() {
        this.settings = new MultiBansSettings(this).load();
    }

    private void loadMessages() {
        final File messages = new File(this.getDataFolder(), "messages_en.properties");
        if (!messages.exists()) {
            try {
                if (!messages.createNewFile()) {
                    return;
                }
            } catch (IOException exception) {
                // TODO: Logger
            }
        }

        try (InputStream input = new FileInputStream(messages)) {
            final var properties = new Properties();
            properties.load(new StringReader(new String(input.readAllBytes(), StandardCharsets.UTF_8)));

            for (final Message message : Message.values()) {
                if (properties.getProperty(message.getKey()) != null) {
                    message.setText(properties.getProperty(message.getKey()));
                } else {
                    properties.put(message.getKey(), message.getText());
                }
            }

            try (OutputStream output = new FileOutputStream(messages);
                Writer writer = new OutputStreamWriter(output, StandardCharsets.UTF_8)){
                properties.store(writer, null);
            }

        } catch (IOException exception) {
            // TODO: Logger
        }
    }
}