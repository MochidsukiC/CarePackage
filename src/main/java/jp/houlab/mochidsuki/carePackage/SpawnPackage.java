package jp.houlab.mochidsuki.carePackage;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.ChestBoat;
import org.bukkit.loot.LootTable;
import org.bukkit.scheduler.BukkitRunnable;


import java.util.Random;

import static jp.houlab.mochidsuki.carePackage.CarePackage.config;
import static jp.houlab.mochidsuki.carePackage.CarePackage.plugin;

public class SpawnPackage {
    /**
     * ケアパッケージをスポーン
     * @param table ルートテーブル
     * @param location
     */
    public void spawn(LootTable table, Location location) {
        ChestBoat carePackage = location.getWorld().spawn(location, ChestBoat.class);
        carePackage.setLootTable(table);

    }
    public void spawn(int tier, Location location) {
        if(config.getString("PackageLootTable.Tier"+tier) != null) {
            spawn(plugin.getServer().getLootTable(NamespacedKey.minecraft(config.getString("PackageLootTable.Tier" + tier))), location);
        }
    }

    public void randomSpawn(LootTable table, Location pos1, Location pos2) {
        int x1 = pos1.getBlockX();
        int z1 = pos1.getBlockZ();

        int x2 = pos2.getBlockX();
        int z2 = pos2.getBlockZ();
        Random random = new Random();
        int randomX = random.nextInt(x2 - x1 + 1) + x1;
        int randomZ = random.nextInt(z2 - z1 + 1) + z1;
        int y = (pos1.getBlockY() + pos2.getBlockY())/2;
        spawn(table,new Location(pos1.getWorld(), randomX, y, randomZ));
    }

    public void randomSpawn(int tier, Location pos1, Location pos2) {
        if(config.getString("PackageLootTable.Tier"+tier) != null) {
            LootTable table = (plugin.getServer().getLootTable(NamespacedKey.minecraft(config.getString("PackageLootTable.Tier" + tier))), location);
            randomSpawn(table, pos1, pos2);
        }
    }


    public void spawnFallingParticle(ChestBoat carePackage) {
        new BukkitRunnable() {
            public void run() {
                //降下中のパーティクル

                //着陸検知
                if(!carePackage.getLocation().add(0,-1,0).getBlock().getType().equals(Material.AIR)){

                    cancel();
                }
            }
        }.runTaskTimer(plugin,0,1);
    }
}
