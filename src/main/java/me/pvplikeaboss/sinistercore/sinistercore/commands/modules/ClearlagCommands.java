package me.pvplikeaboss.sinistercore.sinistercore.commands.modules;

import me.pvplikeaboss.sinistercore.sinistercore.Instances;
import me.pvplikeaboss.sinistercore.sinistercore.SinisterCore;
import me.pvplikeaboss.sinistercore.sinistercore.commands.util.CommandContext;
import me.pvplikeaboss.sinistercore.sinistercore.modules.clearlag.ClearlagModule;
import me.pvplikeaboss.sinistercore.sinistercore.objects.PlayerObject;
import me.pvplikeaboss.sinistercore.sinistercore.utilites.misc.Messages;
import me.pvplikeaboss.sinistercore.sinistercore.utilites.serverutils.BroadcastUtils;

public class ClearlagCommands {
    private static SinisterCore plugin;
    private static Messages utilMsgs;
    private static BroadcastUtils utilBroadcast;

    public boolean onCommand(String name, CommandContext context) {
        plugin = context.getPlugin();
        utilMsgs = (Messages) Instances.getInstance(Instances.InstanceType.Utilities, 2);
        utilBroadcast = (BroadcastUtils) Instances.getInstance(Instances.InstanceType.Utilities, 4);

        PlayerObject sender = null;
        if(context.isPlayer()) {
            sender = plugin.getPlayer(context.getPlayer().getUniqueId());
        }

        if(name.equalsIgnoreCase("clearlag")) {
            if(context.getArgs().size() > 0) {
                String subcmd = context.argAt(0);
                if(subcmd.equalsIgnoreCase("autotoggle")) {// Starts clearlag module
                    if(ClearlagModule.getRunning()) {
                        ClearlagModule.stopClearLagModule();
                        if(sender != null) {
                            utilMsgs.infoMessage(sender, "&9Stopped clear lag module!");
                            return true;
                        }
                        utilMsgs.logInfoMessage("Stopped clear lag module!");
                        return true;
                    }
                    ClearlagModule.startClearLagModule();
                    if(sender != null) {
                        utilMsgs.infoMessage(sender, "&9Started clear lag module!");
                        return true;
                    }
                    utilMsgs.logInfoMessage("Started clear lag module!");
                    return true;
                } else if(subcmd.equalsIgnoreCase("lagg")) {// runs clearlag once
                    int removed = ClearlagModule.clearLag(true);
                    utilBroadcast.rawBroadcast("&9Sinister&3Lagg &8>> &bCleared &9"+removed+" &bentities from server!");
                    return true;
                }
            }
            if(sender != null) {
                utilMsgs.infoMessage(sender, "&6Usage: &9/clearlag autotoggle");
                utilMsgs.infoMessage(sender, "&6Usage: &9/clearlag lagg");
                return true;
            }
            utilMsgs.logInfoMessage("Usage: /clearlag autotoggle");
            utilMsgs.logInfoMessage("Usage: /clearlag lagg");
            return true;
        }
        return false;
    }
}
