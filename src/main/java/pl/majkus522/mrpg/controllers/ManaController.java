package pl.majkus522.mrpg.controllers;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.noise.PerlinNoiseGenerator;
import pl.majkus522.mrpg.Main;
import pl.majkus522.mrpg.common.ExtensionMethods;
import pl.majkus522.mrpg.common.classes.Character;

public class ManaController
{
    static NamespacedKey key = new NamespacedKey(Main.plugin, "mana");
    static NamespacedKey keyMax = new NamespacedKey(Main.plugin, "maxMana");
    static NamespacedKey keyRegen = new NamespacedKey(Main.plugin, "manaRegen");

    public static void generateMana(Chunk chunk)
    {
        PerlinNoiseGenerator generator = new PerlinNoiseGenerator(chunk.getWorld());
        double random = generator.noise(chunk.getX(), chunk.getZ());
        int maxMana = Math.abs((int)(random * 1000));
        chunk.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, maxMana);
        chunk.getPersistentDataContainer().set(keyMax, PersistentDataType.INTEGER, maxMana);
        chunk.getPersistentDataContainer().set(keyRegen, PersistentDataType.DOUBLE, Math.abs(generator.noise(chunk.getX(), chunk.getZ()) * ExtensionMethods.randomRange(45, 95)));
    }

    public static void displayMana(Player player)
    {
        if (SkillsController.playerHasSkillEnabled(player, "manaSense"))
            PlayersController.getCharacter(player).displayMana();
    }

    public static int getChunkMana(Location location)
    {
        return getChunkMana(location.getChunk());
    }

    public static int getChunkMana(Chunk chunk)
    {
        PersistentDataContainer data = chunk.getPersistentDataContainer();
        if (!data.has(key, PersistentDataType.INTEGER))
            return 0;
        return data.get(key, PersistentDataType.INTEGER);
    }

    public static int getChunkMaxMana(Chunk chunk)
    {
        PersistentDataContainer data = chunk.getPersistentDataContainer();
        if (!data.has(keyMax, PersistentDataType.INTEGER))
            return 0;
        return data.get(keyMax, PersistentDataType.INTEGER);
    }

    public static double getChunkManaRegen(Chunk chunk)
    {
        PersistentDataContainer data = chunk.getPersistentDataContainer();
        if (!data.has(keyRegen, PersistentDataType.DOUBLE))
            return 0;
        return data.get(keyRegen, PersistentDataType.DOUBLE);
    }

    public static int removeMana(Location location, int amount)
    {
        int mana = getChunkMana(location);
        if (getChunkMana(location) < amount)
            amount = mana;
        mana -= amount;
        location.getChunk().getPersistentDataContainer().set(key, PersistentDataType.INTEGER, mana);
        return amount;
    }

    public static void addMana(Location location, int amount)
    {
        addMana(location.getChunk(), amount);
    }

    public static void addMana(Chunk chunk, int amount)
    {
        int mana = getChunkMana(chunk) + amount;
        chunk.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, mana);
    }

    public static void gatherMana(Player player)
    {
        gatherMana(player, 15);
    }

    public static void gatherMana(Player player, int amount)
    {
        Character character = PlayersController.getCharacter(player);
        amount = Math.min(character.getManaDiffrence(), amount);
        amount = ManaController.removeMana(player.getLocation(), amount);
        character.addMana(amount);
    }
}