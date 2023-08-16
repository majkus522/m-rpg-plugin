package pl.majkus522.mrpg.controllers;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import pl.majkus522.mrpg.common.classes.HttpBuilder;
import pl.majkus522.mrpg.common.classes.api.RequestPlayer;
import pl.majkus522.mrpg.common.enums.HttpMethod;

import java.util.ArrayList;
import java.util.Collections;

public class ScoreboardController
{
    public static Scoreboard createScoreboard(Player player)
    {
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective objective = scoreboard.registerNewObjective("scoreboard", "dummy");

        objective.setDisplayName(ChatColor.AQUA + "M-RPG");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        HttpBuilder request = new HttpBuilder(HttpMethod.GET, "endpoints/players/" + player.getName()).setSessionHeaders(player);
        if(!request.isOk())
        {
            player.sendMessage("Server error");
            throw new RuntimeException(new Exception(request.getError().message));
        }
        RequestPlayer playerData = (RequestPlayer)request.getResult(RequestPlayer.class);
        ArrayList<String> elements = new ArrayList<String>();
        elements.add("Money: " + playerData.money);
        elements.add("Exp: " + playerData.exp);
        elements.add("Level: " + playerData.level);
        Collections.max(elements);

        ArrayList<Integer> lengths = new ArrayList<Integer>();
        int index = 1;
        for (String line : elements)
        {
            lengths.add(line.length());
            Score score = objective.getScore(ChatColor.BLUE + "| " + ChatColor.RESET + line);
            score.setScore(index);
            index++;
        }

        int max = Collections.max(lengths);
        Score score = objective.getScore(createLine(max));
        score.setScore(index + 1);
        score = objective.getScore(createLine(max) + ChatColor.RED);
        score.setScore(0);

        return scoreboard;
    }

    static String createLine(int length)
    {
        length += 2;
        if (length % 2 == 1)
            length--;
        String result = ChatColor.BLUE + "=";
        for(int index = 0; index < (length / 2); index++)
            result += "-=";
        return result;
    }
}