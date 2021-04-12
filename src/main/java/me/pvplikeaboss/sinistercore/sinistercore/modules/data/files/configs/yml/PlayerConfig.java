package me.pvplikeaboss.sinistercore.sinistercore.modules.data.files.configs.yml;

import me.pvplikeaboss.sinistercore.sinistercore.SinisterCore;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PlayerConfig {
    private final SinisterCore plugin;
    private File configFile;
    private FileConfiguration config;
    private Logger utilMsgs;


    public PlayerConfig(SinisterCore p) {
        this.plugin = p;
        this.utilMsgs = Bukkit.getServer().getLogger();
        load();
    }

    public void unload() {
        //this.saveConfig();
        configFile = null;
        config = null;
    }

    public void load() {
        configFile = new File(plugin.getDataFolder(), "players.yml");
        if (!configFile.exists()) {
            configFile.getParentFile().mkdirs();
            plugin.saveResource("players.yml", false);
        }

        config = new YamlConfiguration();
        try {
            config.load(configFile);
        } catch (IOException | InvalidConfigurationException e) {
            utilMsgs.log(Level.SEVERE, "Failed to load players.yml");
        }
    }

    public FileConfiguration getConfig() {
        return this.config;
    }

    public void saveConfig() {
        try {
            this.config.save(configFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        configFile = new File(plugin.getDataFolder(), "players.yml");
        config = new YamlConfiguration();
        try {
            config.load(configFile);
        } catch (IOException | InvalidConfigurationException e) {
            utilMsgs.log(Level.SEVERE, "Failed to reload players.yml");
        }
    }
}
