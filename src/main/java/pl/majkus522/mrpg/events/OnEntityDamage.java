package pl.majkus522.mrpg.events;

import net.minecraft.world.entity.Entity;
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import pl.majkus522.mrpg.common.classes.mobs.CustomEntity;

public class OnEntityDamage implements Listener
{
    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event)
    {
        Entity entityHandle = ((CraftEntity)event.getEntity()).getHandle();
        if (entityHandle instanceof CustomEntity)
        {
            CustomEntity entity = (CustomEntity) entityHandle;
            event.setDamage(entity.handleDamage(event.getDamage()));
        }
    }
}