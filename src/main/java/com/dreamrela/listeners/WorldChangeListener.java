package com.dreamrela.listeners;

import com.dreamrela.DreamFlight;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;

public class WorldChangeListener implements Listener {
    
    private final DreamFlight plugin;
    
    public WorldChangeListener(DreamFlight plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler
    public void onWorldChange(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();
        String newWorldName = player.getWorld().getName();
        
        // Check if the new world allows flight
        if (!plugin.getConfigManager().isWorldAllowed(newWorldName)) {
            // If player has flight enabled, disable it
            if (player.getAllowFlight()) {
                player.setAllowFlight(false);
                player.setFlying(false);
                
                // Send message if enabled in config
                if (plugin.getConfigManager().shouldSendMessages()) {
                    player.sendMessage(ChatColor.YELLOW + "Flight has been " + 
                                     ChatColor.RED + "disabled" + 
                                     ChatColor.YELLOW + " in this world!");
                }
            }
        }
    }
}