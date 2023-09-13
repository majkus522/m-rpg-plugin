package pl.majkus522.mrpg.common.classes;

import pl.majkus522.mrpg.common.enums.SkillRarity;

import java.util.Arrays;

public class SkillData
{
    public String label;
    public boolean toggle;
    public String description;
    public SkillRarity rarity;
    public String[] evolution = new String[0];

    @Override
    public java.lang.String toString()
    {
        return "SkillData{" +
                "label=" + label +
                ", toggle=" + toggle +
                ", description=" + description +
                ", rarity=" + rarity +
                ", evolution=" + Arrays.toString(evolution) +
                '}';
    }
}