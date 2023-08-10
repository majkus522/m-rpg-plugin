package pl.majkus522.mrpg.common.classes.api;

public class RequestSkill
{
    public int id;
    public String skill;
    public int player;
    int toggle;

    public boolean getToggle()
    {
        return toggle == 1;
    }

    @Override
    public String toString()
    {
        return "RequestSkills{" +
                "id=" + id +
                ", skill='" + skill + '\'' +
                ", player=" + player +
                ", toggle=" + toggle +
                '}';
    }
}