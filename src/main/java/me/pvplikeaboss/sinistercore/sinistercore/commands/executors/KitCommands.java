package me.pvplikeaboss.sinistercore.sinistercore.commands.executors;

import me.pvplikeaboss.sinistercore.sinistercore.Instances;
import me.pvplikeaboss.sinistercore.sinistercore.SinisterCore;
import me.pvplikeaboss.sinistercore.sinistercore.commands.util.CommandContext;
import me.pvplikeaboss.sinistercore.sinistercore.modules.data.files.configs.yml.KitConfig;
import me.pvplikeaboss.sinistercore.sinistercore.objects.KitObject;
import me.pvplikeaboss.sinistercore.sinistercore.objects.PlayerObject;
import me.pvplikeaboss.sinistercore.sinistercore.utilites.misc.Cooldown;
import me.pvplikeaboss.sinistercore.sinistercore.utilites.misc.Messages;
import me.pvplikeaboss.sinistercore.sinistercore.utilites.serverutils.PlayerUtils;

import java.util.List;

public class KitCommands {
    private static SinisterCore plugin = null;
    private static KitConfig cfgKits = null;
    private static Messages utilMsgs = null;
    private static Cooldown utilCooldown = null;
    private static PlayerUtils utilPlayers = null;

    public boolean onCommand(String name, CommandContext context) {
        plugin = context.getPlugin();
        cfgKits = (KitConfig) Instances.getInstance(Instances.InstanceType.Config, 1);
        utilMsgs = (Messages) Instances.getInstance(Instances.InstanceType.Utilities, 2);
        utilCooldown = (Cooldown) Instances.getInstance(Instances.InstanceType.Utilities, 1);
        utilPlayers = (PlayerUtils) Instances.getInstance(Instances.InstanceType.Utilities, 3);
        List<String> args = context.getArgs();

        PlayerObject sender = null;
        if(context.isPlayer()) {
            sender = plugin.getPlayer(context.getPlayer().getUniqueId());
        }

        if (name.equalsIgnoreCase("kit")) {
            if (args.size() == 1) {
                if (sender != null) {
                    String kitName = args.get(0);
                    KitObject kit = new KitObject(plugin, kitName);
                    if (kit.kitExists()) {
                        String timeLeft = utilCooldown.isKitCooldown(sender.playerUUID, kit.kitName);

                        if(kit.delay == -1 && timeLeft == null) {
                            if(!kit.giveKit(sender, false)) {
                                utilMsgs.errorMessage(sender, "&7No permissions for kit!");
                                return true;
                            }
                            utilCooldown.putKitCooldown(sender.playerUUID, kit.kitName, kit.delay);
                            utilMsgs.infoMessage(sender, "&7Given kit &6" + kitName + "&7!");
                            return true;
                        } else if(kit.delay == -1 && timeLeft != null) {
                            utilMsgs.infoMessage(sender, "&7Kit &6" + kitName + "&7 is a one time kit!");
                            return true;
                        } else if (timeLeft != null) {
                            utilMsgs.infoMessage(sender, "&7You will recieve kit &6" + kitName + " &7in &6" + timeLeft + "&7!");
                            return true;
                        } else if (!kit.giveKit(sender, false)) {
                            utilMsgs.errorMessage(sender, "&7No permissions for kit!");
                            return true;
                        } else {
                            utilMsgs.infoMessage(sender, "&7Given kit &6" + kitName + "&7!");
                            utilCooldown.putKitCooldown(sender.playerUUID, kit.kitName, kit.delay);
                            return true;
                        }
                    } else {
                        utilMsgs.infoMessage(sender, "&7Kit &6" + kitName + "&7 not exists!");
                    }
                    return true;
                } else {
                    utilMsgs.logErrorMessage("&7Console Cant Run This Command!");
                }
                return true;
            } else if (args.size() == 2) {
                String kitName = args.get(0);
                String target = args.get(1);
                String newTarget = null;
                if ((newTarget = utilPlayers.playerOnline(target)) != null) {
                    KitObject kit = new KitObject(plugin, kitName);
                    PlayerObject p = plugin.getPlayer(newTarget);
                    if (kit.kitExists()) {
                        if (!kit.giveKit(p, true)) {
                            if(sender != null) {
                                utilMsgs.errorMessage(sender, "&7No permissions for kit!");
                            } else {
                                utilMsgs.logErrorMessage("&7No permissions for kit!");
                            }
                            return true;
                        } else {
                            if (sender != null) {
                                utilMsgs.infoMessage(sender, "&7Given player &6" + p.playerName + " &7kit &6" + kitName + "&7!");
                            } else {
                                utilMsgs.logInfoMessage("&7Given player &6" + p.playerName + " &7kit &6" + kitName + "&7!");
                            }
                            return true;
                        }
                    } else {
                        if (sender != null) {
                            utilMsgs.infoMessage(sender, "&7Kit &6" + kitName + "&7 not exists!");
                        } else {
                            utilMsgs.logInfoMessage("&7Kit &6" + kitName + "&7 not exists!");
                        }
                        return true;
                    }
                } else {
                    if (sender != null) {
                        utilMsgs.errorMessage(sender, "&7Player &6" +target+ " &7Offline/Not exists!");
                    } else {
                        utilMsgs.logErrorMessage("&7Player &6" +target+ " &7Offline/Not exists!");
                    }
                    return true;
                }
            } else if (args.size() == 0) {
                if(cfgKits.getConfig().isSet("kits")) {
                    StringBuilder kits = new StringBuilder();
                    int kits_size = 0;
                    for (String kit : cfgKits.getConfig().getConfigurationSection("kits").getKeys(false)) {
                        if(context.isPlayer()) {
                            if(!sender.getPlayer().hasPermission("sinistercore.kit."+kit.toLowerCase())) {
                                continue;
                            }
                        }
                        kits.append(kit+" ");
                        kits_size++;
                    }
                    if(kits_size > 0) {
                        if (sender != null) {
                            utilMsgs.infoMessage(sender, "&7Available Kits: &6" + kits);
                        } else {
                            utilMsgs.logInfoMessage("&7Available Kits: &6" + kits);
                        }
                        return true;
                    }// else fall back to no available kits
                }
                if (sender != null) {
                    utilMsgs.errorMessage(sender, "&7No Available Kits!");
                } else {
                    utilMsgs.logErrorMessage("&7No Available Kits!");
                }
                return true;
            } else {
                if (sender != null) {
                    utilMsgs.errorMessage(sender, "&7Usage: /kit <name>");
                } else {
                    utilMsgs.logErrorMessage("&7Usage: /kit <name> <player>");
                }
                return true;
            }
        } else if (name.equalsIgnoreCase("createkit")) {
            if (sender != null) {
                if (args.size() == 2) {
                    String kitName = args.get(0);
                    int delay = Integer.parseInt(args.get(1));
                    KitObject kit = new KitObject(plugin, kitName);
                    kit.createKit(sender, delay);
                    utilMsgs.infoMessage(sender, "&7Created Kit &6" + kitName + "&7!");
                } else {
                    utilMsgs.errorMessage(sender, "&7Usage: /createkit <name> <delay>");
                }
                return true;
            } else {
                utilMsgs.logErrorMessage("&7Console Cant Run This Command!");
            }
            return true;
        } else if (name.equalsIgnoreCase("deletekit")) {
            if (args.size() == 1) {
                String kitName = args.get(0);
                KitObject kit = new KitObject(plugin, kitName);
                if (kit.deleteKit()) {
                    if (sender != null) {
                        utilMsgs.infoMessage(sender, "&7Deleted Kit &6" + kitName + "&7!");
                    } else {
                        utilMsgs.logInfoMessage("&7Deleted Kit &6" + kitName + "&7!");
                    }
                    return true;
                } else {
                    if (sender != null) {
                        utilMsgs.errorMessage(sender, "&7Kit &6" + kitName + " &7not exists!");
                    } else {
                        utilMsgs.logErrorMessage("&7Kit &6" + kitName + " &7not exists!");
                    }
                    return true;
                }
            } else {
                if (sender != null) {
                    utilMsgs.errorMessage(sender, "&7Usage: /deletekit <name>");
                } else {
                    utilMsgs.logErrorMessage("&7Usage: /deletekit <name>");
                }
                return true;
            }
        }
        return false;
    }
}
