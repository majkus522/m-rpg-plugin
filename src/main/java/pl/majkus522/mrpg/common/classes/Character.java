package pl.majkus522.mrpg.common.classes;

import com.google.gson.JsonParser;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import pl.majkus522.mrpg.Config;
import pl.majkus522.mrpg.Main;
import pl.majkus522.mrpg.common.ExtensionMethods;
import pl.majkus522.mrpg.common.classes.api.RequestPlayer;
import pl.majkus522.mrpg.common.classes.api.RequestSkill;
import pl.majkus522.mrpg.common.enums.DamageType;
import pl.majkus522.mrpg.common.enums.HttpMethod;
import pl.majkus522.mrpg.common.interfaces.IRequestResult;
import pl.majkus522.mrpg.controllers.ScoreboardController;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
        this.money = data.money;

        initStats(new JsonParser().parse(request.content).getAsJsonObject());
        setMaxHealth();
        setSpeed();

        skills = new ArrayList<CharacterSkill>();
        request = new HttpBuilder(HttpMethod.GET, "endpoints/skills/" + player.getName()).setHeader("Session-Key", session).setHeader("Session-Type", "game").setHeader("Items", "0-999");
        if(request.isOk())
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
            String query = "update `players` set `money` = ?, `level` = ?, `exp` = ?";
            for (Map.Entry<String, Integer> element : stats.entrySet())
                query += ", `" + element.getKey() + "` = " + element.getValue();
            PreparedStatement stmt = MySQL.getConnection().prepareStatement(query + " where `username` = ?");
            stmt.setFloat(1, money);
            stmt.setInt(2, level);
            stmt.setInt(3, exp);
            stmt.setString(4, player.getName());
            stmt.executeUpdate();

            List<CharacterSkill> toAdd = skills.stream().filter(p -> p.status == Status.add).collect(Collectors.toList());
            if (toAdd.size() > 0)
            {
                String data = "";
                boolean first = true;
                for(CharacterSkill element : toAdd)
                {
                    if (!first)
                        data += ",";
                    data += "(" + id + ",\"" + element.skill + "\")";
                    first = false;
                    element.status = Status.ok;
                }
                query = "insert into `skills`(`player`, `skill`) values ";
                if (toAdd.size() > 1)
                    query += "(";
                query += data;
                if (toAdd.size() > 1)
                    query += ")";
                stmt = MySQL.getConnection().prepareStatement(query);
                stmt.executeUpdate();
            }

            List<CharacterSkill> toRemove = skills.stream().filter(p -> p.status == Status.remove).collect(Collectors.toList());
            if (toRemove.size() > 0)
            {
                query = "delete from `skills` where `player` = ? and (";
                boolean first = true;
                for(CharacterSkill element : toRemove)
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
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    void setMaxHealth()
    {
        player.setMaxHealth(getStat("vtl"));
        player.setHealth(getStat("vtl"));
    }

    void setSpeed()
    {
        player.setWalkSpeed(Config.baseWalkSpeed * (1 + (getStat("agl") / 200)));
    }

    public void deathPenalty()
    {
        exp = 0;
        money = (float)ExtensionMethods.round(money * 0.99f, 2);
        ScoreboardController.update(this);
    }

    public double getDamage()
    {
        return getStat("str");
    }

    public double handleDamage(double input)
    {
        return handleDamage(input, DamageType.physical);
    }

    public double handleDamage(double input, DamageType type)
    {
        switch (type)
        {
            case mental:
            case poison:
                return input;

            case magical:
                input -= (getStat("def") * 0.25f);
                break;

            case physical:
                input -= getStat("def");
                break;
        }
        double random = Math.random() * 100;
        if (random < ((double)getStat("dex")) / 5)
            return -1;
        return Math.max(input, 0);
    }

    public void addExp(int input)
    {
        exp += input;
        while(exp >= ExtensionMethods.levelExp(level))
            levelUp();
        changes = true;
    }

    @Override
    public int getStat(String label)
    {
        return stats.get(label);
    }

    public void setStat(String label, int value)
    {
        stats.put(label, value);
        switch (label)
        {
            case "agl":
                setSpeed();
                break;

            case "vtl":
                setMaxHealth();
                break;
        }
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
        for (Map.Entry<String, Integer> element : stats.entrySet())
            element.setValue(element.getValue() + 1);
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