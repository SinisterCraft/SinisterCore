package me.pvplikeaboss.sinistercore.sinistercore.events;

import me.pvplikeaboss.sinistercore.sinistercore.Instances;
import me.pvplikeaboss.sinistercore.sinistercore.SinisterCore;
import me.pvplikeaboss.sinistercore.sinistercore.modules.punishment.Punishment;
import me.pvplikeaboss.sinistercore.sinistercore.objects.PlayerObject;
import me.pvplikeaboss.sinistercore.sinistercore.utilites.misc.Messages;
import me.pvplikeaboss.sinistercore.sinistercore.utilites.serverutils.BroadcastUtils;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public class JoinLeaveEvents implements Listener {

    private final SinisterCore plugin;
    private Punishment punish;
    private Economy ecoImplementer;
    private Messages utilMsgs;
    private BroadcastUtils utilBroadcast;

    public JoinLeaveEvents(SinisterCore p) {
        this.plugin = p;
        this.punish = (Punishment) Instances.getInstance(Instances.InstanceType.Punishment, -1);
        this.ecoImplementer = (Economy) Instances.getInstance(Instances.InstanceType.Economy, -1);
        this.utilMsgs = (Messages) Instances.getInstance(Instances.InstanceType.Utilities, 2);
        this.utilBroadcast = (BroadcastUtils) Instances.getInstance(Instances.InstanceType.Utilities, 4);
    }

    @EventHandler
    public void onPreJoin(AsyncPlayerPreLoginEvent e) {
        if(plugin.getConfig().getBoolean("features.punishment.enabled")) {
            UUID pUUID = e.getUniqueId();
            PlayerObject player = plugin.getPlayer(pUUID);

            if(player == null) {
                return;
            }

            if (player.isBanned()) {
                e.disallow(AsyncPlayerPreLoginEvent.Result.KICK_BANNED, this.punish.getBanMessage(player));
            }
        }
    }

    @EventHandler (priority = EventPriority.HIGHEST)
    public void onJoin(PlayerJoinEvent e) {
        PlayerObject player = plugin.getPlayer(e.getPlayer().getUniqueId());
        if(player == null) {
            plugin.refreshPlayersOffline();
            plugin.refreshPlayersOnline();
            player = plugin.getPlayer(e.getPlayer().getUniqueId());
            if(player == null)
            {
                player = new PlayerObject(plugin, e.getPlayer().getUniqueId());
                plugin.getPlayers().add(player);
            }
        }
        player.setIsPlayerOnline(true);

        //init vanish
        for(PlayerObject p : plugin.getPlayers()) {
            if(p.isPlayerOnline && p.getIsVanish()) {
                player.getPlayer().hidePlayer(p.getPlayer());
            }
        }

        if(player.getPlayer().hasPlayedBefore()) {
            utilBroadcast.rawBroadcast("&8[&a+&8] &6" + player.playerDisplayName + " &7Has Joined!");
            Location lastLogoutLocation = player.getLastPlayerLogoutLocation();
            if(lastLogoutLocation != null) {
                player.teleportPlayer(lastLogoutLocation, false);
            }
        } else {
            utilBroadcast.rawBroadcast("&8[&a+&8] &7Welcome &6" + player.playerDisplayName + " &7Has Joined For The First Time!");
            if(plugin.getConfig().getBoolean("features.teleport.enabled") && plugin.getConfig().isSet("spawn")) {
                double x = plugin.getConfig().getDouble("spawn.x");
                double y = plugin.getConfig().getDouble("spawn.y");
                double z = plugin.getConfig().getDouble("spawn.z");
                float yaw = (float) plugin.getConfig().getDouble("spawn.yaw");
                float pitch = (float) plugin.getConfig().getDouble("spawn.pitch");
                World world = plugin.getServer().getWorld(plugin.getConfig().getString("spawn.world"));
                Location to = new Location(world, x, y, z, yaw, pitch);
                player.teleportPlayer(to, false);
            }
        }

        if(plugin.getConfig().getBoolean("features.economy.enabled")) {
            if (ecoImplementer.getBalance(player.getOfflinePlayer()) == -1) {
                final EconomyResponse result = ecoImplementer.depositPlayer(player.getOfflinePlayer(), 5000);
                if(!result.transactionSuccess()) {
                    utilMsgs.logErrorMessage("Failed to set default balance on player "+player.playerName+"!");
                    utilMsgs.infoMessage(player, "&7Failed to set default balance. Contact staff!");
                }
            }
        }

        e.setJoinMessage("");
    }

    @EventHandler (priority = EventPriority.LOWEST)
    public void onLeave(PlayerQuitEvent e) {
        PlayerObject player = plugin.getPlayer(e.getPlayer().getUniqueId());

        player.setIsPlayerOnline(false);
        player.setIsVanish(false);
        player.setRecieveMsgs(true);
        player.setLastPlayerLogoutLocation(e.getPlayer().getLocation());

        plugin.getServer().broadcastMessage(ChatColor.translateAlternateColorCodes('&',
                "&8[&c-&8] &6" + player.playerDisplayName + " &7Has Left!"));
        e.setQuitMessage("");
    }
}
