package pl.majkus522.mrpg.common.classes.api;

import pl.majkus522.mrpg.common.classes.PlayerStatus;
import pl.majkus522.mrpg.common.interfaces.IRequestResult;

public class RequestPlayer extends PlayerStatus implements IRequestResult
{
    public String username;
    public String email;
    public int energy;
    public String world;

    @Override
    public String toString()
    {
        return "RequestPlayer{" +
                "username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", energy=" + energy +
                ", world='" + world + '\'' +
                ", id=" + id +
                ", level=" + level +
                ", exp=" + exp +
                ", stats=" + stats +
                ", money=" + money +
                '}';
    }
}