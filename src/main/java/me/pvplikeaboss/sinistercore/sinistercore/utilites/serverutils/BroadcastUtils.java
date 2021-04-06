package me.pvplikeaboss.sinistercore.sinistercore.utilites.serverutils;

import me.pvplikeaboss.sinistercore.sinistercore.SinisterCore;
import me.pvplikeaboss.sinistercore.sinistercore.objects.PlayerObject;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

public class BroadcastUtils {
    private SinisterCore plugin = null;

    public BroadcastUtils(SinisterCore p) {
        plugin = p;
    }

    private String broadcast_prefix = "&9&lSinister&b&lBroadcast &8>> &f";
    private String alert_prefix =  "&c&lAlert &8>> &f";
    //
    // General broadcast
    //

    public void broadcast(String message) {
        String buffer = ChatColor.translateAlternateColorCodes('&', broadcast_prefix+message);

        List<UUID> alreadySent = new ArrayList<>();
        for(PlayerObject player : plugin.getPlayers()) {
            boolean sentAlready = false;

            for(UUID uuid : alreadySent) {
                if(uuid.compareTo(player.playerUUID) == 0) {
                    sentAlready = true;
                    break;// attempted to send a message 2 times
                }
            }

            if(sentAlready == false && player.isPlayerOnline) {
                player.sendMessage(buffer);
                alreadySent.add(player.playerUUID);
            }
        }

        plugin.getLogger().log(Level.INFO, buffer);
    }

    public void broadcast(PlayerObject[] players, String message) {
        String buffer = ChatColor.translateAlternateColorCodes('&', broadcast_prefix+message);

        List<UUID> alreadySent = new ArrayList<>();
        for(PlayerObject player : players) {
            boolean sentAlready = false;

            for(UUID uuid : alreadySent) {
                if(uuid.compareTo(player.playerUUID) == 0) {
                    sentAlready = true;
                    break;// attempted to send a message 2 times
                }
            }

            if(sentAlready == false && player.isPlayerOnline) {
                player.sendMessage(buffer);
                alreadySent.add(player.playerUUID);
            }
        }

        plugin.getLogger().log(Level.INFO, "[some players] "+buffer);
    }

    //
    // Server Alerts
    //

    public void alert(String message) {
        String buffer = ChatColor.translateAlternateColorCodes('&', alert_prefix+message);
        List<UUID> alreadySent = new ArrayList<>();
        for(PlayerObject player : plugin.getPlayers()) {
            boolean sentAlready = false;

            for(UUID uuid : alreadySent) {
                if(uuid.compareTo(player.playerUUID) == 0) {
                    sentAlready = true;
                    break;// attempted to send a message 2 times
                }
            }

            if(sentAlready == false && player.isPlayerOnline) {
                player.sendMessage(buffer);
                alreadySent.add(player.playerUUID);
            }
        }

        plugin.getLogger().log(Level.INFO, buffer);
    }

    public void alert(PlayerObject[] players, String message) {
        String buffer = ChatColor.translateAlternateColorCodes('&', alert_prefix+message);
        List<UUID> alreadySent = new ArrayList<>();
        for(PlayerObject player : players) {
            boolean sentAlready = false;

            for(UUID uuid : alreadySent) {
                if(uuid.compareTo(player.playerUUID) == 0) {
                    sentAlready = true;
                    break;// attempted to send a message 2 times
                }
            }

            if(sentAlready == false && player.isPlayerOnline) {
                player.sendMessage(buffer);
                alreadySent.add(player.playerUUID);
            }
        }

        plugin.getLogger().log(Level.INFO, "[some players] "+buffer);
    }

    //
    // Raw broadcast
    //

    public void rawBroadcast(String message) {
        String buffer = ChatColor.translateAlternateColorCodes('&', message);
        List<UUID> alreadySent = new ArrayList<>();
        for(PlayerObject player : plugin.getPlayers()) {
            boolean sentAlready = false;

            for(UUID uuid : alreadySent) {
                if(uuid.compareTo(player.playerUUID) == 0) {
                    sentAlready = true;
                    break;// attempted to send a message 2 times
                }
            }

            if(sentAlready == false && player.isPlayerOnline) {
                player.sendMessage(buffer);
                alreadySent.add(player.playerUUID);
            }
        }

        plugin.getLogger().log(Level.INFO, buffer);
    }

    public void rawBroadcast(PlayerObject[] players, String message) {
        String buffer = ChatColor.translateAlternateColorCodes('&', message);
        List<UUID> alreadySent = new ArrayList<>();
        for(PlayerObject player : players) {
            boolean sentAlready = false;

            for(UUID uuid : alreadySent) {
                if(uuid.compareTo(player.playerUUID) == 0) {
                    sentAlready = true;
                    break;// attempted to send a message 2 times
                }
            }

            if(sentAlready == false && player.isPlayerOnline) {
                player.sendMessage(buffer);
                alreadySent.add(player.playerUUID);
            }
        }

        plugin.getLogger().log(Level.INFO, "[some players] "+buffer);
    }
}
