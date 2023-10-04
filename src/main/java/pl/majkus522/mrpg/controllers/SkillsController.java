package pl.majkus522.mrpg.controllers;

import com.google.gson.Gson;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import pl.majkus522.mrpg.common.ExtensionMethods;
import pl.majkus522.mrpg.common.classes.Character;
import pl.majkus522.mrpg.common.classes.data.SkillData;
import pl.majkus522.mrpg.common.enums.SkillRarity;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class SkillsController
{
    public static void playerObtainSkill(Player player, String skill)
    {
        Character character = PlayersController.getCharacter(player);
        character.skills.add(new Character.CharacterSkill(skill, Character.CharacterSkill.Status.add));
        SkillData data = new Gson().fromJson(ExtensionMethods.readJsonFile("data/skills/" + skill + ".json"), SkillData.class);
        switch (data.rarity)
        {
            case common:
                player.sendMessage("You obtained " + data.label + " skill");
                break;

            case extra:
                ArrayList<Player> players = ExtensionMethods.getPlayersInRange(player.getLocation(), 25);
                players.remove(player);
                player.sendMessage("You obtained " + ChatColor.GREEN + data.label + ChatColor.RESET + " skill");
                ExtensionMethods.sendMessageToPlayers(players, player.getName() + " obtained " + ChatColor.GREEN + data.label + ChatColor.RESET + " skill");
                break;

            case unique:
                players = ExtensionMethods.getPlayersInRange(player.getLocation(), 150);
                players.remove(player);
                player.sendMessage("You obtained " + ChatColor.BLUE + data.label + ChatColor.RESET + " skill");
                ExtensionMethods.sendMessageToPlayers(players, player.getName() + " obtained " + ChatColor.BLUE + data.label + ChatColor.RESET + " skill");
                break;

            case ultimate:
                players = (ArrayList<Player>) player.getWorld().getPlayers();
                players.remove(player);
                player.sendMessage("You obtained " + ChatColor.LIGHT_PURPLE + data.label + ChatColor.RESET + " skill");
                ExtensionMethods.sendMessageToPlayers(players, player.getName() + " obtained " + ChatColor.LIGHT_PURPLE + data.label + ChatColor.RESET + " skill");
                break;

            case unknown:
                players = (ArrayList<Player>) Bukkit.getOnlinePlayers();
                players.remove(player);
                player.sendMessage("You obtained " + ChatColor.BLACK + data.label + ChatColor.RESET + " skill");
                ExtensionMethods.sendMessageToPlayers(players, "??? obtained " + ChatColor.BLACK + "???" + ChatColor.RESET + " skill");
                break;
        }
    }

    public static boolean playerHasSkill(Player player, String skill)
    {
        Character character = PlayersController.getCharacter(player);
        return character.skills.stream().filter(p -> p.skill.equals(skill) && p.status != Character.CharacterSkill.Status.remove).collect(Collectors.toList()).size() > 0;
    }

    public static boolean playerHasSkill(Player player, SkillRarity rarity)
    {
        Character character = PlayersController.getCharacter(player);
        Gson gson = new Gson();
        return character.skills.stream().filter(p -> gson.fromJson(ExtensionMethods.readJsonFile("data/skills/" + p.skill + ".json"), SkillData.class).rarity == rarity && p.status != Character.CharacterSkill.Status.remove).collect(Collectors.toList()).size() > 0;
    }

    public static boolean playerHasSkillEnabled(Player player, String skill)
    {
        Character character = PlayersController.getCharacter(player);
        return character.skills.stream().filter(p -> p.skill.equals(skill) && p.getToggle() && p.status != Character.CharacterSkill.Status.remove).collect(Collectors.toList()).size() > 0;
    }

    public static void evolveSkill(Player player, String skill)
    {
        SkillData data = new Gson().fromJson(ExtensionMethods.readJsonFile("data/skills/" + skill + ".json"), SkillData.class);
        boolean valid = true;
        for(String element : data.evolution)
        {
            if (!playerHasSkill(player, element))
            {
                valid = false;
                break;
            }
        }
        if (!valid)
        {
            player.sendMessage("You don't have requiered skills");
            return;
        }
        if (data.evolution.length > 0)
        {
            Character character = PlayersController.getCharacter(player);
            for(String element : data.evolution)
                character.skills.stream().filter(p -> p.skill.equals(element)).collect(Collectors.toList()).get(0).status = Character.CharacterSkill.Status.remove;
        }
        playerObtainSkill(player, skill);
    }
}