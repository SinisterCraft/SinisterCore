package me.pvplikeaboss.sinistercore.sinistercore.events;

import me.pvplikeaboss.sinistercore.sinistercore.Instances;
import me.pvplikeaboss.sinistercore.sinistercore.SinisterCore;
import me.pvplikeaboss.sinistercore.sinistercore.objects.PlayerObject;
import me.pvplikeaboss.sinistercore.sinistercore.modules.chat.ChatHandler;
import me.pvplikeaboss.sinistercore.sinistercore.utilites.misc.Messages;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatEvents implements Listener {
    private final SinisterCore plugin;
    private ChatHandler chatHandler;
    private Messages utilMsgs;

    public ChatEvents(SinisterCore p) {
        this.plugin = p;
        this.chatHandler = (ChatHandler) Instances.getInstance(Instances.InstanceType.Chat, -1);
        this.utilMsgs = (Messages) Instances.getInstance(Instances.InstanceType.Utilities, 2);
    }

    @EventHandler
    public void onSendChat(AsyncPlayerChatEvent e) {
        PlayerObject player = plugin.getPlayer(e.getPlayer().getUniqueId());

        if(plugin.getConfig().getBoolean("features.punishment.enabled")) {
            if (player.isMuted()) {
                e.setCancelled(true);
                utilMsgs.errorMessage(player, "&7You are &6muted&7!");
                return;
            }
        }

        if(plugin.getConfig().getBoolean("features.chat.enabled")) {
            e.setCancelled(true);

            String origMessage = e.getMessage();
            for (Player p : plugin.getServer().getOnlinePlayers()) {
                p.sendMessage(chatHandler.getDisplayMessage(player, plugin.getPlayer(p.getUniqueId()), origMessage));
            }
            utilMsgs.logInfoMessage(chatHandler.getDisplayMessage(player, player, origMessage));
        }
    }
}
