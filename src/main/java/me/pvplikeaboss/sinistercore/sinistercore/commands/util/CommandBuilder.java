package me.pvplikeaboss.sinistercore.sinistercore.commands.util;

import me.pvplikeaboss.sinistercore.sinistercore.SinisterCore;
import me.pvplikeaboss.sinistercore.sinistercore.commands.SinisterCommand;
import me.pvplikeaboss.sinistercore.sinistercore.commands.util.tab.TabExecutor;
import org.bukkit.command.CommandSender;

import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class CommandBuilder {
    private SinisterCommand command;

    public CommandBuilder(SinisterCore plugin, String name) {
        command = new SinisterCommand(plugin, name);
    }

    public CommandBuilder executor(CommandExecutor executor) {
        command.setCommandExecutor(executor);
        return this;
    }

    public CommandBuilder completer(TabExecutor executor) {
        command.setTabExecutor(executor);
        return this;
    }

    public CommandBuilder aliases(String... aliases) {
        command.setAliases(aliases);
        return this;
    }

    public CommandBuilder aliases(Set<String> aliases) {
        command.setAliases(aliases);
        return this;
    }

    public CommandBuilder description(String description) {
        command.setDescription(description);
        return this;
    }

    public CommandBuilder usage(String usage) {
        command.setUsage(usage);
        return this;
    }

    public CommandBuilder permission(String permission) {
        command.setPermission(permission);
        return this;
    }

    public CommandBuilder requireOp() {
        command.setRequiresOperator(true);
        return this;
    }

    public CommandBuilder requiresOp(boolean requires) {
        command.setRequiresOperator(requires);
        return this;
    }

    public CommandBuilder permissionCheck(Predicate<CommandSender> predicate) {
        command.setPermissionCheck(predicate);
        return this;
    }

    public CommandBuilder minArgs(int minArgs) {
        command.setMinArgs(minArgs);
        return this;
    }

    public CommandBuilder maxArgs(int maxArgs) {
        command.setMaxArgs(maxArgs);
        return this;
    }

    public CommandBuilder playerOnly() {
        command.setPlayerOnly(true);
        return this;
    }

    public CommandBuilder children(SinisterCommand... subCommands) {
        command.setChildren(Stream.of(subCommands).collect(Collectors.toSet()));
        return this;
    }

    public CommandBuilder children(Set<SinisterCommand> subCommands) {
        command.setChildren(subCommands);
        return this;
    }

    public SinisterCommand build() {
        return command;
    }
}
