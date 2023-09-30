package pl.majkus522.mrpg.controllers;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.noise.PerlinNoiseGenerator;
import pl.majkus522.mrpg.Main;

public class ManaController
{
    static NamespacedKey key = new NamespacedKey(Main.plugin, "mana");

    public static void generateMana(Chunk chunk)
    {
        double random = new PerlinNoiseGenerator(chunk.getWorld()).noise(chunk.getX(), chunk.getZ());
        int mana = Math.abs((int)(random * 1000));
        chunk.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, mana);
    }

    public static void displayMana(Player player)
    {
        if (SkillsController.playerHasSkillEnabled(player, "manaSense"))
            PlayersController.getCharacter(player).displayMana();
    }

    public static int getChunkMana(Location location)
    {
        PersistentDataContainer data = location.getChunk().getPersistentDataContainer();
        if (!data.has(key, PersistentDataType.INTEGER))
            return 0;
        return data.get(key, PersistentDataType.INTEGER);
    }
}