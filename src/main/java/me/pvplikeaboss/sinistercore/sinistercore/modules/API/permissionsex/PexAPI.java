package me.pvplikeaboss.sinistercore.sinistercore.modules.API.permissionsex;

import me.pvplikeaboss.sinistercore.sinistercore.SinisterCore;
import me.pvplikeaboss.sinistercore.sinistercore.objects.PlayerObject;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

import java.util.List;

public class PexAPI {
    private static SinisterCore plugin;

    public static void load(SinisterCore p) {
        plugin = p;
    }

    public static String getPlayerPrefix(PlayerObject playerObject) {
        PermissionUser user;

        if(playerObject.isPlayerOnline) {
            user = PermissionsEx.getUser(playerObject.getPlayer());
        } else {
            user = PermissionsEx.getUser(playerObject.playerName);
        }

        if(user == null) {
            return null;
        }

        return user.getPrefix();
    }

    public static List<String> getPlayerPermissions(PlayerObject playerObject) {
        PermissionUser user;

        if(playerObject.isPlayerOnline) {
            user = PermissionsEx.getUser(playerObject.getPlayer());
        } else {
            user = PermissionsEx.getUser(playerObject.playerName);
        }

        if(user == null) {
            return null;
        }

        return user.getPermissions("world");
    }

    public static String[] getPlayerGroups(PlayerObject playerObject) {
        PermissionUser user;

        if(playerObject.isPlayerOnline) {
            user = PermissionsEx.getUser(playerObject.getPlayer());
        } else {
            user = PermissionsEx.getUser(playerObject.playerName);
        }

        if(user == null) {
            return null;
        }

        return user.getGroupNames();
    }
}
