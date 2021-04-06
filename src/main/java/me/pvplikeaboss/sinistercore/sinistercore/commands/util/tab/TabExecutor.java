package me.pvplikeaboss.sinistercore.sinistercore.commands.util.tab;


public interface TabExecutor {

    /**
     * The tab completer method
     *
     * @param context The tab complete context
     */
    void complete(TabContext context);

}