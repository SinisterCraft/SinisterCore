package me.pvplikeaboss.sinistercore.sinistercore.events;

import me.pvplikeaboss.sinistercore.sinistercore.Instances;
import me.pvplikeaboss.sinistercore.sinistercore.SinisterCore;
import me.pvplikeaboss.sinistercore.sinistercore.objects.PlayerObject;
import me.pvplikeaboss.sinistercore.sinistercore.utilites.misc.Messages;
import me.pvplikeaboss.sinistercore.sinistercore.utilites.misc.Time;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.Date;
import java.util.HashMap;

public class MovementEvents implements Listener {
    private SinisterCore plugin;
    private Messages utilMsgs;

    public MovementEvents(SinisterCore p) {
        this.plugin = p;
        this.utilMsgs = (Messages) Instances.getInstance(Instances.InstanceType.Utilities, 2);
    }

    @EventHandler
    public void onMoveTeleportCancel(PlayerMoveEvent e) {
        if(plugin.getConfig().getBoolean("features.teleport.enabled")) {
            HashMap<Integer, PlayerObject> teleportHashMap = (HashMap<Integer, PlayerObject>) Instances.getInstance(Instances.InstanceType.HashMap, 0);
            for (Integer i : teleportHashMap.keySet()) {
                PlayerObject player = teleportHashMap.get(i);
                if (player.playerUUID == e.getPlayer().getUniqueId()) {
                    teleportHashMap.remove(i);
                    Bukkit.getScheduler().cancelTask(i);
                    player.sendMessage("&4You Moved! &7Teleportation Canceled");
                }
            }
        }
    }
}
