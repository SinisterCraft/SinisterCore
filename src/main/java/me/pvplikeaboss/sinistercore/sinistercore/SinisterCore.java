package me.pvplikeaboss.sinistercore.sinistercore;

import me.pvplikeaboss.sinistercore.sinistercore.modules.API.permissionsex.PexAPI;
import me.pvplikeaboss.sinistercore.sinistercore.modules.API.vault.EconomyImplementer;
import me.pvplikeaboss.sinistercore.sinistercore.modules.API.vault.VaultAPI;
import me.pvplikeaboss.sinistercore.sinistercore.modules.chat.ChatHandler;
import me.pvplikeaboss.sinistercore.sinistercore.commands.util.CommandMapp;
import me.pvplikeaboss.sinistercore.sinistercore.commands.Commands;
import me.pvplikeaboss.sinistercore.sinistercore.modules.clearlag.ClearlagModule;
import me.pvplikeaboss.sinistercore.sinistercore.modules.files.configs.yml.EconConfig;
import me.pvplikeaboss.sinistercore.sinistercore.modules.files.configs.yml.HomeConfig;
import me.pvplikeaboss.sinistercore.sinistercore.modules.files.configs.yml.KitConfig;
import me.pvplikeaboss.sinistercore.sinistercore.modules.files.configs.yml.PunishmentConfig;
import me.pvplikeaboss.sinistercore.sinistercore.events.Events;
import me.pvplikeaboss.sinistercore.sinistercore.modules.files.configs.csv.items.methods.AbstractItemDB;
import me.pvplikeaboss.sinistercore.sinistercore.modules.files.configs.csv.items.LegacyItemDB;
import me.pvplikeaboss.sinistercore.sinistercore.objects.PlayerObject;
import me.pvplikeaboss.sinistercore.sinistercore.modules.punishment.Punishment;
import me.pvplikeaboss.sinistercore.sinistercore.utilites.misc.Cooldown;
import me.pvplikeaboss.sinistercore.sinistercore.utilites.misc.Messages;
import me.pvplikeaboss.sinistercore.sinistercore.utilites.misc.Misc;
import me.pvplikeaboss.sinistercore.sinistercore.utilites.serverutils.PlayerUtils;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class SinisterCore extends JavaPlugin {
    public List<PlayerObject> players = null;

    private Messages utilMsgs = null;

    public String prefix = "[SinisterCore]";

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
        players.addAll(playerUtils.getPlayers());
    }

    public void refreshPlayers() {// add new players
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

    public PlayerObject getPlayer(String pName) {
        for(PlayerObject p : players) {
            if(p.playerName.equalsIgnoreCase(pName)) {
                return p;
            }
        }

        // if we get here its a new player or player doesnt exist...
        refreshPlayers();

        for(PlayerObject p : players) {
            if(p.playerName.equalsIgnoreCase(pName)) {
                return p;
            }
        }

        return null;
    }

    public PlayerObject getPlayer(UUID pUUID) {
        for(PlayerObject p : players) {
            if(p.playerUUID.compareTo(pUUID) == 0) {
                return p;
            }
        }
        // if we get here its a new player or player doesnt exist...
        refreshPlayers();

        for(PlayerObject p : players) {
            if(p.playerUUID.compareTo(pUUID) == 0) {
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
        Instances.load_instances(this);

        utilMsgs = (Messages) Instances.getInstance(Instances.InstanceType.Utilities, 2);

        players = new ArrayList<>();
        /*for(Player p : this.getServer().getOnlinePlayers()) {// load up all players
            if(p.getUniqueId() == null) {
                Messages utilMsgs = (Messages) Instances.getInstance(Instances.InstanceType.Utilities, 2);
                utilMsgs.logErrorMessage("Invalid uuid loading players");
                continue;
            }
            players.add(new PlayerObject(this, p.getUniqueId()));
        }*/

        for(OfflinePlayer p : this.getServer().getOfflinePlayers()) {
            players.add(new PlayerObject(this, p.getUniqueId()));
        }

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
