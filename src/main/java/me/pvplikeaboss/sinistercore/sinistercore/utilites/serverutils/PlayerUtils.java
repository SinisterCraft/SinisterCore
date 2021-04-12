package me.pvplikeaboss.sinistercore.sinistercore.utilites.serverutils;

import me.pvplikeaboss.sinistercore.sinistercore.Instances;
import me.pvplikeaboss.sinistercore.sinistercore.SinisterCore;
import me.pvplikeaboss.sinistercore.sinistercore.modules.data.PlayerData;
import me.pvplikeaboss.sinistercore.sinistercore.modules.data.files.configs.yml.PlayerConfig;
import me.pvplikeaboss.sinistercore.sinistercore.objects.PlayerObject;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlayerUtils {
    private SinisterCore plugin;

    public PlayerUtils(SinisterCore p) {
        this.plugin = p;
    }

    public List<PlayerObject> loadPlayers() {
        return PlayerData.getAllPlayers(plugin);
    }

    public void savePlayers(List<PlayerObject> players) {
        PlayerData.savePlayers(plugin, players);
    }

    public String playerExists(String name) {
        for(OfflinePlayer player : plugin.getServer().getOfflinePlayers()) {
            if(player.getName().equalsIgnoreCase(name)) {
                return player.getName();
            }
        }

        for(Player p : plugin.getServer().getOnlinePlayers()) {
            if(p.getName().equalsIgnoreCase(name) || p.getDisplayName().equalsIgnoreCase(name)) {
                return p.getName();
            }
        }

        return null;
    }

    public String playerOnline(String pName) {
        for(Player p : plugin.getServer().getOnlinePlayers()) {
            if(p.getName().equalsIgnoreCase(pName) || p.getDisplayName().equalsIgnoreCase(pName)) {
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
