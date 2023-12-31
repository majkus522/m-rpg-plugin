package pl.majkus522.mrpg.common;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

public class ExtensionMethods
{
    public static ItemStack emptySlot()
    {
        return emptySlot(Material.GRAY_STAINED_GLASS_PANE);
    }

    public static ItemStack emptySlot(Material material)
    {
        ItemStack item = new ItemStack(material, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(" ");
        item.setItemMeta(meta);
        return item;
    }

    public static ArrayList<Player> getPlayersInRange(Location location, int range)
    {
        int chunkRange = (range - (range % 16)) / 16 + 1;
        int xStart = location.getChunk().getX() - chunkRange;
        int zStart = location.getChunk().getZ() - chunkRange;
        int xEnd = location.getChunk().getX() + chunkRange;
        int zEnd = location.getChunk().getZ() + chunkRange;
        ArrayList<Player> players = new ArrayList<>();
        World world = location.getWorld();
        for (int x = xStart; x <= xEnd; x++)
        {
            for (int z = zStart; z <= zEnd; z++)
            {
                for (Entity entity : world.getChunkAt(x, z).getEntities())
                    if(entity instanceof Player && Math.abs(location.distance(entity.getLocation())) <= range)
                        players.add((Player)entity);
            }
        }
        return players;
    }

    public static void sendMessageToPlayers(ArrayList<Player> players, String message)
    {
        for (Player player : players)
            player.sendMessage(message);
    }

    public static double round(double value, int decimalPlace)
    {
        BigDecimal bd = new BigDecimal(Double.toString(value));
        bd = bd.setScale(decimalPlace, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public static int roundInt(double value, int decimalPlace)
    {
        return (int) Math.round(round(value, decimalPlace));
    }

    public static int levelExp(int level)
    {
        if(level == 0)
            return 0;
        return (int)(Math.log(level + 1) * 1500) + levelExp(level - 1);
    }

    public static int randomRange(int min, int max)
    {
        return (int) ((Math.random() * (max - min)) + min);
    }
}