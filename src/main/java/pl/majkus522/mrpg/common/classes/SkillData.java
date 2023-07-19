package pl.majkus522.mrpg.common.classes;

public class SkillData
{
    public String label;
    public boolean active;
    public String description;
    public String rarity;

    @Override
    public String toString()
    {
        return "SkillData{" +
                "label='" + label + '\'' +
                ", active=" + active +
                ", description='" + description + '\'' +
                ", rarity='" + rarity + '\'' +
                '}';
    }
}