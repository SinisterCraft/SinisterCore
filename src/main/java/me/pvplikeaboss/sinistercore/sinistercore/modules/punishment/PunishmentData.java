package me.pvplikeaboss.sinistercore.sinistercore.modules.punishment;

import me.pvplikeaboss.sinistercore.sinistercore.Instances;
import me.pvplikeaboss.sinistercore.sinistercore.SinisterCore;
import me.pvplikeaboss.sinistercore.sinistercore.modules.data.files.configs.yml.PunishmentConfig;
import me.pvplikeaboss.sinistercore.sinistercore.modules.data.mysql.PlayerDatabase;
import me.pvplikeaboss.sinistercore.sinistercore.modules.data.mysql.PunishmentDatabase;
import me.pvplikeaboss.sinistercore.sinistercore.objects.PlayerObject;
import me.pvplikeaboss.sinistercore.sinistercore.utilites.misc.Time;
import org.bukkit.Bukkit;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PunishmentData {
    private SinisterCore plugin;
    private static PunishmentConfig cfgPunish;

    public PunishmentData(SinisterCore p) {
        this.plugin = p;
        this.cfgPunish = (PunishmentConfig) Instances.getInstance(Instances.InstanceType.Config, 2);
    }

    public void setBan(PlayerObject player, PlayerObject whoBanned, String reason, Date endDate, boolean isBan) {
        if(!plugin.useMysql) {//use config
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
        } else {// use database
            if(!isBan) {// unban player
                while(true) {
                    int entry_id = PunishmentDatabase.getEntryID(player, "ban");
                    if(entry_id == -1) {
                        break;
                    }

                    PunishmentDatabase.setActive(entry_id, false);
                }
                return;
            } else {//ban player
                PunishmentDatabase.newEntry(player, "ban", whoBanned, reason, endDate);
                return;
            }
        }
    }

    public void setMute(PlayerObject player, PlayerObject whoMuted, String reason, Date endDate, boolean isMute) {
        if(!plugin.useMysql) {//use file
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
        } else {//use database
            if(!isMute) {// unmute player
                while(true) {
                    int entry_id = PunishmentDatabase.getEntryID(player, "mute");
                    if(entry_id == -1) {
                        break;
                    }

                    PunishmentDatabase.setActive(entry_id, false);
                }
                return;
            } else {//mute player
                PunishmentDatabase.newEntry(player, "mute", whoMuted, reason, endDate);
                return;
            }
        }
    }

    public boolean isBanned(PlayerObject player) {
        if(!plugin.useMysql) {// use config
            if (cfgPunish.getConfig().isSet("bans." + player.playerUUID)) {
                for (String id : cfgPunish.getConfig().getConfigurationSection("bans." + player.playerUUID).getKeys(false)) {
                    boolean active = cfgPunish.getConfig().getBoolean("bans." + player.playerUUID + "." + id + ".active");
                    if (active) {
                        if (cfgPunish.getConfig().isSet("bans." + player.playerUUID + "." + id + ".enddate")) {
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
        } else {// use database
            int entry_id = PunishmentDatabase.getEntryID(player, "ban");
            if(entry_id != -1) {
                String endDateStr = PunishmentDatabase.getEndDate(entry_id);

                if(endDateStr.equalsIgnoreCase("null")) {
                    return true;
                }

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd/HH/mm/ss");
                Date unbanDate = null;

                try {
                    unbanDate = sdf.parse(endDateStr);
                } catch (ParseException e) {
                    e.printStackTrace();
                    return true;
                }

                if (Time.getTimeLeftStr(unbanDate) == null) {
                    PunishmentDatabase.setActive(entry_id,false);// ban is over
                    return false;
                }
                return true;
            }
            return false;
        }
    }

    public boolean isMuted(PlayerObject player) {
        if(!plugin.useMysql) {// use config
            if (cfgPunish.getConfig().isSet("mutes." + player.playerUUID)) {
                for (String id : cfgPunish.getConfig().getConfigurationSection("mutes." + player.playerUUID).getKeys(false)) {
                    boolean active = cfgPunish.getConfig().getBoolean("mutes." + player.playerUUID + "." + id + ".active");
                    if (active) {
                        if (cfgPunish.getConfig().isSet("mutes." + player.playerUUID + "." + id + ".enddate")) {
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
                                cfgPunish.getConfig().set("mutes." + player.playerUUID + "." + id + ".active", false);
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
        } else {// use mysql
            int entry_id = PunishmentDatabase.getEntryID(player, "mute");
            if(entry_id != -1) {
                String endDateStr = PunishmentDatabase.getEndDate(entry_id);

                if(endDateStr.equalsIgnoreCase("null")) {
                    return true;
                }

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd/HH/mm/ss");
                Date unbanDate = null;

                try {
                    unbanDate = sdf.parse(endDateStr);
                } catch (ParseException e) {
                    e.printStackTrace();
                    return true;
                }

                if (Time.getTimeLeftStr(unbanDate) == null) {
                    PunishmentDatabase.setActive(entry_id,false);// mute is over
                    return false;
                }
                return true;
            }
            return false;
        }
    }

    public String getBanMessage(PlayerObject player) {
        StringBuilder ret = new StringBuilder();

        if (isBanned(player)) {
            String reason = null;
            String banDate = null;
            String timeLeft = null;

            if (!plugin.useMysql) {// use config
                if (cfgPunish.getConfig().isSet("bans." + player.playerUUID)) {
                    for (String id : cfgPunish.getConfig().getConfigurationSection("bans." + player.playerUUID).getKeys(false)) {
                        boolean active = cfgPunish.getConfig().getBoolean("bans." + player.playerUUID + "." + id + ".active");
                        if (active) {
                            reason = cfgPunish.getConfig().getString("bans." + player.playerUUID + "." + id + ".reason");
                            banDate = cfgPunish.getConfig().getString("bans." + player.playerUUID + "." + id + ".startdate");
                            if (cfgPunish.getConfig().isSet("bans." + player.playerUUID + "." + id + ".enddate")) {
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
                } else {
                    ret.append("&cPunish\n");
                    ret.append("Unknown Error: Player not found\n");
                    return ret.toString();
                }
            } else {// use database
                int entry_id = PunishmentDatabase.getEntryID(player, "ban");
                if(entry_id != -1) {
                    reason = PunishmentDatabase.getReason(entry_id);
                    banDate = PunishmentDatabase.getStartDate(entry_id);
                    if(!PunishmentDatabase.getEndDate(entry_id).equalsIgnoreCase("null")) {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd/HH/mm/ss");
                        try {
                            Date endDate = sdf.parse(PunishmentDatabase.getEndDate(entry_id));
                            timeLeft = Time.getTimeLeftStr(endDate);
                        } catch (ParseException e) {
                            e.printStackTrace();
                            timeLeft = "null";
                        }
                    }
                } else {
                    ret.append("&cPunish\n");
                    ret.append("Unknown Error: Failed to get entry in database");
                    return ret.toString();
                }
            }

            ret.append("&cYou have been banned!\n");
            ret.append("&7Reason&e: &6" + reason + "\n");
            ret.append("&7Date of ban&e: &6" + banDate + "\n");
            if (timeLeft != null) {
                ret.append("&7Time left&e: &7" + timeLeft + "\n");
            }
            ret.append("&4You can appeal on our website &chttps://www.sinistermc.xyz\n");
        } else {
            ret.append("&cPunish\n");
            ret.append("Unknown Error: Player not banned\n");
        }
        return ret.toString();
    }
}
