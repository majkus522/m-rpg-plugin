package pl.majkus522.mrpg;

import pl.majkus522.mrpg.common.classes.data.StatData;
import pl.majkus522.mrpg.controllers.FilesController;

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
    public static int characterSkills = 5;

    public static void init()
    {
        characterStats = new ArrayList<>(Arrays.asList(FilesController.readJsonFile("data/playerStats", StatData[].class)));
    }
}