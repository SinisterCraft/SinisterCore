package me.pvplikeaboss.sinistercore.sinistercore.commands.executors;

import me.pvplikeaboss.sinistercore.sinistercore.Instances;
import me.pvplikeaboss.sinistercore.sinistercore.SinisterCore;
import me.pvplikeaboss.sinistercore.sinistercore.commands.util.CommandContext;
import me.pvplikeaboss.sinistercore.sinistercore.objects.PlayerObject;
import me.pvplikeaboss.sinistercore.sinistercore.utilites.misc.Messages;
import me.pvplikeaboss.sinistercore.sinistercore.utilites.serverutils.PlayerUtils;

public class SinisterCommands {
    private static SinisterCore plugin;
    private static Messages utilMsgs;
    private static PlayerUtils utilPlayer;

    public boolean onCommand(String name, CommandContext context) {
        plugin = context.getPlugin();
        utilMsgs = (Messages) Instances.getInstance(Instances.InstanceType.Utilities, 2);
        utilPlayer = (PlayerUtils) Instances.getInstance(Instances.InstanceType.Utilities, 3);

        PlayerObject sender = null;
        if(context.isPlayer()) {
            sender = plugin.getPlayer(context.getPlayer().getUniqueId());
        }

        if(name.equalsIgnoreCase("sinistercore")) {
            if(context.getArgs().size() > 0) {
                String subCMD = context.argAt(0);
                if(subCMD.equalsIgnoreCase("reload")) {
                    plugin.reloadAll();
                    if(context.isPlayer()) {
                        utilMsgs.infoMessage(sender, "&9Reloaded SinisterCore");
                    } else {
                        utilMsgs.logInfoMessage("Reloaded SinisterCore");
                    }
                    return true;
                }
            }
            if(context.isPlayer()) {
                utilMsgs.infoMessage(sender, "Usage: /sinistercore reload");
            } else {
                utilMsgs.logInfoMessage("Usage: /sinistercore reload");
            }
            return true;
        }

        return false;
    }
}
