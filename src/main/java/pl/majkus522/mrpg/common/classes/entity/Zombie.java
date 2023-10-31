package pl.majkus522.mrpg.common.classes.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import org.bukkit.Location;
import pl.majkus522.mrpg.common.classes.data.EntityData;

public class Zombie extends Enemy
{
    public Zombie(Location location)
    {
        super(location, new EntityData()
        {
            @Override
            public EntityType<? extends PathfinderMob> getType()
            {
                vtl = 100;
                name = "Zombie";
                return EntityType.ZOMBIE;
            }
        });
    }
}