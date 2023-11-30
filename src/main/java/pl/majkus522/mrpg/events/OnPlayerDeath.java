package pl.majkus522.mrpg.events;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import pl.majkus522.mrpg.Main;
import pl.majkus522.mrpg.controllers.PlayersController;

public class OnPlayerDeath implements Listener
{
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event)
    {
        event.setDeathMessage("");
        event.setKeepInventory(true);
        event.getDrops().clear();
        Bukkit.getScheduler().runTask(Main.plugin, () ->
        {
            Player player = event.getEntity();
            player.spigot().respawn();
            PlayersController.getCharacter(player).deathPenalty();
        });
    }
}