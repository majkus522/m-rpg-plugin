package pl.majkus522.mrpg.common.classes;


import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.bukkit.entity.Player;
import pl.majkus522.mrpg.common.classes.api.RequestError;
import pl.majkus522.mrpg.common.enums.HttpMethod;
import pl.majkus522.mrpg.common.interfaces.IRequestResult;
import pl.majkus522.mrpg.controllers.PlayersController;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpBuilder
{
    HttpURLConnection connection;
    HttpMethod method;
    public String content;

    public HttpBuilder(HttpMethod method, String url)
    {
        try
        {
            this.method = method;
            connection = (HttpURLConnection) new URL("http://127.0.0.1/m-rpg/api/" + url).openConnection();
            connection.setRequestMethod(method.toString());
            if (method == HttpMethod.PATCH)
                setHeader("X-HTTP-Method-Override", "PATCH");
            connection.setDoOutput(true);
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    public HttpBuilder setHeader(String key, String value)
    {
        connection.setRequestProperty(key, value);
        return this;
    }

    public HttpBuilder setSessionHeaders(Player player)
    {
        setHeader("Session-Key", PlayersController.getCharacter(player).session);
        setHeader("Session-Type", "game");
        return this;
    }

    public HttpBuilder setItemsHeaders(int offset, int count)
    {
        setHeader("Items-Offset", Integer.toString(offset));
        setHeader("Items-Count", Integer.toString(count));
        return this;
    }

    public HttpBuilder setBody(String body)
    {
        try
        {
            OutputStream writer = connection.getOutputStream();
            byte[] input = body.getBytes(StandardCharsets.UTF_8);
            writer.write(input, 0, input.length);
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
        return this;
    }

    public int getCode()
    {
        try
        {
            return connection.getResponseCode();
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    public boolean isOk()
    {
        return getCode() >= 200 && getCode() < 300;
    }

    public HashMap<String, String> getOutputHeaders()
    {
        HashMap<String, String> headers = new HashMap<>();
        for(Map.Entry<String, List<String>> line : connection.getHeaderFields().entrySet())
            headers.put(line.getKey(), line.getValue().get(0));
        return headers;
    }

    public String getOutputHeader(String key)
    {
        return getOutputHeaders().get(key);
    }

    public IRequestResult getResult(Class<? extends IRequestResult> clazz)
    {
        if (method == HttpMethod.HEAD)
            return null;
        return new Gson().fromJson(getResultString(), clazz);
    }

    public List<IRequestResult> getResultAll(Class<? extends IRequestResult> clazz)
    {
        ArrayList<IRequestResult> list = new ArrayList<>();
        if (method == HttpMethod.HEAD)
            return list;
        JsonArray array = JsonParser.parseString(getResultString()).getAsJsonArray();
        for (JsonElement element : array)
            list.add(new Gson().fromJson(element, clazz));
        return list;
    }

    public String getResultString()
    {
        try
        {
            StringBuilder response = new StringBuilder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            while ((inputLine = reader.readLine()) != null)
                response.append(inputLine);
            content = response.toString();
            return content;
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    public RequestError getError()
    {
        try
        {
            StringBuilder response = new StringBuilder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
            String inputLine;
            while ((inputLine = reader.readLine()) != null)
                response.append(inputLine);
            reader.close();
            return new Gson().fromJson(response.toString(), RequestError.class);
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }
}
