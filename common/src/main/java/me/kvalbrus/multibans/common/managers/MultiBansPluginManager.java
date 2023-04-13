package me.kvalbrus.multibans.common.managers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.Properties;
import me.kvalbrus.multibans.api.DataProvider;
import me.kvalbrus.multibans.common.storage.DataProviderSettings;
import me.kvalbrus.multibans.common.storage.DataProviderType;
import me.kvalbrus.multibans.common.storage.mysql.MySqlProvider;
import me.kvalbrus.multibans.common.utils.Message;
import me.kvalbrus.multibans.common.utils.MultiBansSettings;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class MultiBansPluginManager implements PluginManager {

    private final me.kvalbrus.multibans.api.managers.PunishmentManager punishmentManager;

    private DataProvider dataProvider;

    private MultiBansSettings settings;

    public MultiBansPluginManager() {
        this.punishmentManager = new PunishmentManager(this);
    }

    @Override
    public final void onLoad() {
        DataProviderSettings dataProviderSettings = new DataProviderSettings(this).load();

        if (dataProviderSettings.getType() == DataProviderType.MY_SQL) {
            this.dataProvider = new MySqlProvider(this, dataProviderSettings);

            try {
                this.dataProvider.initialization();
            } catch (SQLException exception) {
                this.dataProvider = null;
            }
        }

        this.settings = new MultiBansSettings(this).load();

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
            properties.load(input);

            for (final Message message : Message.values()) {
                if (properties.getProperty(message.getKey()) != null) {
                    message.setMessage(properties.getProperty(message.getKey()));
                } else {
                    properties.put(message.getKey(), message.getMessage());
                }
            }

            try (OutputStream output = new FileOutputStream(messages)){
                properties.store(output, null);
            }

        } catch (IOException exception) {
            // TODO: Logger
        }
    }

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
    public final me.kvalbrus.multibans.api.managers.PunishmentManager getPunishmentManager() {
        return this.punishmentManager;
    }
}
