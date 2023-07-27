package pl.majkus522.mrpg.common.classes;

import pl.majkus522.mrpg.common.enums.SkillRarity;

public class SkillData
{
    public String label;
    public boolean active;
    public String description;
    public SkillRarity rarity;

    @Override
    public String toString()
    {
        return "SkillData{" +
                "label='" + label + '\'' +
                ", active=" + active +
                ", description='" + description + '\'' +
                ", rarity='" + rarity.toString() + '\'' +
                '}';
    }
}