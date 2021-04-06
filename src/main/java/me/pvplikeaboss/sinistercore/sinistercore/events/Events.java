package me.pvplikeaboss.sinistercore.sinistercore.events;

import me.pvplikeaboss.sinistercore.sinistercore.SinisterCore;
import org.bukkit.event.HandlerList;

public class Events {

    private final SinisterCore plugin;
    private CombatTagEvents combatTagEvent = null;
    private JoinLeaveEvents jlEvent = null;
    private MovementEvents moveEvent = null;
    private ChatEvents chatEvent = null;
    private DamageEvents deathEvent = null;

    public Events(SinisterCore p) {
        this.plugin = p;
    }

    public void load() {
        this.combatTagEvent = new CombatTagEvents(plugin);
        this.jlEvent = new JoinLeaveEvents(plugin);
        this.moveEvent = new MovementEvents(plugin);
        this.chatEvent = new ChatEvents(plugin);
        this.deathEvent = new DamageEvents(plugin);
        this.initializeEvents();
    }

    public void unload() {
        HandlerList.unregisterAll(combatTagEvent);
        HandlerList.unregisterAll(jlEvent);
        HandlerList.unregisterAll(moveEvent);
        HandlerList.unregisterAll(chatEvent);
        HandlerList.unregisterAll(deathEvent);
        this.combatTagEvent = null;
        this.jlEvent = null;
        this.moveEvent = null;
        this.chatEvent = null;
        this.deathEvent = null;
    }

    private void initializeEvents() {
        this.plugin.getServer().getPluginManager().registerEvents(combatTagEvent, plugin);
        this.plugin.getServer().getPluginManager().registerEvents(jlEvent, plugin);
        this.plugin.getServer().getPluginManager().registerEvents(moveEvent, plugin);
        this.plugin.getServer().getPluginManager().registerEvents(chatEvent, plugin);
        this.plugin.getServer().getPluginManager().registerEvents(deathEvent, plugin);
    }
}
