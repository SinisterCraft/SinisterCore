package me.pvplikeaboss.sinistercore.sinistercore.modules.data.mysql;

import jdk.nashorn.internal.runtime.regexp.joni.Regex;
import me.pvplikeaboss.sinistercore.sinistercore.SinisterCore;
import me.pvplikeaboss.sinistercore.sinistercore.objects.PlayerObject;
import org.bukkit.Location;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.sql.*;
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
        List<PlayerObject> players = null;// leave null until first player is found

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
                    if (!lastLogoutLocation.equalsIgnoreCase("null")) {
                        try {
                            String[] values = lastLogoutLocation.split(",");
                            x = Double.parseDouble(values[0]);
                            y = Double.parseDouble(values[1]);
                            z = Double.parseDouble(values[2]);
                            worldStr = values[3];
                            player.setLastPlayerLogoutLocation(new Location(plugin.getServer().getWorld(worldStr), x, y, z));
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                    }
                }

                if (lastDeathLocation.length() > 0) {
                    if(!lastDeathLocation.equalsIgnoreCase("null")) {
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
                }
                if(players == null) {
                    players = new ArrayList<>();
                }
                players.add(player);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return players;
    }

    public static void savePlayer(PlayerObject p) {
        String logoutLocStr = "null";
        if(p.getLastPlayerLogoutLocation() != null) {
            logoutLocStr = p.getLastPlayerLogoutLocation().getX()+","+p.getLastPlayerLogoutLocation().getY()+","+p.getLastPlayerLogoutLocation().getZ()+","+p.getLastPlayerLogoutLocation().getWorld().getName();
        }
        String deathLocStr = "null";
        if(p.getLastPlayerDeathLocation() != null) {
            deathLocStr = p.getLastPlayerDeathLocation().getX()+","+p.getLastPlayerDeathLocation().getY()+","+p.getLastPlayerDeathLocation().getZ()+","+p.getLastPlayerDeathLocation().getWorld().getName();
        }

        if(playerExists(p.playerUUID)) {// update
            Connection mysqlConn = MysqlConnector.getDatabaseConnection();
            try {
                String setGodModeStatement = "UPDATE players SET isGodMode = ? WHERE player_uuid = ?;";
                String setVanishStatement = "UPDATE players SET isVanish = ? WHERE player_uuid = ?;";
                String setRecvMsgsStatement = "UPDATE players SET recieveMsgs = ? WHERE player_uuid = ?;";

                String setLogoutLocStatement = "UPDATE players SET lastPlayerLogoutLocation = ? WHERE player_uuid = ?;";
                String setDeathLocStatement = "UPDATE players SET lastPlayerDeathLocation = ? WHERE player_uuid = ?;";

                PreparedStatement stmt = mysqlConn.prepareStatement(setGodModeStatement);
                stmt.setString(1, Boolean.toString(p.getIsGodMode()));
                stmt.setString(2, p.playerUUID.toString());
                stmt.executeUpdate();
                stmt.close();

                stmt = mysqlConn.prepareStatement(setVanishStatement);
                stmt.setString(1, Boolean.toString(p.getIsVanish()));
                stmt.setString(2, p.playerUUID.toString());
                stmt.executeUpdate();
                stmt.close();

                stmt = mysqlConn.prepareStatement(setRecvMsgsStatement);
                stmt.setString(1, Boolean.toString(p.getRecieveMsgs()));
                stmt.setString(2, p.playerUUID.toString());
                stmt.executeUpdate();
                stmt.close();

                stmt = mysqlConn.prepareStatement(setLogoutLocStatement);
                stmt.setString(1, logoutLocStr);
                stmt.setString(2, p.playerUUID.toString());
                stmt.executeUpdate();
                stmt.close();

                stmt = mysqlConn.prepareStatement(setDeathLocStatement);
                stmt.setString(1, deathLocStr);
                stmt.setString(2, p.playerUUID.toString());
                stmt.executeUpdate();
                stmt.close();
            } catch(SQLException e) {
                e.printStackTrace();
            }
        } else {// new entry
            Connection mysqlConn = MysqlConnector.getDatabaseConnection();
            try {
                Statement statement = mysqlConn.createStatement();
                String insertStatement = "INSERT INTO `players` (`player_uuid`,`isGodMode`,`isVanish`,`recieveMsgs`,`lastPlayerLogoutLocation`,`lastPlayerDeathLocation`) VALUES (?, ?, ?, ?, ?, ?);";
                PreparedStatement stmt = mysqlConn.prepareStatement(insertStatement);
                stmt.setString(1, p.playerUUID.toString());
                stmt.setString(2, Boolean.toString(p.getIsGodMode()));
                stmt.setString(3, Boolean.toString(p.getIsVanish()));
                stmt.setString(4, Boolean.toString(p.getRecieveMsgs()));
                stmt.setString(5, logoutLocStr);
                stmt.setString(6, deathLocStr);
                stmt.executeUpdate();
            } catch(SQLException e) {
                e.printStackTrace();
            }
        }
        return;
    }

}
