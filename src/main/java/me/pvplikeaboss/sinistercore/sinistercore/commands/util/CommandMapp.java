package me.pvplikeaboss.sinistercore.sinistercore.commands.util;

import me.pvplikeaboss.sinistercore.sinistercore.SinisterCore;
import me.pvplikeaboss.sinistercore.sinistercore.commands.SinisterCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.*;

import java.lang.reflect.Field;
import java.util.*;

public class CommandMapp {
    private final SinisterCore plugin;
    private Set<SinisterCommand> commands = new HashSet<>();

    public CommandMapp(SinisterCore p) {
        this.plugin = p;
    }

    public final void addCommand(SinisterCommand... commands) {
        CommandMap map = null;
        Field field;

        try {
            field = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            field.setAccessible(true);
            map = (CommandMap)field.get(Bukkit.getServer());
        }
        catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        for (SinisterCommand command : commands) {
            if (map.getCommand(command.getName()) == null) {
                this.commands.add(command);
                map.register(plugin.getDescription().getName(), new BukkitCommandRegister(command, plugin));
            }
        }
    }

    public final void addCommand(SinisterCommand command) {
        CommandMap map = null;
        Field field;

        try {
            field = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            field.setAccessible(true);
            map = (CommandMap)field.get(Bukkit.getServer());
        }
        catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        if (map.getCommand(command.getName()) == null) {
            this.commands.add(command);
            map.register(plugin.getDescription().getName(), new BukkitCommandRegister(command, plugin));
        }
    }
}
