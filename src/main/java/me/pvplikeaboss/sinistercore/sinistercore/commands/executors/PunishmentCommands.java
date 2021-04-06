package me.pvplikeaboss.sinistercore.sinistercore.commands.executors;

import me.pvplikeaboss.sinistercore.sinistercore.Instances;
import me.pvplikeaboss.sinistercore.sinistercore.SinisterCore;
import me.pvplikeaboss.sinistercore.sinistercore.commands.util.CommandContext;
import me.pvplikeaboss.sinistercore.sinistercore.objects.PlayerObject;
import me.pvplikeaboss.sinistercore.sinistercore.utilites.misc.Messages;
import me.pvplikeaboss.sinistercore.sinistercore.utilites.misc.Time;
import me.pvplikeaboss.sinistercore.sinistercore.utilites.serverutils.PlayerUtils;

import java.util.Date;

public class PunishmentCommands {
    private static SinisterCore plugin;
    private static Messages utilMsgs;
    private static PlayerUtils utilPlayer;

    public boolean onCommand(String name, CommandContext context) {
        plugin = context.getPlugin();
        utilMsgs = (Messages) Instances.getInstance(Instances.InstanceType.Utilities, 2);
        utilPlayer = (PlayerUtils) Instances.getInstance(Instances.InstanceType.Utilities, 3);
        
        if(name.equalsIgnoreCase("kick")) {
            PlayerObject sender = null;
            if(context.isPlayer()) {
                sender = plugin.getPlayer(context.getPlayer().getUniqueId());
            }
            if((context.getArgs().size() >= 1)) {
                String playerName = context.argAt(0);
                String reason = "&4&lYou've been kicked";
                if(context.getArgs().size() >= 2) {
                    StringBuilder reasonBuilder = new StringBuilder();
                    for(int i = 1; i < context.getArgs().size(); i++) {
                        reasonBuilder.append(context.argAt(i) + " ");
                    }
                    reason = reasonBuilder.toString();
                }

                String newName;
                if((newName = utilPlayer.playerExists(playerName)) != null) {
                    PlayerObject player = plugin.getPlayer(newName);

                    player.kickPlayer(reason);
                    if(sender != null) {
                        utilMsgs.infoMessage(sender, "&6Player &b" + newName + "&6 has been kicked for the reason &b" + reason);
                    } else {
                        utilMsgs.logInfoMessage("Player "+newName+" has been kicked for the reason "+reason);
                    }
                } else {
                    if(context.isPlayer()) {
                        utilMsgs.errorMessage(sender, "Player doesn't exist!");
                    } else {
                        utilMsgs.logErrorMessage("Player doesn't exist!");
                    }
                }
            } else {
                if(context.isPlayer()) {
                    utilMsgs.errorMessage(sender, "&bUsage: /kick <name> <reason>");
                } else {
                    utilMsgs.logErrorMessage("&bUsage: /kick <name> <reason>");
                }
            }
            return true;
        } else if(name.equalsIgnoreCase("mute")) {
            PlayerObject sender = null;
            if(context.isPlayer()) {
                sender = plugin.getPlayer(context.getPlayer().getUniqueId());
            }
            if((context.getArgs().size() >= 1)) {
                String playerName = context.argAt(0);
                String reason = "none";
                if(context.getArgs().size() >= 2) {
                    StringBuilder reasonBuilder = new StringBuilder();
                    for(int i = 1; i < context.getArgs().size(); i++) {
                        reasonBuilder.append(context.argAt(i) + " ");
                    }
                    reason = reasonBuilder.toString();
                }

                String newName;
                if((newName = utilPlayer.playerExists(playerName)) != null) {
                    PlayerObject player = plugin.getPlayer(newName);

                    player.setMute(sender, reason, null, true);
                    if(sender != null) {
                        utilMsgs.infoMessage(sender, "&6Player &b" + newName + "&6 has been muted for the reason &b" + reason);
                    } else {
                        utilMsgs.logInfoMessage("Player "+newName+" has been muted for the reason "+reason);
                    }
                    if(player.isPlayerOnline) {
                        utilMsgs.infoMessage(player, "&6You've been muted for the reason &b" + reason);
                    }
                } else {
                    if(context.isPlayer()) {
                        utilMsgs.errorMessage(sender, "Player doesn't exist!");
                    } else {
                        utilMsgs.logErrorMessage("Player doesn't exist!");
                    }
                }
            } else {
                if(context.isPlayer()) {
                    utilMsgs.errorMessage(sender, "&bUsage: /mute <name> <reason>");
                } else {
                    utilMsgs.logErrorMessage("&bUsage: /mute <name> <reason>");
                }
            }
            return true;
        } else if(name.equalsIgnoreCase("tempmute")) {
            PlayerObject sender = null;
            if(context.isPlayer()) {
                sender = plugin.getPlayer(context.getPlayer().getUniqueId());
            }
            if((context.getArgs().size() >= 2)) {
                String playerName = context.argAt(0);
                String length = context.argAt(1);
                String reason = "&4&lThe Ban Hammer Has Spoken";
                if(context.getArgs().size() >= 3) {
                    StringBuilder reasonBuilder = new StringBuilder();
                    for(int i = 2; i < context.getArgs().size(); i++) {
                        reasonBuilder.append(context.argAt(i) + " ");
                    }
                    reason = reasonBuilder.toString();
                }

                String newName;
                if((newName = utilPlayer.playerExists(playerName)) != null) {
                    PlayerObject player = plugin.getPlayer(newName);

                    Date endDate;
                    endDate = Time.parseDate(length);
                    if(endDate == null) {
                        if(sender != null) {
                            utilMsgs.infoMessage(sender, "&6Invalid time... Try (/tempmute <player> 15d <reason>)");
                        } else {
                            utilMsgs.logInfoMessage("&6Invalid time... Try (/tempmute <player> 15d <reason>)");
                        }
                        return true;
                    }

                    player.setMute(sender, reason, endDate, true);
                    if(sender != null) {
                        utilMsgs.infoMessage(sender, "&6Player &b" + newName + "&6 has been temp muted for &b"+length+"&6 the reason &b" + reason);
                    } else {
                        utilMsgs.logInfoMessage("Player "+newName+" has been temp muted for "+length+" for the reason "+reason);
                    }
                    if(player.isPlayerOnline) {
                        utilMsgs.infoMessage(player, "&6You've been temp muted for &b"+length+"&6 for the reason &b" + reason);
                    }
                } else {
                    if(context.isPlayer()) {
                        utilMsgs.errorMessage(sender, "Player doesn't exist!");
                    } else {
                        utilMsgs.logErrorMessage("Player doesn't exist!");
                    }
                }
            } else {
                if(context.isPlayer()) {
                    utilMsgs.errorMessage(sender, "&bUsage: /tempmute <name> <length> <reason>");
                } else {
                    utilMsgs.logErrorMessage("&bUsage: /tempmute <name> <length> <reason>");
                }
            }
            return true;
        } else if(name.equalsIgnoreCase("unmute")) {
            PlayerObject sender = null;
            if(context.isPlayer()) {
                sender = plugin.getPlayer(context.getPlayer().getUniqueId());
            }
            if((context.getArgs().size() == 1)) {
                String playerName = context.argAt(0);
                String newName;
                if((newName = utilPlayer.playerExists(playerName)) != null) {
                    PlayerObject player = plugin.getPlayer(newName);
                    player.setMute(null, null, null, false);
                    if(sender != null) {
                        utilMsgs.infoMessage(sender, "&6Player &b" + newName + "&6 has been unmuted");
                    } else {
                        utilMsgs.logInfoMessage("Player "+newName+" has been unmuted");
                    }
                } else {
                    if(context.isPlayer()) {
                        utilMsgs.errorMessage(sender, "Player doesn't exist!");
                    } else {
                        utilMsgs.logErrorMessage("Player doesn't exist!");
                    }
                }
            } else {
                if(context.isPlayer()) {
                    utilMsgs.errorMessage(sender, "&bUsage: /unmute <name>");
                } else {
                    utilMsgs.logErrorMessage("&bUsage: /unmute <name>");
                }
            }
            return true;
        } else if(name.equalsIgnoreCase("warn")) {

        } else if(name.equalsIgnoreCase("unwarn")) {

        } else if(name.equalsIgnoreCase("ban")) {// ban player reason
            PlayerObject sender = null;
            if(context.isPlayer()) {
                sender = plugin.getPlayer(context.getPlayer().getUniqueId());
            }
            if((context.getArgs().size() >= 1)) {
                String playerName = context.argAt(0);
                String reason = "&4&lThe Ban Hammer Has Spoken";
                if(context.getArgs().size() >= 2) {
                    StringBuilder reasonBuilder = new StringBuilder();
                    for(int i = 1; i < context.getArgs().size(); i++) {
                        reasonBuilder.append(context.argAt(i) + " ");
                    }
                    reason = reasonBuilder.toString();
                }

                String newName;
                if((newName = utilPlayer.playerExists(playerName)) != null) {
                    PlayerObject player = plugin.getPlayer(newName);

                    player.setBanned(sender, reason, null, true);
                    if(sender != null) {
                        utilMsgs.infoMessage(sender, "&6Player &b" + newName + "&6 has been banned for the reason &b" + reason);
                    } else {
                        utilMsgs.logInfoMessage("Player "+newName+" has been banned for the reason "+reason);
                    }
                } else {
                    if(context.isPlayer()) {
                        utilMsgs.errorMessage(sender, "Player doesn't exist!");
                    } else {
                        utilMsgs.logErrorMessage("Player doesn't exist!");
                    }
                }
            } else {
                if(context.isPlayer()) {
                    utilMsgs.errorMessage(sender, "&bUsage: /ban <name> <reason>");
                } else {
                    utilMsgs.logErrorMessage("&bUsage: /ban <name> <reason>");
                }
            }
            return true;
        } else if(name.equalsIgnoreCase("tempban")) {
            PlayerObject sender = null;
            if(context.isPlayer()) {
                sender = plugin.getPlayer(context.getPlayer().getUniqueId());
            }
            if((context.getArgs().size() >= 2)) {
                String playerName = context.argAt(0);
                String length = context.argAt(1);
                String reason = "&4&lThe Ban Hammer Has Spoken";
                if(context.getArgs().size() >= 3) {
                    StringBuilder reasonBuilder = new StringBuilder();
                    for(int i = 2; i < context.getArgs().size(); i++) {
                        reasonBuilder.append(context.argAt(i) + " ");
                    }
                    reason = reasonBuilder.toString();
                }

                String newName;
                if((newName = utilPlayer.playerExists(playerName)) != null) {
                    PlayerObject player = plugin.getPlayer(newName);

                    Date endDate;
                    endDate = Time.parseDate(length);
                    if(endDate == null) {
                        if(sender != null) {
                            utilMsgs.infoMessage(sender, "&6Invalid time... Try (/tempban <player> 15d <reason>)");
                        } else {
                            utilMsgs.logInfoMessage("&6Invalid time... Try (/tempban <player> 15d <reason>)");
                        }
                        return true;
                    }

                    player.setBanned(sender, reason, endDate, true);
                    if(sender != null) {
                        utilMsgs.infoMessage(sender, "&6Player &b" + newName + "&6 has been temp banned for "+length+" the reason &b" + reason);
                    } else {
                        utilMsgs.logInfoMessage("Player "+newName+" has been temp banned for "+length+" the reason "+reason);
                    }
                } else {
                    if(context.isPlayer()) {
                        utilMsgs.errorMessage(sender, "Player doesn't exist!");
                    } else {
                        utilMsgs.logErrorMessage("Player doesn't exist!");
                    }
                }
            } else {
                if(context.isPlayer()) {
                    utilMsgs.errorMessage(sender, "&bUsage: /tempban <name> <length> <reason>");
                } else {
                    utilMsgs.logErrorMessage("&bUsage: /tempban <name> <length> <reason>");
                }
            }
            return true;
        } else if(name.equalsIgnoreCase("unban")) {
            PlayerObject sender = null;
            if(context.isPlayer()) {
                sender = plugin.getPlayer(context.getPlayer().getUniqueId());
            }
            if((context.getArgs().size() == 1)) {
                String playerName = context.argAt(0);
                String newName;
                if((newName = utilPlayer.playerExists(playerName)) != null) {
                    PlayerObject player = plugin.getPlayer(newName);
                    player.setBanned(null, null, null, false);
                    if(sender != null) {
                        utilMsgs.infoMessage(sender, "&6Player &b" + newName + "&6 has been unbanned");
                    } else {
                        utilMsgs.logInfoMessage("Player "+newName+" has been unbanned");
                    }
                } else {
                    if(context.isPlayer()) {
                        utilMsgs.errorMessage(sender, "Player doesn't exist!");
                    } else {
                        utilMsgs.logErrorMessage("Player doesn't exist!");
                    }
                }
            } else {
                if(context.isPlayer()) {
                    utilMsgs.errorMessage(sender, "&bUsage: /unban <name>");
                } else {
                    utilMsgs.logErrorMessage("&bUsage: /unban <name>");
                }
            }
            return true;
        } else if(name.equalsIgnoreCase("history")) {

        } else if(name.equalsIgnoreCase("")) {

        } else if(name.equalsIgnoreCase("")) {

        }
        return false;
    }
}
