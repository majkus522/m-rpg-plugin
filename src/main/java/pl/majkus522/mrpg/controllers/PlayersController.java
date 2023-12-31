package pl.majkus522.mrpg.controllers;

import org.bukkit.entity.Player;
import pl.majkus522.mrpg.common.classes.Character;

import java.util.HashMap;

public class PlayersController
{
    public static HashMap<String, Character> players = new HashMap<>();

    public static Character getCharacter(Player player)
    {
        return players.get(player.getName());
    }

    public static void playerJoin(Character character)
    {
        players.put(character.player.getName(), character);
    }

    public static void playerLeave(Player player)
    {
        if (isPlayerLogged(player))
            players.remove(player.getName()).playerLeave();
    }

    public static boolean isPlayerLogged(Player player)
    {
        return players.containsKey(player.getName());
    }
}