package me.pvplikeaboss.sinistercore.sinistercore.modules.punishment;

import me.pvplikeaboss.sinistercore.sinistercore.Instances;
import me.pvplikeaboss.sinistercore.sinistercore.SinisterCore;
import me.pvplikeaboss.sinistercore.sinistercore.modules.files.configs.yml.PunishmentConfig;
import me.pvplikeaboss.sinistercore.sinistercore.objects.PlayerObject;
import me.pvplikeaboss.sinistercore.sinistercore.utilites.misc.Time;
import org.bukkit.ChatColor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Punishment {
    private SinisterCore plugin;
    private PunishmentConfig cfgPunish;

    public Punishment(SinisterCore p) {
        this.plugin = p;
        this.cfgPunish = (PunishmentConfig) Instances.getInstance(Instances.InstanceType.Config, 2);
    }

    public String getBanMessage(PlayerObject player) {
        StringBuilder ret = new StringBuilder();

        if(isBanned(player)) {
            String reason = null;
            String banDate = null;
            String timeLeft = null;
            if (cfgPunish.getConfig().isSet("bans." + player.playerUUID)) {
                for (String id : cfgPunish.getConfig().getConfigurationSection("bans." + player.playerUUID).getKeys(false)) {
                    boolean active = cfgPunish.getConfig().getBoolean("bans."+player.playerUUID+"."+id+".active");
                    if(active) {
                        reason = cfgPunish.getConfig().getString("bans."+player.playerUUID+"."+id+".reason");
                        banDate = cfgPunish.getConfig().getString("bans."+player.playerUUID+"."+id+".startdate");
                        if(cfgPunish.getConfig().isSet("bans."+player.playerUUID+"."+id+".enddate")) {
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd/HH/mm/ss");
                            try {
                                Date endDate = sdf.parse(cfgPunish.getConfig().getString("bans." + player.playerUUID + "." + id + ".enddate"));
                                timeLeft = Time.getTimeLeftStr(endDate);
                            } catch (ParseException e) {
                                e.printStackTrace();
                                timeLeft = "null";
                            }
                        }
                    }
                }
                ret.append("&8[&9Sinister&cCraft&8] &6Ban System &7(Developed by &4&lPvPLikeABoss&r&7)\n");
                ret.append("&6Reason: &4"+reason+"\n");
                ret.append("&6Date of ban: &4"+banDate+"\n");
                if(timeLeft != null) {
                    ret.append("&6Time left: &4" + timeLeft + "\n");
                }
                ret.append("&aYou can appeal on our website &chttps://www.sinistermc.xyz\n");
            } else {
                ret.append("&8[&9Sinister&cCraft&8] Ban System (Developed by PvPLikeABoss)\n");
                ret.append("Unknown Error: Player not found\n");
            }
        } else {
            ret.append("&8[&9Sinister&cCraft&8] Ban System (Developed by PvPLikeABoss)\n");
            ret.append("Unknown Error: Player not banned\n");
        }
        return ChatColor.translateAlternateColorCodes('&', ret.toString());
    }

    public boolean isBanned(PlayerObject player) {
        if (cfgPunish.getConfig().isSet("bans." + player.playerUUID)) {
            for(String id : cfgPunish.getConfig().getConfigurationSection("bans."+player.playerUUID).getKeys(false)) {
                boolean active = cfgPunish.getConfig().getBoolean("bans."+player.playerUUID+"."+id+".active");
                if(active) {
                    if(cfgPunish.getConfig().isSet("bans."+player.playerUUID+"."+id+".enddate")) {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd/HH/mm/ss");
                        String unbanDateStr = cfgPunish.getConfig().getString("bans." + player.playerUUID + "." + id + ".enddate");
                        Date unbanDate = null;
                        try {
                            unbanDate = sdf.parse(unbanDateStr);
                        } catch (ParseException e) {
                            e.printStackTrace();
                            return true;
                        }

                        if (Time.getTimeLeftStr(unbanDate) == null) {
                            cfgPunish.getConfig().set("bans." + player.playerUUID + "." + id + ".active", false);
                            return false;
                        } else {
                            return true;
                        }
                    } else {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public boolean isMuted(PlayerObject player) {
        if (cfgPunish.getConfig().isSet("mutes." + player.playerUUID)) {
            for(String id : cfgPunish.getConfig().getConfigurationSection("mutes."+player.playerUUID).getKeys(false)) {
                boolean active = cfgPunish.getConfig().getBoolean("mutes."+player.playerUUID+"."+id+".active");
                if(active) {
                    if(cfgPunish.getConfig().isSet("mutes."+player.playerUUID+"."+id+".enddate")) {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd/HH/mm/ss");
                        String unmuteDateStr = cfgPunish.getConfig().getString("mutes." + player.playerUUID + "." + id + ".enddate");
                        Date unmuteDate = null;
                        try {
                            unmuteDate = sdf.parse(unmuteDateStr);
                        } catch (ParseException e) {
                            e.printStackTrace();
                            return true;
                        }

                        if (Time.getTimeLeftStr(unmuteDate) == null) {
                            cfgPunish.getConfig().set("mutes."+player.playerUUID+"."+id+".active", false);
                            return false;
                        } else {
                            return true;
                        }
                    } else {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public void setBanPlayer(PlayerObject player, PlayerObject whoBanned, String reason, Date endDate, boolean isBan) {// ban player + ip
        if(!isBan) {// unban player
            if (cfgPunish.getConfig().isSet("bans." + player.playerUUID)) {
                for(String id : cfgPunish.getConfig().getConfigurationSection("bans."+player.playerUUID).getKeys(false)) {
                    boolean active = cfgPunish.getConfig().getBoolean("bans."+player.playerUUID+"."+id+".active");
                    if(active) {
                        cfgPunish.getConfig().set("bans."+player.playerUUID+"."+id+".active", false);
                        cfgPunish.saveConfig();
                    }
                }
            }
            return;
        } else {
            int last_id = -1;
            if (cfgPunish.getConfig().isSet("bans." + player.playerUUID)) {
                for (String id : cfgPunish.getConfig().getConfigurationSection("bans." + player.playerUUID).getKeys(false)) {
                    last_id = Integer.parseInt(id);
                }
            }
            int id = last_id+1;// if no bans start at 0 otherwise add 1

            cfgPunish.getConfig().set("bans."+player.playerUUID+"."+id+".active", true);

            if(whoBanned != null) {
                cfgPunish.getConfig().set("bans." + player.playerUUID + "." + id + ".whobanned", whoBanned.playerName);
            } else {
                cfgPunish.getConfig().set("bans." + player.playerUUID + "." + id + ".whobanned", "console");
            }

            if(reason != null) {
                cfgPunish.getConfig().set("bans."+player.playerUUID+"."+id+".reason", reason);
            } else {
                cfgPunish.getConfig().set("bans."+player.playerUUID+"."+id+".reason", "&4The Ban Hammer Has Spoken!");
            }

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd/HH/mm/ss");
            Date startDate = Time.getCurrentDateTime();
            cfgPunish.getConfig().set("bans."+player.playerUUID+"."+id+".startdate", sdf.format(startDate));

            if(endDate != null) {
                cfgPunish.getConfig().set("bans."+player.playerUUID+"."+id+".enddate", sdf.format(endDate));
            }

            cfgPunish.saveConfig();
            return;
        }
    }

    public void setMutePlayer(PlayerObject player, PlayerObject whoMuted, String reason, Date endDate, boolean isMute) {// mute player + ip
        if(!isMute) {
            if (cfgPunish.getConfig().isSet("mutes." + player.playerUUID)) {
                for(String id : cfgPunish.getConfig().getConfigurationSection("mutes."+player.playerUUID).getKeys(false)) {
                    boolean active = cfgPunish.getConfig().getBoolean("mutes."+player.playerUUID+"."+id+".active");
                    if(active) {
                        cfgPunish.getConfig().set("mutes."+player.playerUUID+"."+id+".active", false);
                        cfgPunish.saveConfig();
                    }
                }
            }
            return;
        } else {
            int last_id = -1;
            if (cfgPunish.getConfig().isSet("mutes." + player.playerUUID)) {
                for (String id : cfgPunish.getConfig().getConfigurationSection("mutes." + player.playerUUID).getKeys(false)) {
                    last_id = Integer.parseInt(id);
                }
            }
            int id = last_id+1;// if no bans start at 0 otherwise add 1
            cfgPunish.getConfig().set("mutes."+player.playerUUID+"."+id+".active", true);

            if(whoMuted != null) {
                cfgPunish.getConfig().set("mutes." + player.playerUUID + "." + id + ".whomuted", whoMuted.playerName);
            } else {
                cfgPunish.getConfig().set("mutes." + player.playerUUID + "." + id + ".whomuted", "console");
            }

            if(reason != null) {
                cfgPunish.getConfig().set("mutes."+player.playerUUID+"."+id+".reason", reason);
            } else {
                cfgPunish.getConfig().set("mutes."+player.playerUUID+"."+id+".reason", "&4The Mute Hammer Has Spoken!");
            }

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd/HH/mm/ss");
            Date startDate = Time.getCurrentDateTime();
            cfgPunish.getConfig().set("mutes."+player.playerUUID+"."+id+".startdate", sdf.format(startDate));

            if(endDate != null) {
                cfgPunish.getConfig().set("mutes."+player.playerUUID+"."+id+".enddate", sdf.format(endDate));
            }
            cfgPunish.saveConfig();
            return;
        }
    }

    public void setWarnPlayer(PlayerObject player, String reason, String time, boolean isWarn) {// warn player + ip

    }
}
