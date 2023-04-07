package me.kvalbrus.multibans.common.utils;

import com.moandjiezana.toml.Toml;
import com.moandjiezana.toml.TomlWriter;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import me.kvalbrus.multibans.common.managers.PluginManager;
import org.jetbrains.annotations.NotNull;

public class MultiBansSettings {

    private static final String PATH = "settings.toml";

    private final PluginManager pluginManager;

    @Getter
    private long idSize = 6;

    @Getter
    private boolean maskIp = true;

    public MultiBansSettings(@NotNull PluginManager pluginManager) {
        this.pluginManager = pluginManager;
    }

    @NotNull
    public synchronized MultiBansSettings load() {
        File file = new File(pluginManager.getDataFolder(), PATH);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException exception) {
                // TODO: ???
                return this;
            }
        }

        Toml toml = new Toml().read(file);
        TomlWriter tomlWriter = new TomlWriter();

        Map<String, Object> map = new HashMap<>();

        if (toml.contains("idSize")) {
            this.idSize = toml.getLong("idSize");
        }
        map.put("isSize", this.idSize);

        if (toml.contains("maskIp")) {
            this.maskIp = toml.getBoolean("maskIp");
        }
        map.put("maskIp", this.maskIp);

        try {
            tomlWriter.write(map, file);
        } catch (IOException exception) {
            return this;
        }

        return this;
    }
}