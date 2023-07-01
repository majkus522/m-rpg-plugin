package pl.majkus522.mrpg.common;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ExtensionMethods
{
    public static RequestResult httpRequest(String method, String url)
    {
        try
        {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod(method);
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
}