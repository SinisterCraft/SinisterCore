package me.pvplikeaboss.sinistercore.sinistercore.modules.data.files.configs;

import me.pvplikeaboss.sinistercore.sinistercore.SinisterCore;
import me.pvplikeaboss.sinistercore.sinistercore.modules.data.files.configs.csv.items.LegacyItemDB;
import me.pvplikeaboss.sinistercore.sinistercore.modules.data.files.configs.csv.items.methods.AbstractItemDB;
import me.pvplikeaboss.sinistercore.sinistercore.modules.data.files.configs.yml.*;

public class Configs {
    private SinisterCore plugin = null;

    //
    // Yml configs
    //

    public HomeConfig cfgHomes = null;
    public KitConfig cfgKits = null;
    public PunishmentConfig cfgPunish = null;
    public EconConfig cfgEcon = null;
    public PlayerConfig cfgPlayers = null;

    //
    // csv configs
    //

    public AbstractItemDB itemDB = null;


    public Configs(SinisterCore p) {
        this.plugin = p;
    }

    public void load() {
        //Configs
        plugin.saveResource("config.yml", false);
        plugin.saveResource("items.csv", false);
        plugin.reloadConfig();
        plugin.saveDefaultConfig();

        cfgHomes = new HomeConfig(plugin);
        cfgKits = new KitConfig(plugin);
        cfgPunish = new PunishmentConfig(plugin);
        cfgEcon = new EconConfig(plugin);
        cfgPlayers = new PlayerConfig(plugin);
        itemDB = new LegacyItemDB(plugin);
    }

    public void reload() {
        //Configs
        plugin.saveResource("config.yml", false);
        plugin.saveResource("items.csv", false);
        plugin.reloadConfig();

        cfgHomes.unload();
        cfgHomes.load();

        cfgKits.unload();
        cfgKits.load();

        cfgPunish.unload();
        cfgPunish.load();

        cfgEcon.unload();
        cfgEcon.load();

        cfgPlayers.unload();
        cfgPlayers.load();

        itemDB.reloadConfig();
    }

    public Object getConfig(int id) {
        switch(id) {
            case 0: {
                return cfgHomes;
            }
            case 1: {
                return cfgKits;
            }
            case 2: {
                return cfgPunish;
            }
            case 3: {
                return cfgEcon;
            }
            case 4: {
                return itemDB;
            }
            case 5: {
                return cfgPlayers;
            }
        }
        return null;
    }
}
