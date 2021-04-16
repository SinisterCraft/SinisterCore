package me.pvplikeaboss.sinistercore.sinistercore.modules.economy;

import me.pvplikeaboss.sinistercore.sinistercore.Instances;
import me.pvplikeaboss.sinistercore.sinistercore.SinisterCore;
import me.pvplikeaboss.sinistercore.sinistercore.modules.data.files.configs.yml.EconConfig;
import me.pvplikeaboss.sinistercore.sinistercore.modules.data.mysql.EconomyDatabase;

import java.util.UUID;

public class EconomyData {
    private SinisterCore plugin;
    private EconConfig cfgEcon;

    public EconomyData(SinisterCore p) {
        this.plugin = p;
        this.cfgEcon = (EconConfig) Instances.getInstance(Instances.InstanceType.Config, 3);
    }

    public double getBalance(UUID playerUUID) {
        if(!plugin.useMysql) {// use config
            if (cfgEcon.getConfig().isSet("econ")) {
                for (String sUUID : cfgEcon.getConfig().getConfigurationSection("econ").getKeys(false)) {
                    if (playerUUID.compareTo(UUID.fromString(sUUID)) == 0) {
                        return cfgEcon.getConfig().getDouble("econ." + sUUID);
                    }
                }
            }
            return -1;
        } else {// use database
            return EconomyDatabase.getBalance(playerUUID);
        }
    }

    public void setBalance(UUID playerUUID, double balance) {
        if(!plugin.useMysql) {// use config
            cfgEcon.getConfig().set("econ." + playerUUID, balance);
            cfgEcon.saveConfig();
        } else {//use database
            EconomyDatabase.setBalance(playerUUID, balance);
        }
    }
}
