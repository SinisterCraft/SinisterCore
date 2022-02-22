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
                    utilMsgs.infoMessage(sender, "&7Turning off weather on server");
                    return true;
                } else if(weatherType.equalsIgnoreCase("rain") || weatherType.equalsIgnoreCase("on")) {
                    for(World world : Bukkit.getWorlds()) {
                        world.setStorm(true);
                    }
                    utilMsgs.infoMessage(sender, "&7Turning on weather on server");
                    return true;
                }// else {// no need can just fallback to usage prints
            }
            utilMsgs.infoMessage(sender, "&7Usage: /weather clear");
            utilMsgs.infoMessage(sender, "&7Usage: /weather rain");
            return true;
        } else if(name.equalsIgnoreCase("time")) {
            if(context.getArgs().size() == 1) {
                String timeType = context.argAt(0);
                if(timeType.equalsIgnoreCase("day")) {
                    sender.getPlayer().setPlayerTime(0, false);
                    utilMsgs.infoMessage(sender, "&7Set time day on server");
                    return true;
                } else if(timeType.equalsIgnoreCase("night")) {
                    sender.getPlayer().setPlayerTime(14000, false);
                    utilMsgs.infoMessage(sender, "&7Set time night on server");
                    return true;
                }// else {// no need can just fallback to usage prints
            }
            utilMsgs.infoMessage(sender, "&7Usage: /time day");
            utilMsgs.infoMessage(sender, "&7Usage: /time night");
            return true;
        } else if(name.equalsIgnoreCase("spawnmob")) {
            if(sender != null) {
                if(context.getArgs().size() == 1) {
                    World world = sender.getPlayer().getWorld();
                    Location TargetLocation = sender.getPlayer().getLocation();
                    if(context.argAt(0).equalsIgnoreCase("Wolf")) {
                        world.spawnEntity(TargetLocation, EntityType.WOLF);
                    } else if(context.argAt(0).equalsIgnoreCase("Pig")) {
                        world.spawnEntity(TargetLocation, EntityType.PIG);
                    } else if(context.argAt(0).equalsIgnoreCase("PigZombie")) {
                        world.spawnEntity(TargetLocation, EntityType.PIG_ZOMBIE);
                    } else if(context.argAt(0).equalsIgnoreCase("Cow")) {
                        world.spawnEntity(TargetLocation, EntityType.COW);
                    } else if(context.argAt(0).equalsIgnoreCase("Blaze")) {
                        world.spawnEntity(TargetLocation, EntityType.BLAZE);
                    } else if(context.argAt(0).equalsIgnoreCase("CaveSpider")) {
                        world.spawnEntity(TargetLocation, EntityType.CAVE_SPIDER);
                    } else if(context.argAt(0).equalsIgnoreCase("Chicken")) {
                        world.spawnEntity(TargetLocation, EntityType.CHICKEN);
                    } else if(context.argAt(0).equalsIgnoreCase("Creeper")) {
                        world.spawnEntity(TargetLocation, EntityType.CREEPER);
                    } else if(context.argAt(0).equalsIgnoreCase("EnderDragon")) {
                        world.spawnEntity(TargetLocation, EntityType.ENDER_DRAGON);
                    } else if(context.argAt(0).equalsIgnoreCase("Enderman")) {
                        world.spawnEntity(TargetLocation, EntityType.ENDERMAN);
                    } else if(context.argAt(0).equalsIgnoreCase("Ghast")) {
                        world.spawnEntity(TargetLocation, EntityType.GHAST);
                    } else if(context.argAt(0).equalsIgnoreCase("Giant")) {
                        world.spawnEntity(TargetLocation, EntityType.GIANT);
                    } else if(context.argAt(0).equalsIgnoreCase("IronGolem")) {
                        world.spawnEntity(TargetLocation, EntityType.IRON_GOLEM);
                    } else if(context.argAt(0).equalsIgnoreCase("MagmaCube")) {
                        world.spawnEntity(TargetLocation, EntityType.MAGMA_CUBE);
                    } else if(context.argAt(0).equalsIgnoreCase("Ocelot")) {
                        world.spawnEntity(TargetLocation, EntityType.OCELOT);
                    } else if(context.argAt(0).equalsIgnoreCase("Sheep")) {
                        world.spawnEntity(TargetLocation, EntityType.SHEEP);
                    } else if(context.argAt(0).equalsIgnoreCase("SilverFish")) {
                        world.spawnEntity(TargetLocation, EntityType.SILVERFISH);
                    } else if(context.argAt(0).equalsIgnoreCase("Skeleton")) {
                        world.spawnEntity(TargetLocation, EntityType.SKELETON);
                    } else if(context.argAt(0).equalsIgnoreCase("Slime")) {
                        world.spawnEntity(TargetLocation, EntityType.SLIME);
                    } else if(context.argAt(0).equalsIgnoreCase("Spider")) {
                        world.spawnEntity(TargetLocation, EntityType.SPIDER);
                    } else if(context.argAt(0).equalsIgnoreCase("Squid")) {
                        world.spawnEntity(TargetLocation, EntityType.SQUID);
                    } else if(context.argAt(0).equalsIgnoreCase("Villager")) {
                        world.spawnEntity(TargetLocation, EntityType.VILLAGER);
                    } else if(context.argAt(0).equalsIgnoreCase("Zombie")) {
                        world.spawnEntity(TargetLocation, EntityType.ZOMBIE);
                    } else {
                        utilMsgs.errorMessage(sender, "&7Invalid mob type!");
                        //utilMsgs.infoMessage(sender, "&7Mob Types: &7Zombie&9, &bVillager&9, &bSquid&9, &bSpider&9, &bSlime");
                        return true;
                    }
                    utilMsgs.infoMessage(sender, "&7Spawned mob at player location!");
                    return true;
                }
                utilMsgs.infoMessage(sender, "&7Usage: /spawnmob <mob>");
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
                utilMsgs.infoMessage(sender, "&7Usage: /broadcast <message>");
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
                utilMsgs.infoMessage(sender, "&7Usage: /alert <message>");
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
                utilMsgs.infoMessage(sender, "&7Usage: /rbroadcast <message>");
            }
            utilMsgs.logInfoMessage("usage: rbroadcast <message>");
            return true;
        }
        return true;
    }
}
