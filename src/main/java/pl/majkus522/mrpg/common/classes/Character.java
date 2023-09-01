package pl.majkus522.mrpg.common.classes;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import pl.majkus522.mrpg.Main;
import pl.majkus522.mrpg.common.ExtensionMethods;
import pl.majkus522.mrpg.common.classes.api.RequestPlayer;
import pl.majkus522.mrpg.common.enums.HttpMethod;
import pl.majkus522.mrpg.controllers.ScoreboardController;

import java.sql.PreparedStatement;

public class Character extends PlayerStatus
{
    public Player player;
    public String session;
    boolean changes = false;

    public Character(Player player, String session)
    {
        this.player = player;
        this.session = session;
        HttpBuilder request = new HttpBuilder(HttpMethod.GET, "endpoints/players/" + player.getName()).setHeader("Session-Key", session).setHeader("Session-Type", "game");
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

        Bukkit.getScheduler().runTaskTimerAsynchronously(Main.plugin, new Runnable()
        {
            @Override
            public void run()
            {
                update();
            }
        }, 0, 20 * 60);
    }

    public void update()
    {
        if (changes)
        {
            try
            {
                PreparedStatement stmt = MySQL.getConnection().prepareStatement("update `players` set `money` = ?, `str` = ?, `agl` = ?, `chr` = ?, `intl` = ?, `level` = ?, `exp` = ? where `username` = ?");
                stmt.setFloat(1, money);
                stmt.setInt(2, str);
                stmt.setInt(3, agl);
                stmt.setInt(4, chr);
                stmt.setInt(5, intl);
                stmt.setInt(6, level);
                stmt.setInt(7, exp);
                stmt.setString(8, player.getName());
                stmt.executeUpdate();
            }
            catch (Exception e)
            {
                throw new RuntimeException(e);
            }
        }
    }

    public void deathPenalty()
    {
        exp = 0;
        money = (float)ExtensionMethods.round(money * 0.99f, 2);
        ScoreboardController.update(this);
    }

    public void addExp(int input)
    {
        exp += input;
        while(exp > ExtensionMethods.levelExp(level))
            levelUp();
        changes = true;
    }

    public void setStr(int input)
    {
        str = input;
        changes = true;
    }

    public void setAgl(int input)
    {
        agl = input;
        changes = true;
    }

    public void setChr(int input)
    {
        chr = input;
        changes = true;
    }

    public void setIntl(int input)
    {
        intl = input;
        changes = true;
    }

    public boolean hasMoney(float input)
    {
        return money > input;
    }

    public void addMoney(float input)
    {
        money += input;
        changes = true;
    }

    public void levelUp()
    {
        exp -= ExtensionMethods.levelExp(level);
        level++;
        str++;
        agl++;
        chr++;
        intl++;
        player.sendMessage("Your level has increased");
    }
}