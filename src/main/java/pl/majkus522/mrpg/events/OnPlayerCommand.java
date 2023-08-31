package pl.majkus522.mrpg.events;

import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class OnPlayerCommand implements Listener
{
    @EventHandler
    public void onPlayerCommand(PlayerCommandPreprocessEvent event)
    {
        Player player = event.getPlayer();
        PluginCommand command = Bukkit.getPluginCommand(event.getMessage().split(" ")[0].replace("/", ""));
        if (command == null)
        {
            player.sendMessage("Command doesn't exists");
            event.setCancelled(true);
            return;
        }
        String permission = command.getPermission();
        if (permission == null || permission.isEmpty())
            return;
        if (!player.hasPermission(permission))
        {
            event.setCancelled(true);
            player.sendMessage("Unauthorized operation");
        }
    }
}