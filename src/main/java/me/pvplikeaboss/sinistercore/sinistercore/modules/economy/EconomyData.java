package me.pvplikeaboss.sinistercore.sinistercore.modules.economy;

import me.pvplikeaboss.sinistercore.sinistercore.Instances;
import me.pvplikeaboss.sinistercore.sinistercore.SinisterCore;
import me.pvplikeaboss.sinistercore.sinistercore.modules.data.files.configs.yml.EconConfig;
import me.pvplikeaboss.sinistercore.sinistercore.modules.data.mysql.EconomyDatabase;
import me.pvplikeaboss.sinistercore.sinistercore.objects.PlayerObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class EconomyData {
    private EconConfig cfgEcon;

    public static void convert(SinisterCore plugin, boolean toDatabase) {
        List<EconomyEntry> econEntries;
        plugin.useMysql = !toDatabase;
        econEntries = getAllEntries(plugin);
        plugin.useMysql = toDatabase;
        for(EconomyEntry entry : econEntries) {
            setBalance(plugin, entry.getPlayerUUID(), entry.getBalance().doubleValue());
        }
    }

    public static List<EconomyEntry> getAllEntries(SinisterCore plugin) {
        List<EconomyEntry> ret = null;
        if(!plugin.useMysql) {// use config
            EconConfig cfgEcon = (EconConfig) Instances.getInstance(Instances.InstanceType.Config, 3);
            if (cfgEcon.getConfig().isSet("econ")) {
                for (String sUUID : cfgEcon.getConfig().getConfigurationSection("econ").getKeys(false)) {
                    PlayerObject p = plugin.getPlayer(UUID.fromString(sUUID));
                    if (p == null) {
                        continue;
                    }
                    if(ret == null) {
                        ret = new ArrayList<>();
                    }
                    ret.add(new EconomyEntry(p.playerUUID, p.playerDisplayName, BigDecimal.valueOf(cfgEcon.getConfig().getDouble("econ." + sUUID))));
                }
            }
            return ret;
        } else {// use database
            ret = EconomyDatabase.getAllEntries();
            return ret;
        }
    }

    public static double getBalance(SinisterCore plugin, UUID playerUUID) {
        if(!plugin.useMysql) {// use config
            EconConfig cfgEcon = (EconConfig) Instances.getInstance(Instances.InstanceType.Config, 3);
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

    public static void setBalance(SinisterCore plugin, UUID playerUUID, double balance) {
        if(!plugin.useMysql) {// use config
            EconConfig cfgEcon = (EconConfig) Instances.getInstance(Instances.InstanceType.Config, 3);
            cfgEcon.getConfig().set("econ." + playerUUID, balance);
            cfgEcon.saveConfig();
        } else {//use database
            EconomyDatabase.setBalance(playerUUID, balance);
        }
    }
}
