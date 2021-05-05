package me.pvplikeaboss.sinistercore.sinistercore.modules.chat;

import me.pvplikeaboss.sinistercore.sinistercore.modules.API.FactionsAPI.FactionsAPI;
import me.pvplikeaboss.sinistercore.sinistercore.modules.API.permissionsex.PexAPI;
import me.pvplikeaboss.sinistercore.sinistercore.SinisterCore;
import me.pvplikeaboss.sinistercore.sinistercore.objects.PlayerObject;
import me.pvplikeaboss.sinistercore.sinistercore.modules.API.vault.VaultAPI;
import org.bukkit.ChatColor;

import java.util.Arrays;
import java.util.List;

public class ChatHandler {
    private SinisterCore plugin;
    private VaultAPI vaultAPI;
    private PexAPI pexAPI;

    public ChatHandler(SinisterCore p) {
        this.plugin = p;
    }

    public String getDisplayMessage(PlayerObject player, PlayerObject recipient, String message) {
        StringBuilder newMessage = new StringBuilder();

        if(plugin.getConfig().getBoolean("features.chat.enabled") == false) {
            return null;//sinistercore chat not enabled
        }

        if(plugin.getConfig().getBoolean("features.chat.factions")) {
            String factionName = FactionsAPI.getPlayerFactionTagRelation(this.plugin, player, recipient);
            newMessage.append("&7(" + factionName + "&7) ");
        }

        List<String> playerGroups = Arrays.asList(PexAPI.getPlayerGroups(player));
        for(String group : playerGroups) {
            newMessage.append(PexAPI.getGroupPrefix(group));
            newMessage.append(" &7");
        }

        newMessage.append(player.playerDisplayName);
        newMessage.append(" &3: &7");
        newMessage.append(message);
        return ChatColor.translateAlternateColorCodes('&', newMessage.toString());
    }
}
