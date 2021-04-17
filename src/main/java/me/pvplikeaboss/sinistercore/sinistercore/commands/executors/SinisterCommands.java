package me.pvplikeaboss.sinistercore.sinistercore.commands.executors;

import me.pvplikeaboss.sinistercore.sinistercore.Instances;
import me.pvplikeaboss.sinistercore.sinistercore.SinisterCore;
import me.pvplikeaboss.sinistercore.sinistercore.commands.util.CommandContext;
import me.pvplikeaboss.sinistercore.sinistercore.modules.data.PlayerData;
import me.pvplikeaboss.sinistercore.sinistercore.modules.economy.EconomyData;
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
                        utilMsgs.infoMessage(sender, "&7Reloaded SinisterCore");
                    } else {
                        utilMsgs.logInfoMessage("Reloaded SinisterCore");
                    }
                    return true;
                } else if(subCMD.equalsIgnoreCase("convert")) {
                    subCMD = context.argAt(1);
                    if(subCMD.equalsIgnoreCase("fromdatabase")) {
                        PlayerData.convert(plugin, false);
                        EconomyData.convert(plugin, false);
                        if(context.isPlayer()) {
                            utilMsgs.infoMessage(sender, "&7Converted SinisterCore data to files");
                        } else {
                            utilMsgs.logInfoMessage("&7Converted SinisterCore data to files");
                        }
                        return true;
                    } else if(subCMD.equalsIgnoreCase("fromfile")) {
                        PlayerData.convert(plugin, true);
                        EconomyData.convert(plugin, true);
                        if(context.isPlayer()) {
                            utilMsgs.infoMessage(sender, "&7Converted SinisterCore data to database");
                        } else {
                            utilMsgs.logInfoMessage("&7Converted SinisterCore data to database");
                        }
                        return true;
                    }
                }
            }
            if(context.isPlayer()) {
                utilMsgs.infoMessage(sender, "&7Usage: /sinistercore reload");
                utilMsgs.infoMessage(sender, "&7Usage: /sinistercore convert fromdatabase");
                utilMsgs.infoMessage(sender, "&7Usage: /sinistercore convert fromfile");
            } else {
                utilMsgs.logInfoMessage("&7Usage: /sinistercore reload");
                utilMsgs.logInfoMessage("&7Usage: /sinistercore convert fromdatabase");
                utilMsgs.logInfoMessage("&7Usage: /sinistercore convert fromfile");
            }
            return true;
        }

        return false;
    }
}
