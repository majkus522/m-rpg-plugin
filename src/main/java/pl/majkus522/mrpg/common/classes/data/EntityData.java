package pl.majkus522.mrpg.common.classes.data;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;

public class EntityData
{
    public String name = "";
    public int str = 1;
    public int agl = 1;
    public int def = 1;
    public int vtl = 1;
    public int dex = 1;
    public int exp = 1;
    String type;

    public EntityType<? extends PathfinderMob> getType()
    {
        return (EntityType<? extends PathfinderMob>) EntityType.byString(type).get();
    }
}