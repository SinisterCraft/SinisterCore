package me.pvplikeaboss.sinistercore.sinistercore.commands.util.base;

import me.pvplikeaboss.sinistercore.sinistercore.SinisterCore;
import me.pvplikeaboss.sinistercore.sinistercore.commands.SinisterCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

public interface ITabContext {
    SinisterCommand getCommand();
    SinisterCore getPlugin();
    ICommandContext getContext();
    CommandSender getSender();
    Set<SinisterCommand> getCommandChildren();
    void playerCompletion(int index, Predicate<? super Player> predicate);
    void playerCompletion(int index);
    int getLength();
    boolean length(int length);
    String getPrevious();
    boolean previous(String previousArg);
    void completion(String... completions);
    void completion(ArrayList<String> completions);
    void completionAt(int index, String... completions);
    void completionAfter(String previousText, String... completions);
    List<String> currentPossibleCompletion();

}