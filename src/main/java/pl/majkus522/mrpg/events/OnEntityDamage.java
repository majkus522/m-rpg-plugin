package pl.majkus522.mrpg.events;

import net.minecraft.world.entity.Entity;
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import pl.majkus522.mrpg.common.classes.Character;
import pl.majkus522.mrpg.common.classes.entity.CustomEntity;
import pl.majkus522.mrpg.common.classes.entity.Enemy;
import pl.majkus522.mrpg.controllers.PlayersController;

public class OnEntityDamage implements Listener
{
    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event)
    {
        if (event.getEntity() instanceof Player)
        {
            Character character = PlayersController.getCharacter((Player)event.getEntity());
            double newDamage = character.handleDamage(event.getDamage());
            if (newDamage <= 0)
            {
                event.setCancelled(true);
                return;
            }
            event.setDamage(newDamage);
            if (((CraftEntity)event.getDamager()).getHandle() instanceof Enemy && ((LivingEntity)event.getEntity()).getHealth() - event.getDamage() < 0)
                ((Enemy) ((CraftEntity)event.getDamager()).getHandle()).cancelTaunt();
            return;
        }

        Entity entityHandle = ((CraftEntity)event.getEntity()).getHandle();
        if(entityHandle instanceof CustomEntity)
        {
            CustomEntity entity = (CustomEntity) entityHandle;
            event.setDamage(entity.handleDamage(event.getDamage()));
        }
    }
}