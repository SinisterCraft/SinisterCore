package me.pvplikeaboss.sinistercore.sinistercore.commands.executors;

import me.pvplikeaboss.sinistercore.sinistercore.Instances;
import me.pvplikeaboss.sinistercore.sinistercore.SinisterCore;
import me.pvplikeaboss.sinistercore.sinistercore.commands.util.CommandContext;
import me.pvplikeaboss.sinistercore.sinistercore.modules.files.configs.yml.KitConfig;
import me.pvplikeaboss.sinistercore.sinistercore.objects.KitObject;
import me.pvplikeaboss.sinistercore.sinistercore.objects.PlayerObject;
import me.pvplikeaboss.sinistercore.sinistercore.utilites.misc.Cooldown;
import me.pvplikeaboss.sinistercore.sinistercore.utilites.misc.Messages;
import me.pvplikeaboss.sinistercore.sinistercore.utilites.serverutils.PlayerUtils;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

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
                                utilMsgs.errorMessage(sender, "&aNo permissions for kit!");
                                return true;
                            }
                            utilCooldown.putKitCooldown(sender.playerUUID, kit.kitName, kit.delay);
                            utilMsgs.infoMessage(sender, "&aGiven kit &b" + kitName + "&a!");
                            return true;
                        } else if(kit.delay == -1 && timeLeft != null) {
                            utilMsgs.infoMessage(sender, "&aKit &b" + kitName + "&a is a one time kit&a!");
                            return true;
                        } else if (timeLeft != null) {
                            utilMsgs.infoMessage(sender, "&aTime left to recieve kit &b" + kitName + "&a &b" + timeLeft + "&a!");
                            return true;
                        } else if (!kit.giveKit(sender, false)) {
                            utilMsgs.errorMessage(sender, "&aNo permissions for kit!");
                            return true;
                        } else {
                            utilMsgs.infoMessage(sender, "&aGiven kit &b" + kitName + "&a!");
                            utilCooldown.putKitCooldown(sender.playerUUID, kit.kitName, kit.delay);
                            return true;
                        }
                    } else {
                        utilMsgs.infoMessage(sender, "&aKit &b" + kitName + "&a not exists!");
                    }
                    return true;
                } else {
                    utilMsgs.logErrorMessage("&aConsole Cant Run This Command!");
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
                                utilMsgs.errorMessage(sender, "&aNo permissions for kit!");
                            } else {
                                utilMsgs.logErrorMessage("No permissions for kit!");
                            }
                            return true;
                        } else {
                            if (sender != null) {
                                utilMsgs.infoMessage(sender, "&aPlayer " + p.playerName + "'s given kit &b" + kitName + "&a!");
                            } else {
                                utilMsgs.logInfoMessage("&aPlayer " + p.playerName + "'s given kit &b" + kitName + "&a!");
                            }
                            return true;
                        }
                    } else {
                        if (sender != null) {
                            utilMsgs.infoMessage(sender, "&aKit &b" + kitName + "&a not exists!");
                        } else {
                            utilMsgs.logInfoMessage("&aKit &b" + kitName + "&a not exists!");
                        }
                        return true;
                    }
                } else {
                    if (sender != null) {
                        utilMsgs.errorMessage(sender, "&aPlayer &b" +target+ " Offline/Not exists!");
                    } else {
                        utilMsgs.logErrorMessage("&aPlayer &b" + target + " Offline/Not exists!");
                    }
                    return true;
                }
            } else if (args.size() == 0) {
                if(cfgKits.getConfig().isSet("kits")) {
                    StringBuilder kits = new StringBuilder();
                    for (String kit : cfgKits.getConfig().getConfigurationSection("kits").getKeys(false)) {
                        kits.append(kit+" ");
                    }
                    if (sender != null) {
                        utilMsgs.infoMessage(sender, "&aAvailable Kits: &b"+kits);
                    } else {
                        utilMsgs.logInfoMessage("&aAvailable Kits: &b"+kits);
                    }
                    return true;
                } else {
                    if (sender != null) {
                        utilMsgs.errorMessage(sender, "&aNo Available Kits!");
                    } else {
                        utilMsgs.logErrorMessage("&aNo Available Kits!");
                    }
                    return true;
                }
            } else {
                if (sender != null) {
                    utilMsgs.errorMessage(sender, "&bUsage: /kit <name>");
                } else {
                    utilMsgs.logErrorMessage("&bUsage: /kit <name> <player>");
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
                    utilMsgs.infoMessage(sender, "&aCreated Kit &b" + kitName + "&a!");
                } else {
                    utilMsgs.errorMessage(sender, "&bUsage: /createkit <name> <delay>");
                }
                return true;
            } else {
                utilMsgs.logErrorMessage("&aConsole Cant Run This Command!");
            }
            return true;
        } else if (name.equalsIgnoreCase("deletekit")) {
            if (args.size() == 1) {
                String kitName = args.get(0);
                KitObject kit = new KitObject(plugin, kitName);
                if (kit.deleteKit()) {
                    if (sender != null) {
                        utilMsgs.infoMessage(sender, "&aDeleted Kit &b" + kitName + "&a!");
                    } else {
                        utilMsgs.logInfoMessage("&aDeleted Kit &b" + kitName + "&a!");
                    }
                    return true;
                } else {
                    if (sender != null) {
                        utilMsgs.errorMessage(sender, "&aKit &b" + kitName + " &anot exists!");
                    } else {
                        utilMsgs.logErrorMessage("&aKit &b" + kitName + " &anot exists!");
                    }
                    return true;
                }
            } else {
                if (sender != null) {
                    utilMsgs.errorMessage(sender, "&bUsage: /deletekit <name>");
                } else {
                    utilMsgs.logErrorMessage("&bUsage: /deletekit <name>");
                }
                return true;
            }
        }
        return false;
    }
}
