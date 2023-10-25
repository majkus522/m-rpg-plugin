package pl.majkus522.mrpg.controllers;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;
import pl.majkus522.mrpg.common.ExtensionMethods;
import pl.majkus522.mrpg.common.classes.Character;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;

public class ScoreboardController
{
    public static void createScoreboard(Character character)
    {
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective objective = scoreboard.registerNewObjective("scoreboard", Criteria.DUMMY, "dummy");

        objective.setDisplayName(ChatColor.AQUA + "M-RPG");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        ArrayList<String> elements = new ArrayList<>();
        elements.add("Mana: " + character.getMana() + "/" + character.getMaxMana());
        elements.add("Money: " + character.getMoney() + "$");
        elements.add("Exp: " + character.getExp() + " / " + ExtensionMethods.levelExp(character.getLevel()));
        elements.add("Level: " + character.getLevel());

        ArrayList<Integer> lengths = new ArrayList<>();
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
        StringBuilder result = new StringBuilder(ChatColor.BLUE + "=");
        for(int index = 0; index < (length / 2); index++)
            result.append("-=");
        return result.toString();
    }

    static String createScore(String input)
    {
        return ChatColor.BLUE + "| " + ChatColor.RESET + input;
    }

    public static void updateLevel(Player player)
    {
        updateLevel(PlayersController.getCharacter(player));
    }

    public static void updateLevel(Character character)
    {
        Scoreboard scoreboard = character.player.getScoreboard();
        Objective objective = scoreboard.getObjective(DisplaySlot.SIDEBAR);
        Set<String> entries = scoreboard.getEntries();
        for (String line : entries)
        {
            if (line.contains("Exp"))
            {
                scoreboard.resetScores(line);
                Score score = objective.getScore(createScore("Exp: " + character.getExp() + " / " + ExtensionMethods.levelExp(character.getLevel())));
                score.setScore(2);
            }
            else if(line.contains("Level"))
            {
                scoreboard.resetScores(line);
                Score score = objective.getScore(createScore("Level: " + character.getLevel()));
                score.setScore(3);
            }
        }
    }

    public static void updateMoney(Player player)
    {
        updateMoney(PlayersController.getCharacter(player));
    }

    public static void updateMoney(Character character)
    {
        Scoreboard scoreboard = character.player.getScoreboard();
        Objective objective = scoreboard.getObjective(DisplaySlot.SIDEBAR);
        Set<String> entries = scoreboard.getEntries();
        for (String line : entries)
        {
            if (line.contains("Money"))
            {
                scoreboard.resetScores(line);
                Score score = objective.getScore(createScore("Money: " + character.getMoney() + "$"));
                score.setScore(1);
                return;
            }
        }
    }

    public static void updateMana(Player player)
    {
        updateMana(PlayersController.getCharacter(player));
    }

    public static void updateMana(Character character)
    {
        Scoreboard scoreboard = character.player.getScoreboard();
        Objective objective = scoreboard.getObjective(DisplaySlot.SIDEBAR);
        Set<String> entries = scoreboard.getEntries();
        for (String line : entries)
        {
            if (line.contains("Mana"))
            {
                scoreboard.resetScores(line);
                Score score = objective.getScore(createScore("Mana: " + character.getMana() + "/" + character.getMaxMana()));
                score.setScore(1);
                return;
            }
        }
    }

    public static void update(Character character)
    {
        updateMoney(character);
        updateLevel(character);
        updateMana(character);
    }
}