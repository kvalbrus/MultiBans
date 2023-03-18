package me.kvalbrus.multibans.bukkit;

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;
import lombok.Getter;
import me.kvalbrus.multibans.common.managers.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class BukkitPlugin extends JavaPlugin {

    @Getter
    private static BukkitPlugin instance;

    @Getter
    private PluginManager pluginManager;

    @Override
    public void onLoad() {
        instance = this;
        this.pluginManager = new PluginManagerBukkit(this);

        this.pluginManager.onLoad();
    }

    @Override
    public void onEnable() {
        this.pluginManager.onEnable();
    }

    @Override
    public void onDisable() {
        this.pluginManager.onDisable();
    }
}