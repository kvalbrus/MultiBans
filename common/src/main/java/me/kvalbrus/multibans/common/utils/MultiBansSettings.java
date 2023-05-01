package me.kvalbrus.multibans.common.utils;

import com.moandjiezana.toml.Toml;
import com.moandjiezana.toml.TomlWriter;
import java.util.ArrayList;
import java.util.List;
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
    private long idSize;

    @Getter
    private boolean maskIp;

    @Getter
    private List<String> chatMuteCommands;

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

        long idSize = toml.getLong("IdSize", 6L);
        if (idSize > 0 && idSize < 100) {
            this.idSize = 6L;
        }

        this.maskIp = toml.getBoolean("MaskIp", true);
        this.chatMuteCommands = toml.getList("ChatMuteCommands", new ArrayList<>());

        map.put("IdSize", this.idSize);
        map.put("MaskIp", this.maskIp);
        map.put("ChatMuteCommands", this.chatMuteCommands);

        try {
            tomlWriter.write(map, file);
        } catch (IOException exception) {
            return this;
        }

        return this;
    }
}