package pl.majkus522.mrpg.common.classes;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import pl.majkus522.mrpg.Main;
import pl.majkus522.mrpg.common.ExtensionMethods;
import pl.majkus522.mrpg.common.classes.api.RequestPlayer;
import pl.majkus522.mrpg.common.classes.api.RequestSkill;
import pl.majkus522.mrpg.common.enums.HttpMethod;
import pl.majkus522.mrpg.common.interfaces.IRequestResult;
import pl.majkus522.mrpg.controllers.ScoreboardController;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Character extends PlayerStatus
{
    public Player player;
    public String session;
    boolean changes = false;
    public ArrayList<CharacterSkill> skills;

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
        this.id = data.id;
        this.level = data.level;
        this.exp = data.exp;
        this.str = data.str;
        this.agl = data.agl;
        this.chr = data.chr;
        this.intl = data.intl;
        this.def = data.def;
        this.vtl = data.vtl;
        this.money = data.money;

        skills = new ArrayList<CharacterSkill>();
        request = new HttpBuilder(HttpMethod.GET, "endpoints/skills/" + player.getName()).setHeader("Session-Key", session).setHeader("Session-Type", "game").setHeader("Items", "0-999");
        if(!request.isOk())
        {
            player.sendMessage("Server error");
            throw new RuntimeException(new Exception(request.getError().message));
        }
        for (IRequestResult element : request.getResultAll(RequestSkill.class))
            skills.add(new CharacterSkill((RequestSkill) element));

        Bukkit.getScheduler().runTaskTimerAsynchronously(Main.plugin, new Runnable()
        {
            @Override
            public void run()
            {
                if (changes)
                    update();
            }
        }, 0, 20 * 60);
    }

    public void update()
    {
        try
        {
            PreparedStatement stmt = MySQL.getConnection().prepareStatement("update `players` set `money` = ?, `str` = ?, `agl` = ?, `chr` = ?, `intl` = ?, `def` = ?, `vtl` = ?, `level` = ?, `exp` = ? where `username` = ?");
            stmt.setFloat(1, money);
            stmt.setInt(2, str);
            stmt.setInt(3, agl);
            stmt.setInt(4, chr);
            stmt.setInt(5, intl);
            stmt.setInt(6, def);
            stmt.setInt(7, vtl);
            stmt.setInt(8, level);
            stmt.setInt(9, exp);
            stmt.setString(10, player.getName());
            stmt.executeUpdate();

            String data = "";
            boolean first = true;
            List<CharacterSkill> toAdd = skills.stream().filter(p -> p.status == Status.add).collect(Collectors.toList());
            for(CharacterSkill element : toAdd)
            {
                if (!first)
                    data += ",";
                data += "(" + id + ",\"" + element.skill + "\")";
                first = false;
                element.status = Status.ok;
            }
            String query = "insert into `skills`(`player`, `skill`) values ";
            if (toAdd.size() > 1)
                query += "(";
            query += data;
            if (toAdd.size() > 1)
                query += ")";
            stmt = MySQL.getConnection().prepareStatement(query);
            stmt.executeUpdate();

            query = "delete from `skills` where `player` = ? and (";
            first = true;
            for(CharacterSkill element : skills.stream().filter(p -> p.status == Status.remove).collect(Collectors.toList()))
            {
                if (!first)
                    query += " or ";
                query += "`skill` = \"" + element.skill + "\"";
                first = false;
                skills.remove(element);
            }
            stmt = MySQL.getConnection().prepareStatement(query + ")");
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
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
        while(exp >= ExtensionMethods.levelExp(level))
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

    public void setDef(int input)
    {
        def = input;
        changes = true;
    }

    public void setVtl(int input)
    {
        vtl = input;
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
        def++;
        vtl++;
        player.sendMessage("Your level has increased");
    }

    public static class CharacterSkill extends RequestSkill
    {
        public Status status = Status.ok;

        public CharacterSkill(String skill)
        {
            this.skill = skill;
            this.toggle = 0;
        }

        public CharacterSkill(String skill, Status status)
        {
            this(skill);
            this.status = status;
        }

        public CharacterSkill(RequestSkill request)
        {
            this.skill = request.skill;
            this.toggle = request.getToggle() ? 1 : 0;
        }

        public CharacterSkill(RequestSkill request, Status status)
        {
            this(request);
            this.status = status;
        }

        @Override
        public String toString()
        {
            return "CharacterSkill{" +
                    "status=" + status +
                    ", skill='" + skill + '\'' +
                    ", toggle=" + toggle +
                    '}';
        }
    }

    public enum Status
    {
        ok, add, remove
    }
}