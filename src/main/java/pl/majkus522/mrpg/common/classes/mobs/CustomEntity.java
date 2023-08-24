package pl.majkus522.mrpg.common.classes.mobs;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.PathfinderMob;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_20_R1.CraftWorld;

public class CustomEntity extends PathfinderMob
{
    EntityData data;

    public CustomEntity(Location location, EntityData data)
    {
        super(data.getType(), ((CraftWorld)location.getWorld()).getHandle());
        this.setPos(location.getX(), location.getY(), location.getZ());
        this.setCustomName((Component) Component.Serializer.fromJson("\"" + ChatColor.translateAlternateColorCodes('$', data.name) + "\""));
        this.setCustomNameVisible(true);
        this.data = data;
        this.setHealth(data.health);
    }

    public int getDamage()
    {
        return data.str * 2;
    }

    @Override
    protected void registerGoals() { }
}