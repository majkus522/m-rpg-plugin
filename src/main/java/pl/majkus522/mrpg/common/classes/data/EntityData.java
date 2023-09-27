package pl.majkus522.mrpg.common.classes.data;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;

public class EntityData
{
    public String name = "";
    public int str = 0;
    public int agl = 0;
    public int def = 0;
    public int vtl = 0;
    public int dex = 0;
    public int exp = 0;
    String type;

    public EntityType<? extends PathfinderMob> getType()
    {
        return (EntityType<? extends PathfinderMob>) EntityType.byString(type).get();
    }
}