package pl.majkus522.mrpg.common.classes.api;

import pl.majkus522.mrpg.common.enums.SkillRarity;

public class RequestSkills
{
    public int id;
    public String skill;
    public int player;
    public SkillRarity rarity;
    public boolean toggle;

    @Override
    public String toString()
    {
        return "RequestSkills{" +
                "id=" + id +
                ", skill='" + skill + '\'' +
                ", player=" + player +
                ", rarity='" + rarity + '\'' +
                ", toggle=" + toggle +
                '}';
    }
}