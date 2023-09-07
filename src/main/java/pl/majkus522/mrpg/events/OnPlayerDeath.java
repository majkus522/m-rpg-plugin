package pl.majkus522.mrpg.events;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import pl.majkus522.mrpg.Main;
import pl.majkus522.mrpg.common.classes.entity.Enemy;
import pl.majkus522.mrpg.controllers.PlayersController;

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
                player.sendMessage("You have died");
                PlayersController.getCharacter(player).deathPenalty();
            }
        });
    }

    @EventHandler
    public void dfgdfjgdfg(EntityDamageByEntityEvent event)
    {
        if (event.getEntity() instanceof Player && ((CraftEntity)event.getDamager()).getHandle() instanceof Enemy && ((LivingEntity)event.getEntity()).getHealth() - event.getFinalDamage() < 0)
            ((Enemy) ((CraftEntity)event.getDamager()).getHandle()).cancelTaunt();
    }
}