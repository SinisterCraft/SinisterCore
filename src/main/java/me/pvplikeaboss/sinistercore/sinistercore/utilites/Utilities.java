package me.pvplikeaboss.sinistercore.sinistercore.utilites;

import me.pvplikeaboss.sinistercore.sinistercore.SinisterCore;
import me.pvplikeaboss.sinistercore.sinistercore.modules.files.configs.yml.EconConfig;
import me.pvplikeaboss.sinistercore.sinistercore.modules.files.configs.yml.HomeConfig;
import me.pvplikeaboss.sinistercore.sinistercore.modules.files.configs.yml.KitConfig;
import me.pvplikeaboss.sinistercore.sinistercore.modules.files.configs.yml.PunishmentConfig;
import me.pvplikeaboss.sinistercore.sinistercore.utilites.misc.Cooldown;
import me.pvplikeaboss.sinistercore.sinistercore.utilites.misc.Messages;
import me.pvplikeaboss.sinistercore.sinistercore.utilites.misc.Misc;
import me.pvplikeaboss.sinistercore.sinistercore.utilites.serverutils.BroadcastUtils;
import me.pvplikeaboss.sinistercore.sinistercore.utilites.serverutils.PlayerUtils;

public class Utilities {
    private SinisterCore plugin;

    /*

    Misc Utils

    */

    public Misc misc = null;
    public Messages msgs = null;
    public Cooldown cooldown = null;

    /*

    Server Utils

    */
    
    public BroadcastUtils broadcast = null;
    public PlayerUtils player = null;


    public Utilities(SinisterCore p) {
        this.plugin = p;
    }

    public void load() {
        this.misc = new Misc();
        this.misc.load(plugin);

        this.msgs = new Messages(plugin);

        this.player = new PlayerUtils(plugin);

        this.cooldown = new Cooldown(plugin);
        this.cooldown.loadKitCooldown();

        this.broadcast = new BroadcastUtils(plugin);
    }

    public Object getUtil(int id) {
        switch(id) {
            case 0: {
                return misc;
            }
            case 1: {
                return cooldown;
            }
            case 2: {
                return msgs;
            }
            case 3: {
                return player;
            }
            case 4: {
                return broadcast;
            }
        }
        return null;
    }
}
