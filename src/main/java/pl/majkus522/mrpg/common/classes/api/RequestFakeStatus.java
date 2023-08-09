package pl.majkus522.mrpg.common.classes.api;

import pl.majkus522.mrpg.common.classes.PlayerStatus;

public class RequestFakeStatus extends PlayerStatus
{
    public int player;

    @Override
    public String toString()
    {
        return "RequestFakeStatus{" +
                "id=" + id +
                ", player=" + player +
                ", money=" + money +
                ", level=" + level +
                ", strength=" + strength +
                ", agility=" + agility +
                ", charisma=" + charisma +
                ", intelligence=" + intelligence +
                '}';
    }
}