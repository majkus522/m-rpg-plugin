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
        if (event.getDamager() instanceof Player)
            event.setDamage(PlayersController.getCharacter((Player)event.getDamager()).getDamage());
        if (event.getEntity() instanceof Player)
        {
            Character character = PlayersController.getCharacter((Player)event.getEntity());
            double damage = character.handleDamage(event.getDamage());
            if (damage < 0)
            {
                event.setCancelled(true);
                return;
            }
            event.setDamage(damage);
            if(((LivingEntity)event.getEntity()).getHealth() - event.getDamage() <= 0)
            {
                if (((CraftEntity)event.getDamager()).getHandle() instanceof Enemy)
                {
                    Enemy enemy = (Enemy) ((CraftEntity)event.getDamager()).getHandle();
                    enemy.cancelTaunt();
                    event.getEntity().sendMessage("You have been killed by " + enemy.getEntityName());
                }
                else if (event.getDamager() instanceof Player)
                    event.getEntity().sendMessage("You have been killed by " + event.getDamager().getName());
            }

            return;
        }
        Entity entityHandle = ((CraftEntity)event.getEntity()).getHandle();
        if(entityHandle instanceof CustomEntity)
        {
            double damage = event.getDamage();
            CustomEntity entity = (CustomEntity) entityHandle;
            damage = entity.handleDamage(damage);
            if (damage < 0)
            {
                event.setCancelled(true);
                return;
            }
            event.setDamage(damage);
        }
    }
}