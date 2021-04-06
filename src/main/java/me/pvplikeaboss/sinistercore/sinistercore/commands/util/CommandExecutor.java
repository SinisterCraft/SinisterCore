package me.pvplikeaboss.sinistercore.sinistercore.commands.util;

@FunctionalInterface
public interface CommandExecutor {
    boolean execute(String name, CommandContext commandMethod);
}
