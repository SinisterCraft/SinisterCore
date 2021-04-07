package me.pvplikeaboss.sinistercore.sinistercore.objects;

import me.pvplikeaboss.sinistercore.sinistercore.Instances;
import me.pvplikeaboss.sinistercore.sinistercore.SinisterCore;
import me.pvplikeaboss.sinistercore.sinistercore.modules.punishment.Punishment;
import me.pvplikeaboss.sinistercore.sinistercore.utilites.misc.Messages;
import me.pvplikeaboss.sinistercore.sinistercore.utilites.misc.Misc;
import me.pvplikeaboss.sinistercore.sinistercore.utilites.serverutils.PlayerUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.craftbukkit.v1_8_R3.CraftOfflinePlayer;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

public class PlayerObject implements Comparable<PlayerObject> {
    private final PlayerObject thisObject;// simple workaround
    private SinisterCore plugin;
    private Punishment punish;
    private final PlayerUtils utilPlayer;
    private Misc utilMisc;
    private Messages utilMsgs;

    public UUID playerUUID = null;
    public String playerName = null;
    public String playerDisplayName = null;
    public boolean isPlayerOnline = false;
    public boolean recieveMsgs = true;
    public boolean isGodMode = false;
    public boolean isVanish = false;
    public boolean isInCombat = false;
    public Date endOfCombat = null;
    public Location lastPlayerDeathLocation = null;
    public Location lastPlayerLogoutLocation = null;

    enum Permission {
        PERMISSION_FOUND,
        PERMISSION_NOT_FOUND,
        PERMISSION_WILDCARD,
    };

    public PlayerObject(SinisterCore p, UUID pUUID) {
        this.plugin = p;
        this.utilPlayer = (PlayerUtils) Instances.getInstance(Instances.InstanceType.Utilities, 3);
        this.utilMsgs = (Messages) Instances.getInstance(Instances.InstanceType.Utilities, 2);
        this.punish = (Punishment) Instances.getInstance(Instances.InstanceType.Punishment, -1);
        this.playerUUID = pUUID;
        if(this.isPlayerOnline = this.utilPlayer.playerOnline(pUUID)) {
            this.playerName = p.getServer().getPlayer(pUUID).getName();
            this.playerDisplayName = p.getServer().getPlayer(pUUID).getDisplayName();
        } else {
            this.playerName = p.getServer().getOfflinePlayer(pUUID).getName();
            this.playerDisplayName = this.playerName;//cant get displayname cus offline
        }
        this.thisObject = this;
    }

    /*

    Get/Set Strings

     */

    public String getPlayerName() { return thisObject.playerName; }
    public void setPlayerName(String name) {
        if(name != null) {
            thisObject.playerName = name;
        }
        return;
    }

    public String getPlayerDisplayName() {
        boolean hasWildcard = false;
        boolean hasBase = false;
        Permission permRc = null;

        //null checks for wildcard and base permission
        if((permRc = thisObject.getPlayerDisplayNamePermission(null)) == Permission.PERMISSION_WILDCARD) {
            hasWildcard = true;
            hasBase = true;
        } else if(permRc == Permission.PERMISSION_FOUND) {
            hasWildcard = false;
            hasBase = true;
        }

        if (hasWildcard || thisObject.getPlayerDisplayNamePermission("color") == Permission.PERMISSION_FOUND) {
                //let player use color
                //todo: make more permissions for italicized and other format specifiers
                return ChatColor.translateAlternateColorCodes('&', thisObject.playerDisplayName);
        } else if (hasBase) {
                // default to basic display name string
                return thisObject.playerDisplayName;
        }

        return null;
    }

    public void setPlayerDisplayName(String name) {
        if(name != null) {
            if(name.equalsIgnoreCase("none") || name.equalsIgnoreCase("off")) {
                thisObject.playerDisplayName = thisObject.playerName;
            } else {
                thisObject.playerDisplayName = name;
            }
            thisObject.getPlayer().setDisplayName(getPlayerDisplayName());
        }
        return;
    }

    private Permission getPlayerDisplayNamePermission(String sub) {
        if (thisObject.getPlayer().hasPermission("sinistercore.*")) {
            return Permission.PERMISSION_WILDCARD;
        } else if (sub != null && thisObject.getPlayer().hasPermission("sinistercore.nickname." + sub)) {
            //we have found subpermission
            return Permission.PERMISSION_FOUND;
        } else if (thisObject.getPlayer().hasPermission("sinistercore.nickname")) {
            // we have found base permission
            return Permission.PERMISSION_FOUND;
        }
        // otherwise permission is not found
        return Permission.PERMISSION_NOT_FOUND;
    }

    /*

    Get/Set local objects

     */

    public Location getLastPlayerDeathLocation() { return thisObject.lastPlayerDeathLocation; }
    public void setLastPlayerDeathLocation(Location loc) {
        thisObject.lastPlayerDeathLocation = loc;
        return;
    }

    public Location getLastPlayerLogoutLocation() { return thisObject.lastPlayerLogoutLocation; }
    public void setLastPlayerLogoutLocation(Location loc) {
        thisObject.lastPlayerLogoutLocation = loc;
        return;
    }

    /*

    Get/Set all booleans

     */

