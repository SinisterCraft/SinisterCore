package me.pvplikeaboss.sinistercore.sinistercore.commands.util;

import me.pvplikeaboss.sinistercore.sinistercore.SinisterCore;
import me.pvplikeaboss.sinistercore.sinistercore.commands.SinisterCommand;
import me.pvplikeaboss.sinistercore.sinistercore.commands.util.CommandContext;
import me.pvplikeaboss.sinistercore.sinistercore.commands.util.tab.TabContext;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginIdentifiableCommand;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

public final class BukkitCommandRegister extends Command implements PluginIdentifiableCommand {
    private final SinisterCore plugin;
    private final SinisterCommand command;

    public BukkitCommandRegister(SinisterCommand cmd, SinisterCore p) {
        super(cmd.getName());
        this.plugin = p;
        this.command = cmd;
        this.description = command.getDescription();
        this.usageMessage = command.getUsage();
        List<String> aliases = new ArrayList<>();
        command.getAliases().forEach(alias -> aliases.add(alias));
        this.setAliases(aliases);
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (command.matchesCommand(commandLabel)) {
            command.execute(new CommandContext(sender, args, plugin));
            return true;
        }
        return false;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        return command.completer(new TabContext(plugin, command, new CommandContext(sender, args, plugin)));
    }

    @Override
    public Plugin getPlugin() {
        return plugin;
    }
}