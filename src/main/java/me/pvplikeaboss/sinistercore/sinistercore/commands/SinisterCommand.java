package me.pvplikeaboss.sinistercore.sinistercore.commands;

import com.google.common.collect.Lists;
import me.pvplikeaboss.sinistercore.sinistercore.SinisterCore;
import me.pvplikeaboss.sinistercore.sinistercore.commands.util.CommandContext;
import me.pvplikeaboss.sinistercore.sinistercore.commands.util.CommandExecutor;
import me.pvplikeaboss.sinistercore.sinistercore.commands.util.base.ICommandContext;
import me.pvplikeaboss.sinistercore.sinistercore.commands.util.base.ITabContext;
import me.pvplikeaboss.sinistercore.sinistercore.commands.util.tab.TabContext;
import me.pvplikeaboss.sinistercore.sinistercore.commands.util.tab.TabExecutor;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SinisterCommand {
    private String name;
    private Set<SinisterCommand> children;
    private TabExecutor tabExecutor;
    private final SinisterCore plugin;
    private Set<String> aliases;
    private CommandExecutor commandExecutor;
    private boolean requiresOperator = false;
    private boolean playerOnly = false;
    private String description = "";
    private String usage = "";
    private String permission;
    private int minArgs = -1;
    private int maxArgs = -1;
    private Predicate<CommandSender> permissionCheck;

    public SinisterCommand(SinisterCore plugin, String name) {
        this.plugin = plugin;
        this.name = name;
        //Defaults
        aliases = new HashSet<>();
        children = new HashSet<>();
    }

    public void execute(ICommandContext context) {

        //If there were no child commands that matched the context
        if (!checkChildren(context)) {

            //Check if console is trying to use a player only command
            if (context.isConsole() && playerOnly) {
                context.pluginMessage(ChatColor.RED + "This command can only be used by players!");
                return;
            }

            //Check if the sender has perms
            if (permission != null && !context.getSender().hasPermission(permission)) {
                context.pluginMessage(ChatColor.RED + "You do not have permission for this command! Required Permission: " + ChatColor.GRAY + permission);
                return;
            }

            if (requiresOperator && !context.getSender().isOp()) {
                context.pluginMessage(ChatColor.RED + "You do not have permission for this command! Required Permission: " + ChatColor.GRAY + "OPERATOR");
                return;
            }

            if (permissionCheck != null && !permissionCheck.test(context.getSender())) {
                context.pluginMessage(ChatColor.RED + "You do not have permission for this command!");
                return;
            }

            //Check for too many args
            if (context.getArgs().size() > maxArgs && maxArgs > -1) {
                context.pluginMessage(ChatColor.RED + "Too many arguments!" + " Input: " + ChatColor.GRAY + context.getArgs().size() + ChatColor.RED + " Max: " + ChatColor.GRAY + maxArgs);
                if (usage != null) {
                    context.pluginMessage(ChatColor.GRAY + "Correct Usage: " + usage);
                }
                return;
            }

            //Check for not enough args
            if (context.getArgs().size() < minArgs && minArgs > -1) {
                context.pluginMessage(ChatColor.RED + "Not enough arguments!" + " Input: " + ChatColor.GRAY + context.getArgs().size() + ChatColor.RED + " Min: " + ChatColor.GRAY + minArgs);
                if (usage != null) {
                    context.pluginMessage(ChatColor.GRAY + "Correct Usage: " + usage);
                }
                return;
            }

            //Everything seems okay, so lets execute the command
            if(!commandExecutor.execute(this.name, (CommandContext) context)) {// no perms // todo; convert from boolean to uint8 (num & 0xFF)
                context.pluginMessage(ChatColor.RED + "You do not have permission for this command!");
                return;
            }

        }
    }

    public List<String> completer(ITabContext tabContext) {
        List<String> sub = Lists.newArrayList();
        if (tabExecutor == null) { //If no executor exists for this command it defaults into looking for the command children
            if (tabContext.getCommandChildren().isEmpty()) { //If no children exist then there will be no tab complete.
                return null;
            }
            if (tabContext.getLength() == 0) {
                List<String> children = new ArrayList<>();
                tabContext.getCommandChildren().forEach(child -> children.add(child.getName()));
                return children; //List of children commands.
            }
            return null; //Dont want the child commands going past argument 1
        }
        tabExecutor.complete((TabContext) tabContext);
        for (String completion : tabContext.currentPossibleCompletion()) {
            if (completion.toLowerCase().startsWith(tabContext.getContext().argAt(tabContext.getContext().getArgs().size() - 1))) {
                sub.add(completion);
            }
        }
        return sub; //Custom completer.
    }

    private boolean checkChildren(ICommandContext context) {
        if (children.isEmpty()) {
            return false;
        }
        if (!context.hasArgs()) {
            return false;
        }
        for (SinisterCommand childCommand : children) {
            //This is safe because we know that there is at least one argument
            if (childCommand.matchesCommand(context.argAt(0))) {

                context.getArgs().remove(0);
                childCommand.execute(context);
                return true;
            }
        }
        return false;
    }

    public CommandExecutor getCommandExecutor() {
        return commandExecutor;
    }

    public void setCommandExecutor(CommandExecutor commandExecutor) {
        this.commandExecutor = commandExecutor;
    }

    public boolean matchesCommand(String input) {
        input = input.toLowerCase();
        return (input.equals(name) || aliases.contains(input));
    }

    public SinisterCore getPlugin() {
        return plugin;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<String> getAliases() {
        return aliases;
    }

    public void setAliases(Set<String> aliases) {
        this.aliases = aliases;
    }

    public void setAliases(String... aliases) {
        this.aliases = Stream.of(aliases).map(String::toLowerCase).collect(Collectors.toSet());
    }

    public boolean hasAliases() {
        return aliases.size() > 0;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUsage() {
        return usage;
    }

    public void setUsage(String usage) {
        this.usage = usage;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public void setRequiresOperator(boolean requiresOperator) {
        this.requiresOperator = requiresOperator;
    }

    public void setPermissionCheck(Predicate<CommandSender> permissionCheck) {
        this.permissionCheck = permissionCheck;
    }

    public int getMinArgs() {
        return minArgs;
    }

    public void setMinArgs(int minArgs) {
        this.minArgs = minArgs;
    }

    public int getMaxArgs() {
        return maxArgs;
    }

    public void setMaxArgs(int maxArgs) {
        this.maxArgs = maxArgs;
    }

    public boolean isPlayerOnly() {
        return playerOnly;
    }

    public void setPlayerOnly(boolean playerOnly) {
        this.playerOnly = playerOnly;
    }

    public Set<SinisterCommand> getChildren() {
        return children;
    }

    public void setChildren(Set<SinisterCommand> children) {
        this.children = children;
    }

    public TabExecutor getTabExecutor() {
        return tabExecutor;
    }

    public void setTabExecutor(TabExecutor tabExecutor) {
        this.tabExecutor = tabExecutor;
    }
}
