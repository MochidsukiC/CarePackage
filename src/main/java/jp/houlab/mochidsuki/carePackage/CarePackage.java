package jp.houlab.mochidsuki.carePackage;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public final class CarePackage extends JavaPlugin {
    public static Plugin plugin;
    public static FileConfiguration config;

    @Override
    public void onEnable() {
        // Plugin startup logic
        plugin = this;
        saveDefaultConfig();
        config = getConfig();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
