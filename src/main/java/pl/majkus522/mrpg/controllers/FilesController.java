package pl.majkus522.mrpg.controllers;

import com.google.gson.Gson;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FilesController
{
    public static <T> T readJsonFile(String file, Class<T> clazz)
    {
        try
        {
            return new Gson().fromJson(new String(Files.readAllBytes(Paths.get(file + ".json")), StandardCharsets.UTF_8), clazz);
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    public static void writeJsonFile(String file, Object data)
    {
        writeJsonFile(file, new Gson().toJson(data));
    }

    public static void writeJsonFile(String file, String content)
    {
        try
        {
            String[] split = file.split("/");
            String dir = "";
            for(int index = 0; index < split.length - 1; index++)
                dir += split[index] + "/";
            File f = new File(dir);
            f.mkdirs();
            BufferedWriter writer = new BufferedWriter(new FileWriter(file + ".json"));
            writer.write(content);
            writer.close();
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    public static boolean fileExists(String file)
    {
        return new File(file).exists();
    }

    public static File[] scanDir(String url)
    {
        return new File(url).listFiles(File::isFile);
    }
}