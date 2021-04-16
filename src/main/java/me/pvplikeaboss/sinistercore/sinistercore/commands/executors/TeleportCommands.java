package me.pvplikeaboss.sinistercore.sinistercore.commands.executors;

import me.pvplikeaboss.sinistercore.sinistercore.Instances;
import me.pvplikeaboss.sinistercore.sinistercore.SinisterCore;
import me.pvplikeaboss.sinistercore.sinistercore.commands.util.CommandContext;
import me.pvplikeaboss.sinistercore.sinistercore.modules.data.files.configs.yml.HomeConfig;
import me.pvplikeaboss.sinistercore.sinistercore.objects.PlayerObject;
import me.pvplikeaboss.sinistercore.sinistercore.utilites.misc.Messages;
import me.pvplikeaboss.sinistercore.sinistercore.utilites.serverutils.PlayerUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;

public class TeleportCommands {
    private static SinisterCore plugin;
    private static Messages utilMsgs;
    private static PlayerUtils utilPlayer;
    private static HomeConfig cfgHome;
    public static HashMap<PlayerObject, PlayerObject> pendingTpaRequests;

    public boolean onCommand(String name, CommandContext context) {
        List<String> args = context.getArgs();
        plugin = context.getPlugin();
        utilMsgs = (Messages) Instances.getInstance(Instances.InstanceType.Utilities, 2);
        utilPlayer = (PlayerUtils) Instances.getInstance(Instances.InstanceType.Utilities, 3);
        cfgHome = (HomeConfig) Instances.getInstance(Instances.InstanceType.Config, 0);
        pendingTpaRequests = (HashMap<PlayerObject, PlayerObject>) Instances.getInstance(Instances.InstanceType.HashMap, 2);

        PlayerObject sender = null;
        if(context.getSender() instanceof Player) {
            sender = plugin.getPlayer(context.getPlayer().getUniqueId());
        }

        if(name.equalsIgnoreCase("tptoggle")) {
            return true;
        } else if(name.equalsIgnoreCase("tpoffline")) {
            return true;
        } else if (name.equalsIgnoreCase("home")) {
            String homeName = "home";
            if (args.size() > 1) {// teleport another player to  their home
                homeName = args.get(0);
                String playerName = null;
                if ((playerName = utilPlayer.playerOnline(args.get(1))) != null) {
                    PlayerObject p = plugin.getPlayer(playerName);
                    if (cfgHome.getConfig().isSet("homes." + p.playerUUID)) {
                        boolean found = false;
                        for (String tmp : cfgHome.getConfig().getConfigurationSection("homes." + p.playerUUID).getKeys(false)) {
                            if (tmp.equalsIgnoreCase(homeName)) {
                                homeName = tmp;
                                found = true;
                                break;
                            }
                        }
                        if (found) {
                            double x = cfgHome.getConfig().getDouble("homes." + p.playerUUID + "." + homeName + ".x");
                            double y = cfgHome.getConfig().getDouble("homes." + p.playerUUID + "." + homeName + ".y");
                            double z = cfgHome.getConfig().getDouble("homes." + p.playerUUID + "." + homeName + ".z");
                            float yaw = (float) cfgHome.getConfig().getDouble("homes." + p.playerUUID + "." + homeName + ".yaw");
                            float pitch = (float) cfgHome.getConfig().getDouble("homes." + p.playerUUID + "." + homeName + ".pitch");
                            String sWorld = cfgHome.getConfig().getString("homes." + p.playerUUID + "." + homeName + ".world");
                            Location loc = new Location(plugin.getServer().getWorld(sWorld), x, y, z, yaw, pitch);
                            p.teleportPlayer(loc, false);
                            if (context.isPlayer()) {
                                utilMsgs.infoMessage(sender, "&7Teleported player &6" + p.playerName + " &7to &6" + homeName + "&7!");
                            } else {
                                utilMsgs.logErrorMessage("&7Teleported player &6" + p.playerName + " &7to &6" + homeName + "&7!");
                            }
                            return true;
                        } else {
                            if (context.isPlayer()) {
                                utilMsgs.infoMessage(sender, "&7Home &6" + homeName + " &7not exists!");
                            } else {
                                utilMsgs.logErrorMessage("&7Home &6" + homeName + " &7not exists!");
                            }
                        }
                    } else {
                        if (context.isPlayer()) {
                            utilMsgs.errorMessage(sender, "&7Player &6" + p.playerName + "&7 has no homes!");
                        } else {
                            utilMsgs.logErrorMessage("&7Player &6" + p.playerName + "&7 has no homes!");
                        }
                    }
                } else {
                    if (context.isPlayer()) {
                        utilMsgs.logErrorMessage("&7Player &6" + args.get(1) + " &7Offline/Not exists!");
                    } else {
                        utilMsgs.logErrorMessage("&7Player &6" + args.get(1) + " &7Offline/Not exists!");
                    }
                }
            } else {
                if (context.isPlayer()) {
                    boolean doList = false;
                    boolean isArgs = false;
                    if (args.size() == 1) {
                        isArgs = true;
                        homeName = args.get(0);
                        if(homeName.equalsIgnoreCase("list")) {
                            doList = true;
                        }
                    } else {
                        isArgs = false;
                    }
                    if (cfgHome.getConfig().isSet("homes." + sender.playerUUID)) {
                        int homesLen = 0;
                        StringBuilder homeList = new StringBuilder();
                        int size = cfgHome.getConfig().getConfigurationSection("homes." + sender.playerUUID + "").getKeys(false).size();
                        for (String home : cfgHome.getConfig().getConfigurationSection("homes." + sender.playerUUID + "").getKeys(false)) {
                            if (doList == false && home.equalsIgnoreCase(homeName)) {
                                int x = cfgHome.getConfig().getInt("homes." + sender.playerUUID + "." + home + ".x");
                                int y = cfgHome.getConfig().getInt("homes." + sender.playerUUID + "." + home + ".y");
                                int z = cfgHome.getConfig().getInt("homes." + sender.playerUUID + "." + home + ".z");
                                float yaw = (float) cfgHome.getConfig().getDouble("homes." + sender.playerUUID + "." + home + ".yaw");
                                float pitch = (float) cfgHome.getConfig().getDouble("homes." + sender.playerUUID + "." + home + ".pitch");
                                String sWorld = cfgHome.getConfig().getString("homes." + sender.playerUUID + "." + home + ".world");
                                Location loc = new Location(plugin.getServer().getWorld(sWorld), x, y, z, yaw, pitch);
                                sender.teleportPlayer(loc, true);
                                return true;
                            }

                            if(homesLen == size-1) {
                                homesLen++;
                                homeList.append("&7"+home);
                                break;
                            }
                            homesLen++;
                            homeList.append("&7"+home+"&6, ");
                        }
                        if(homesLen == 0) {
                            utilMsgs.errorMessage(sender, "&7You have no homes!");
                            return true;
                        }
                        if(doList == false && isArgs == true) {
                            utilMsgs.infoMessage(sender, "&7Home &6" + homeName + " &7not found!");
                        }
                        utilMsgs.infoMessage(sender, "&7Available Homes: "+homeList.toString());
                    } else {
                        utilMsgs.errorMessage(sender, "&7You has no homes!");
                    }
                } else {
                    utilMsgs.logErrorMessage("&7Console Cant Run This Command!");
                }
            }
            return true;
        } else if (name.equalsIgnoreCase("sethome")) {
            if (context.isPlayer()) {
                Location loc = sender.getPlayer().getLocation();
                String homeName = "home";

                if (args.size() > 0) {
                    homeName = args.get(0);
                }

                int home_count = 0;

                if (cfgHome.getConfig().isSet("homes." + sender.playerUUID)) {
                    for (String home : cfgHome.getConfig().getConfigurationSection("homes." + sender.playerUUID + "").getKeys(false)) {
                        if (home.equalsIgnoreCase(homeName)) {
                            cfgHome.getConfig().set("homes." + sender.playerUUID + "." + home, null);
                        }
                        home_count++;
                    }
                }

                if(!sender.getPlayer().hasPermission("sinistercore.home.multiple.*")) {
                    int max_homes = 0;
                    for (int i = 1; i < 50; i++) {
                        if(sender.getPlayer().hasPermission("sinistercore.home.multiple."+i)) {
                            max_homes = i;
                        }
                    }
                    if(home_count >= max_homes) {
                        utilMsgs.infoMessage(sender, "&7Cant set more than &6"+max_homes+"&7 homes!");
                        return true;
                    }
                }

                cfgHome.getConfig().set("homes." + sender.playerUUID + "." + homeName + ".x", loc.getX());
                cfgHome.getConfig().set("homes." + sender.playerUUID + "." + homeName + ".y", loc.getY());
                cfgHome.getConfig().set("homes." + sender.playerUUID + "." + homeName + ".z", loc.getZ());
                cfgHome.getConfig().set("homes." + sender.playerUUID + "." + homeName + ".yaw", loc.getYaw());
                cfgHome.getConfig().set("homes." + sender.playerUUID + "." + homeName + ".pitch", loc.getPitch());
                cfgHome.getConfig().set("homes." + sender.playerUUID + "." + homeName + ".world", loc.getWorld().getName());
                cfgHome.saveConfig();

                utilMsgs.infoMessage(sender, "&7Home &6" + homeName + " &7set!");
                return true;
            }
            utilMsgs.logErrorMessage("&aConsole Cant Run This Command!");
            return true;
        } else if (name.equalsIgnoreCase("delhome")) {
            String homeName = "home";
            if (args.size() > 1) {// delete other players home
                String playerName = null;
                if ((playerName = utilPlayer.playerOnline(args.get(1))) != null) {
                    PlayerObject p = plugin.getPlayer(playerName);
                    if (cfgHome.getConfig().isSet("homes." + p.playerUUID)) {
                        boolean found = false;
                        for (String tmp : cfgHome.getConfig().getConfigurationSection("homes." + p.playerUUID).getKeys(false)) {
                            if (tmp.equalsIgnoreCase(homeName)) {
                                homeName = tmp;
                                found = true;
                            }
                        }
                        if (found) {
                            cfgHome.getConfig().set("homes." + p.playerUUID + "." + homeName, null);
                            if (context.isPlayer()) {
                                utilMsgs.infoMessage(sender, "&7Player &6" + p.playerName + "'s &7home &6" + homeName + " &7has been deleted!");
                            } else {
                                utilMsgs.logErrorMessage("&7Player &6" + p.playerName + "'s &7home &6" + homeName + " &7has been deleted!");
                            }
                            return true;
                        } else {
                            if (context.isPlayer()) {
                                utilMsgs.infoMessage(sender, "&7Home &6" + homeName + " &7not found!");
                            } else {
                                utilMsgs.logErrorMessage("&7Home &6" + homeName + " &7not found!");
                            }
                            return true;
                        }
                    } else {
                        if (context.isPlayer()) {
                            utilMsgs.errorMessage(sender, "&7Player &6" + p.playerName + "&7 has no homes!");
                        } else {
                            utilMsgs.logErrorMessage("&7Player &6" + p.playerName + " &7has no homes!");
                        }
                        return true;
                    }
                } else {
                    if (context.isPlayer()) {
                        utilMsgs.errorMessage(sender, "&7Player &6" + args.get(1) + " &7Offline/Not exists!");
                    } else {
                        utilMsgs.logErrorMessage("&7Player &6" + args.get(1) + " &7Offline/Not exists!");
                    }
                    return true;
                }
            } else if (args.size() == 1) {
                homeName = args.get(0);
            }
            if (context.isPlayer()) {
                if (cfgHome.getConfig().isSet("homes." + sender.playerUUID)) {
                    boolean found = false;
                    for (String tmp : cfgHome.getConfig().getConfigurationSection("homes." + sender.playerUUID).getKeys(false)) {
                        if (tmp.equalsIgnoreCase(homeName)) {
                            homeName = tmp;
                            found = true;
                        }
                    }
                    if (found) {
                        cfgHome.getConfig().set("homes." + sender.playerUUID + "." + homeName, null);
                        utilMsgs.infoMessage(sender, "&7Home &6" + homeName + " &7deleted!");
                    } else {
                        utilMsgs.infoMessage(sender, "&7Home &6" + homeName + " &7not found!");
                    }
                } else {
                    utilMsgs.errorMessage(sender, "&7You have no homes!");
                }
            } else {
                utilMsgs.logErrorMessage("&7Usage: /delhome <home> <player>");
            }
            return true;
        } else if (name.equalsIgnoreCase("spawn")) {
            if (!plugin.getConfig().isSet("spawn")) {
                if (context.isPlayer()) {
                    utilMsgs.errorMessage(sender, "&7No spawn set!");
                } else {
                    utilMsgs.logErrorMessage("&7No spawn set!");
                }
            } else {
                double x = plugin.getConfig().getDouble("spawn.x");
                double y = plugin.getConfig().getDouble("spawn.y");
                double z = plugin.getConfig().getDouble("spawn.z");
                float yaw = (float)plugin.getConfig().getDouble("spawn.yaw");
                float pitch = (float)plugin.getConfig().getDouble("spawn.pitch");
                World world = plugin.getServer().getWorld(plugin.getConfig().getString("spawn.world"));
                Location to = new Location(world, x, y, z, yaw, pitch);
                if (args.size() > 0) {// teleport other player to spawn
                    if(context.isPlayer()) {
                        if(!sender.getPlayer().hasPermission("sinistercore.spawn.other")) {
                            utilMsgs.permMessage(sender);
                        }
                    }
                    String playerName = null;
                    if ((playerName = utilPlayer.playerOnline(args.get(0))) != null) {
                        PlayerObject targetPlayer = plugin.getPlayer(playerName);
                        targetPlayer.teleportPlayer(to, false);
                        if (context.isPlayer()) {
                            utilMsgs.infoMessage(sender, "&7Teleported Player &6" + targetPlayer.playerName + "&7 to spawn!");
                        } else {
                            utilMsgs.logInfoMessage("&7Teleported Player &6" + targetPlayer.playerName + "&7 to spawn!");
                        }
                    } else {
                        if (context.isPlayer()) {
                            utilMsgs.errorMessage(sender, "&7Player &6" + args.get(1) + " &7Offline/Not exists!");
                        } else {
                            utilMsgs.logErrorMessage("&7Player &6" + args.get(1) + " &7Offline/Not exists!");
                        }
                    }
                } else {// teleport sender to spawn
                    if (context.isPlayer()) {
                        sender.teleportPlayer(to, true);
                    } else {
                        utilMsgs.logErrorMessage("&7Usage: /spawn <player>");
                    }
                }
            }
            return true;
        } else if (name.equalsIgnoreCase("warp")) {
            if (!plugin.getConfig().isSet("warps")) {
                if (context.isPlayer()) {
                    utilMsgs.errorMessage(sender, "&7No available warps!");
                } else {
                    utilMsgs.logErrorMessage("&7No available warps!");
                }
                return true;
            } else {
                if (args.size() == 0) {
                    StringBuilder warpList = new StringBuilder();
                    int warpsLen = 0;
                    int size = plugin.getConfig().getConfigurationSection("warps").getKeys(false).size();
                    for (String tmp : plugin.getConfig().getConfigurationSection("warps").getKeys(false)) {
                        if(warpsLen == size-1) {
                            warpsLen++;
                            warpList.append("&7"+tmp);
                            break;
                        }
                        warpsLen++;
                        warpList.append("&7"+tmp+"&6, ");
                    }
                    if(warpsLen == 0) {
                        if (context.isPlayer()) {
                            utilMsgs.errorMessage(sender, "&7No available warps!");
                        } else {
                            utilMsgs.logErrorMessage("&7No available warps!");
                        }
                        return true;
                    }
                    if (context.isPlayer()) {
                        utilMsgs.errorMessage(sender, "&7Usage: /warp <name>");
                        utilMsgs.errorMessage(sender, "&cAvailable Warps: "+warpList);
                    } else {
                        utilMsgs.logErrorMessage("&7Usage: /warp <name> <player>");
                    }
                    return true;
                }

                boolean found = false;
                for (String warpName : plugin.getConfig().getConfigurationSection("warps").getKeys(false)) {
                    if (warpName.equalsIgnoreCase(args.get(0))) {
                        found = true;
                    }
                }

                if (!found) {
                    if (context.isPlayer()) {
                        utilMsgs.errorMessage(sender, "&7Warp &6" + args.get(0) + "&7 doesn't exist!");
                    } else {
                        utilMsgs.logErrorMessage("&7Warp &6" + args.get(0) + "&7 doesn't exist!");
                    }
                    return true;
                }

                double x = plugin.getConfig().getDouble("warps." + args.get(0) + ".x");
                double y = plugin.getConfig().getDouble("warps." + args.get(0) + ".y");
                double z = plugin.getConfig().getDouble("warps." + args.get(0) + ".z");
                float yaw = (float)plugin.getConfig().getDouble("warps." + args.get(0) + ".yaw");
                float pitch = (float)plugin.getConfig().getDouble("warps." + args.get(0) + ".pitch");
                World world = plugin.getServer().getWorld(plugin.getConfig().getString("warps." + args.get(0) + ".world"));
                Location to = new Location(world, x, y, z, yaw, pitch);

                if (args.size() > 1) {// teleport other player to warp
                    if(context.isPlayer()) {
                        if(!sender.getPlayer().hasPermission("sinistercore.warp.other")) {
                            utilMsgs.errorMessage(sender,"&7No permission to teleport others!");
                        }
                    }
                    String playerName = null;
                    if ((playerName = utilPlayer.playerOnline(args.get(1))) != null) {
                        PlayerObject targetPlayer = plugin.getPlayer(playerName);
                        targetPlayer.teleportPlayer(to, false);
                        if (context.isPlayer()) {
                            utilMsgs.infoMessage(sender, "&7Teleported Player &6" + targetPlayer.playerName + "&7 to warp &6" + args.get(0) + "&7!");
                        } else {
                            utilMsgs.logInfoMessage("&7Teleported Player &6" + targetPlayer.playerName + "&7 to warp &6" + args.get(0) + "&7!");
                        }
                    } else {
                        if (context.isPlayer()) {
                            utilMsgs.errorMessage(sender, "&7Player &6" + args.get(1) + " &7Offline/Not exists!");
                        } else {
                            utilMsgs.logErrorMessage("&7Player &6" + args.get(1) + " &7Offline/Not exists!");
                        }
                    }
                } else if (args.size() == 1) {// teleport sender to warp
                    if (context.isPlayer()) {
                        if(sender.getPlayer().hasPermission("sinistercore.warp."+args.get(0).toLowerCase())) {
                            sender.teleportPlayer(to, true);
                        } else {
                            utilMsgs.errorMessage(sender,"&7No permission to teleport to &6"+args.get(0)+"&7!");
                        }
                    } else {
                        utilMsgs.logErrorMessage("&7Console Cant Run This Command!");
                    }
                }
            }
            return true;
        } else if (name.equalsIgnoreCase("setwarp")) {
            if (context.isPlayer()) {
                if (args.size() > 0) {
                    Location playerLocation = sender.getPlayer().getLocation();
                    if (plugin.getConfig().isSet("warps")) {
                        for (String warpName : plugin.getConfig().getConfigurationSection("warps").getKeys(false)) {
                            if (warpName.equalsIgnoreCase(args.get(0))) {
                                plugin.getConfig().set("warps." + warpName, null);
                            }
                        }
                    }
                    plugin.getConfig().set("warps." + args.get(0) + ".x", playerLocation.getX());
                    plugin.getConfig().set("warps." + args.get(0) + ".y", playerLocation.getY());
                    plugin.getConfig().set("warps." + args.get(0) + ".z", playerLocation.getZ());
                    plugin.getConfig().set("warps." + args.get(0) + ".yaw", playerLocation.getYaw());
                    plugin.getConfig().set("warps." + args.get(0) + ".pitch", playerLocation.getPitch());
                    plugin.getConfig().set("warps." + args.get(0) + ".world", playerLocation.getWorld().getName());
                    plugin.saveConfig();
                    utilMsgs.infoMessage(sender, "&7Set warp &6" + args.get(0) + "&7!");
                } else {
                    utilMsgs.errorMessage(sender, "&7Usage: /setwarp <name>");
                }
            } else {
                utilMsgs.logErrorMessage("&7Console Cant Run This Command!");
            }
            return true;
        } else if (name.equalsIgnoreCase("setspawn")) {
            if (context.isPlayer()) {
                Location playerLocation = sender.getPlayer().getLocation();
                if (plugin.getConfig().isSet("spawn")) {
                    plugin.getConfig().set("spawn", null);
                }
                playerLocation.getWorld().setSpawnLocation((int) playerLocation.getX(), (int) playerLocation.getBlockY(), (int) playerLocation.getBlockZ());
                plugin.getConfig().set("spawn.x", playerLocation.getX());
                plugin.getConfig().set("spawn.y", playerLocation.getY());
                plugin.getConfig().set("spawn.z", playerLocation.getZ());
                plugin.getConfig().set("spawn.yaw", playerLocation.getYaw());
                plugin.getConfig().set("spawn.pitch", playerLocation.getPitch());
                plugin.getConfig().set("spawn.world", playerLocation.getWorld().getName());
                plugin.saveConfig();
                utilMsgs.infoMessage(sender, "&7Set spawn!");
            } else {
                utilMsgs.logErrorMessage("&7Console Cant Run This Command!");
            }
            return true;
        } else if (name.equalsIgnoreCase("delwarp")) {
            if (args.size() > 0) {
                for (String warpName : plugin.getConfig().getConfigurationSection("warps").getKeys(false)) {
                    if (warpName.equalsIgnoreCase(args.get(0))) {
                        plugin.getConfig().set("warps." + args.get(0), null);
                        utilMsgs.infoMessage(sender, "&7Deleted warp &6" + args.get(0) + "&7!");
                        return true;
                    }
                }
                utilMsgs.errorMessage(sender, "&7Warp &6" + args.get(0) + " &7Doesn't exist!");
            } else {
                utilMsgs.errorMessage(sender, "&7Usage: /delwarp <name>");
            }
            return true;
        } else if (name.equalsIgnoreCase("tp")) {
            if (args.size() > 1) {// force Player to player tp
                String playerName = null;
                String playerName1 = null;
                if ((playerName = utilPlayer.playerOnline(args.get(0))) != null && (playerName1 = utilPlayer.playerOnline(args.get(1))) != null) {
                    PlayerObject p1 = plugin.getPlayer(playerName);
                    PlayerObject p2 = plugin.getPlayer(playerName1);
                    p1.teleportPlayer(p2.getPlayer().getLocation(), false);
                } else {
                    utilMsgs.errorMessage(sender, "&7Player Offline/Not exists!");
                }
            } else if (args.size() == 1) {// tp this player to player
                if (context.isPlayer()) {
                    String playerName = null;
                    if ((playerName = utilPlayer.playerOnline(args.get(0))) != null) {
                        PlayerObject p1 = sender;
                        PlayerObject p2 = plugin.getPlayer(playerName);
                        p1.teleportPlayer(p2.getPlayer().getLocation(), false);
                    } else {
                        utilMsgs.errorMessage(sender, "&7Player Offline/Not exists!");
                    }
                } else {
                    utilMsgs.errorMessage(sender, "&7Usage: /tp <from player> <to player>");
                }
            } else {
                utilMsgs.errorMessage(sender, "&7Usage: /tp <player>");
                utilMsgs.errorMessage(sender, "&7Usage: /tp <from player> <to player>");
            }
            return true;
        } else if (name.equalsIgnoreCase("tpo")) {
            if (context.isPlayer()) {
                for (Player player : plugin.getServer().getOnlinePlayers()) {
                    PlayerObject playerObject = plugin.getPlayer(player.getUniqueId());
                    playerObject.teleportPlayer(sender.getPlayer().getLocation(), false);
                }
                utilMsgs.infoMessage(sender, "&7Teleported all players to location!");
            } else {
                utilMsgs.logErrorMessage("&7Console Cant Run This Command!");
            }
            return true;
        } else if (name.equalsIgnoreCase("tpa")) {
            if (context.isPlayer()) {
                if (args.size() > 0) {
                    String playerName = null;
                    if ((playerName = utilPlayer.playerOnline(args.get(0))) != null) {
                        PlayerObject p1 = sender;
                        PlayerObject p2 = plugin.getPlayer(playerName);

                        boolean found = false;

                        if(!found) {
                            for (PlayerObject tmpp : pendingTpaRequests.keySet()) {
                                PlayerObject tmpp2 = pendingTpaRequests.get(tmpp);
                                if (tmpp == p1 || tmpp == p2 || tmpp2 == p1 || tmpp2 == p2) {
                                    utilMsgs.infoMessage(tmpp, "&7Canceled previous tpa request.");
                                    utilMsgs.infoMessage(tmpp2, "&7Canceled previous tpa request.");
                                    pendingTpaRequests.remove(tmpp);
                                    found = true;
                                }
                            }
                        }

                        pendingTpaRequests.put(p1, p2);
                        utilMsgs.infoMessage(p1, "&7Sent Teleport Request!");
                        utilMsgs.infoMessage(p2, "&7Player &6" + p1.playerName + " &7wishes to teleport to you. &8(&6/tpyes&7,&6 /tpno&8)");
                        int delay = plugin.getConfig().getInt("features.teleport.tpactive");
                        Bukkit.getScheduler().scheduleAsyncDelayedTask(plugin, new Runnable() {
                            @Override
                            public void run() {
                                if (pendingTpaRequests.containsKey(p1) && pendingTpaRequests.get(p1).equals(p2)) {
                                    pendingTpaRequests.remove(p1);
                                    utilMsgs.infoMessage(p1, "&7Tpa Request Timed Out!");
                                    utilMsgs.infoMessage(p2, "&7Tpa Request Timed Out!");
                                }
                            }
                        }, delay * 20L);
                    } else {
                        utilMsgs.errorMessage(sender, "&7Player Offline/Not exists!");
                    }
                } else {
                    utilMsgs.errorMessage(sender, "&7Usage: /tpa <player>");
                }
            } else {
                utilMsgs.logErrorMessage("&7Console Cant Run This Command!");
            }
            return true;
        } else if (name.equalsIgnoreCase("tpahere")) {
            if (context.isPlayer()) {
                if (args.size() > 0) {
                    String playerName = null;
                    if ((playerName = utilPlayer.playerOnline(args.get(0))) != null) {
                        PlayerObject p1 = sender;
                        PlayerObject p2 = plugin.getPlayer(playerName);
                        boolean found = false;

                        if(!found) {
                            for (PlayerObject tmpp : pendingTpaRequests.keySet()) {
                                PlayerObject tmpp2 = pendingTpaRequests.get(tmpp);
                                if (tmpp == p1 || tmpp == p2 || tmpp2 == p1 || tmpp2 == p2) {
                                    utilMsgs.infoMessage(tmpp, "&7Canceled previous tpa request.");
                                    utilMsgs.infoMessage(tmpp2, "&7Canceled previous tpa request.");
                                    pendingTpaRequests.remove(tmpp);
                                    found = true;
                                }
                            }
                        }

                        pendingTpaRequests.put(p2, p1);
                        utilMsgs.infoMessage(p1, "&7Sent Teleport Request!");
                        utilMsgs.infoMessage(p2, "&7Player &6" + p1.playerName + " &7wishes for you to teleport to them. &8(&6/tpyes&7,&6 /tpno&8)");
                        int delay = plugin.getConfig().getInt("features.teleport.tpactive");
                        Bukkit.getScheduler().scheduleAsyncDelayedTask(plugin, new Runnable() {
                            @Override
                            public void run() {
                                if (pendingTpaRequests.containsKey(p2) && pendingTpaRequests.get(p2).equals(p1)) {
                                    pendingTpaRequests.remove(p2);
                                    utilMsgs.infoMessage(p1, "&7Tpa Request Timed Out!");
                                    utilMsgs.infoMessage(p2, "&7Tpa Request Timed Out!");
                                }
                            }
                        }, delay * 20L);
                    } else {
                        utilMsgs.errorMessage(sender, "&7Player Offline/Not exists!");
                    }
                } else {
                    utilMsgs.errorMessage(sender, "&7Usage: /tphere <player>");
                }
            } else {
                utilMsgs.logErrorMessage("&7Console Cant Run This Command!");
            }
            return true;
        } else if (name.equalsIgnoreCase("tpyes") || name.equalsIgnoreCase("tpaccept")) {
            if (context.isPlayer()) {
                PlayerObject player = sender;
                PlayerObject toPlayer = null;
                PlayerObject fromPlayer = null;

                if(toPlayer == null || fromPlayer == null) {
                    for (PlayerObject tmpp : pendingTpaRequests.keySet()) {
                        PlayerObject tmpp2 = pendingTpaRequests.get(tmpp);
                        if ((tmpp.compareTo(player) == 1) || (tmpp2.compareTo(player) == 1)) {// tpa
                            fromPlayer = tmpp;
                            toPlayer = tmpp2;
                            pendingTpaRequests.remove(fromPlayer);
                            break;
                        }
                    }
                }

                utilMsgs.infoMessage(fromPlayer, "&7Tpa Request Accepted!");
                utilMsgs.infoMessage(toPlayer, "&7Tpa Request Accepted!");
                fromPlayer.teleportPlayer(toPlayer.getPlayer().getLocation(), true);
            } else {
                utilMsgs.logErrorMessage("&aConsole Cant Run This Command!");
            }
            return true;
        } else if (name.equalsIgnoreCase("tpno") || name.equalsIgnoreCase("tpdeny")) {
            if (context.isPlayer()) {
                PlayerObject player = sender;
                for (PlayerObject tmpp : pendingTpaRequests.keySet()) {
                    PlayerObject tmpp2 = pendingTpaRequests.get(tmpp);
                    if ((tmpp.compareTo(player) == 1) || (tmpp2.compareTo(player) == 1)) {// tpa
                        utilMsgs.infoMessage(tmpp, "&7Tpa Request Denied!");
                        utilMsgs.infoMessage(tmpp2, "&7Tpa Request Denied!");
                        pendingTpaRequests.remove(tmpp);
                        return true;
                    }
                }
                utilMsgs.errorMessage(player, "&7No active teleport request!");
            } else {
                utilMsgs.logErrorMessage("&7Console Cant Run This Command!");
            }
            return true;
        }

        return false;
    }
}
