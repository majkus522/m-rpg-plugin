package pl.majkus522.mrpg.controllers;

import org.bukkit.*;
import org.bukkit.entity.Player;
import pl.majkus522.mrpg.Main;

public class WorldController
{
    public static void teleport(Player player, String world, Location location)
    {
        Bukkit.getScheduler().runTask(Main.plugin, new WorldGenerator(player, world, location));
    }

    public static class WorldGenerator implements Runnable
    {
        String name;
        Player player;
        Location location;

        public WorldGenerator(Player player, String world, Location location)
        {
            this.name = world;
            this.player = player;
            this.location = location;
        }

        @Override
        public void run()
        {
            World world = Bukkit.getWorld(name);
            if(world == null)
            {
                WorldCreator creator = new WorldCreator(name);
                creator.type(WorldType.NORMAL);
                creator.environment(World.Environment.NORMAL);
                creator.generateStructures(false);
                world = creator.createWorld();
            }
            location.setWorld(world);
            player.teleport(location);
        }
    }

    public static World getWorld(String name)
    {
        if (Bukkit.getWorld(name) == null)
            return Bukkit.getServer().createWorld(new WorldCreator(name.replace("worlds/", "")));
        return Bukkit.getWorld(name);
    }
}