package me.pvplikeaboss.sinistercore.sinistercore.events;

import me.pvplikeaboss.sinistercore.sinistercore.Instances;
import me.pvplikeaboss.sinistercore.sinistercore.SinisterCore;
import me.pvplikeaboss.sinistercore.sinistercore.objects.PlayerObject;
import me.pvplikeaboss.sinistercore.sinistercore.utilites.misc.Messages;
import me.pvplikeaboss.sinistercore.sinistercore.utilites.misc.Time;
import me.pvplikeaboss.sinistercore.sinistercore.utilites.serverutils.BroadcastUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Date;

public class CombatTagEvents implements Listener {
    private final SinisterCore plugin;
    private BroadcastUtils utilBroadcast;
    private Messages utilMsgs;

    public CombatTagEvents(SinisterCore p) {
        this.plugin = p;
        this.utilBroadcast = (BroadcastUtils) Instances.getInstance(Instances.InstanceType.Utilities, 4);
        this.utilMsgs = (Messages) Instances.getInstance(Instances.InstanceType.Utilities, 2);
    }

    @EventHandler
    public void onPlayerCommand(PlayerCommandPreprocessEvent e) {
        PlayerObject player = plugin.getPlayer(e.getPlayer().getUniqueId());
        if(player.getIsInCombat()) {
            Date end = player.getEndOfCombat();
            int timeLeft = Time.getTimeLeft(end);

            if(timeLeft == -1) {//player no longer in combat
                player.setIsInCombat(false, null);
                utilMsgs.infoMessage(player, "You are no longer in combat!");
                return;
            }
            if(plugin.getConfig().isSet("combat-log.allowed-cmds")) {
                for (String allowedCmd : plugin.getConfig().getConfigurationSection("combat-log.allowed-cmds").getKeys(false)) {
                    if (e.getMessage().equalsIgnoreCase(allowedCmd)) {
                        return;
                    }
                }
            }

            utilMsgs.errorMessage(player, "&9You are not allowed to send commands in combat!");
            utilMsgs.errorMessage(player, "&9Please wait &b"+timeLeft+" &9seconds.");
            e.setCancelled(true);
            return;
        }
    }

    @EventHandler
    public void onMoveCombatTag(PlayerMoveEvent e) {
        PlayerObject player = plugin.getPlayer(e.getPlayer().getUniqueId());
        if(player.getIsInCombat()) {
            Date end = player.getEndOfCombat();
            if(Time.getTimeLeft(end) == -1) {//player no longer in combat
                player.setIsInCombat(false, null);
                utilMsgs.infoMessage(player, "You are no longer in combat!");
            }
        }
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        PlayerObject player = plugin.getPlayer(e.getPlayer().getUniqueId());

        if (player.getIsInCombat()) {
            utilBroadcast.rawBroadcast("&9Sinister&6Combat &8>> &bPlayer &a" + player.playerDisplayName + " &bhas combat logged");
            player.getPlayer().setHealth(0);
            player.setIsInCombat(false, null);// reset combat after killing player
            //todo; leave IsInCombat true and when player joins back send a information message
        }
    }

    @EventHandler
    public void onPlayerDamageByPlayer(EntityDamageByEntityEvent e) {
        if(e.getEntity() instanceof Player && e.getDamager() instanceof Player) {
            PlayerObject player = plugin.getPlayer(e.getEntity().getUniqueId());
            PlayerObject attacker = plugin.getPlayer(((Player)e.getDamager()).getUniqueId());

            Date endOfCombat = Time.addDateSeconds(30);

            if(!player.getIsInCombat()) {
                player.setIsInCombat(true, endOfCombat);
                utilMsgs.infoMessage(player, "&9You are now in combat!");
            }

            if(!attacker.getIsInCombat()) {
                attacker.setIsInCombat(true, endOfCombat);
                utilMsgs.infoMessage(attacker, "&9You are now in combat!");
            }
        }
    }
}
