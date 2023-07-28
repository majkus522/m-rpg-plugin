package pl.majkus522.mrpg.common.classes.api;

public class RequestPlayer
{
    public int id;
    public String username;
    public String email;
    public int money;
    public int level;
    public int exp;
    public int energy;
    public String world;
    public int strength;
    public int agility;
    public int charisma;
    public int intelligence;

    @Override
    public String toString()
    {
        return "RequestPlayer{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", money=" + money +
                ", level=" + level +
                ", exp=" + exp +
                ", energy=" + energy +
                ", world='" + world + '\'' +
                ", strength=" + strength +
                ", agility=" + agility +
                ", charisma=" + charisma +
                ", intelligence=" + intelligence +
                '}';
    }
}