package pl.majkus522.mrpg.controllers;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import pl.majkus522.mrpg.common.classes.Character;
import pl.majkus522.mrpg.common.classes.HttpBuilder;
import pl.majkus522.mrpg.common.classes.api.RequestPlayer;
import pl.majkus522.mrpg.common.enums.HttpMethod;

import java.util.ArrayList;
import java.util.Collections;

public class ScoreboardController
{
    public static void createScoreboard(Character character)
    {
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective objective = scoreboard.registerNewObjective("scoreboard", "dummy");

        objective.setDisplayName(ChatColor.AQUA + "M-RPG");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        ArrayList<String> elements = new ArrayList<String>();
        elements.add("Money: " + character.money);
        elements.add("Exp: " + character.exp);
        elements.add("Level: " + character.level);
        Collections.max(elements);

        ArrayList<Integer> lengths = new ArrayList<Integer>();
        int index = 1;
        for (String line : elements)
        {
            lengths.add(line.length());
            Score score = objective.getScore(createScore(line));
            score.setScore(index);
            index++;
        }

        int max = Collections.max(lengths);
        Score score = objective.getScore(createLine(max));
        score.setScore(index);
        score = objective.getScore(createLine(max) + ChatColor.RED);
        score.setScore(0);

        character.player.setScoreboard(scoreboard);
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

    static String createScore(String input)
    {
        return ChatColor.BLUE + "| " + ChatColor.RESET + input;
    }
}