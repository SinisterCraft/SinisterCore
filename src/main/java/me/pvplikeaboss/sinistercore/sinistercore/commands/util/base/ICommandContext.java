package me.pvplikeaboss.sinistercore.sinistercore.commands.util.base;

import me.pvplikeaboss.sinistercore.sinistercore.SinisterCore;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public interface ICommandContext {
    SinisterCore getPlugin();

    boolean isPlayer();
    boolean isConsole();
    Player getPlayer();
    ConsoleCommandSender getConsole();
    CommandSender getSender();
    boolean hasArgs();
    List<String> getArgs();
    String argAt(int index);
    String joinArgs(int start, int finish);
    String joinArgs(int start);
    String joinArgs();
    void pluginMessage(String message);
    void send(String message);
}
