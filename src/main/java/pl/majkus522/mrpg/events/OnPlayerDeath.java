package pl.majkus522.mrpg.events;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import pl.majkus522.mrpg.Main;

import java.util.Calendar;
import java.util.Date;

public class OnPlayerDeath implements Listener
{
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event)
    {
        event.setDeathMessage("");
        Bukkit.getScheduler().runTask(Main.plugin, new Runnable()
        {
            @Override
            public void run()
            {
                Player player = event.getEntity();
                player.spigot().respawn();
                Date date = new Date();
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                calendar.add(Calendar.DATE, 1);
                player.ban("You have died", calendar.getTime(), "death", true);
            }
        });
    }
}