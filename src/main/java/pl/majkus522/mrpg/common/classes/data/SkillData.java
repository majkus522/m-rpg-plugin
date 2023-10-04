package pl.majkus522.mrpg.common.classes.data;

import pl.majkus522.mrpg.common.enums.SkillRarity;

import java.util.Arrays;

public class SkillData
{
    public String label;
    public boolean toggle = false;
    public boolean usable = false;
    public String description;
    public SkillRarity rarity;
    public String[] evolution = new String[0];

    @Override
    public String toString()
    {
        return "SkillData{" +
                "label='" + label + '\'' +
                ", toggle=" + toggle +
                ", usable=" + usable +
                ", description='" + description + '\'' +
                ", rarity=" + rarity +
                ", evolution=" + Arrays.toString(evolution) +
                '}';
    }
}