package pl.majkus522.mrpg.controllers;

import com.google.gson.Gson;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import pl.majkus522.mrpg.Main;
import pl.majkus522.mrpg.common.ExtensionMethods;
import pl.majkus522.mrpg.common.classes.SkillData;
import pl.majkus522.mrpg.common.classes.api.RequestResult;
import pl.majkus522.mrpg.common.classes.api.RequestSkill;
import pl.majkus522.mrpg.common.enums.SkillRarity;

import java.util.ArrayList;
import java.util.HashMap;

public class SkillsController
{
    public static void playerObtainSkill(Player player, String skill)
    {
        String body = "{\"player\": \"" + player.getName() + "\", \"skill\": \"" + skill + "\"}";
        RequestResult request = ExtensionMethods.httpRequest("POST", Main.mainUrl + "endpoints/skills", body, player);
        Gson gson = new Gson();
        if(request.isOk())
        {
            SkillData data = gson.fromJson(ExtensionMethods.readJsonFile("data/skills/" + skill + ".json"), SkillData.class);
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
        request.printError();
    }

    public static boolean playerHasSkill(Player player, String skill)
    {
        RequestResult request = ExtensionMethods.httpRequest("GET", Main.mainUrl + "endpoints/skills/" + player.getName() + "/" + skill, player);
        return request.isOk();
    }

    public static boolean playerHasSkill(Player player, SkillRarity rarity)
    {
        HashMap<String, String> headers = ExtensionMethods.getSessionHeaders(player);
        headers.put("Items", "0-1");
        RequestResult request = ExtensionMethods.httpRequest("HEAD", Main.mainUrl + "endpoints/skills/" + player.getName() + "?rarity[]=" + rarity.toString(), headers);
        return request.isOk();
    }

    public static boolean playerHasSkillEnabled(Player player, String skill)
    {
        RequestResult request = ExtensionMethods.httpRequest("GET", Main.mainUrl + "endpoints/skills/" + player.getName() + "?toggle=true", player);
        if(!request.isOk())
            return false;
        RequestSkill[] skills = new Gson().fromJson(request.content, RequestSkill[].class);
        for (RequestSkill element : skills)
        {
            if(element.skill.equals(skill))
                return true;
        }
        return false;
    }
}