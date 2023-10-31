package pl.majkus522.mrpg.events;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.monster.Monster;
import org.bukkit.craftbukkit.v1_20_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;
import pl.majkus522.mrpg.common.classes.entity.Zombie;

public class OnEntitySpawns implements Listener
{
    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent event)
    {
        if (event.getLocation().getBlock().isLiquid())
            return;
        if (!(((CraftEntity)event.getEntity()).getHandle() instanceof Monster))
            return;
        event.setCancelled(true);
        ServerLevel world = ((CraftWorld)event.getLocation().getWorld()).getHandle();
        world.addFreshEntity(new Zombie(event.getLocation()));
    }
}