package pl.majkus522.mrpg.events;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import pl.majkus522.mrpg.Main;
import pl.majkus522.mrpg.common.classes.HttpBuilder;
import pl.majkus522.mrpg.common.enums.HttpMethod;
import pl.majkus522.mrpg.controllers.PlayersController;
import pl.majkus522.mrpg.controllers.WorldController;

public class OnPlayerJoin implements Listener
{
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event)
    {
        event.setJoinMessage("");
        Player player = event.getPlayer();
        HttpBuilder request = new HttpBuilder(HttpMethod.HEAD, "endpoints/players/" + player.getName());
        if(!request.isOk())
        {
            player.kickPlayer("Please first register on our webstie \n\n" + ChatColor.BLUE + "M-RPG.COM");
            return;
        }
        player.setGameMode(GameMode.ADVENTURE);
        player.teleport(new Location(WorldController.getWorld("worlds/login"), 0.5, 100, 0.5));
        player.sendTitle(ChatColor.GREEN + "" + ChatColor.BOLD + "PLEASE LOGIN", "Type /login <password>", 5, 80, 5);
        Bukkit.getScheduler().runTaskLater(Main.plugin, new Runnable()
        {
            @Override
            public void run()
            {
                if (!PlayersController.isPlayerLogged(player))
                    player.kickPlayer("Login timeout");
            }
        }, 60 * 20L);
    }
}