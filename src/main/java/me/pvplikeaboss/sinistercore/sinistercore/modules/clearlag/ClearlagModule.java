package me.pvplikeaboss.sinistercore.sinistercore.modules.clearlag;

import me.pvplikeaboss.sinistercore.sinistercore.Instances;
import me.pvplikeaboss.sinistercore.sinistercore.SinisterCore;
import me.pvplikeaboss.sinistercore.sinistercore.utilites.misc.Time;
import me.pvplikeaboss.sinistercore.sinistercore.utilites.serverutils.BroadcastUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.Date;

public class ClearlagModule {
    private static SinisterCore plugin;
    private static BroadcastUtils utilBroadcast;
    private static BukkitTask taskId;
    private static BukkitTask messageTaskId;
    private static int running;

    public static EntityType clearEntityType[] = {
            EntityType.BAT,
            EntityType.BLAZE,
            EntityType.CAVE_SPIDER,
            EntityType.CHICKEN,
            EntityType.COW,
            EntityType.CREEPER,
            EntityType.ENDERMAN,
            EntityType.ENDERMITE,
            EntityType.GHAST,
            EntityType.GUARDIAN,
            EntityType.HORSE,
            EntityType.IRON_GOLEM,
            EntityType.MUSHROOM_COW,
            EntityType.MAGMA_CUBE,
            EntityType.EGG,
            EntityType.OCELOT,
            EntityType.PIG,
            EntityType.PIG_ZOMBIE,
            EntityType.RABBIT,
            EntityType.SHEEP,
            EntityType.SILVERFISH,
            EntityType.SKELETON,
            EntityType.SLIME,
            EntityType.WITHER,
            EntityType.ZOMBIE,
            EntityType.WOLF,
            EntityType.WITCH,
            EntityType.VILLAGER,
            EntityType.SQUID,
            EntityType.SPIDER,
            EntityType.SNOWMAN
    };

    public static boolean enabled = true;

    public static void load(SinisterCore p) {
        plugin = p;
        utilBroadcast = (BroadcastUtils) Instances.getInstance(Instances.InstanceType.Utilities, 4);
    }

    private static void startWarningMessages(Date timeOfClear) {
        messageTaskId = (BukkitTask) new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    utilBroadcast.rawBroadcast("&9Sinister&3Lagg &8>> &bClearing entities from server in &960s&b!");
                    Thread.sleep(1000*30);
                    utilBroadcast.rawBroadcast("&9Sinister&3Lagg &8>> &bClearing entities from server in &930s&b!");
                    Thread.sleep(1000*15);
                    utilBroadcast.rawBroadcast("&9Sinister&3Lagg &8>> &bClearing entities from server in &915s&b!");
                    Thread.sleep(1000*(15-3));
                    utilBroadcast.rawBroadcast("&9Sinister&3Lagg &8>> &bClearing entities from server in &93s&b!");
                    Thread.sleep(1000*1);
                    utilBroadcast.rawBroadcast("&9Sinister&3Lagg &8>> &bClearing entities from server in &92s&b!");
                    Thread.sleep(1000*1);
                    utilBroadcast.rawBroadcast("&9Sinister&3Lagg &8>> &bClearing entities from server in &91s&b!");
                    Thread.sleep(1000*1);
                    utilBroadcast.rawBroadcast("&9Sinister&3Lagg &8>> &bCleared &9"+clearLag(true)+"&b entities");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }.runTaskLaterAsynchronously(plugin, (Time.getTimeLeft(timeOfClear)-60)*20L);
    }

    public static boolean getRunning() {
        if(running == 1) return true;
        return false;
    }

    public static void stopClearLagModule() {
        taskId.cancel();
        running = 0;
    }

    public static boolean startClearLagModule() {
        if(plugin.getConfig().isSet("features.clearlag.enabled")) {
            int delay = plugin.getConfig().getInt("features.clearlag.delay");
            running = 1;
            taskId = (BukkitTask) new BukkitRunnable() {
             @Override
             public void run() {
                 startWarningMessages(Time.addDateSeconds(delay));
             }
            }.runTaskTimer(plugin, 600L, delay*20L);
        }
        return true;
    }

    public static int clearLag(boolean deep) {
        int removed = 0;
        for(World  world : plugin.getServer().getWorlds()) {
            for(Entity entity : world.getEntities()) {
                if(entity.getType() == EntityType.PLAYER) {
                    continue;
                }
                if(entity.isOnGround()) {
                    entity.remove();
                    removed += 1;
                    continue;
                }
                if(deep == true) {
                    for(EntityType type : clearEntityType) {
                        if(entity.getType() == type) {
                            entity.remove();
                            removed += 1;
                            break;
                        }
                    }
                }
                continue;
            }
        }

        return removed;
    }
}
