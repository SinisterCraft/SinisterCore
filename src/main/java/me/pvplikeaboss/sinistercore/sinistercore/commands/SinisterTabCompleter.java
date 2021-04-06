package me.pvplikeaboss.sinistercore.sinistercore.commands;

import me.pvplikeaboss.sinistercore.sinistercore.SinisterCore;
import me.pvplikeaboss.sinistercore.sinistercore.commands.util.tab.TabContext;
import me.pvplikeaboss.sinistercore.sinistercore.commands.util.tab.TabExecutor;
import me.pvplikeaboss.sinistercore.sinistercore.objects.PlayerObject;

import java.util.ArrayList;

public class SinisterTabCompleter  implements TabExecutor {
    private SinisterCore plugin;

    public SinisterTabCompleter(SinisterCore p) {
        plugin = p;
        return;
    }

    @Override
    public void complete(TabContext context) {
        ArrayList<String> usernames = new ArrayList();
        int x = 0;
        for(PlayerObject playerObject : plugin.getPlayers()) {
            if(playerObject.isPlayerOnline) {
                usernames.add(playerObject.playerName);
                x++;
            } else {
                continue;
            }
        }
        context.completion(usernames);
        return;
    }
}
