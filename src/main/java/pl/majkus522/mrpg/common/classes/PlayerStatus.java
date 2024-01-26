package pl.majkus522.mrpg.common.classes;

import com.google.gson.JsonObject;
import pl.majkus522.mrpg.Config;
import pl.majkus522.mrpg.common.classes.data.StatData;

import java.util.HashMap;

public class PlayerStatus
{
    public int id;
    protected int level;
    protected int exp;
    protected HashMap<String, Integer> stats;
    protected float money;
    public String clazz = null;
    public String guild;
    public String helmet;
    public String chestplate;
    public String leggings;
    public String boots;

    public void initStats(JsonObject obj)
    {
        stats = new HashMap<>();
        for (StatData element : Config.characterStats)
            stats.put(element.label, obj.get(element.label).getAsInt());
    }

    public int getLevel()
    {
        return level;
    }

    public int getExp()
    {
        return exp;
    }

    public float getMoney()
    {
        return money;
    }

    public int getStat(String label)
    {
        return stats.get(label);
    }
}