package me.pvplikeaboss.sinistercore.sinistercore.modules.data.mysql;

import me.pvplikeaboss.sinistercore.sinistercore.objects.PlayerObject;
import me.pvplikeaboss.sinistercore.sinistercore.utilites.misc.Time;
import org.bukkit.Bukkit;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class PunishmentDatabase {
    public static int getEntryID(PlayerObject player, String type) {
        Connection mysqlConn = MysqlConnector.getDatabaseConnection();
        String punishTable = MysqlConnector.getPunishTable();
        try {
            PreparedStatement preparedStatement = null;
            String getQueryStatement = "SELECT * FROM "+punishTable;
            preparedStatement = mysqlConn.prepareStatement(getQueryStatement);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                int tmp_id = rs.getInt("entry_id");
                String tmp_player_uuid = rs.getString("player_uuid");
                boolean tmp_active = Boolean.parseBoolean(rs.getString("active"));
                String tmp_type = rs.getString("type");
                if(UUID.fromString(tmp_player_uuid).equals(player.playerUUID) && tmp_active && tmp_type.equalsIgnoreCase(type)) {//player punishment found
                    return tmp_id;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static String getReason(int entry_id) {
        Connection mysqlConn = MysqlConnector.getDatabaseConnection();
        String punishTable = MysqlConnector.getPunishTable();
        try {
            PreparedStatement preparedStatement = null;
            String getQueryStatement = "SELECT * FROM "+punishTable;
            preparedStatement = mysqlConn.prepareStatement(getQueryStatement);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                int tmp_id = rs.getInt("entry_id");
                if(tmp_id == entry_id) {
                    return rs.getString("reason");
                }
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getStartDate(int entry_id) {
        Connection mysqlConn = MysqlConnector.getDatabaseConnection();
        String punishTable = MysqlConnector.getPunishTable();
        try {
            PreparedStatement preparedStatement = null;
            String getQueryStatement = "SELECT * FROM "+punishTable;
            preparedStatement = mysqlConn.prepareStatement(getQueryStatement);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                int tmp_id = rs.getInt("entry_id");
                if(tmp_id == entry_id) {
                    return rs.getString("startdate");
                }
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getEndDate(int entry_id) {
        Connection mysqlConn = MysqlConnector.getDatabaseConnection();
        String punishTable = MysqlConnector.getPunishTable();
        try {
            PreparedStatement preparedStatement = null;
            String getQueryStatement = "SELECT * FROM "+punishTable;
            preparedStatement = mysqlConn.prepareStatement(getQueryStatement);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                int tmp_id = rs.getInt("entry_id");
                if(tmp_id == entry_id) {
                    return rs.getString("enddate");
                }
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void setActive(int entry_id, boolean state) {
        Connection mysqlConn = MysqlConnector.getDatabaseConnection();
        String punishTable = MysqlConnector.getPunishTable();
        try {
            String setGodModeStatement = "UPDATE "+punishTable+" SET active = ? WHERE entry_id = ?;";
            PreparedStatement stmt = mysqlConn.prepareStatement(setGodModeStatement);
            stmt.setString(1, Boolean.toString(state));
            stmt.setInt(2, entry_id);
            stmt.executeUpdate();
            stmt.close();
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    public static void newEntry(PlayerObject player, String type, PlayerObject whoBanned, String reason, Date endDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd/HH/mm/ss");
        Date startDate = Time.getCurrentDateTime();

        Connection mysqlConn = MysqlConnector.getDatabaseConnection();
        String punishTable = MysqlConnector.getPunishTable();
        try {
            String insertStatement = "INSERT INTO `"+punishTable+"` (`player_uuid`,`active`,`type`,`whoBanned`,`reason`,`startdate`,`enddate`) VALUES (?, ?, ?, ?, ?, ?, ?);";
            PreparedStatement stmt = mysqlConn.prepareStatement(insertStatement);
            stmt.setString(1, player.playerUUID.toString());
            stmt.setString(2, "true");
            stmt.setString(3, type);
            if(whoBanned == null) {
                stmt.setString(4, "console");
            } else {
                stmt.setString(4, whoBanned.playerName);
            }
            stmt.setString(5, reason);
            stmt.setString(6, sdf.format(startDate));
            if(endDate == null) {
                stmt.setString(7, "null");
            } else {
                stmt.setString(7, sdf.format(endDate));
            }
            stmt.executeUpdate();
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }
}
