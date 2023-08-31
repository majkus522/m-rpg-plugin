package pl.majkus522.mrpg.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import pl.majkus522.mrpg.controllers.PlayersController;

public class OnPlayerLeave implements Listener
{
    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event)
    {
        event.setQuitMessage("");
        PlayersController.playerLeave(event.getPlayer());
    }
}