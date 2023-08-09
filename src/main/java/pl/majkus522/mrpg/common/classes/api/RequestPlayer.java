package pl.majkus522.mrpg.common.classes.api;

import pl.majkus522.mrpg.common.classes.PlayerStatus;

public class RequestPlayer extends PlayerStatus
{
    public String username;
    public String email;
    public int exp;
    public int energy;
    public String world;

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