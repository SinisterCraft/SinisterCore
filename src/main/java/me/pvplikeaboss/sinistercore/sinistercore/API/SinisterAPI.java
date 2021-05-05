package me.pvplikeaboss.sinistercore.sinistercore.API;

import me.pvplikeaboss.sinistercore.sinistercore.Instances;
import me.pvplikeaboss.sinistercore.sinistercore.SinisterCore;
import me.pvplikeaboss.sinistercore.sinistercore.commands.Commands;
import me.pvplikeaboss.sinistercore.sinistercore.commands.util.CommandMapp;
import me.pvplikeaboss.sinistercore.sinistercore.modules.economy.EconomyImplementer;
import me.pvplikeaboss.sinistercore.sinistercore.objects.PlayerObject;

import java.util.List;
import java.util.UUID;

public class SinisterAPI {
    private SinisterCore plugin;

    public SinisterAPI(SinisterCore p) {
        this.plugin = p;
    }

    public SinisterCore getPlugin() {
        return plugin;
    }

    public PlayerObject getPlayer(UUID pUUID) {
        return plugin.getPlayer(pUUID);
    }

    public PlayerObject getPlayer(String pName) {
        return plugin.getPlayer(pName);
    }

    public List<PlayerObject> getAllPlayers() {
        return plugin.getPlayers();
    }

    public EconomyImplementer getEconomy() {
        return (EconomyImplementer) Instances.getInstance(Instances.InstanceType.Economy, -1);
    }

    public Object getInstance(Instances.InstanceType type, int intstance_id) {
        return Instances.getInstance(type, intstance_id);
    }

    public CommandMapp getCmdMapp() {
        return ((Commands) Instances.getInstance(Instances.InstanceType.Commands, -1)).getCmdMap();
    }
}