    public boolean getIsPlayerOnline() { return thisObject.isPlayerOnline; }
    public void setIsPlayerOnline(boolean state) {
        //todo:// debug print if old state is the same as new state
        //     // Also might add some type of reaction
        thisObject.isPlayerOnline = state;
        return;
    }

    public boolean getIsGodMode() { return thisObject.isGodMode; }
    public void setIsGodMode(boolean state) {
        thisObject.isGodMode = state;
        return;
    }


    public boolean getRecieveMsgs() { return thisObject.recieveMsgs; }
    public void setRecieveMsgs(boolean state) {
        thisObject.recieveMsgs = state;
        return;
    }

    public boolean getIsVanish() { return thisObject.isVanish; }
    public void setIsVanish(boolean state) {
        if(thisObject.isPlayerOnline) {
            if (thisObject.isVanish && state == false) {
                for (PlayerObject player : plugin.getPlayers()) {
                    if (player.isPlayerOnline) {
                        player.getPlayer().showPlayer(this.getPlayer());
                    }
                }
            } else if ((!thisObject.isVanish) && (state == true)) {
                for (PlayerObject player : plugin.getPlayers()) {
                    if (player.isPlayerOnline) {
                        player.getPlayer().hidePlayer(this.getPlayer());
                    }
                }
            }
        }
        thisObject.isVanish = state;
        return;
    }

    public boolean getIsInCombat() {
        return thisObject.isInCombat;
    }
    public Date getEndOfCombat() {
        return endOfCombat;
    }
    public void setIsInCombat(boolean state, Date end) {
        thisObject.isInCombat = state;
        if(end != null) {
            endOfCombat = end;
        }
        return;
    }

    /*

    Get Bukkit Player Instances

     */

    public CraftPlayer getCraftPlayer() {
        if(!this.isPlayerOnline) {
            return null;
        }
        return (CraftPlayer)getPlayer();
    }

    public CraftOfflinePlayer getCraftOfflinePlayer() {
        return (CraftOfflinePlayer)getOfflinePlayer();
    }

    public Player getPlayer() {
        if(!this.isPlayerOnline) {
            return null;
        }
        return plugin.getServer().getPlayer(this.playerUUID);
    }

    public OfflinePlayer getOfflinePlayer() {
        return plugin.getServer().getOfflinePlayer(playerUUID);
    }

    // not used

    public void sendMessage(String msg) {
        if (this.isPlayerOnline) {
            if (getPlayer() == null) {
                setIsPlayerOnline(false);
                return;
            }
            getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
        }
    }

    /*

    Punishment related functions

     */

    public void kickPlayer(String reason) {
        if(this.isPlayerOnline) {
            if(getPlayer() == null) {
                setIsPlayerOnline(false);
                return;
            }
            if(reason == null) {
                reason = "You've been kicked";
            }
            this.getPlayer().kickPlayer(ChatColor.translateAlternateColorCodes('&', reason));
            this.isPlayerOnline = false;
        }
    }

    public boolean isBanned() {
        return punish.isBanned(this);
    }

    public boolean isMuted() {
        return punish.isMuted(this);
    }

    public void setBanned(PlayerObject whoBanned, String reason, Date time, boolean isBan) {
        punish.setBanPlayer(this, whoBanned, reason, time, isBan);
        if(isBan) {
            this.kickPlayer(reason);
        }
    }

    public void setMute(PlayerObject whoMuted, String reason, Date time, boolean isMute) {
        punish.setMutePlayer(this, whoMuted, reason, time, isMute);
    }

    /*

    Teleport player

     */

    public boolean teleportPlayer(Location to, boolean isDelay) {
        if (this.isPlayerOnline) {
            if(getPlayer() == null) {
                setIsPlayerOnline(false);
                return false;
            }
            int delay = plugin.getConfig().getInt("features.teleport.delay");
            if ((isDelay) && (!getPlayer().hasPermission("sinistercore.teleport.nodelay"))) {
                utilMsgs.infoMessage(this, "&cDon't Move!&b Teleporting in &6" + delay + "&b seconds!");
                HashMap<Integer, PlayerObject> teleportHashMap = (HashMap<Integer, PlayerObject>) Instances.getInstance(Instances.InstanceType.HashMap, 0);
                teleportHashMap.put(Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                    @Override
                    public void run() {
                        utilMsgs.infoMessage(thisObject, "&bTeleporting!");
                        getPlayer().teleport(to);
                        for (Integer i : teleportHashMap.keySet()) {
                            PlayerObject player = teleportHashMap.get(i);
                            if (player.playerUUID == playerUUID) {
                                teleportHashMap.remove(i);
                            }
                        }
                    }
                }, (delay) * 20L), thisObject);
            } else {
                utilMsgs.infoMessage(thisObject, "&bTeleporting!");
                this.getPlayer().teleport(to);
            }
            return true;
        } else {
            return false;
        }
    }

    public boolean compareTo_safe(PlayerObject playerObject) {
        if (compareTo(playerObject) == 1) { return true; }
        return false;
    }

    public int compareTo(PlayerObject o) {
        if (this.playerName.equals(o.playerName)) {
            return (int)(1 & 0xFF);// bitmask for converting to a 8bit integer
        }
        return (int)(0 & 0xFF);// bitmask for converting to a 8bit integer
    }

}
