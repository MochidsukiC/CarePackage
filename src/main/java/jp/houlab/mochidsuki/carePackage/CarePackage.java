package jp.houlab.mochidsuki.carePackage;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
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

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if(cmd.getName().equalsIgnoreCase("carepackage")) {
            if(args.length >= 4) {
                SpawnPackage.spawn(Integer.parseInt(args[0]),new Location(((Player) sender).getLocation().getWorld(),Integer.parseInt(args[1]),Integer.parseInt(args[2]),Integer.parseInt(args[3])));
            }
            SpawnPackage.spawn(Integer.parseInt(args[0]),((Player) sender).getLocation());
        }
        return true;
    }
}
