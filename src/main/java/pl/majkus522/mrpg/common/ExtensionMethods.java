package pl.majkus522.mrpg.common;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import pl.majkus522.mrpg.Main;
import pl.majkus522.mrpg.common.api.RequestResult;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExtensionMethods
{
    public static RequestResult httpRequest(String method, String url)
    {
        return httpRequest(method, url, new HashMap<>());
    }

    public static RequestResult httpRequest(String method, String url, HashMap<String, String> headers)
    {
        try
        {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod(method);
            headers.forEach((key, value) -> connection.setRequestProperty(key, value));
            BufferedReader reader;
            if(connection.getResponseCode() < 300)
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            else
                reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = reader.readLine()) != null)
                response.append(inputLine);
            reader.close();
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
}