package me.pvplikeaboss.sinistercore.sinistercore.commands.util;

import me.pvplikeaboss.sinistercore.sinistercore.SinisterCore;
import me.pvplikeaboss.sinistercore.sinistercore.commands.util.base.AbstractCommandContext;
import org.bukkit.command.CommandSender;

public class CommandContext extends AbstractCommandContext {

    public CommandContext(CommandSender sender, String[] args, SinisterCore plugin) {
        super(sender, args, plugin);
    }
}
