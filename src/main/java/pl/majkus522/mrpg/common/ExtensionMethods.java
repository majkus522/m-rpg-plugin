package pl.majkus522.mrpg.common;

import org.bukkit.entity.Player;
import pl.majkus522.mrpg.Main;
import pl.majkus522.mrpg.common.api.RequestResult;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

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
            return new RequestResult(connection.getResponseCode(), response.toString());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return new RequestResult(600, "");
    }

    public static boolean isPlayerLogged(Player player)
    {
        return Main.playersSessions.containsKey(player.getName());
    }
}