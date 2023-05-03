package me.kvalbrus.multibans.common.storage;

import com.moandjiezana.toml.Toml;
import com.moandjiezana.toml.TomlWriter;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import lombok.Getter;
import me.kvalbrus.multibans.common.managers.PluginManager;
import org.jetbrains.annotations.NotNull;

import static me.kvalbrus.multibans.common.storage.DataProviderType.MY_SQL;

public class DataProviderSettings {

    private static final String PATH = "data_provider.toml";

    private final PluginManager pluginManager;

    @Getter
    private DataProviderType type = MY_SQL;

    @Getter
    private Properties properties;

    public DataProviderSettings(@NotNull PluginManager pluginManager) {
        this.pluginManager = pluginManager;
        this.properties = new Properties();
    }

    @NotNull
    public synchronized DataProviderSettings load() {
        File file = new File(this.pluginManager.getDataFolder(), PATH);

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException exception) {
                return this;
            }
        }

        Toml toml = new Toml().read(file);
        TomlWriter tomlWriter = new TomlWriter.Builder().build();

        Map<String, Object> map = new HashMap<>();


        DataProviderType type = DataProviderType.getType(toml.getString("name"));
        if (type != null) {
            this.type = type;
        }

        this.properties.setProperty("dataSource.databaseName", toml.getString("databaseName") != null ?
            toml.getString("databaseName") : "MultiPunishments");
        this.properties.setProperty("dataSource.serverName", toml.getString("address") != null ?
            toml.getString("address") : "localhost");
        long port = (toml.getLong("port") != null && toml.getLong("port") > 0) ? toml.getLong("port") : 3306L;
        this.properties.setProperty("dataSource.portNumber", String.valueOf(port ));
        this.properties.setProperty("dataSource.user", toml.getString("user") != null ?
            toml.getString("user") : "");
        this.properties.setProperty("dataSource.password", toml.getString("password") != null ?
            toml.getString("password") : "");

        map.put("name", this.type.getName());
        map.put("databaseName", this.properties.getProperty("dataSource.databaseName"));
        map.put("address", this.properties.getProperty("dataSource.serverName"));
        map.put("port", port);
        map.put("user", this.properties.getProperty("dataSource.user"));
        map.put("password", this.properties.getProperty("dataSource.password"));

        try {
            tomlWriter.write(map, file);
        } catch (IOException exception) {
            return this;
        }

        return this;
    }
}