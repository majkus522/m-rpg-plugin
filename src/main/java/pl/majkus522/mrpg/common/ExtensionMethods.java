package pl.majkus522.mrpg.common;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import pl.majkus522.mrpg.Main;

import java.io.File;
import java.io.FileFilter;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class ExtensionMethods
{
    public static boolean isPlayerLogged(Player player)
    {
        return Main.playersSessions.containsKey(player.getName());
    }

    public static ItemStack emptySlot()
    {
        ItemStack item = new ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(" ");
        item.setItemMeta(meta);
        return item;
    }

    public static String readJsonFile(String url)
    {
        try
        {
            return new String(Files.readAllBytes(Paths.get(url)), StandardCharsets.UTF_8);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return "";
    }

    public static File[] scanDir(String url)
    {
        return new File(url).listFiles(new FileFilter()
        {
            public boolean accept(File f)
            {
                return f.isFile();
            }
        });
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
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return bd.doubleValue();
    }

    public static int roundInt(double value, int decimalPlace)
    {
        return (int) Math.round(round(value, decimalPlace));
    }
}