package me.pvplikeaboss.sinistercore.sinistercore.events;

import me.pvplikeaboss.sinistercore.sinistercore.SinisterCore;
import me.pvplikeaboss.sinistercore.sinistercore.objects.PlayerObject;
import me.pvplikeaboss.sinistercore.sinistercore.utilites.misc.Time;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.spigotmc.event.player.PlayerSpawnLocationEvent;

import java.util.Date;

public class DamageEvents implements Listener {
    private final SinisterCore plugin;

    public DamageEvents(SinisterCore p) {
        this.plugin = p;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        PlayerObject player = plugin.getPlayer(e.getEntity().getUniqueId());
        player.setLastPlayerDeathLocation(e.getEntity().getLocation());
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent e) {
        if(plugin.getConfig().getBoolean("features.teleport.enabled") && plugin.getConfig().isSet("spawn")) {
            double x = plugin.getConfig().getDouble("spawn.x");
            double y = plugin.getConfig().getDouble("spawn.y");
            double z = plugin.getConfig().getDouble("spawn.z");
            float yaw = (float) plugin.getConfig().getDouble("spawn.yaw");
            float pitch = (float) plugin.getConfig().getDouble("spawn.pitch");
            World world = plugin.getServer().getWorld(plugin.getConfig().getString("spawn.world"));
            Location to = new Location(world, x, y, z, yaw, pitch);
            e.setRespawnLocation(to);
        }
    }

    @EventHandler
    public void onPlayerSpawn(PlayerSpawnLocationEvent e) {
        if(plugin.getConfig().getBoolean("features.teleport.enabled") && plugin.getConfig().isSet("spawn")) {
            double x = plugin.getConfig().getDouble("spawn.x");
            double y = plugin.getConfig().getDouble("spawn.y");
            double z = plugin.getConfig().getDouble("spawn.z");
            float yaw = (float) plugin.getConfig().getDouble("spawn.yaw");
            float pitch = (float) plugin.getConfig().getDouble("spawn.pitch");
            World world = plugin.getServer().getWorld(plugin.getConfig().getString("spawn.world"));
            Location to = new Location(world, x, y, z, yaw, pitch);
            e.setSpawnLocation(to);
        }
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent e) {
        if(e.getEntity() instanceof Player) {
            PlayerObject player = plugin.getPlayer(e.getEntity().getUniqueId());
            if(player.getIsGodMode()) {
                e.setCancelled(true);
                return;
            }
        }
    }
}
