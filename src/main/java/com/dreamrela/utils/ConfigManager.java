package com.dreamrela.utils;

import com.dreamrela.DreamFlight;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;

public class ConfigManager {
    
    private final DreamFlight plugin;
    private FileConfiguration config;
    private List<String> allowedWorlds;
    private boolean sendMessages;
    private boolean disableOnWorldChange;
    
    public ConfigManager(DreamFlight plugin) {
        this.plugin = plugin;
    }
    
    public void loadConfig() {
        // Save default config if it doesn't exist
        plugin.saveDefaultConfig();
        
        // Reload config from disk
        plugin.reloadConfig();
        config = plugin.getConfig();
        
        // Load allowed worlds
        allowedWorlds = config.getStringList("allowed-worlds");
        
        // Load settings
        sendMessages = config.getBoolean("settings.send-messages", true);
        disableOnWorldChange = config.getBoolean("settings.disable-on-world-change", true);
        
        // Validate config
        validateConfig();
    }
    
    private void validateConfig() {
        if (allowedWorlds == null || allowedWorlds.isEmpty()) {
            plugin.getLogger().warning("No allowed worlds specified in config! Flight will be disabled in all worlds.");
        } else {
            plugin.getLogger().info("Flight allowed in worlds: " + String.join(", ", allowedWorlds));
        }
    }
    
    public boolean isWorldAllowed(String worldName) {
        return allowedWorlds != null && allowedWorlds.contains(worldName);
    }
    
    public List<String> getAllowedWorlds() {
        return allowedWorlds;
    }
    
    public FileConfiguration getConfig() {
        return config;
    }
    
    public boolean shouldSendMessages() {
        return sendMessages;
    }
    
    public boolean shouldDisableOnWorldChange() {
        return disableOnWorldChange;
    }
}