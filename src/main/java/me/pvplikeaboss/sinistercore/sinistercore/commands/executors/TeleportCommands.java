package me.pvplikeaboss.sinistercore.sinistercore.commands.executors;

import me.pvplikeaboss.sinistercore.sinistercore.Instances;
import me.pvplikeaboss.sinistercore.sinistercore.SinisterCore;
import me.pvplikeaboss.sinistercore.sinistercore.commands.util.CommandContext;
import me.pvplikeaboss.sinistercore.sinistercore.modules.files.configs.csv.items.methods.AbstractItemDB;
import me.pvplikeaboss.sinistercore.sinistercore.modules.files.configs.yml.HomeConfig;
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
                                utilMsgs.infoMessage(sender, "&aTeleported player &b" + p.playerName + " &sto &b" + homeName + "&a!");
                            } else {
                                utilMsgs.logErrorMessage("&aTeleported player &b" + p.playerName + " &sto &b" + homeName + "&a!");
                            }
                            return true;
                        } else {
                            if (context.isPlayer()) {
                                utilMsgs.infoMessage(sender, "&aHome &b" + homeName + " &anot exists!");
                            } else {
                                utilMsgs.logErrorMessage("&aHome &b" + homeName + " &anot exists!");
                            }
                        }
                    } else {
                        if (context.isPlayer()) {
                            utilMsgs.errorMessage(sender, "&aPlayer " + p.playerName + " has no homes!");
                        } else {
                            utilMsgs.logErrorMessage("&aPlayer " + p.playerName + " has no homes!");
                        }
                    }
                } else {
                    if (context.isPlayer()) {
                        utilMsgs.logErrorMessage("&aPlayer &b" + args.get(1) + " Offline/Not exists!");
                    } else {
                        utilMsgs.logErrorMessage("&aPlayer &b" + args.get(1) + " Offline/Not exists!");
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
                            homesLen++;
                            homeList.append(home+" ");
                        }
                        if(homesLen == 0) {
                            utilMsgs.errorMessage(sender, "&aYou has no homes!");
                            return true;
                        }
                        if(doList == false && isArgs == true) {
                            utilMsgs.infoMessage(sender, "&aHome &b" + homeName + " &anot found!");
                        }
                        utilMsgs.infoMessage(sender, "Available Homes: "+homeList.toString());
                    } else {
                        utilMsgs.errorMessage(sender, "&aYou has no homes!");
                    }
                } else {
                    utilMsgs.logErrorMessage("&aConsole Cant Run This Command!");
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
                        utilMsgs.infoMessage(sender, "&aCant set more than &b"+max_homes+"&a homes!");
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

                utilMsgs.infoMessage(sender, "&aHome &b" + homeName + " &aset!");
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
                                utilMsgs.infoMessage(sender, "&aPlayer " + p.playerName + "'s home &b" + homeName + " &ahas been deleted!");
                            } else {
                                utilMsgs.logErrorMessage("&aPlayer " + p.playerName + "'s home &b" + homeName + " &ahas been deleted!");
                            }
                            return true;
                        } else {
                            if (context.isPlayer()) {
                                utilMsgs.infoMessage(sender, "&aHome &b" + homeName + " &anot found!");
                            } else {
                                utilMsgs.logErrorMessage("&aHome &b" + homeName + " &anot found!");
                            }
                            return true;
                        }
                    } else {
                        if (context.isPlayer()) {
                            utilMsgs.errorMessage(sender, "&aPlayer " + p.playerName + " has no homes!");
                        } else {
                            utilMsgs.logErrorMessage("&aPlayer " + p.playerName + " has no homes!");
                        }
                        return true;
                    }
                } else {
                    if (context.isPlayer()) {
                        utilMsgs.errorMessage(sender, "&aPlayer &b" + args.get(1) + " Offline/Not exists!");
                    } else {
                        utilMsgs.logErrorMessage("&aPlayer &b" + args.get(1) + " Offline/Not exists!");
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
                        utilMsgs.infoMessage(sender, "&aHome &b" + homeName + " &adeleted!");
                    } else {
                        utilMsgs.infoMessage(sender, "&aHome &b" + homeName + " &anot found!");
                    }
                } else {
                    utilMsgs.errorMessage(sender, "&aYou have no homes!");
                }
            } else {
                utilMsgs.logErrorMessage("&bUsage: /delhome <home> <player>");
            }
            return true;
        } else if (name.equalsIgnoreCase("spawn")) {
            if (!plugin.getConfig().isSet("spawn")) {
                if (context.isPlayer()) {
                    utilMsgs.errorMessage(sender, "&aNo spawn set!");
                } else {
                    utilMsgs.logErrorMessage("&aNo spawn set!");
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
                            utilMsgs.errorMessage(sender,"&9No permission to teleport others!");
                        }
                    }
                    String playerName = null;
                    if ((playerName = utilPlayer.playerOnline(args.get(0))) != null) {
                        PlayerObject targetPlayer = plugin.getPlayer(playerName);
                        targetPlayer.teleportPlayer(to, false);
                        if (context.isPlayer()) {
                            utilMsgs.infoMessage(sender, "&aTeleported Player &b" + targetPlayer.playerName + "&a to spawn!");
                        } else {
                            utilMsgs.logInfoMessage("&aTeleported Player &b" + targetPlayer.playerName + "&a to spawn!");
                        }
                    } else {
                        if (context.isPlayer()) {
                            utilMsgs.errorMessage(sender, "&aPlayer &b" + args.get(1) + " Offline/Not exists!");
                        } else {
                            utilMsgs.logErrorMessage("&aPlayer &b" + args.get(1) + " Offline/Not exists!");
                        }
                    }
                } else {// teleport sender to spawn
                    if (context.isPlayer()) {
                        sender.teleportPlayer(to, true);
                    } else {
                        utilMsgs.logErrorMessage("&bUsage: /spawn <player>");
                    }
                }
            }
            return true;
        } else if (name.equalsIgnoreCase("warp")) {
            if (!plugin.getConfig().isSet("warps")) {
                if (context.isPlayer()) {
                    utilMsgs.errorMessage(sender, "&aNo available warps!");
                } else {
                    utilMsgs.logErrorMessage("&aNo available warps!");
                }
                return true;
            } else {
                if (args.size() == 0) {
                    StringBuilder warpList = new StringBuilder();
                    int warpsLen = 0;
                    for (String tmp : plugin.getConfig().getConfigurationSection("warps").getKeys(false)) {
                        warpsLen++;
                        warpList.append(tmp+" ");
                    }
                    if(warpsLen == 0) {
                        if (context.isPlayer()) {
                            utilMsgs.errorMessage(sender, "&aNo available warps!");
                        } else {
                            utilMsgs.logErrorMessage("&aNo available warps!");
                        }
                        return true;
                    }
                    if (context.isPlayer()) {
                        utilMsgs.errorMessage(sender, "&bUsage: /warp <name>");
                        utilMsgs.errorMessage(sender, "Available Warps: "+warpList);
                    } else {
                        utilMsgs.logErrorMessage("&bUsage: /warp <name> <player>");
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
                        utilMsgs.errorMessage(sender, "&aWarp &b" + args.get(0) + "&a doesn't exist!");
                    } else {
                        utilMsgs.logErrorMessage("&aWarp &b" + args.get(0) + "&a doesn't exist!");
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
                            utilMsgs.errorMessage(sender,"&9No permission to teleport others!");
                        }
                    }
                    String playerName = null;
                    if ((playerName = utilPlayer.playerOnline(args.get(1))) != null) {
                        PlayerObject targetPlayer = plugin.getPlayer(playerName);
                        targetPlayer.teleportPlayer(to, false);
                        if (context.isPlayer()) {
                            utilMsgs.infoMessage(sender, "&aTeleported Player &b" + targetPlayer.playerName + "&a to warp &b" + args.get(0) + "&a!");
                        } else {
                            utilMsgs.logInfoMessage("&aTeleported Player &b" + targetPlayer.playerName + "&a to warp &b" + args.get(0) + "&a!");
                        }
                    } else {
                        if (context.isPlayer()) {
                            utilMsgs.errorMessage(sender, "&aPlayer &b" + args.get(1) + " Offline/Not exists!");
                        } else {
                            utilMsgs.logErrorMessage("&aPlayer &b" + args.get(1) + " Offline/Not exists!");
                        }
                    }
                } else if (args.size() == 1) {// teleport sender to warp
                    if (context.isPlayer()) {
                        if(sender.getPlayer().hasPermission("sinistercore.warp."+args.get(0).toLowerCase())) {
                            sender.teleportPlayer(to, true);
                        } else {
                            utilMsgs.errorMessage(sender,"&9No permission to teleport to &b"+args.get(0)+"&9!");
                        }
                    } else {
                        utilMsgs.logErrorMessage("&aConsole Cant Run This Command!");
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
                    utilMsgs.infoMessage(sender, "&aSet warp &b" + args.get(0) + "&a!");
                } else {
                    utilMsgs.errorMessage(sender, "&bUsage: /setwarp <name>");
                }
            } else {
                utilMsgs.logErrorMessage("&aConsole Cant Run This Command!");
            }
            return true;
        } else if (name.equalsIgnoreCase("setspawn")) {
            if (context.isPlayer()) {
                Location playerLocation = sender.getPlayer().getLocation();
                if (plugin.getConfig().isSet("spawn")) {
                    plugin.getConfig().set("spawn", null);
                }
                plugin.getConfig().set("spawn.x", playerLocation.getX());
                plugin.getConfig().set("spawn.y", playerLocation.getY());
                plugin.getConfig().set("spawn.z", playerLocation.getZ());
                plugin.getConfig().set("spawn.yaw", playerLocation.getYaw());
                plugin.getConfig().set("spawn.pitch", playerLocation.getPitch());
                plugin.getConfig().set("spawn.world", playerLocation.getWorld().getName());
                plugin.saveConfig();
                utilMsgs.infoMessage(sender, "&aSet spawn!");
            } else {
                utilMsgs.logErrorMessage("&aConsole Cant Run This Command!");
            }
            return true;
        } else if (name.equalsIgnoreCase("delwarp")) {
            if (args.size() > 0) {
                for (String warpName : plugin.getConfig().getConfigurationSection("warps").getKeys(false)) {
                    if (warpName.equalsIgnoreCase(args.get(0))) {
                        plugin.getConfig().set("warps." + args.get(0), null);
                        utilMsgs.infoMessage(sender, "&aDeleted warp &b" + args.get(0) + "&a!");
                        return true;
                    }
                }
                utilMsgs.errorMessage(sender, "&aWarp &b" + args.get(0) + " &aDoesn't exist!");
            } else {
                utilMsgs.errorMessage(sender, "&bUsage: /delwarp <name>");
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
                    utilMsgs.errorMessage(sender, "&aPlayer Offline/Not exists!");
                }
            } else if (args.size() == 1) {// tp this player to player
                if (context.isPlayer()) {
                    String playerName = null;
                    if ((playerName = utilPlayer.playerOnline(args.get(0))) != null) {
                        PlayerObject p1 = sender;
                        PlayerObject p2 = plugin.getPlayer(playerName);
                        p1.teleportPlayer(p2.getPlayer().getLocation(), false);
                    } else {
                        utilMsgs.errorMessage(sender, "&aPlayer Offline/Not exists!");
                    }
                } else {
                    utilMsgs.errorMessage(sender, "&bUsage: /tp <from player> <to player>");
                }
            } else {
                utilMsgs.errorMessage(sender, "&bUsage: /tp <player>");
                utilMsgs.errorMessage(sender, "&bUsage: /tp <from player> <to player>");
            }
            return true;
        } else if (name.equalsIgnoreCase("tpo")) {
            if (context.isPlayer()) {
                for (Player player : plugin.getServer().getOnlinePlayers()) {
                    PlayerObject playerObject = plugin.getPlayer(player.getUniqueId());
                    playerObject.teleportPlayer(sender.getPlayer().getLocation(), false);
                }
                utilMsgs.infoMessage(sender, "&aTeleported all players to location!");
            } else {
                utilMsgs.logErrorMessage("&aConsole Cant Run This Command!");
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
                                    utilMsgs.infoMessage(tmpp, "&aCanceled previous tpa request.");
                                    utilMsgs.infoMessage(tmpp2, "&aCanceled previous tpa request.");
                                    pendingTpaRequests.remove(tmpp);
                                    found = true;
                                }
                            }
                        }

                        pendingTpaRequests.put(p1, p2);
                        utilMsgs.infoMessage(p1, "&aSent Teleport Request!");
                        utilMsgs.infoMessage(p2, "&aPlayer &b" + p1.playerName + " &awishes to teleport to you &b(&6/tpyes&9,&6 /tpno&b).");
                        int delay = plugin.getConfig().getInt("features.teleport.tpactive");
                        Bukkit.getScheduler().scheduleAsyncDelayedTask(plugin, new Runnable() {
                            @Override
                            public void run() {
                                if (pendingTpaRequests.containsKey(p1) && pendingTpaRequests.get(p1).equals(p2)) {
                                    pendingTpaRequests.remove(p1);
                                    utilMsgs.infoMessage(p1, "&aTpa Request Timed Out!");
                                    utilMsgs.infoMessage(p2, "&aTpa Request Timed Out!");
                                }
                            }
                        }, delay * 20L);
                    } else {
                        utilMsgs.errorMessage(sender, "&aPlayer Offline/Not exists!");
                    }
                } else {
                    utilMsgs.errorMessage(sender, "&bUsage: /tpa <player>");
                }
            } else {
                utilMsgs.logErrorMessage("&aConsole Cant Run This Command!");
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
                                    utilMsgs.infoMessage(tmpp, "&aCanceled previous tpa request.");
                                    utilMsgs.infoMessage(tmpp2, "&aCanceled previous tpa request.");
                                    pendingTpaRequests.remove(tmpp);
                                    found = true;
                                }
                            }
                        }

                        pendingTpaRequests.put(p2, p1);
                        utilMsgs.infoMessage(p1, "&aSent Teleport Request!");
                        utilMsgs.infoMessage(p2, "&aPlayer &b" + p1.playerName + " &awishes for you to teleport to them &b(&6/tpyes, /tpno&b).");
                        int delay = plugin.getConfig().getInt("features.teleport.tpactive");
                        Bukkit.getScheduler().scheduleAsyncDelayedTask(plugin, new Runnable() {
                            @Override
                            public void run() {
                                if (pendingTpaRequests.containsKey(p2) && pendingTpaRequests.get(p2).equals(p1)) {
                                    pendingTpaRequests.remove(p2);
                                    utilMsgs.infoMessage(p1, "&aTpa Request Timed Out!");
                                    utilMsgs.infoMessage(p2, "&aTpa Request Timed Out!");
                                }
                            }
                        }, delay * 20L);
                    } else {
                        utilMsgs.errorMessage(sender, "&aPlayer Offline/Not exists!");
                    }
                } else {
                    utilMsgs.errorMessage(sender, "&bUsage: /tphere <player>");
                }
            } else {
                utilMsgs.logErrorMessage("&aConsole Cant Run This Command!");
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

                if(toPlayer == null) {
                    utilMsgs.logErrorMessage("toPlayer is null");
                } else if(fromPlayer == null) {
                    utilMsgs.logErrorMessage("FromPlayer is null");
                }

                utilMsgs.infoMessage(fromPlayer, "&aTpa Request Accepted!");
                utilMsgs.infoMessage(toPlayer, "&aTpa Request Accepted!");
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
                        utilMsgs.infoMessage(tmpp, "&aTpa Request Denied!");
                        utilMsgs.infoMessage(tmpp2, "&aTpa Request Denied!");
                        pendingTpaRequests.remove(tmpp);
                        return true;
                    }
                }
                utilMsgs.errorMessage(player, "&6No active teleport request!");
            } else {
                utilMsgs.logErrorMessage("&aConsole Cant Run This Command!");
            }
            return true;
        }

        return false;
    }
}
