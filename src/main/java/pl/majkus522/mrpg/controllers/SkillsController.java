package pl.majkus522.mrpg.controllers;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import pl.majkus522.mrpg.Config;
import pl.majkus522.mrpg.common.ExtensionMethods;
import pl.majkus522.mrpg.common.classes.Character;
import pl.majkus522.mrpg.common.classes.data.SkillData;
import pl.majkus522.mrpg.common.enums.Rarity;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class SkillsController
{
    public static void playerObtainSkill(Player player, String skill)
    {
        playerObtainSkill(player, skill, true);
    }
    public static void playerObtainSkill(Player player, String skill, boolean message)
    {
        Character character = PlayersController.getCharacter(player);
        character.skills.add(new Character.CharacterSkill(skill, Character.CharacterSkill.Status.add));
        SkillData data = getSkillData(skill);
        ArrayList<Player> players;
        switch (data.rarity)
        {
            case common:
                if(message)
                    player.sendMessage("You obtained " + data.label + " skill");
                break;

            case extra:
                players = ExtensionMethods.getPlayersInRange(player.getLocation(), 25);
                players.remove(player);
                if(message)
                    player.sendMessage("You obtained " + ChatColor.GREEN + data.label + ChatColor.RESET + " skill");
                ExtensionMethods.sendMessageToPlayers(players, player.getName() + " obtained " + ChatColor.GREEN + data.label + ChatColor.RESET + " skill");
                break;

            case unique:
                players = ExtensionMethods.getPlayersInRange(player.getLocation(), 150);
                players.remove(player);
                if(message)
                    player.sendMessage("You obtained " + ChatColor.BLUE + data.label + ChatColor.RESET + " skill");
                ExtensionMethods.sendMessageToPlayers(players, player.getName() + " obtained " + ChatColor.BLUE + data.label + ChatColor.RESET + " skill");
                break;

            case ultimate:
                players = (ArrayList<Player>) player.getWorld().getPlayers();
                players.remove(player);
                if(message)
                    player.sendMessage("You obtained " + ChatColor.LIGHT_PURPLE + data.label + ChatColor.RESET + " skill");
                ExtensionMethods.sendMessageToPlayers(players, player.getName() + " obtained " + ChatColor.LIGHT_PURPLE + data.label + ChatColor.RESET + " skill");
                break;

            case unknown:
                players = (ArrayList<Player>) Bukkit.getOnlinePlayers();
                players.remove(player);
                if(message)
                    player.sendMessage("You obtained " + ChatColor.BLACK + data.label + ChatColor.RESET + " skill");
                ExtensionMethods.sendMessageToPlayers(players, "??? obtained " + ChatColor.BLACK + "???" + ChatColor.RESET + " skill");
                break;
        }
    }

    public static Character.CharacterSkill getSkill(Player player, String skill)
    {
        return PlayersController.getCharacter(player).skills.stream().filter(p -> p.skill.equals(skill)).collect(Collectors.toList()).get(0);
    }

    public static boolean playerHasSkill(Player player, String skill)
    {
        Character character = PlayersController.getCharacter(player);
        return character.skills.stream().anyMatch(p -> p.skill.equals(skill) && p.status != Character.CharacterSkill.Status.remove);
    }

    public static boolean playerHasSkill(Player player, Rarity rarity)
    {
        Character character = PlayersController.getCharacter(player);
        return character.skills.stream().anyMatch(p -> SkillsController.getSkillData(p.skill).rarity == rarity && p.status != Character.CharacterSkill.Status.remove);
    }

    public static boolean playerHasSkillEnabled(Player player, String skill)
    {
        Character character = PlayersController.getCharacter(player);
        return character.skills.stream().anyMatch(p -> p.skill.equals(skill) && p.toggle && p.status != Character.CharacterSkill.Status.remove);
    }

    public static void evolveSkill(Player player, String skill)
    {
        SkillData data = SkillsController.getSkillData(skill);
        for(String element : data.evolution)
        {
            if (!playerHasSkill(player, element))
            {
                player.sendMessage("You don't have required skills");
                return;
            }
        }
        StringBuilder message = new StringBuilder();
        boolean first = true;
        if (data.evolution.length > 0)
        {
            Character character = PlayersController.getCharacter(player);
            for(String element : data.evolution)
            {
                character.skills.stream().filter(p -> p.skill.equals(element)).collect(Collectors.toList()).get(0).status = Character.CharacterSkill.Status.remove;
                SkillData elementData = SkillsController.getSkillData(element);
                if (!first)
                    message.append(", ");
                message.append(elementData.rarity.getColor() + elementData.label + ChatColor.RESET);
                first = false;
            }
        }
        player.sendMessage((data.evolution.length == 1 ? "Skill " : "Skills ") + message + " evolved to " + data.rarity.getColor() + data.label);
        playerObtainSkill(player, skill, false);
    }

    public static void removeSkill(Player player, String skill)
    {
        Character character = PlayersController.getCharacter(player);
        character.skills.stream().filter(p -> p.skill.equals(skill)).collect(Collectors.toList()).get(0).status = Character.CharacterSkill.Status.remove;
        if(character.isSkillAssigned(skill))
        {
            for(int index = 0; index < Config.characterSkills; index++)
            {
                if(character.getAssignedSkill(index) == null)
                    continue;
                if(character.getAssignedSkill(index).equals(skill))
                {
                    character.assignSkill(null, index);
                    return;
                }
            }
        }
    }

    public static SkillData getSkillData(String skill)
    {
        return FilesController.readJsonFile("data/skills/" + skill, SkillData.class);
    }
}