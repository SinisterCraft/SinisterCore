package me.pvplikeaboss.sinistercore.sinistercore.utilites.misc;

import me.pvplikeaboss.sinistercore.sinistercore.SinisterCore;
import me.pvplikeaboss.sinistercore.sinistercore.objects.PlayerObject;
import org.bukkit.ChatColor;

import java.util.logging.Level;

public class Messages {
    private final SinisterCore plugin;

    public Messages(SinisterCore p) {
        this.plugin = p;
    }

    public void logErrorMessage(String error) {
        plugin.getLogger().log(Level.WARNING, error);
    }

    public void errorMessage(PlayerObject p, String error) {
        p.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', "&9Error> &f"+error));
    }

    public void logInfoMessage(String info) {
        plugin.getLogger().log(Level.INFO, info);
    }

    public void infoMessage(PlayerObject p, String info) {
        p.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', "&9Information> &f"+info));
    }
}
