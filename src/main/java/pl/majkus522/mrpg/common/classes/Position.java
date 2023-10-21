package pl.majkus522.mrpg.common.classes;

import org.bukkit.Location;
import pl.majkus522.mrpg.controllers.WorldController;

public class Position
{
    public int x;
    public int y;
    public int z;
    public String world;

    public Position(Location location)
    {
        this.x = (int)location.getX();
        this.y = (int)location.getY();
        this.z = (int)location.getZ();
        this.world = location.getWorld().getName();
    }

    public Location toLocation()
    {
        return new Location(WorldController.getWorld(world, false), x, y, z);
    }
}
