package me.pvplikeaboss.sinistercore.sinistercore.modules.data.mysql;

import me.pvplikeaboss.sinistercore.sinistercore.SinisterCore;

import java.sql.*;
import java.util.logging.Level;

public class MysqlConnector {
    private static SinisterCore plugin = null;
    private static Connection databaseConnection = null;

    public MysqlConnector(SinisterCore p) {
        this.plugin = p;
    }

    public static boolean initMysqlConnection(SinisterCore p) {
        plugin = p;
        if(plugin.getConfig().getBoolean("features.mysql.enabled")) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                String host = plugin.getConfig().getString("features.mysql.server.host");
                String port = plugin.getConfig().getString("features.mysql.server.port");
                String database = plugin.getConfig().getString("features.mysql.connection.database");
                String user = plugin.getConfig().getString("features.mysql.connection.username");
                String pass = plugin.getConfig().getString("features.mysql.connection.password");
                String params = plugin.getConfig().getString("features.mysql.connection.params");

                String url;
                if (params != null || params.length() > 0) {
                    url = "jdbc:mysql://" + host + ":" + port + "/" + database + "?" + params;
                } else {
                    url = "jdbc:mysql://" + host + ":" + port + "/" + database;
                }
                databaseConnection = DriverManager.getConnection(url, user, pass);
                if (databaseConnection != null) {
                    Statement statement = databaseConnection.createStatement();
                    statement.executeUpdate("CREATE TABLE IF NOT EXISTS players (player_id INTEGER NOT NULL AUTO_INCREMENT, player_uuid varchar(36) NOT NULL UNIQUE, isGodMode varchar(8) NOT NULL, isVanish varchar(8) NOT NULL, recieveMsgs varchar(8) NOT NULL, lastPlayerLogoutLocation varchar(64) NOT NULL, lastPlayerDeathLocation varchar(64) NOT NULL, PRIMARY KEY (`player_id`), KEY (`player_uuid`));");
                    statement.close();

                    statement = databaseConnection.createStatement();
                    statement.executeUpdate("CREATE TABLE IF NOT EXISTS economy (player_id INTEGER NOT NULL AUTO_INCREMENT, player_uuid varchar(36) NOT NULL UNIQUE, balance DOUBLE(50,2) NOT NULL, PRIMARY KEY (`player_id`), KEY (`player_uuid`));");
                    statement.close();

                    statement = databaseConnection.createStatement();
                    statement.executeUpdate("CREATE TABLE IF NOT EXISTS punishment (entry_id INTEGER NOT NULL AUTO_INCREMENT, player_uuid varchar(36) NOT NULL, active varchar(8) NOT NULL, type varchar(16) NOT NULL, whoBanned varchar(48) NOT NULL, reason varchar(48) NOT NULL, startdate varchar(32) NOT NULL, enddate varchar(32) NOT NULL, PRIMARY KEY (`entry_id`), KEY (`player_uuid`));");
                    statement.close();
                    return true;
                } else {
                    plugin.getLogger().log(Level.SEVERE, "MYSQL Failed on getConnection");
                    return false;
                }
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    public static Connection getDatabaseConnection() {
        return databaseConnection;
    }
}
