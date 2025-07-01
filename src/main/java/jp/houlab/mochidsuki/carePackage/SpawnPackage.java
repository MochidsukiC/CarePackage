package jp.houlab.mochidsuki.carePackage;

import org.bukkit.*;
import org.bukkit.entity.ChestBoat;
import org.bukkit.loot.LootTable;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;
import org.bukkit.util.Vector;


import java.util.Random;

import static jp.houlab.mochidsuki.carePackage.CarePackage.config;
import static jp.houlab.mochidsuki.carePackage.CarePackage.plugin;

public class SpawnPackage {
    /**
     * ケアパッケージをスポーン
     * @param table ルートテーブル
     * @param location スポーン地点
     */
    static public void spawn(LootTable table, Location location) {
        ChestBoat carePackage = location.getWorld().spawn(location, ChestBoat.class);
        carePackage.setLootTable(table);
        carePackage.setGlowing(true);
        Scoreboard board = Bukkit.getScoreboardManager().getMainScoreboard();
        Team team;
        if(board.getTeam("CarePackage") == null) {
            team = board.registerNewTeam("CarePackage");
        }else {
            team = board.getTeam("CarePackage");
        }
        team.setColor(ChatColor.YELLOW);
        team.addEntry(carePackage.getUniqueId().toString());
        spawnFallingParticle(carePackage);
    }

    /**
     * ケアパッケージをスポーン
     * @param tier レア度
     * @param location スポーン地点
     */
    static public void spawn(int tier, Location location) {
        if(config.getString("PackageLootTable.Tier"+tier) != null) {
            spawn(plugin.getServer().getLootTable(NamespacedKey.fromString(config.getString("PackageLootTable.Tier" + tier))), location);
        }
    }

    /**
     * ランダムな地点にケアパッケージをスポーン
     * @param table ルートテーブル
     * @param pos1 スポーン範囲の始点
     * @param pos2 スポーン範囲の終点
     */
    static public void randomSpawn(LootTable table, Location pos1, Location pos2) {
        int x1 = pos1.getBlockX();
        int z1 = pos1.getBlockZ();

        int x2 = pos2.getBlockX();
        int z2 = pos2.getBlockZ();
        Random random = new Random();
        int randomX = 0;
        int randomZ = 0;
        for (int ii = 0; ii < Math.abs(pos1.getBlockX()-pos2.getBlockX()) * Math.abs(pos1.getBlockZ()-pos2.getBlockZ()); ii++) {
            randomX = random.nextInt(x2 - x1 + 1) + x1;
            randomZ = random.nextInt(z2 - z1 + 1) + z1;
            boolean foundObstacles = false;
            boolean foundFloor = false;
            for(int i = 320; i > config.getInt("Height.MAX"); i--) {
                if (pos1.getWorld().getBlockAt(randomX, i, randomZ).getType() != Material.AIR) {
                    foundObstacles = true;
                }
            }
            for(int i = config.getInt("Height.MIN"); i < config.getInt("Height.MAX"); i++) {
                if (pos1.getWorld().getBlockAt(randomX, i, randomZ).getType() != Material.AIR) {
                    foundFloor = true;
                }
            }
            if(!foundObstacles && foundFloor) {
                break;
            }

        }
        int y = (pos1.getBlockY() + pos2.getBlockY())/2;
        spawn(table,new Location(pos1.getWorld(), randomX, y, randomZ));
    }

    /**
     * ランダムな地点にケアパッケージをスポーン
     * @param tier レア度
     * @param pos1 スポーン範囲の始点
     * @param pos2 スポーン範囲の終点
     */
    static public void randomSpawn(int tier, Location pos1, Location pos2) {
        if(config.getString("PackageLootTable.Tier"+tier) != null) {
            LootTable table = (plugin.getServer().getLootTable(NamespacedKey.fromString(config.getString("PackageLootTable.Tier" + tier))));
            randomSpawn(table, pos1, pos2);
        }
    }

    /**
     * ランダムな地点にケアパッケージをスポーン
     * @param table ルートテーブル
     * @param center スポーン範囲の中心
     * @param radius スポーン範囲の半径
     */
    static public void randomSpawn(LootTable table, Location center, int radius) {
        Random random = new Random();
        int randomX = 0;
        int randomZ = 0;
        for (int ii = 0; ii < Math.abs(radius*radius); ii++) {
            randomX = random.nextInt(radius*2) - radius + center.getBlockX();
            randomZ = random.nextInt(radius*2) - radius + center.getBlockZ();
            boolean foundObstacles = false;
            boolean foundFloor = false;
            for(int i = 320; i > config.getInt("Height.MAX"); i--) {
                if (center.getWorld().getBlockAt(randomX, i, randomZ).getType() != Material.AIR) {
                    foundObstacles = true;
                }
            }
            for(int i = config.getInt("Height.MIN"); i < config.getInt("Height.MAX"); i++) {
                if (center.getWorld().getBlockAt(randomX, i, randomZ).getType() != Material.AIR) {
                    foundFloor = true;
                }
            }
            if(!foundObstacles && foundFloor) {
                break;
            }

        }

        spawn(table , new Location(center.getWorld(),randomX,center.getBlockY(),randomZ));
    }

    /**
     * ランダムな地点にケアパッケージをスポーン
     * @param tier レア度
     * @param center スポーン範囲の中心
     * @param radius スポーン範囲の半径
     */
    static public void randomSpawn(int tier, Location center, int radius) {
        if(config.getString("PackageLootTable.Tier"+tier) != null) {
            LootTable table = (plugin.getServer().getLootTable(NamespacedKey.fromString(config.getString("PackageLootTable.Tier" + tier))));
            randomSpawn(table, center, radius);
        }
    }

    /**
     * ケアパッケージに降下パーティクルを与える
     * @param carePackage ケアパッケージエンティティ
     */
    static public void spawnFallingParticle(ChestBoat carePackage) {
        new BukkitRunnable() {
            public void run() {
                //降下中のパーティクル
                Location loc = carePackage.getLocation();
                loc.getWorld().spawnParticle(Particle.DUST_COLOR_TRANSITION,loc,5,0.2,0.2,0.2,1, new Particle.DustTransition(Color.WHITE,Color.WHITE,2),true);
                //loc.getWorld().spawnParticle(Particle.DUST_COLOR_TRANSITION,loc,5,0.2,0.2,0.2,1, new Particle.DustTransition(Color.WHITE,Color.WHITE,2));
                loc.getWorld().spawnParticle(Particle.LAVA,loc,3,0.2,0.2,0.2,1,null,true);

                carePackage.setVelocity(new Vector(0,-0.2,0));

                //着陸検知
                if(!carePackage.getLocation().add(0,-1,0).getBlock().getType().equals(Material.AIR)){
                    loc.getWorld().spawnParticle(Particle.DUST_COLOR_TRANSITION,loc,5,0.2,0.2,0.2,4, new Particle.DustTransition(Color.WHITE,Color.WHITE,2));
                    cancel();
                }
            }
        }.runTaskTimer(plugin,0,1);
    }
}
