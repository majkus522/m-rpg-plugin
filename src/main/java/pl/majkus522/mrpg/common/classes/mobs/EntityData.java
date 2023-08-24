package pl.majkus522.mrpg.common.classes.mobs;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;

public class EntityData
{
    public String name;
    public int str;
    public int def;
    public int health;
    String type;

    public EntityType<? extends PathfinderMob> getType()
    {
        return (EntityType<? extends PathfinderMob>) EntityType.byString(type).get();
    }
}