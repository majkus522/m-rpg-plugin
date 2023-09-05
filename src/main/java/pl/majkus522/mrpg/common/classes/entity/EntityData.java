package pl.majkus522.mrpg.common.classes.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;

public class EntityData
{
    public String name = "";
    public int str = 0;
    public int def = 0;
    public int health = 0;
    public double speed = 0.23;
    public int exp = 0;
    String type;

    public EntityType<? extends PathfinderMob> getType()
    {
        return (EntityType<? extends PathfinderMob>) EntityType.byString(type).get();
    }
}