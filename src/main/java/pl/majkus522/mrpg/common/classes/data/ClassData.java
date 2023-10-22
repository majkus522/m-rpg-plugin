package pl.majkus522.mrpg.common.classes.data;

import pl.majkus522.mrpg.common.enums.Rarity;

public class ClassData
{
    public String label = "None";
    public Rarity rarity = Rarity.common;
    public String evolution = "";

    public String toPrettyString()
    {
        return rarity.getColor() + label.substring(0, 1).toUpperCase() + label.substring(1);
    }
}