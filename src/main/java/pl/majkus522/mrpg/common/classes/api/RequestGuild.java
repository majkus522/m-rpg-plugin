package pl.majkus522.mrpg.common.classes.api;

import pl.majkus522.mrpg.common.interfaces.IRequestResult;

public class RequestGuild implements IRequestResult
{
    public int id;
    public String name;
    public String slug;
    public int leader;
}