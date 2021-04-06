package me.pvplikeaboss.sinistercore.sinistercore.commands.executors;

import me.pvplikeaboss.sinistercore.sinistercore.Instances;
import me.pvplikeaboss.sinistercore.sinistercore.SinisterCore;
import me.pvplikeaboss.sinistercore.sinistercore.commands.util.CommandContext;
import me.pvplikeaboss.sinistercore.sinistercore.objects.PlayerObject;
import me.pvplikeaboss.sinistercore.sinistercore.utilites.misc.Messages;
import me.pvplikeaboss.sinistercore.sinistercore.utilites.serverutils.BroadcastUtils;
import org.bukkit.*;
import org.bukkit.entity.EntityType;

public class ServerCommands {
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

        if(name.equalsIgnoreCase("weather")) {
            if(context.getArgs().size() == 1) {
                String weatherType = context.argAt(0);
                if(weatherType.equalsIgnoreCase("clear") || weatherType.equalsIgnoreCase("none") || weatherType.equalsIgnoreCase("off")) {
                    for(World world : Bukkit.getWorlds()) {
                        world.setStorm(false);
                    }
                    utilMsgs.infoMessage(sender, "&9Turning off weather on server");
                    return true;
                } else if(weatherType.equalsIgnoreCase("rain") || weatherType.equalsIgnoreCase("on")) {
                    for(World world : Bukkit.getWorlds()) {
                        world.setStorm(true);
                    }
                    utilMsgs.infoMessage(sender, "&9Turning on weather on server");
                    return true;
                }// else {// no need can just fallback to usage prints
            }
            utilMsgs.infoMessage(sender, "&6Usage: &9/weather clear");
            utilMsgs.infoMessage(sender, "&6Usage: &9/weather rain");
            return true;
        } else if(name.equalsIgnoreCase("time")) {
            if(context.getArgs().size() == 1) {
                String timeType = context.argAt(0);
                if(timeType.equalsIgnoreCase("day")) {
                    sender.getPlayer().setPlayerTime(0, false);
                    utilMsgs.infoMessage(sender, "&9Set time day on server");
                    return true;
                } else if(timeType.equalsIgnoreCase("night")) {
                    sender.getPlayer().setPlayerTime(14000, false);
                    utilMsgs.infoMessage(sender, "&9Set time night on server");
                    return true;
                }// else {// no need can just fallback to usage prints
            }
            utilMsgs.infoMessage(sender, "&6Usage: &9/time day");
            utilMsgs.infoMessage(sender, "&6Usage: &9/time night");
            return true;
        } else if(name.equalsIgnoreCase("spawnmob")) {
            if(sender != null) {
                if(context.getArgs().size() == 1) {
                    World world = sender.getPlayer().getWorld();
                    Location TargetLocation = sender.getPlayer().getLocation();
                    if(context.argAt(0).equalsIgnoreCase("Wolf")) {
                        world.spawnCreature(TargetLocation, EntityType.WOLF);
                    } else if(context.argAt(0).equalsIgnoreCase("Pig")) {
                        world.spawnCreature(TargetLocation, EntityType.PIG);
                    } else if(context.argAt(0).equalsIgnoreCase("PigZombie")) {
                        world.spawnCreature(TargetLocation, EntityType.PIG_ZOMBIE);
                    } else if(context.argAt(0).equalsIgnoreCase("Cow")) {
                        world.spawnCreature(TargetLocation, EntityType.COW);
                    } else if(context.argAt(0).equalsIgnoreCase("Blaze")) {
                        world.spawnCreature(TargetLocation, EntityType.BLAZE);
                    } else if(context.argAt(0).equalsIgnoreCase("CaveSpider")) {
                        world.spawnCreature(TargetLocation, EntityType.CAVE_SPIDER);
                    } else if(context.argAt(0).equalsIgnoreCase("Chicken")) {
                        world.spawnCreature(TargetLocation, EntityType.CHICKEN);
                    } else if(context.argAt(0).equalsIgnoreCase("Creeper")) {
                        world.spawnCreature(TargetLocation, EntityType.CREEPER);
                    } else if(context.argAt(0).equalsIgnoreCase("EnderDragon")) {
                        world.spawnCreature(TargetLocation, EntityType.ENDER_DRAGON);
                    } else if(context.argAt(0).equalsIgnoreCase("Enderman")) {
                        world.spawnCreature(TargetLocation, EntityType.ENDERMAN);
                    } else if(context.argAt(0).equalsIgnoreCase("Ghast")) {
                        world.spawnCreature(TargetLocation, EntityType.GHAST);
                    } else if(context.argAt(0).equalsIgnoreCase("Giant")) {
                        world.spawnCreature(TargetLocation, EntityType.GIANT);
                    } else if(context.argAt(0).equalsIgnoreCase("IronGolem")) {
                        world.spawnCreature(TargetLocation, EntityType.IRON_GOLEM);
                    } else if(context.argAt(0).equalsIgnoreCase("MagmaCube")) {
                        world.spawnCreature(TargetLocation, EntityType.MAGMA_CUBE);
                    } else if(context.argAt(0).equalsIgnoreCase("Ocelot")) {
                        world.spawnCreature(TargetLocation, EntityType.OCELOT);
                    } else if(context.argAt(0).equalsIgnoreCase("Sheep")) {
                        world.spawnCreature(TargetLocation, EntityType.SHEEP);
                    } else if(context.argAt(0).equalsIgnoreCase("SilverFish")) {
                        world.spawnCreature(TargetLocation, EntityType.SILVERFISH);
                    } else if(context.argAt(0).equalsIgnoreCase("Skeleton")) {
                        world.spawnCreature(TargetLocation, EntityType.SKELETON);
                    } else if(context.argAt(0).equalsIgnoreCase("Slime")) {
                        world.spawnCreature(TargetLocation, EntityType.SLIME);
                    } else if(context.argAt(0).equalsIgnoreCase("Spider")) {
                        world.spawnCreature(TargetLocation, EntityType.SPIDER);
                    } else if(context.argAt(0).equalsIgnoreCase("Squid")) {
                        world.spawnCreature(TargetLocation, EntityType.SQUID);
                    } else if(context.argAt(0).equalsIgnoreCase("Villager")) {
                        world.spawnCreature(TargetLocation, EntityType.VILLAGER);
                    } else if(context.argAt(0).equalsIgnoreCase("Zombie")) {
                        world.spawnCreature(TargetLocation, EntityType.ZOMBIE);
                    } else {
                        utilMsgs.errorMessage(sender, "&9Invalid mob type!");
                        utilMsgs.infoMessage(sender, "&9Mob Types: &bZombie&9, &bVillager&9, &bSquid&9, &bSpider&9, &bSlime");
                        return true;
                    }
                    utilMsgs.infoMessage(sender, "&9Spawned mob at player location!");
                    return true;
                }
                utilMsgs.infoMessage(sender, "&6Usage: &9/spawnmob <mob>");
                return true;
            }
            utilMsgs.logErrorMessage("must be ran as player");
            return true;
        } else if(name.equalsIgnoreCase("broadcast")) {// server broadcast
            if(context.getArgs().size() > 0) {
                StringBuilder messageBuilder = new StringBuilder();

                int x;
                for(x = 0; x < context.getArgs().size(); x++) {
                    messageBuilder.append(context.argAt(x));
                    if(x != context.getArgs().size()-1) {
                        messageBuilder.append(" ");
                    }
                }

                utilBroadcast.broadcast(messageBuilder.toString());
                return true;
            }
            if(sender != null) {
                utilMsgs.infoMessage(sender, "&6Usage: &9/broadcast <message>");
            }
            utilMsgs.logInfoMessage("usage: broadcast <message>");
            return true;
        } else if(name.equalsIgnoreCase("alert")) {// alert broadcast
            if(context.getArgs().size() > 0) {
                StringBuilder messageBuilder = new StringBuilder();

                int x;
                for(x = 0; x < context.getArgs().size(); x++) {
                    messageBuilder.append(context.argAt(x));
                    if(x != context.getArgs().size()-1) {
                        messageBuilder.append(" ");
                    }
                }

                utilBroadcast.alert(messageBuilder.toString());
                return true;
            }
            if(sender != null) {
                utilMsgs.infoMessage(sender, "&6Usage: &9/alert <message>");
            }
            utilMsgs.logInfoMessage("usage: alert <message>");
            return true;
        } else if(name.equalsIgnoreCase("rbroadcast")) {// raw broadcast
            if(context.getArgs().size() > 0) {
                StringBuilder messageBuilder = new StringBuilder();

                int x;
                for(x = 0; x < context.getArgs().size(); x++) {
                    messageBuilder.append(context.argAt(x));
                    if(x != context.getArgs().size()-1) {
                        messageBuilder.append(" ");
                    }
                }

                utilBroadcast.rawBroadcast(messageBuilder.toString());
                return true;
            }
            if(sender != null) {
                utilMsgs.infoMessage(sender, "&6Usage: &9/rbroadcast <message>");
            }
            utilMsgs.logInfoMessage("usage: rbroadcast <message>");
            return true;
        }
        return true;
    }
}
