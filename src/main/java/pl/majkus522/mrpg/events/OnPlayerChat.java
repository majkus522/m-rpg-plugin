package pl.majkus522.mrpg.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import pl.majkus522.mrpg.Config;
import pl.majkus522.mrpg.common.ExtensionMethods;
import pl.majkus522.mrpg.controllers.PlayersController;

public class OnPlayerChat implements Listener
{
    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event)
    {
        event.setCancelled(true);
        Player player = event.getPlayer();
        if(!PlayersController.isPlayerLogged(player))
        {
            player.sendMessage("You must be logged in");
            return;
        }
        ExtensionMethods.sendMessageToPlayers(ExtensionMethods.getPlayersInRange(player.getLocation(), Config.chatRange), player.getName() + " > " + event.getMessage());
    }
}