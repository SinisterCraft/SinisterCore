package me.pvplikeaboss.sinistercore.sinistercore.commands;

import me.pvplikeaboss.sinistercore.sinistercore.SinisterCore;
import me.pvplikeaboss.sinistercore.sinistercore.commands.executors.*;
import me.pvplikeaboss.sinistercore.sinistercore.commands.modules.ClearlagCommands;
import me.pvplikeaboss.sinistercore.sinistercore.commands.util.CommandBuilder;
import me.pvplikeaboss.sinistercore.sinistercore.commands.util.CommandMapp;

import java.util.ArrayList;
import java.util.List;

public class Commands {
    private final SinisterCore plugin;
    private final CommandMapp cmdMap;

    private SinisterTabCompleter tabCompleter;

    private SinisterCommands sinisterCmd;
    private MessageCommands msgCmd;
    private TeleportCommands tpCmd;
    private KitCommands kitCmd;
    private PunishmentCommands punishCmd;
    private PlayerCommands playerCmd;
    private ServerCommands serverCmd;
    private EconomyCommands econCmd;
    private ClearlagCommands clearlagCmd;

    public Commands(SinisterCore p) {
        this.plugin = p;
        this.cmdMap = new CommandMapp(plugin);
    }

    public CommandMapp getCmdMap() {
        return cmdMap;
    }

    public void load() {
        sinisterCmd = new SinisterCommands();
        msgCmd = new MessageCommands();
        tpCmd = new TeleportCommands();
        kitCmd = new KitCommands();
        punishCmd = new PunishmentCommands();
        playerCmd = new PlayerCommands();
        serverCmd = new ServerCommands();
        econCmd = new EconomyCommands();
        clearlagCmd = new ClearlagCommands();
        tabCompleter = new SinisterTabCompleter(plugin);
        initializeCommands();
    }

    public void unload() {
        sinisterCmd = null;
        msgCmd = null;
        tpCmd = null;
        kitCmd = null;
        punishCmd = null;
        playerCmd = null;
        serverCmd = null;
        econCmd = null;
        clearlagCmd = null;
        tabCompleter = null;
    }

