package pl.majkus522.mrpg.common.classes.api;

import pl.majkus522.mrpg.common.classes.PlayerStatus;
import pl.majkus522.mrpg.common.interfaces.IRequestResult;

public class RequestFakeStatus extends PlayerStatus implements IRequestResult
{
    public int player;

    @Override
    public String toString()
    {
        return "RequestFakeStatus{" +
                "player=" + player +
                ", id=" + id +
                ", level=" + level +
                ", exp=" + exp +
                ", stats=" + stats +
                ", money=" + money +
                '}';
    }
}