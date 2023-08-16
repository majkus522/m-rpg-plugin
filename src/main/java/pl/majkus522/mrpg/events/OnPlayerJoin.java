package pl.majkus522.mrpg.events;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import pl.majkus522.mrpg.common.classes.HttpBuilder;
import pl.majkus522.mrpg.common.enums.HttpMethod;

public class OnPlayerJoin implements Listener
{
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event)
    {
        event.setJoinMessage("");
        Player player = event.getPlayer();
        HttpBuilder request = new HttpBuilder(HttpMethod.HEAD, "endpoints/players/" + player.getName());
        if(!request.isOk())
            player.kickPlayer("Please first register on our webstie \n\n" + ChatColor.BLUE + "M-RPG.COM");
    }
}