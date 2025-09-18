package com.dreamrela;

import com.dreamrela.commands.FlyCommand;
import com.dreamrela.listeners.WorldChangeListener;
import com.dreamrela.utils.ConfigManager;
import org.bukkit.plugin.java.JavaPlugin;

public class DreamFlight extends JavaPlugin {
    
    private ConfigManager configManager;
    
    @Override
    public void onEnable() {
        // Initialize config manager
        configManager = new ConfigManager(this);
        configManager.loadConfig();
        
        // Register commands
        getCommand("fly").setExecutor(new FlyCommand(this));
        
        // Register event listeners
        getServer().getPluginManager().registerEvents(new WorldChangeListener(this), this);
        
        // Plugin startup message
        getLogger().info("DreamFlight has been enabled!");
        getLogger().info("Loaded " + configManager.getAllowedWorlds().size() + " allowed worlds for flight.");
    }
    
    @Override
    public void onDisable() {
        getLogger().info("DreamFlight has been disabled!");
    }
    
    public ConfigManager getConfigManager() {
        return configManager;
    }
    
    public void reloadPluginConfig() {
        configManager.loadConfig();
        getLogger().info("DreamFlight configuration reloaded!");
    }
}