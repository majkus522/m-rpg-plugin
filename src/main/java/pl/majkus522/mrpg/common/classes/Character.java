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
        RequestPlayer request = (RequestPlayer)new HttpBuilder(HttpMethod.GET, "endpoints/players/" + player.getName()).setSessionHeaders(player).getResult(RequestPlayer.class);
        this.level = request.level;
        this.exp = request.exp;
        this.str = request.str;
        this.agl = request.agl;
        this.chr = request.chr;
        this.intl = request.intl;
        this.money = request.money;
    }
}