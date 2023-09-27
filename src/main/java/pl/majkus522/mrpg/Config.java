package pl.majkus522.mrpg;

import com.google.gson.Gson;
import pl.majkus522.mrpg.common.ExtensionMethods;
import pl.majkus522.mrpg.common.classes.data.StatData;

import java.util.ArrayList;
import java.util.Arrays;

public class Config
{
    public final static int chatRange = 15;
    public final static int shoutRange = 50;
    public final static int statusRange = 5;
    public final static int payRange = 5;
    public final static double tauntRange = 20;
    public final static float baseWalkSpeed = 0.2f;
    public static ArrayList<StatData> characterStats;

    public static void init()
    {
        characterStats = new ArrayList<>(Arrays.asList(new Gson().fromJson(ExtensionMethods.readJsonFile("data/playerStats.json"), StatData[].class)));
    }
}