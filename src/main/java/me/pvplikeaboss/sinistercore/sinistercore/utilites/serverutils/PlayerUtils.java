package me.pvplikeaboss.sinistercore.sinistercore.utilites.serverutils;

import me.pvplikeaboss.sinistercore.sinistercore.Instances;
import me.pvplikeaboss.sinistercore.sinistercore.SinisterCore;
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

    public List<PlayerObject> getPlayers() {
        PlayerConfig playerCfg = (PlayerConfig) Instances.getInstance(Instances.InstanceType.Config, 5);
        List<PlayerObject> ret = new ArrayList<PlayerObject>();

        for(String pUUIDStr : playerCfg.getConfig().getConfigurationSection("players").getKeys(false)) {
            PlayerObject player = new PlayerObject(plugin, UUID.fromString(pUUIDStr));

            player.setIsGodMode(playerCfg.getConfig().getBoolean("players."+player.playerUUID+".isGodMode"));
            player.setIsVanish(playerCfg.getConfig().getBoolean("players."+player.playerUUID+".isVanish"));
            player.setRecieveMsgs(playerCfg.getConfig().getBoolean("players."+player.playerUUID+".recieveMsgs"));

            double x, y, z;
            String worldStr;

            if(playerCfg.getConfig().isSet("players." + player.playerUUID + ".lastPlayerDeathLocation")) {
                x = playerCfg.getConfig().getDouble("players." + player.playerUUID + ".lastPlayerDeathLocation.x");
                y = playerCfg.getConfig().getDouble("players." + player.playerUUID + ".lastPlayerDeathLocation.y");
                z = playerCfg.getConfig().getDouble("players." + player.playerUUID + ".lastPlayerDeathLocation.z");
                worldStr = playerCfg.getConfig().getString("players." + player.playerUUID + ".lastPlayerDeathLocation.world");

                player.setLastPlayerDeathLocation(new Location(plugin.getServer().getWorld(worldStr), x, y, z));
            }

            if(playerCfg.getConfig().isSet("players." + player.playerUUID + ".lastPlayerLogoutLocation")) {
                x = playerCfg.getConfig().getDouble("players." + player.playerUUID + ".lastPlayerLogoutLocation.x");
                y = playerCfg.getConfig().getDouble("players." + player.playerUUID + ".lastPlayerLogoutLocation.y");
                z = playerCfg.getConfig().getDouble("players." + player.playerUUID + ".lastPlayerLogoutLocation.z");
                worldStr = playerCfg.getConfig().getString("players." + player.playerUUID + ".lastPlayerLogoutLocation.world");

                player.setLastPlayerDeathLocation(new Location(plugin.getServer().getWorld(worldStr), x, y, z));
            }

            ret.add(player);
        }

        return ret;
    }

    public void savePlayers(List<PlayerObject> players) {
        PlayerConfig playerCfg = (PlayerConfig) Instances.getInstance(Instances.InstanceType.Config, 5);
        //plugin.getLogger().log(Level.INFO, "Saving players");
        for(PlayerObject player : players) {
            //plugin.getLogger().log(Level.INFO, "Saving player "+player.playerName);
            playerCfg.getConfig().set("players."+player.playerUUID+".isGodMode", player.getIsGodMode());
            playerCfg.getConfig().set("players."+player.playerUUID+".isVanish", player.getIsVanish());
            playerCfg.getConfig().set("players."+player.playerUUID+".recieveMsgs", player.getRecieveMsgs());

            if(player.getLastPlayerDeathLocation() != null) {
                playerCfg.getConfig().set("players." + player.playerUUID + ".lastPlayerDeathLocation.x", player.getLastPlayerDeathLocation().getX());
                playerCfg.getConfig().set("players." + player.playerUUID + ".lastPlayerDeathLocation.y", player.getLastPlayerDeathLocation().getY());
                playerCfg.getConfig().set("players." + player.playerUUID + ".lastPlayerDeathLocation.z", player.getLastPlayerDeathLocation().getZ());
                playerCfg.getConfig().set("players." + player.playerUUID + ".lastPlayerDeathLocation.world", player.getLastPlayerDeathLocation().getWorld().getName());
            }
            if(player.getLastPlayerLogoutLocation() != null) {
                playerCfg.getConfig().set("players." + player.playerUUID + ".lastPlayerLogoutLocation.x", player.getLastPlayerLogoutLocation().getX());
                playerCfg.getConfig().set("players." + player.playerUUID + ".lastPlayerLogoutLocation.y", player.getLastPlayerLogoutLocation().getY());
                playerCfg.getConfig().set("players." + player.playerUUID + ".lastPlayerLogoutLocation.z", player.getLastPlayerLogoutLocation().getZ());
                playerCfg.getConfig().set("players." + player.playerUUID + ".lastPlayerLogoutLocation.world", player.getLastPlayerLogoutLocation().getWorld().getName());
            }
        }
        playerCfg.saveConfig();
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
