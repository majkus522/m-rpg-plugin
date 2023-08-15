package pl.majkus522.mrpg.common;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import pl.majkus522.mrpg.Main;
import pl.majkus522.mrpg.common.classes.api.RequestResult;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExtensionMethods
{
    public static RequestResult httpRequest(String method, String url)
    {
        return httpRequest(method, url, "", new HashMap<>());
    }

    public static RequestResult httpRequest(String method, String url, HashMap<String, String> headers)
    {
        return httpRequest(method, url, "", headers);
    }

    public static RequestResult httpRequest(String method, String url, Player player)
    {
        return httpRequest(method, url, "", getSessionHeaders(player));
    }

    public static RequestResult httpRequest(String method, String url, String body)
    {
        return httpRequest(method, url, body, new HashMap<>());
    }

    public static RequestResult httpRequest(String method, String url, String body, Player player)
    {
        return httpRequest(method, url, body, getSessionHeaders(player));
    }

    public static RequestResult httpRequest(String method, String url, String body, HashMap<String, String> headers)
    {
        try
        {
            HttpURLConnection connection = (HttpURLConnection) new URL("http://127.0.0.1/m-rpg/api/" + url).openConnection();
            connection.setRequestMethod(method);
            connection.setDoOutput(true);

            headers.forEach((key, value) -> connection.setRequestProperty(key, value));

            if (method.equals("POST") || method.equals("PUT") || method.equals("PATCH"))
            {
                OutputStream writter = connection.getOutputStream();
                byte[] input = body.getBytes("utf-8");
                writter.write(input, 0, input.length);
            }

            StringBuilder response = new StringBuilder();
            if(method != "HEAD")
            {
                BufferedReader reader;
                if(connection.getResponseCode() < 300)
                    reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                else
                    reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                String inputLine;
                while ((inputLine = reader.readLine()) != null)
                    response.append(inputLine);
                reader.close();
            }

            HashMap<String, String> outputHeaders = new HashMap<>();
            for(Map.Entry<String, List<String>> line : connection.getHeaderFields().entrySet())
            {
                outputHeaders.put(line.getKey(), line.getValue().get(0));
            }
            return new RequestResult(connection.getResponseCode(), response.toString(), outputHeaders);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return new RequestResult(600, "", new HashMap<>());
    }

    public static HashMap<String, String> getSessionHeaders(Player player)
    {
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Session-Key", Main.playersSessions.get(player.getName()));
        headers.put("Session-Type", "game");
        return headers;
    }

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