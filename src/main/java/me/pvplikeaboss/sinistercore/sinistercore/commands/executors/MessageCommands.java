package me.pvplikeaboss.sinistercore.sinistercore.commands.executors;

import me.pvplikeaboss.sinistercore.sinistercore.Instances;
import me.pvplikeaboss.sinistercore.sinistercore.SinisterCore;
import me.pvplikeaboss.sinistercore.sinistercore.commands.util.CommandContext;
import me.pvplikeaboss.sinistercore.sinistercore.objects.PlayerObject;
import me.pvplikeaboss.sinistercore.sinistercore.utilites.misc.Messages;
import me.pvplikeaboss.sinistercore.sinistercore.utilites.serverutils.PlayerUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MessageCommands {
    private static SinisterCore plugin;
    private static PlayerUtils utilPlayers;
    private static Messages utilMsgs;
    public static HashMap<UUID, UUID> messageHashMap;

    private static void sendMessage(PlayerObject from, PlayerObject to, String message) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("&8[&6Private-Message&8] ");
        stringBuilder.append("&8[&6"+from.playerName+"&8] &9-> ");
        stringBuilder.append("&b"+message);
        to.sendMessage(stringBuilder.toString());
        from.sendMessage(stringBuilder.toString());
    }

    public boolean onCommand(String name, CommandContext context) {
        plugin = context.getPlugin();
        utilPlayers = (PlayerUtils) Instances.getInstance(Instances.InstanceType.Utilities, 3);
        utilMsgs = (Messages) Instances.getInstance(Instances.InstanceType.Utilities, 2);
        messageHashMap = (HashMap<UUID, UUID>) Instances.getInstance(Instances.InstanceType.HashMap, 1);
        for(String cmdName : plugin.getConfig().getConfigurationSection("cmd-messages").getKeys(false)) {
            if(name.equalsIgnoreCase(cmdName)) {
                for(String line : plugin.getConfig().getStringList("cmd-messages."+cmdName)) {
                    context.send(line);
                }
                return true;
            }
        }

        PlayerObject sender = null;
        if(context.isPlayer()) {
            sender = plugin.getPlayer(context.getPlayer().getUniqueId());
        }

        if(name.equalsIgnoreCase("tell") || name.equalsIgnoreCase("msg") || name.equalsIgnoreCase("t")) {
            if(sender != null) {
                if(context.getArgs().size() > 1) {
                    String targetName = context.argAt(0);
                    if((targetName = utilPlayers.playerExists(targetName)) != null) {
                        if(utilPlayers.playerOnline(targetName) != null) {
                            PlayerObject targetPlayer = plugin.getPlayer(targetName);

                            if(targetPlayer.getRecieveMsgs() == false) {
                                utilMsgs.errorMessage(sender, "&9Recipient has private messages disabled");
                                return true;
                            }

                            for (Map.Entry<UUID, UUID> entry : messageHashMap.entrySet()) {
                                if ((sender.playerUUID.compareTo(entry.getKey()) == 0) || (targetPlayer.playerUUID.compareTo(entry.getKey()) == 0)) {
                                    messageHashMap.remove(entry.getKey());
                                } else if ((sender.playerUUID.compareTo(entry.getValue()) == 0) || (targetPlayer.playerUUID.compareTo(entry.getValue()) == 0)) {
                                    messageHashMap.remove(entry.getKey());
                                }
                            }

                            StringBuilder messageBuilder = new StringBuilder();
                            int i;
                            for(i = 1; i < context.getArgs().size(); i++) {
                                messageBuilder.append(context.argAt(i));
                                if(i == context.getArgs().size()-1) {
                                    continue;
                                }
                                messageBuilder.append(" ");
                            }

                            sendMessage(sender, targetPlayer, messageBuilder.toString());

                            messageHashMap.put(sender.playerUUID, targetPlayer.playerUUID);
                            return true;
                        } else {
                            utilMsgs.errorMessage(sender, "&9Player not online!");
                            return true;
                        }
                    } else {
                        utilMsgs.errorMessage(sender, "&9Player not exists!");
                        return true;
                    }
                } else {
                    utilMsgs.errorMessage(sender, "&9Usage: /msg <player> <message>");
                    return true;
                }
            } else {
                utilMsgs.logErrorMessage("Console isnt aloud this command!");
                return true;
            }
        } else if(name.equalsIgnoreCase("msgtoggle")) {
            if(sender != null) {
                boolean state = sender.getRecieveMsgs();
                if(state == true) {
                    sender.setRecieveMsgs(false);
                    utilMsgs.infoMessage(sender, "&8[&6Private-Message&8] Disabled (you wont recieve pms)");
                } else {
                    sender.setRecieveMsgs(true);
                    utilMsgs.infoMessage(sender, "&8[&6Private-Message&8] Enabled (you will recieve pms)");
                }
                return true;
            } else {
                utilMsgs.logErrorMessage("Console isnt aloud this command!");
                return true;
            }
        } else if(name.equalsIgnoreCase("r") || name.equalsIgnoreCase("reply")) {
            if(sender != null) {
                if(context.getArgs().size() > 0) {
                    UUID targetUUID = null;
                    for (Map.Entry<UUID, UUID> entry : messageHashMap.entrySet()) {
                        if (sender.playerUUID.compareTo(entry.getKey()) == 0) {
                            targetUUID = entry.getValue();
                        } else if (sender.playerUUID.compareTo(entry.getValue()) == 0) {
                            targetUUID = entry.getKey();
                        }
                    }

                    if (targetUUID == null) {
                        utilMsgs.errorMessage(sender, "&9No active messages");
                        return true;
                    }

                    PlayerObject targetPlayer = plugin.getPlayer(targetUUID);

                    if(targetPlayer.getRecieveMsgs() == false) {
                        utilMsgs.errorMessage(sender, "&9Recipient has private messages disabled");
                        return true;
                    }

                    StringBuilder messageBuilder = new StringBuilder();
                    int i;
                    for (i = 0; i < context.getArgs().size(); i++) {
                        messageBuilder.append(context.argAt(i));
                        if (i == context.getArgs().size() - 1) {
                            continue;
                        }
                        messageBuilder.append(" ");
                    }

                    sendMessage(sender, targetPlayer, messageBuilder.toString());
                    return true;
                } else {
                    utilMsgs.errorMessage(sender, "&9Usage: /r <message>");
                    return true;
                }
            } else {
                utilMsgs.logErrorMessage("Console isnt aloud this command!");
                return true;
            }
        }
        return false;
    }
}
