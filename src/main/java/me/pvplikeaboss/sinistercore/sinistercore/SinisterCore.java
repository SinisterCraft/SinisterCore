package me.pvplikeaboss.sinistercore.sinistercore;

import me.pvplikeaboss.sinistercore.sinistercore.modules.clearlag.ClearlagModule;
import me.pvplikeaboss.sinistercore.sinistercore.modules.data.mysql.MysqlConnector;
import me.pvplikeaboss.sinistercore.sinistercore.objects.PlayerObject;
import me.pvplikeaboss.sinistercore.sinistercore.utilites.misc.Messages;
import me.pvplikeaboss.sinistercore.sinistercore.utilites.serverutils.PlayerUtils;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

public class SinisterCore extends JavaPlugin {
    public List<PlayerObject> players = null;

    private Messages utilMsgs = null;

    public String prefix = "[SinisterCore]";

    public MysqlConnector mysqlConnector = null;

    public boolean useMysql = false;

    public void onEnable() {
        this.loadAll();
    }

    public void onDisable() {
        this.unloadAll();
    }

    public void loadPlayers() {
        players = new ArrayList<>();
        players.clear();

        PlayerUtils playerUtils = (PlayerUtils) Instances.getInstance(Instances.InstanceType.Utilities, 3);
        List<PlayerObject> tmpPlayers = playerUtils.loadPlayers();
        if(tmpPlayers != null) {// if no players in database or file
            players.addAll(tmpPlayers);
        }

        refreshPlayersOffline();
    }

    public void refreshPlayersOnline() {// add new players
        for(Player player : this.getServer().getOnlinePlayers()) {
            boolean found = false;
            for(PlayerObject tmpplayer : players) {
                if(tmpplayer.playerUUID.compareTo(player.getUniqueId()) == 0) {
                    found = true;
                    break;
                }
            }

            if(found == false) {
                players.add(new PlayerObject(this, player.getUniqueId()));
            }
        }
    }

    public void refreshPlayersOffline() {// add new players
        for(OfflinePlayer player : this.getServer().getOfflinePlayers()) {
            boolean found = false;
            for(PlayerObject tmpplayer : players) {
                if(tmpplayer.playerUUID.compareTo(player.getUniqueId()) == 0) {
                    found = true;
                    break;
                }
            }

            if(found == false) {
                players.add(new PlayerObject(this, player.getUniqueId()));
            }
        }
    }

    public PlayerObject getPlayer(String pName) {
        for(PlayerObject p : players) {
            if(p.playerName.equalsIgnoreCase(pName) || p.playerDisplayName.equalsIgnoreCase(pName)) {
                return p;
            }
        }

        // if we get here its a new player or player doesnt exist...
        refreshPlayersOnline();

        for(PlayerObject p : players) {
            if(p.playerName.equalsIgnoreCase(pName) || p.playerDisplayName.equalsIgnoreCase(pName)) {
                return p;
            }
        }

        refreshPlayersOffline();

        for(PlayerObject p : players) {
            if(p.playerName.equalsIgnoreCase(pName) || p.playerDisplayName.equalsIgnoreCase(pName)) {
                return p;
            }
        }

        return null;
    }

    public PlayerObject getPlayer(UUID pUUID) {
        for(PlayerObject p : players) {
            if(p.playerUUID.equals(pUUID)) {
                return p;
            }
        }
        // if we get here its a new player or player doesnt exist...
        refreshPlayersOnline();

        for(PlayerObject p : players) {
            if(p.playerUUID.equals(pUUID)) {
                return p;
            }
        }

        refreshPlayersOffline();

        for(PlayerObject p : players) {
            if(p.playerUUID.equals(pUUID)) {
                return p;
            }
        }

        return null;
    }

    public List<PlayerObject> getPlayers() {
        int i = 0;
        for(PlayerObject p : players) {
            i++;
        }
        if(i == 0) {
            loadPlayers();
        }
        return players;
    }

    public void loadAll() {
        if(getConfig().getBoolean("features.mysql.enabled")) {
            useMysql = true;
            if (!MysqlConnector.initMysqlConnection(this)) {
                getLogger().log(Level.SEVERE, "Failed to initalize mysql database!");
            }
        }

        Instances.load_instances(this);

        loadPlayers();

        ClearlagModule.load(this);
        ClearlagModule.startClearLagModule();
    }

    public void unloadAll() {
        PlayerUtils playerUtils = (PlayerUtils) Instances.getInstance(Instances.InstanceType.Utilities, 3);
        playerUtils.savePlayers(players);
        players.clear();
        Instances.unload_instances();
        Runtime.getRuntime().gc();
    }

    public void reloadAll() {
        PlayerUtils playerUtils = (PlayerUtils) Instances.getInstance(Instances.InstanceType.Utilities, 3);
        playerUtils.savePlayers(players);
        players.clear();
        loadPlayers();
        Instances.reload_instances();
    }

    public String format(String message) {
        return prefix + " " + message;
    }

}
