package me.pvplikeaboss.sinistercore.sinistercore.utilites.serverutils;

import me.pvplikeaboss.sinistercore.sinistercore.SinisterCore;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.UUID;

public class PlayerUtils {
    private SinisterCore plugin;

    public PlayerUtils(SinisterCore p) {
        this.plugin = p;
    }

    public String playerExists(String name) {
        for(OfflinePlayer player : plugin.getServer().getOfflinePlayers()) {
            if(player.getName().equalsIgnoreCase(name)) {
                return player.getName();
            }
        }

        for(Player p : plugin.getServer().getOnlinePlayers()) {
            if(p.getName().equalsIgnoreCase(name)) {
                return p.getName();
            }
        }

        return null;
    }

    public String playerOnline(String pName) {
        for(Player p : plugin.getServer().getOnlinePlayers()) {
            if(p.getName().equalsIgnoreCase(pName)) {
                return p.getName();
            }
        }
        return null;
    }

    public boolean playerOnline(UUID pUUID) {
        for(Player p : plugin.getServer().getOnlinePlayers()) {
            if(p.getUniqueId().equals(pUUID)) {
                return true;
            }
        }
        return false;
    }
}
