package pl.majkus522.mrpg.events;

import org.bukkit.craftbukkit.v1_20_R1.entity.CraftEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import pl.majkus522.mrpg.common.classes.mobs.CustomEntity;
import pl.majkus522.mrpg.controllers.PlayersController;
import pl.majkus522.mrpg.controllers.ScoreboardController;

public class OnPlayerKillsEntity implements Listener
{
    @EventHandler
    public void onPlayerKills(EntityDeathEvent event)
    {
        if (event.getEntity() instanceof Player)
            return;
        Player player = event.getEntity().getKiller();
        CustomEntity entity = (CustomEntity)((CraftEntity)event.getEntity()).getHandle();
        PlayersController.getCharacter(player).addExp(entity.getExp(player));
        ScoreboardController.updateLevel(player);
    }
}