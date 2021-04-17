package me.pvplikeaboss.sinistercore.sinistercore.modules.data;

import me.pvplikeaboss.sinistercore.sinistercore.Instances;
import me.pvplikeaboss.sinistercore.sinistercore.SinisterCore;
import me.pvplikeaboss.sinistercore.sinistercore.modules.data.files.configs.yml.PlayerConfig;
import me.pvplikeaboss.sinistercore.sinistercore.modules.data.mysql.PlayerDatabase;
import me.pvplikeaboss.sinistercore.sinistercore.objects.PlayerObject;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlayerData {

    public static void convert(SinisterCore plugin, boolean toDatabase) {
        List<PlayerObject> players;
        plugin.useMysql = !toDatabase;
        players = getAllPlayers(plugin);
        plugin.useMysql = toDatabase;
        savePlayers(plugin, players);
    }

    public static void savePlayers(SinisterCore plugin, List<PlayerObject> players) {
        if(!plugin.useMysql)
        {// use players.yml
            PlayerConfig playerCfg = (PlayerConfig) Instances.getInstance(Instances.InstanceType.Config, 5);
            for(PlayerObject player : players) {
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
            return;
        }
        else
        {// use mysql database
            for(PlayerObject player : players) {
                PlayerDatabase.savePlayer(player);
            }
            return;
        }
    }

    public static List<PlayerObject> getAllPlayers(SinisterCore plugin) {
        if(!plugin.useMysql) {// use players.yml
            List<PlayerObject> ret = new ArrayList<PlayerObject>();
            PlayerConfig playerCfg = (PlayerConfig) Instances.getInstance(Instances.InstanceType.Config, 5);
            for (String pUUIDStr : playerCfg.getConfig().getConfigurationSection("players").getKeys(false)) {
                PlayerObject player = new PlayerObject(plugin, UUID.fromString(pUUIDStr));

                player.setIsGodMode(playerCfg.getConfig().getBoolean("players." + player.playerUUID + ".isGodMode"));
                player.setIsVanish(playerCfg.getConfig().getBoolean("players." + player.playerUUID + ".isVanish"));
                player.setRecieveMsgs(playerCfg.getConfig().getBoolean("players." + player.playerUUID + ".recieveMsgs"));

                double x, y, z;
                String worldStr;

                if (playerCfg.getConfig().isSet("players." + player.playerUUID + ".lastPlayerDeathLocation")) {
                    x = playerCfg.getConfig().getDouble("players." + player.playerUUID + ".lastPlayerDeathLocation.x");
                    y = playerCfg.getConfig().getDouble("players." + player.playerUUID + ".lastPlayerDeathLocation.y");
                    z = playerCfg.getConfig().getDouble("players." + player.playerUUID + ".lastPlayerDeathLocation.z");
                    worldStr = playerCfg.getConfig().getString("players." + player.playerUUID + ".lastPlayerDeathLocation.world");
                    player.setLastPlayerDeathLocation(new Location(plugin.getServer().getWorld(worldStr), x, y, z));
                }

                if (playerCfg.getConfig().isSet("players." + player.playerUUID + ".lastPlayerLogoutLocation")) {
                    x = playerCfg.getConfig().getDouble("players." + player.playerUUID + ".lastPlayerLogoutLocation.x");
                    y = playerCfg.getConfig().getDouble("players." + player.playerUUID + ".lastPlayerLogoutLocation.y");
                    z = playerCfg.getConfig().getDouble("players." + player.playerUUID + ".lastPlayerLogoutLocation.z");
                    worldStr = playerCfg.getConfig().getString("players." + player.playerUUID + ".lastPlayerLogoutLocation.world");
                    player.setLastPlayerLogoutLocation(new Location(plugin.getServer().getWorld(worldStr), x, y, z));
                }

                ret.add(player);
            }
            return ret;
        } else {
            return PlayerDatabase.getAllPlayers(plugin);
        }
    }
}
