package me.pvplikeaboss.sinistercore.sinistercore.commands.util.base;

import me.pvplikeaboss.sinistercore.sinistercore.SinisterCore;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class AbstractCommandContext implements ICommandContext {

    private CommandSender sender;
    private List<String> args;
    private SinisterCore plugin;

    public AbstractCommandContext(CommandSender sender, String[] args, SinisterCore plugin) {
        this.sender = sender;
        this.plugin = plugin;
        List<String> list = new ArrayList<>();
        list.addAll(Arrays.asList(args));
        this.args = list;
    }

    @Override
    public SinisterCore getPlugin() {
        return plugin;
    }

    @Override
    public void pluginMessage(String message) {
        send(plugin.format(message));
    }

    @Override
    public boolean isPlayer() {
        return sender instanceof Player;
    }

    @Override
    public Player getPlayer() {
        return (Player)sender;
    }

    @Override
    public boolean isConsole() {
        return sender instanceof ConsoleCommandSender;
    }

    @Override
    public ConsoleCommandSender getConsole() {
        return (ConsoleCommandSender)sender;
    }

    @Override
    public CommandSender getSender() {
        return sender;
    }

    @Override
    public List<String> getArgs() {
        return args;
    }

    @Override
    public boolean hasArgs() {
        return !args.isEmpty();
    }

    @Override
    public String argAt(int index) {
        if (index < 0 || index >= args.size()) {
            return null;
        }
        return args.get(index);
    }

    @Override
    public String joinArgs(int start, int finish) {
        if (args.isEmpty()) {
            return "";
        }
        return String.join(" ", args.subList(start, finish));
    }

    @Override
    public String joinArgs(int start) {
        return joinArgs(start, args.size());
    }

    @Override
    public String joinArgs() {
        return joinArgs(0);
    }

    @Override
    public void send(String message) {
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    }

}
