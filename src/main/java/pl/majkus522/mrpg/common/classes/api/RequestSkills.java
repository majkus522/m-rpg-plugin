package pl.majkus522.mrpg.common.classes.api;

public class RequestSkills
{
    public int id;
    public String skill;
    public int player;
    public String rarity;

    @Override
    public String toString() {
        return "RequestSkillsArray{" +
                "id=" + id +
                ", skill='" + skill + '\'' +
                ", player=" + player +
                ", rarity='" + rarity + '\'' +
                '}';
    }
}