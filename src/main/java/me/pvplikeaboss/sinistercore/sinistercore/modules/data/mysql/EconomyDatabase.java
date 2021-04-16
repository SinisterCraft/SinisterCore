package me.pvplikeaboss.sinistercore.sinistercore.modules.data.mysql;

import jdk.jfr.internal.LogLevel;
import jdk.jfr.internal.LogTag;
import jdk.jfr.internal.Logger;
import org.bukkit.Bukkit;

import java.sql.*;
import java.util.UUID;

public class EconomyDatabase {
    public static boolean accountExists(UUID playerUUID) {
        Connection mysqlConn = MysqlConnector.getDatabaseConnection();
        try {
            PreparedStatement preparedStatement = null;
            String getQueryStatement = "SELECT * FROM economy";
            preparedStatement = mysqlConn.prepareStatement(getQueryStatement);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                String player_uuid = rs.getString("player_uuid");
                if(UUID.fromString(player_uuid).equals(playerUUID)) {//player found
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static double getBalance(UUID playerUUID) {
        Connection mysqlConn = MysqlConnector.getDatabaseConnection();
        //Bukkit.getServer().broadcastMessage("looking for uuid "+playerUUID);
        try {
            PreparedStatement preparedStatement = null;
            String getQueryStatement = "SELECT * FROM economy";
            preparedStatement = mysqlConn.prepareStatement(getQueryStatement);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                String player_uuid = rs.getString("player_uuid");
                //Bukkit.getServer().broadcastMessage("got player uuid "+player_uuid);
                if(UUID.fromString(player_uuid).equals(playerUUID)) {//player found
                    //Bukkit.getServer().broadcastMessage("found player with balance "+rs.getDouble("balance"));
                    return rs.getDouble("balance");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static void setBalance(UUID playerUUID, double balance) {
        if(accountExists(playerUUID)) {// update query
            String setBalanceStatement = "UPDATE economy SET balance = ? WHERE player_uuid = ?;";
            Connection mysqlConn = MysqlConnector.getDatabaseConnection();
            try {
                PreparedStatement stmt = mysqlConn.prepareStatement(setBalanceStatement);
                stmt.setDouble(1, balance);
                stmt.setString(2, playerUUID.toString());
                stmt.executeUpdate();
                stmt.close();
            } catch(SQLException e) {
                e.printStackTrace();
            }
        } else {// insert query
            Connection mysqlConn = MysqlConnector.getDatabaseConnection();
            try {
                Statement statement = mysqlConn.createStatement();
                String insertStatement = "INSERT INTO `economy` (`player_uuid`,`balance`) VALUES (?, ?);";
                PreparedStatement stmt = mysqlConn.prepareStatement(insertStatement);
                stmt.setString(1, playerUUID.toString());
                stmt.setDouble(2, balance);
                stmt.executeUpdate();
            } catch(SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
