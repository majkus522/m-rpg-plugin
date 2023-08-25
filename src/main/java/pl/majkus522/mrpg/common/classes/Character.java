package pl.majkus522.mrpg.common.classes;

import org.bukkit.entity.Player;
import pl.majkus522.mrpg.common.classes.api.RequestPlayer;
import pl.majkus522.mrpg.common.enums.HttpMethod;

public class Character
{
    public Player player;
    public int level;
    public int exp;
    public int str;
    public int agl;
    public int chr;
    public int intl;
    public float money;
    public String session;

    public Character(Player player, String session)
    {
        this.player = player;
        this.session = session;
        HttpBuilder request = new HttpBuilder(HttpMethod.GET, "endpoints/players/" + player.getName()).setSessionHeaders(player);
        if(!request.isOk())
        {
            player.sendMessage("Server error");
            throw new RuntimeException(new Exception(request.getError().message));
        }
        RequestPlayer data = (RequestPlayer)request.getResult(RequestPlayer.class);
        this.level = data.level;
        this.exp = data.exp;
        this.str = data.str;
        this.agl = data.agl;
        this.chr = data.chr;
        this.intl = data.intl;
        this.money = data.money;
    }
}