package me.pvplikeaboss.sinistercore.sinistercore.modules.chat;

import me.pvplikeaboss.sinistercore.sinistercore.modules.API.FactionsAPI.FactionsAPI;
import me.pvplikeaboss.sinistercore.sinistercore.modules.API.permissionsex.PexAPI;
import me.pvplikeaboss.sinistercore.sinistercore.SinisterCore;
import me.pvplikeaboss.sinistercore.sinistercore.objects.PlayerObject;
import me.pvplikeaboss.sinistercore.sinistercore.modules.API.vault.VaultAPI;
import org.bukkit.ChatColor;

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
            newMessage.append("&8[" + factionName + "&8] ");
        }

        newMessage.append(PexAPI.getPlayerPrefix(player));
        newMessage.append(" ");

        newMessage.append(player.playerDisplayName);
        newMessage.append(" &8> &3");
        newMessage.append(message);

        return ChatColor.translateAlternateColorCodes('&', newMessage.toString());
    }
}
