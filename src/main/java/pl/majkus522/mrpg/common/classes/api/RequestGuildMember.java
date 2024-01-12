package pl.majkus522.mrpg.common.classes.api;

import pl.majkus522.mrpg.common.enums.GuildMemberType;
import pl.majkus522.mrpg.common.interfaces.IRequestResult;

public class RequestGuildMember implements IRequestResult
{
    public String username;
    public GuildMemberType type;
}