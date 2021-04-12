package me.pvplikeaboss.sinistercore.sinistercore.modules.data.mysql;

import me.pvplikeaboss.sinistercore.sinistercore.SinisterCore;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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

                    StringBuilder mysqlQuery = new StringBuilder();
                    mysqlQuery.append("SET SQL_MODE = \"NO_AUTO_VALUE_ON_ZERO\"              ");
                    mysqlQuery.append("CREATE TABLE IF NOT EXISTS `players` (                ");
                    mysqlQuery.append("  `player_id` INT(4) UNSIGNED NOT NULL AUTO_INCREMENT,");
                    mysqlQuery.append("  `player_uuid` CHAR(36) NOT NULL UNIQUE,             ");
                    mysqlQuery.append("  `isGodMode` CHAR(8) NOT NULL,                       ");
                    mysqlQuery.append("  `isVanish` CHAR(8) NOT NULL,                        ");
                    mysqlQuery.append("  `recieveMsgs` CHAR(8) NOT NULL,                     ");
                    mysqlQuery.append("  `lastPlayerLogoutLocation` CHAR(64) NOT NULL,       ");
                    mysqlQuery.append("  `lastPlayerDeathLocation` CHAR(64) NOT NULL,        ");
                    mysqlQuery.append("  PRIMARY KEY (`player_id`),                          ");
                    mysqlQuery.append("  KEY (`player_uuid`)                                 ");
                    mysqlQuery.append(")                                                     ");
                    mysqlQuery.append("ENGINE =InnoDB DEFAULT CHARSET =latin1;               ");

                    PreparedStatement PrepareStatement;
                    PrepareStatement = databaseConnection.prepareStatement(mysqlQuery.toString());
                    PrepareStatement.execute();

                    return true;
                } else {
                    return false;
                }
            } catch (SQLException e) {
                return false;
            }
        }
        return false;
    }

    public static Connection getDatabaseConnection() {
        return databaseConnection;
    }
}
