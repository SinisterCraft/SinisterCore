package me.pvplikeaboss.sinistercore.sinistercore.modules.data.mysql;

import jdk.nashorn.internal.runtime.regexp.joni.Regex;
import me.pvplikeaboss.sinistercore.sinistercore.SinisterCore;
import me.pvplikeaboss.sinistercore.sinistercore.objects.PlayerObject;
import org.bukkit.Location;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlayerDatabase {
    public static boolean playerExists(UUID pUUID) {
        Connection mysqlConn = MysqlConnector.getDatabaseConnection();
        try {
            PreparedStatement preparedStatement = null;
            String getQueryStatement = "SELECT * FROM players";
            preparedStatement = mysqlConn.prepareStatement(getQueryStatement);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                String player_uuid = rs.getString("player_uuid");
                if(UUID.fromString(player_uuid).equals(pUUID)) {//player found
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static PlayerObject getPlayer(SinisterCore plugin, String pUUID) {
        Connection mysqlConn = MysqlConnector.getDatabaseConnection();
        try {
            PreparedStatement preparedStatement = null;
            String getQueryStatement = "SELECT * FROM players";
            preparedStatement = mysqlConn.prepareStatement(getQueryStatement);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                String player_uuid_str = rs.getString("player_uuid");
                UUID player_uuid = UUID.fromString(player_uuid_str);
                if(player_uuid.equals(pUUID)) {// player found
                    PlayerObject player = new PlayerObject(plugin, player_uuid);

                    player.setIsGodMode(Boolean.parseBoolean(rs.getString("isGodMode")));
                    player.setIsVanish(Boolean.parseBoolean(rs.getString("isVanish")));
                    player.setRecieveMsgs(Boolean.parseBoolean(rs.getString("recieveMsgs")));

                    double x, y, z;
                    String worldStr;

                    String lastLogoutLocation = rs.getString("lastPlayerLogoutLocation");
                    String lastDeathLocation = rs.getString("lastPlayerDeathLocation");

                    if (lastLogoutLocation.length() > 0) {
                        try {
                            String[] values = lastLogoutLocation.split(",");
                            x = Double.parseDouble(values[0]);
                            y = Double.parseDouble(values[1]);
                            z = Double.parseDouble(values[2]);
                            worldStr = values[3];
                            player.setLastPlayerLogoutLocation(new Location(plugin.getServer().getWorld(worldStr), x, y, z));
                        } catch(NumberFormatException e) {
                            e.printStackTrace();
                        }
                    }

                    if (lastDeathLocation.length() > 0) {
                        try {
                            String[] values = lastDeathLocation.split(",");
                            x = Double.parseDouble(values[0]);
                            y = Double.parseDouble(values[1]);
                            z = Double.parseDouble(values[2]);
                            worldStr = values[3];
                            player.setLastPlayerDeathLocation(new Location(plugin.getServer().getWorld(worldStr), x, y, z));
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                    }
                    return player;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<PlayerObject> getAllPlayers(SinisterCore plugin) {
        List<PlayerObject> players = new ArrayList<>();

        Connection mysqlConn = MysqlConnector.getDatabaseConnection();
        try {
            PreparedStatement preparedStatement = null;
            String getQueryStatement = "SELECT * FROM players";
            preparedStatement = mysqlConn.prepareStatement(getQueryStatement);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                String player_uuid_str = rs.getString("player_uuid");
                UUID player_uuid = UUID.fromString(player_uuid_str);
                PlayerObject player = new PlayerObject(plugin, player_uuid);

                player.setIsGodMode(Boolean.parseBoolean(rs.getString("isGodMode")));
                player.setIsVanish(Boolean.parseBoolean(rs.getString("isVanish")));
                player.setRecieveMsgs(Boolean.parseBoolean(rs.getString("recieveMsgs")));

                double x, y, z;
                String worldStr;

                String lastLogoutLocation = rs.getString("lastPlayerLogoutLocation");
                String lastDeathLocation = rs.getString("lastPlayerDeathLocation");

                if (lastLogoutLocation.length() > 0) {
                    try {
                        String[] values = lastLogoutLocation.split(",");
                        x = Double.parseDouble(values[0]);
                        y = Double.parseDouble(values[1]);
                        z = Double.parseDouble(values[2]);
                        worldStr = values[3];
                        player.setLastPlayerLogoutLocation(new Location(plugin.getServer().getWorld(worldStr), x, y, z));
                    } catch(NumberFormatException e) {
                        e.printStackTrace();
                    }
                }

                if (lastDeathLocation.length() > 0) {
                    try {
                        String[] values = lastDeathLocation.split(",");
                        x = Double.parseDouble(values[0]);
                        y = Double.parseDouble(values[1]);
                        z = Double.parseDouble(values[2]);
                        worldStr = values[3];
                        player.setLastPlayerDeathLocation(new Location(plugin.getServer().getWorld(worldStr), x, y, z));
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }

                players.add(player);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void savePlayer(PlayerObject p) {
        String logoutLocStr = p.getLastPlayerLogoutLocation().getX()+","+p.getLastPlayerLogoutLocation().getY()+","+p.getLastPlayerLogoutLocation().getZ()+","+p.getLastPlayerLogoutLocation().getWorld().getName();
        String deathLocStr = p.getLastPlayerDeathLocation().getX()+","+p.getLastPlayerDeathLocation().getY()+","+p.getLastPlayerDeathLocation().getZ()+","+p.getLastPlayerDeathLocation().getWorld().getName();

        if(playerExists(p.playerUUID)) {// update
            Connection mysqlConn = MysqlConnector.getDatabaseConnection();
            try {
                PreparedStatement preparedStatement = null;
                String setGodModeStatement = "UPDATE `players` SET `isGodMode` = '"+ p.getIsGodMode() +"' WHERE `player_uuid` = '"+p.playerUUID+"';";
                String setVanishStatement = "UPDATE `players` SET `isVanish` = '"+ p.getIsVanish() +"' WHERE `player_uuid` = '"+p.playerUUID+"';";
                String setRecvMsgsStatement = "UPDATE `players` SET `recieveMsgs` = '"+ p.getRecieveMsgs() +"' WHERE `player_uuid` = '"+p.playerUUID+"';";

                String setLogoutLocStatement = "UPDATE `players` SET `lastPlayerLogoutLocation` = '"+ logoutLocStr +"' WHERE `player_uuid` = '"+p.playerUUID+"';";
                String setDeathLocStatement = "UPDATE `players` SET `lastPlayerDeathLocation` = '"+ deathLocStr +"' WHERE `player_uuid` = '"+p.playerUUID+"';";

                preparedStatement = mysqlConn.prepareStatement(setGodModeStatement);
                preparedStatement.executeQuery();
                preparedStatement = null;

                preparedStatement = mysqlConn.prepareStatement(setVanishStatement);
                preparedStatement.executeQuery();
                preparedStatement = null;

                preparedStatement = mysqlConn.prepareStatement(setRecvMsgsStatement);
                preparedStatement.executeQuery();
                preparedStatement = null;

                preparedStatement = mysqlConn.prepareStatement(setLogoutLocStatement);
                preparedStatement.executeQuery();
                preparedStatement = null;

                preparedStatement = mysqlConn.prepareStatement(setDeathLocStatement);
                preparedStatement.executeQuery();
                preparedStatement = null;

            } catch(SQLException e) {
                e.printStackTrace();
            }
        } else {// new entry
            Connection mysqlConn = MysqlConnector.getDatabaseConnection();
            try {
                PreparedStatement preparedStatement = null;
                String insertStatement = "INSERT INTO `players` (`player_id`,`player_uuid`,`isGodMode`,`isVanish`,`recieveMsgs`,`lastPlayerLogoutLocation`,`lastPlayerDeathLocation`) VALUES (NULL, "+p.playerUUID+", "+p.getIsGodMode()+", "+p.getIsVanish()+", "+p.getRecieveMsgs()+", "+logoutLocStr+", "+deathLocStr+");";
                preparedStatement = mysqlConn.prepareStatement(insertStatement);
                preparedStatement.executeQuery();
                preparedStatement = null;
            } catch(SQLException e) {
                e.printStackTrace();
            }
        }
        return;
    }

}
