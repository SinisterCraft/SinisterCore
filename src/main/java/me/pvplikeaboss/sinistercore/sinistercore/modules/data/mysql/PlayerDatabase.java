package me.pvplikeaboss.sinistercore.sinistercore.modules.data.mysql;

import me.pvplikeaboss.sinistercore.sinistercore.SinisterCore;
import me.pvplikeaboss.sinistercore.sinistercore.objects.PlayerObject;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.sql.Connection;
import java.util.List;

public class PlayerDatabase {
    private Connection mysqlConn = null;

    public static boolean playerExists(String UUID) {
        return false;
    }

    public static PlayerObject getPlayer(String UUID) {
        return null;
    }

    public static List<PlayerObject> getAllPlayers() {
        return null;
    }

    public static void savePlayer(PlayerObject p) {
        return;
    }

}
