package me.pvplikeaboss.sinistercore.sinistercore.commands.util.tab;

import me.pvplikeaboss.sinistercore.sinistercore.SinisterCore;
import me.pvplikeaboss.sinistercore.sinistercore.commands.SinisterCommand;
import me.pvplikeaboss.sinistercore.sinistercore.commands.util.base.AbstractTabContext;
import me.pvplikeaboss.sinistercore.sinistercore.commands.util.base.ICommandContext;

public class TabContext extends AbstractTabContext {

    /**
     * A new tab completer context.
     *
     * @param plugin         The plugin this completer belongs to.
     * @param command        The command this completer is linked to.
     * @param commandContext The command context the command is linked to.
     */
    public TabContext(SinisterCore plugin, SinisterCommand command, ICommandContext commandContext) {
        super(plugin, command, commandContext);
    }
}