package pl.majkus522.mrpg.common.classes;

public class PlayerSettings
{
    public int health;
    public int mana;
    public String[] skills;

    public PlayerSettings(int health, int mana, String[] skills)
    {
        this.health = health;
        this.mana = mana;
        this.skills = skills;
    }
}