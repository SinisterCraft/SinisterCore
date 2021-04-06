package me.pvplikeaboss.sinistercore.sinistercore.utilites.misc;

import me.pvplikeaboss.sinistercore.sinistercore.Instances;
import me.pvplikeaboss.sinistercore.sinistercore.SinisterCore;
import me.pvplikeaboss.sinistercore.sinistercore.modules.files.configs.yml.KitConfig;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Cooldown {
    private static class KitCooldownMap {
        UUID pUUID = null;
        String kit = null;
        Date end = null;

        KitCooldownMap(UUID pUUID, String kit, Date end) {
            this.pUUID = pUUID;
            this.kit = kit;
            this.end = end;
        }
    }

    private final SinisterCore plugin;
    private final Messages msgs;
    private final KitConfig cfgKits;

    List<KitCooldownMap> kitCooldownMap;

    public Cooldown(SinisterCore p) {
        this.plugin = p;
        this.cfgKits = (KitConfig) Instances.getInstance(Instances.InstanceType.Config, 1);
        this.msgs = (Messages) Instances.getInstance(Instances.InstanceType.Utilities, 2);
        kitCooldownMap = new ArrayList<>();
    }

    public String isKitCooldown(UUID pUUID, String kit) {
        for(KitCooldownMap cd : kitCooldownMap) {
            if(cd.pUUID.toString().equalsIgnoreCase(pUUID.toString())) {
                if(cd.kit.equalsIgnoreCase(kit)) {
                    if(cd.end == null) {
                        return "One time use";
                    }
                    String str = Time.getTimeLeftStr(cd.end);
                    if(str != null) {
                        return str;
                    } else {
                        kitCooldownMap.remove(cd);
                    }
                }
            }
        }
        return null;
    }

    public void putKitCooldown(UUID pUUID, String kit, long end) {
        Date date = null;
        if(end != -1) {
            date = new Date(Calendar.getInstance().getTime().getTime() + (end * 1000));// seconds to milliseconds
        }

        kitCooldownMap.add(new KitCooldownMap(pUUID, kit, date));
    }

    public void saveKitCooldown() {
        for(KitCooldownMap cd : kitCooldownMap) {
            UUID pUUID = cd.pUUID;
            if(pUUID == null) {
                continue;
            }

            String kit = cd.kit;
            if(kit == null) {
                continue;
            }

            Date end = cd.end;
            if(end == null) {
                cfgKits.getConfig().set("cooldown."+pUUID.toString()+"."+kit, "never");
                continue;
            }

            // only save if time isnt up
            if(Time.getTimeLeft(cd.end) == -1) {
                continue;
            }

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd/HH/mm/ss");
            String line = sdf.format(end);
            cfgKits.getConfig().set("cooldown."+pUUID.toString()+"."+kit, line);
        }
        cfgKits.saveConfig();
    }

    public void loadKitCooldown() {
        kitCooldownMap = new ArrayList<>();
        if(cfgKits.getConfig().getConfigurationSection("cooldown") != null) {
            for(String sUUID : cfgKits.getConfig().getConfigurationSection("cooldown").getKeys(false)) {
                UUID pUUID = UUID.fromString(sUUID);
                for(String kit : cfgKits.getConfig().getConfigurationSection("cooldown."+pUUID).getKeys(false)) {
                    try {
                        String endDateStr = cfgKits.getConfig().getString("cooldown." + pUUID + "." + kit);
                        if(endDateStr.equalsIgnoreCase("never")) {
                            kitCooldownMap.add(new KitCooldownMap(pUUID, kit, null));
                            continue;
                        }
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd/HH/mm/ss");
                        Date date = sdf.parse(endDateStr);
                        String str = Time.getTimeLeftStr(date);
                        if(str == null) {// remove entry if kit cooldown is over
                            cfgKits.getConfig().set("cooldown." + pUUID + "." + kit, null);
                            continue;
                        }
                        kitCooldownMap.add(new KitCooldownMap(pUUID, kit, date));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
