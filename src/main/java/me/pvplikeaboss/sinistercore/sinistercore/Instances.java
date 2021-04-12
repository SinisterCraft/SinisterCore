package me.pvplikeaboss.sinistercore.sinistercore;

import me.pvplikeaboss.sinistercore.sinistercore.commands.Commands;
import me.pvplikeaboss.sinistercore.sinistercore.events.Events;
import me.pvplikeaboss.sinistercore.sinistercore.modules.API.permissionsex.PexAPI;
import me.pvplikeaboss.sinistercore.sinistercore.modules.API.vault.EconomyImplementer;
import me.pvplikeaboss.sinistercore.sinistercore.modules.API.vault.VaultAPI;
import me.pvplikeaboss.sinistercore.sinistercore.modules.chat.ChatHandler;
import me.pvplikeaboss.sinistercore.sinistercore.modules.data.files.configs.Configs;
import me.pvplikeaboss.sinistercore.sinistercore.modules.punishment.Punishment;
import me.pvplikeaboss.sinistercore.sinistercore.objects.PlayerObject;
import me.pvplikeaboss.sinistercore.sinistercore.utilites.Utilities;
import net.milkbowl.vault.economy.Economy;

import java.util.HashMap;
import java.util.UUID;

public class Instances {
    private static SinisterCore plugin;

    public static Configs configs = null;
    public static Utilities utilities = null;
    public static Events events = null;
    public static Commands commands = null;
    public static Punishment punishment = null;
    public static Economy ecoImplementer = null;
    public static ChatHandler chatHandler = null;

    public static HashMap<Integer, PlayerObject> teleportHashMap = null;
    public static HashMap<UUID, UUID> messageHashMap = null;// TODO: migrate to playerobject list for less ram usage
    public static HashMap<PlayerObject, PlayerObject> pendingTpaRequests = null;// TODO: migrate to playerobject list for less ram usage

    public static void reload_instances() {
        unload_instances();
        configs.reload();
    }

    public static void unload_instances() {
        utilities.unload();
    }

    public enum InstanceType {
        Events,
        Commands,
        Config,
        Chat,
        Punishment,
        Economy,
        HashMap,
        List,
        Utilities
    };

    static void load_instances (SinisterCore p) {
        plugin = p;

        configs = new Configs(plugin);
        configs.load();

        //Utilities
        utilities = new Utilities(plugin);
        utilities.load();

        punishment = new Punishment(plugin);

        //Variables
        teleportHashMap = new HashMap<>();
        messageHashMap = new HashMap<>();
        pendingTpaRequests = new HashMap<PlayerObject, PlayerObject>();// key = from; value = to

        //Punishment
        punishment = new Punishment(plugin);

        //Economy
        if (plugin.getConfig().getBoolean("features.economy.enabled")) {
            ecoImplementer = new EconomyImplementer(plugin);
            VaultAPI.load(plugin);
        }

        //Chat
        if (plugin.getConfig().getBoolean("features.chat.enabled")) {
            PexAPI.load(plugin);
            chatHandler = new ChatHandler(plugin);
        }

        //Events and Commands
        events = new Events(plugin);
        events.load();

        commands = new Commands(plugin);
        commands.load();

    }

    public static Object getInstance(InstanceType instance_type, int instance_id) {
        switch(instance_type) {
            case Chat: {
                if(instance_id == -1) {
                    return chatHandler;
                }
                break;
            }

            case List: {
                break;
            }

            case Config: {
                if(instance_id == -1) {// return parent instance
                    return configs;
                }

                return configs.getConfig(instance_id);
            }

            case Events: {
                if(instance_id == -1) {
                    return events;
                }
                break;
            }

            case Economy: {
                if(instance_id == -1) {
                    return ecoImplementer;
                }
                break;
            }

            case HashMap: {
                if(instance_id == 0) {
                    return teleportHashMap;
                }
                if(instance_id == 1) {
                    return messageHashMap;
                }
                if(instance_id == 2) {
                    return pendingTpaRequests;
                }
                break;
            }

            case Commands: {
                if(instance_id == -1) {
                    return commands;
                }
            }

            case Punishment: {
                if(instance_id == -1) {
                    return punishment;
                }
            }

            case Utilities: {
                if(instance_id == -1) {// return parent instance
                    return utilities;
                }

                return utilities.getUtil(instance_id);
            }
        }
        return null;
    }


}
