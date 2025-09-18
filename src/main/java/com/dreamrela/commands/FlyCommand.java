package com.dreamrela.commands;

import com.dreamrela.DreamFlight;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FlyCommand implements CommandExecutor {
    
    private final DreamFlight plugin;
    
    public FlyCommand(DreamFlight plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Check if sender is a player
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Only players can use this command!");
            return true;
        }
        
        Player player = (Player) sender;
        
        // Handle different argument cases
        if (args.length == 0) {
            // Toggle own flight
            return toggleFlight(player, player);
        } else if (args.length == 1) {
            // Toggle flight for another player
            String targetName = args[0];
            Player target = Bukkit.getPlayer(targetName);
            
            if (target == null) {
                player.sendMessage(ChatColor.RED + "Player '" + targetName + "' not found!");
                return true;
            }
            
            return toggleFlight(player, target);
        } else {
            // Too many arguments
            player.sendMessage(ChatColor.RED + "Usage: /fly [player]");
            return true;
        }
    }
    
    private boolean toggleFlight(Player executor, Player target) {
        // Check permissions
        if (executor.equals(target)) {
            // Toggling own flight
            if (!executor.hasPermission("dreamflight.fly")) {
                executor.sendMessage(ChatColor.RED + "You don't have permission to toggle flight!");
                return true;
            }
        } else {
            // Toggling another player's flight
            if (!executor.hasPermission("dreamflight.others")) {
                executor.sendMessage(ChatColor.RED + "You don't have permission to toggle flight for other players!");
                return true;
            }
        }
        
        // Check if world allows flight
        String worldName = target.getWorld().getName();
        if (!plugin.getConfigManager().isWorldAllowed(worldName)) {
            executor.sendMessage(ChatColor.RED + "Flight is not allowed in world '" + worldName + "'!");
            return true;
        }
        
        // Toggle flight
        boolean newFlightState = !target.getAllowFlight();
        target.setAllowFlight(newFlightState);
        
        // If disabling flight and player is flying, make them fall safely
        if (!newFlightState && target.isFlying()) {
            target.setFlying(false);
        }
        
        // Send messages
        if (executor.equals(target)) {
            // Self toggle
            String status = newFlightState ? ChatColor.GREEN + "enabled" : ChatColor.RED + "disabled";
            target.sendMessage(ChatColor.YELLOW + "Flight " + status + ChatColor.YELLOW + "!");
        } else {
            // Other player toggle
            String status = newFlightState ? ChatColor.GREEN + "enabled" : ChatColor.RED + "disabled";
            executor.sendMessage(ChatColor.YELLOW + "Flight " + status + ChatColor.YELLOW + " for " + target.getName() + "!");
            target.sendMessage(ChatColor.YELLOW + "Your flight has been " + status + ChatColor.YELLOW + " by " + executor.getName() + "!");
        }
        
        return true;
    }
}