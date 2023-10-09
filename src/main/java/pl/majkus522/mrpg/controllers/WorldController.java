package pl.majkus522.mrpg.controllers;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.generator.ChunkGenerator;
import pl.majkus522.mrpg.Main;

import javax.annotation.Nonnull;
import java.util.Random;

public class WorldController
{
    static String[] worlds = new String[] {"login", "main"};

    public static void init()
    {
        Bukkit.getScheduler().runTask(Main.plugin, new Runnable()
        {
            @Override
            public void run()
            {
                for(String element : worlds)
                    getWorld(element, false);
            }
        });
    }

    public static World getWorld(String name, boolean isVoid)
    {
        if (Bukkit.getWorld(name) == null)
        {
            WorldCreator creator = new WorldCreator(name.replace("worlds/", ""));
            if (isVoid)
                creator.generator(new VoidChunkGenerator());
            return Bukkit.getServer().createWorld(creator);
        }
        return Bukkit.getWorld(name);
    }

    public static class VoidChunkGenerator extends ChunkGenerator
    {
        @Override
        @Nonnull
        public ChunkData generateChunkData(@Nonnull World world, @Nonnull Random random, int x, int z, @Nonnull BiomeGrid biome)
        {
            return createChunkData(world);
        }
    }
}