    private void initializeCommands() {
        ArrayList<SinisterCommand> cmds = new ArrayList();

        cmds.add(new CommandBuilder(plugin, "sinistercore").executor(sinisterCmd::onCommand).usage("/sinistercore").description("Cusom SinisterCore Command").completer(tabCompleter).build());

        // Initialize custom messages
        for(String cmdName : plugin.getConfig().getConfigurationSection("cmd-messages").getKeys(false)) {
            cmds.add(new CommandBuilder(plugin, cmdName).executor(msgCmd::onCommand).usage("/"+cmdName).description("Custom SinisterCore message").completer(tabCompleter).build());
        }

        // Initialize message Commands

        cmds.add(new CommandBuilder(plugin, "tell").executor(msgCmd::onCommand).usage("/tell").description("Cusom SinisterCore message").completer(tabCompleter).build());
        cmds.add(new CommandBuilder(plugin, "msg").executor(msgCmd::onCommand).usage("/msg").description("Cusom SinisterCore message").completer(tabCompleter).build());
        cmds.add(new CommandBuilder(plugin, "t").executor(msgCmd::onCommand).usage("/t").description("Cusom SinisterCore message").completer(tabCompleter).build());
        cmds.add(new CommandBuilder(plugin, "reply").executor(msgCmd::onCommand).usage("/reply").description("Cusom SinisterCore message").completer(tabCompleter).build());
        cmds.add(new CommandBuilder(plugin, "r").executor(msgCmd::onCommand).usage("/r").description("Cusom SinisterCore message").completer(tabCompleter).build());
        cmds.add(new CommandBuilder(plugin, "msgtoggle").executor(msgCmd::onCommand).usage("/msgtoggle").description("Cusom SinisterCore message").completer(tabCompleter).build());

        // Initialize Player Commands
        cmds.add(new CommandBuilder(plugin, "hat").executor(playerCmd::onCommand).usage("/hat").description("Custom SinisterCore PlayerCmds").completer(tabCompleter).permission("sinistercore.hat").build());
        cmds.add(new CommandBuilder(plugin, "more").executor(playerCmd::onCommand).usage("/more").description("Custom SinisterCore PlayerCmds").completer(tabCompleter).permission("sinistercore.more").build());
        cmds.add(new CommandBuilder(plugin, "pweather").executor(playerCmd::onCommand).usage("/pweather").description("Custom SinisterCore PlayerCmds").completer(tabCompleter).permission("sinistercore.pweather").build());
        cmds.add(new CommandBuilder(plugin, "ptime").executor(playerCmd::onCommand).usage("/ptime").description("Custom SinisterCore PlayerCmds").completer(tabCompleter).permission("sinistercore.ptime").build());
        cmds.add(new CommandBuilder(plugin, "invsee").executor(playerCmd::onCommand).usage("/invsee").description("Custom SinisterCore PlayerCmds").completer(tabCompleter).permission("sinistercore.invsee").build());
        cmds.add(new CommandBuilder(plugin, "enchant").executor(playerCmd::onCommand).usage("/enchant").description("Custom SinisterCore PlayerCmds").completer(tabCompleter).permission("sinistercore.enchant").build());
        cmds.add(new CommandBuilder(plugin, "workbench").executor(playerCmd::onCommand).usage("/workbench").aliases("wb").description("Custom SinisterCore PlayerCmds").completer(tabCompleter).permission("sinistercore.workbench").build());
        cmds.add(new CommandBuilder(plugin, "anvil").executor(playerCmd::onCommand).usage("/anvil").description("Custom SinisterCore PlayerCmds").completer(tabCompleter).permission("sinistercore.anvil").build());
        cmds.add(new CommandBuilder(plugin, "give").executor(playerCmd::onCommand).usage("/give").aliases("i").description("Custom SinisterCore PlayerCmds").completer(tabCompleter).permission("sinistercore.give").build());
        cmds.add(new CommandBuilder(plugin, "enderchest").executor(playerCmd::onCommand).usage("/enderchest").aliases("ec").description("Custom SinisterCore PlayerCmds").completer(tabCompleter).permission("sinistercore.enderchest").build());
        cmds.add(new CommandBuilder(plugin, "sudo").executor(playerCmd::onCommand).usage("/sudo").description("Custom SinisterCore PlayerCmds").completer(tabCompleter).permission("sinistercore.sudo").build());
        cmds.add(new CommandBuilder(plugin, "vanish").executor(playerCmd::onCommand).usage("/vanish").aliases("v").description("Custom SinisterCore PlayerCmds").completer(tabCompleter).permission("sinistercore.vanish").build());
        cmds.add(new CommandBuilder(plugin, "godmode").executor(playerCmd::onCommand).usage("/godmode").aliases("god").description("Custom SinisterCore PlayerCmds").completer(tabCompleter).permission("sinistercore.godmode").build());
        cmds.add(new CommandBuilder(plugin, "clearinventory").executor(playerCmd::onCommand).usage("/clearinventory").aliases("clear", "ci").description("Custom SinisterCore PlayerCmds").completer(tabCompleter).permission("sinistercore.clearinventory").build());
        cmds.add(new CommandBuilder(plugin, "rename").executor(playerCmd::onCommand).usage("/rename").aliases("setname").description("Custom SinisterCore PlayerCmds").completer(tabCompleter).permission("sinistercore.rename").build());
        cmds.add(new CommandBuilder(plugin, "relore").executor(playerCmd::onCommand).usage("/relore").aliases("setlore").description("Custom SinisterCore PlayerCmds").completer(tabCompleter).permission("sinistercore.relore").build());
        cmds.add(new CommandBuilder(plugin, "nickname").executor(playerCmd::onCommand).usage("/nickname").aliases("nick").description("Custom SinisterCore PlayerCmds").completer(tabCompleter).permission("sinistercore.nickname").build());
        cmds.add(new CommandBuilder(plugin, "fix").executor(playerCmd::onCommand).usage("/fix").aliases("repair").description("Custom SinisterCore PlayerCmds").completer(tabCompleter).permission("sinistercore.repair").build());
        cmds.add(new CommandBuilder(plugin, "feed").executor(playerCmd::onCommand).usage("/feed").description("Custom SinisterCore PlayerCmds").completer(tabCompleter).permission("sinistercore.feed").build());
        cmds.add(new CommandBuilder(plugin, "heal").executor(playerCmd::onCommand).usage("/heal").description("Custom SinisterCore PlayerCmds").completer(tabCompleter).permission("sinistercore.heal").build());
        cmds.add(new CommandBuilder(plugin, "gmc").executor(playerCmd::onCommand).usage("/gmc").description("Custom SinisterCore PlayerCmds").completer(tabCompleter).permission("sinistercore.gamemode").build());
        cmds.add(new CommandBuilder(plugin, "gms").executor(playerCmd::onCommand).usage("/gms").description("Custom SinisterCore PlayerCmds").completer(tabCompleter).permission("sinistercore.gamemode").build());
        cmds.add(new CommandBuilder(plugin, "fly").executor(playerCmd::onCommand).usage("/fly").description("Custom SinisterCore PlayerCmds").completer(tabCompleter).permission("sinistercore.fly").build());
        cmds.add(new CommandBuilder(plugin, "back").executor(playerCmd::onCommand).usage("/back").description("Custom SinisterCore PlayerCmds").completer(tabCompleter).permission("sinistercore.back").build());

        //Initialize Server Commands
        cmds.add(new CommandBuilder(plugin, "weather").executor(serverCmd::onCommand).usage("/weather").description("Custom SinisterCore ServerCmds").completer(tabCompleter).permission("sinistercore.weather").build());
        cmds.add(new CommandBuilder(plugin, "time").executor(serverCmd::onCommand).usage("/time").description("Custom SinisterCore ServerCmds").completer(tabCompleter).permission("sinistercore.time").build());
        cmds.add(new CommandBuilder(plugin, "spawnmob").executor(serverCmd::onCommand).usage("/spawnmob").description("Custom SinisterCore ServerCmds").completer(tabCompleter).permission("sinistercore.spawnmob").build());
        cmds.add(new CommandBuilder(plugin, "broadcast").executor(serverCmd::onCommand).usage("/broadcast").description("Custom SinisterCore PlayerCmds").completer(tabCompleter).permission("sinistercore.broadcast").build());
        cmds.add(new CommandBuilder(plugin, "alert").executor(serverCmd::onCommand).usage("/alert").description("Custom SinisterCore PlayerCmds").completer(tabCompleter).permission("sinistercore.alert").build());
        cmds.add(new CommandBuilder(plugin, "rbroadcast").executor(serverCmd::onCommand).usage("/rbroadcast").description("Custom SinisterCore PlayerCmds").completer(tabCompleter).permission("sinistercore.rbroadcast").build());

        //Initalize Clearlag Commads
        cmds.add(new CommandBuilder(plugin, "clearlag").executor(clearlagCmd::onCommand).usage("/clearlag").description("Custom SinisterCore ClearLagCmds").completer(tabCompleter).permission("sinistercore.clearlag").build());

        //Initialize Kit Commands
        if(plugin.getConfig().getBoolean("features.kits.enabled")) {
            // Initialize kit commands
            cmds.add(new CommandBuilder(plugin, "kit").executor(kitCmd::onCommand).usage("/kit").description("Custom SinisterCore kits").completer(tabCompleter).build());
            cmds.add(new CommandBuilder(plugin, "createkit").executor(kitCmd::onCommand).usage("/createkit").description("Custom SinisterCore kits").completer(tabCompleter).permission("sinistercore.kit.create").build());
            cmds.add(new CommandBuilder(plugin, "deletekit").executor(kitCmd::onCommand).usage("/deletekit").description("Custom SinisterCore kits").completer(tabCompleter).permission("sinistercore.kit.delete").build());
        }

        //Initialize Teleport Commands
        if(plugin.getConfig().getBoolean("features.teleport.enabled")) {
            // Initialize home commands
            cmds.add(new CommandBuilder(plugin, "home").executor(tpCmd::onCommand).usage("/home").description("Custom SinisterCore homes").completer(tabCompleter).build());
            cmds.add(new CommandBuilder(plugin, "sethome").executor(tpCmd::onCommand).usage("/sethome").description("Custom SinisterCore homes").completer(tabCompleter).build());
            cmds.add(new CommandBuilder(plugin, "delhome").executor(tpCmd::onCommand).usage("/delhome").description("Custom SinisterCore homes").completer(tabCompleter).build());

            // Initialize spawn commands
            cmds.add(new CommandBuilder(plugin, "spawn").executor(tpCmd::onCommand).usage("/spawn").description("Custom SinisterCore spawn").completer(tabCompleter).build());
            cmds.add(new CommandBuilder(plugin, "setspawn").executor(tpCmd::onCommand).usage("/setspawn").description("Custom SinisterCore spawn").completer(tabCompleter).permission("sinistercore.setspawn").build());

            // Initialize warp commands
            cmds.add(new CommandBuilder(plugin, "warp").executor(tpCmd::onCommand).usage("/warp").description("Custom SinisterCore warps").completer(tabCompleter).build());
            cmds.add(new CommandBuilder(plugin, "setwarp").executor(tpCmd::onCommand).usage("/setwarp").description("Custom SinisterCore warps").completer(tabCompleter).permission("sinistercore.setwarp").build());
            cmds.add(new CommandBuilder(plugin, "delwarp").executor(tpCmd::onCommand).usage("/delwarp").description("Custom SinisterCore warps").completer(tabCompleter).permission("sinistercore.delwarp").build());

            // Initialize teleport commands
            cmds.add(new CommandBuilder(plugin, "tp").executor(tpCmd::onCommand).usage("/tp").description("Custom SinisterCore teleport").completer(tabCompleter).permission("sinistercore.tp").build());
            cmds.add(new CommandBuilder(plugin, "tpo").executor(tpCmd::onCommand).usage("/tpo").description("Custom SinisterCore teleport").completer(tabCompleter).permission("sinistercore.tpo").build());
            cmds.add(new CommandBuilder(plugin, "tpa").executor(tpCmd::onCommand).usage("/tpa").description("Custom SinisterCore teleport").completer(tabCompleter).build());
            cmds.add(new CommandBuilder(plugin, "tpahere").executor(tpCmd::onCommand).usage("/tpahere").description("Custom SinisterCore teleport").completer(tabCompleter).build());
            cmds.add(new CommandBuilder(plugin, "tpyes").executor(tpCmd::onCommand).usage("/tpyes").description("Custom SinisterCore teleport").completer(tabCompleter).build());
            cmds.add(new CommandBuilder(plugin, "tpaccept").executor(tpCmd::onCommand).usage("/tpaccept").description("Custom SinisterCore teleport").completer(tabCompleter).build());
            cmds.add(new CommandBuilder(plugin, "tpno").executor(tpCmd::onCommand).usage("/tpno").description("Custom SinisterCore teleport").completer(tabCompleter).build());
            cmds.add(new CommandBuilder(plugin, "tpdeny").executor(tpCmd::onCommand).usage("/tpdeny").description("Custom SinisterCore teleport").completer(tabCompleter).build());
        }

        //Initialize Economy Commands

        if(plugin.getConfig().getBoolean("features.economy.enabled")) {
            cmds.add(new CommandBuilder(plugin, "baltop").executor(econCmd::onCommand).usage("/baltop").description("Custom SinisterCore Economy").completer(tabCompleter).build());
            cmds.add(new CommandBuilder(plugin, "bal").executor(econCmd::onCommand).usage("/bal").description("Custom SinisterCore Economy").completer(tabCompleter).build());
            cmds.add(new CommandBuilder(plugin, "balance").executor(econCmd::onCommand).usage("/bal").description("Custom SinisterCore Economy").completer(tabCompleter).build());
            cmds.add(new CommandBuilder(plugin, "money").executor(econCmd::onCommand).usage("/bal").description("Custom SinisterCore Economy").completer(tabCompleter).build());
            cmds.add(new CommandBuilder(plugin, "pay").executor(econCmd::onCommand).usage("/pay").description("Custom SinisterCore Economy").completer(tabCompleter).build());
            cmds.add(new CommandBuilder(plugin, "eco").executor(econCmd::onCommand).usage("/eco").description("Custom SinisterCore Economy").completer(tabCompleter).permission("sinistercore.eco.admin").build());
        }

        //Initalize Punishment Commands
        if(plugin.getConfig().getBoolean("features.punishment.enabled")) {
            cmds.add(new CommandBuilder(plugin, "kick").executor(punishCmd::onCommand).usage("/kick").description("Custom SinisterCore Punishments").completer(tabCompleter).permission("sinistercore.punish.kick").build());
            cmds.add(new CommandBuilder(plugin, "mute").executor(punishCmd::onCommand).usage("/mute").description("Custom SinisterCore Punishments").completer(tabCompleter).permission("sinistercore.punish.mute").build());
            cmds.add(new CommandBuilder(plugin, "tempmute").executor(punishCmd::onCommand).usage("/tempmute").description("Custom SinisterCore Punishments").completer(tabCompleter).permission("sinistercore.punish.tempmute").build());
            cmds.add(new CommandBuilder(plugin, "unmute").executor(punishCmd::onCommand).usage("/unmute").description("Custom SinisterCore Punishments").completer(tabCompleter).permission("sinistercore.punish.unmute").build());
            cmds.add(new CommandBuilder(plugin, "ban").executor(punishCmd::onCommand).usage("/ban").description("Custom SinisterCore Punishments").completer(tabCompleter).permission("sinistercore.punish.ban").build());
            cmds.add(new CommandBuilder(plugin, "tempban").executor(punishCmd::onCommand).usage("/tempban").description("Custom SinisterCore Punishments").completer(tabCompleter).permission("sinistercore.punish.tempban").build());
            cmds.add(new CommandBuilder(plugin, "unban").executor(punishCmd::onCommand).usage("/unban").description("Custom SinisterCore Punishments").completer(tabCompleter).permission("sinistercore.punish.unban").build());
        }

        //Register all commands
        for (SinisterCommand cmd : cmds) {
            cmdMap.addCommand(cmd);
        }
    }
}
