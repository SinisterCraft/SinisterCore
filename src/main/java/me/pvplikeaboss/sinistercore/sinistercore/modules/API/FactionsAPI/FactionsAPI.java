package me.pvplikeaboss.sinistercore.sinistercore.modules.API.FactionsAPI;

import com.massivecraft.factions.P;
import me.pvplikeaboss.sinistercore.sinistercore.Instances;
import me.pvplikeaboss.sinistercore.sinistercore.SinisterCore;
import me.pvplikeaboss.sinistercore.sinistercore.objects.PlayerObject;
import me.pvplikeaboss.sinistercore.sinistercore.utilites.misc.Messages;

public class FactionsAPI {
    private static SinisterCore plugin = null;
    private static Messages utilMsgs = null;
    private static P factionsPlugin = null;
    private static P factionsInstance = null;

    public static void load(SinisterCore p) {
        if(plugin == null) {
            plugin = p;
        }

        if(utilMsgs == null) {
            utilMsgs = (Messages) Instances.getInstance(Instances.InstanceType.Utilities, 2);
        }

        if(factionsPlugin == null) {
            if ((factionsPlugin = (P)plugin.getServer().getPluginManager().getPlugin("Factions")) == null) {
                utilMsgs.logErrorMessage("Factions not found");
                return;
            }
        }

        if(factionsPlugin != null && factionsInstance == null) {
            factionsInstance = factionsPlugin;
        }
    }

    public static String getPlayerFactionName(SinisterCore plugin, PlayerObject player) {
        load(plugin);
        if(factionsInstance == null) {
            utilMsgs.logErrorMessage("[getPlayerFactionName] factionsInstance is null!");
            return null;
        }

        if(player == null) {
            utilMsgs.logErrorMessage("[getPlayerFactionName] PlayerObject is null!");
            return null;
        }

        if(!player.isPlayerOnline) {
            utilMsgs.logErrorMessage("[getPlayerFactionName] Player is not online!");
            return null;
        }

        try {
            return factionsInstance.getPlayerFactionTag(player.getPlayer());
        } catch(NullPointerException e) {
            return "Wilderness";
        }
    }

    public static String getPlayerFactionTagRelation(SinisterCore plugin, PlayerObject mainPlayer, PlayerObject playerTwo) {
        load(plugin);
        if(factionsInstance == null) {
            utilMsgs.logErrorMessage("[getPlayerFactionTagRelation] factionsInstance is null!");
            return null;
        }

        if(!mainPlayer.isPlayerOnline) {
            return null;
        }

        if(!playerTwo.isPlayerOnline) {
            return null;
        }

        return factionsInstance.getPlayerFactionTagRelation(mainPlayer.getPlayer(), playerTwo.getPlayer());
    }
}
