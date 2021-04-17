package me.pvplikeaboss.sinistercore.sinistercore.modules.punishment;

import me.pvplikeaboss.sinistercore.sinistercore.Instances;
import me.pvplikeaboss.sinistercore.sinistercore.SinisterCore;
import me.pvplikeaboss.sinistercore.sinistercore.modules.data.files.configs.yml.PunishmentConfig;
import me.pvplikeaboss.sinistercore.sinistercore.objects.PlayerObject;
import me.pvplikeaboss.sinistercore.sinistercore.utilites.misc.Time;
import org.bukkit.ChatColor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Punishment {
    private SinisterCore plugin;
    private PunishmentData punishData;

    public Punishment(SinisterCore p) {
        this.plugin = p;
        this.punishData = new PunishmentData(this.plugin);
    }

    public String getBanMessage(PlayerObject player) {
        return ChatColor.translateAlternateColorCodes('&', punishData.getBanMessage(player));
    }

    public boolean isBanned(PlayerObject player) {
        return punishData.isBanned(player);
    }

    public boolean isMuted(PlayerObject player) {
        return punishData.isMuted(player);
    }

    public void setBanPlayer(PlayerObject player, PlayerObject whoBanned, String reason, Date endDate, boolean isBan) {// ban player + ip
        punishData.setBan(player, whoBanned, reason, endDate, isBan);
        return;
    }

    public void setMutePlayer(PlayerObject player, PlayerObject whoMuted, String reason, Date endDate, boolean isMute) {// mute player + ip
        punishData.setMute(player, whoMuted, reason, endDate, isMute);
    }

    public void setWarnPlayer(PlayerObject player, String reason, String time, boolean isWarn) {// warn player + ip

    }
}
