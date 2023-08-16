package pl.majkus522.mrpg.controllers;

import com.google.gson.Gson;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import pl.majkus522.mrpg.common.ExtensionMethods;
import pl.majkus522.mrpg.common.classes.HttpBuilder;
import pl.majkus522.mrpg.common.classes.SkillData;
import pl.majkus522.mrpg.common.classes.api.RequestSkill;
import pl.majkus522.mrpg.common.enums.HttpMethod;
import pl.majkus522.mrpg.common.enums.SkillRarity;
import pl.majkus522.mrpg.common.interfaces.IRequestResult;

import java.util.ArrayList;
import java.util.List;

public class SkillsController
{
    public static void playerObtainSkill(Player player, String skill)
    {
        String body = "{\"player\": \"" + player.getName() + "\", \"skill\": \"" + skill + "\"}";
        HttpBuilder request = new HttpBuilder(HttpMethod.POST, "endpoints/skills").setSessionHeaders(player).setBody(body);
        if (!request.isOk())
            throw new RuntimeException(new Exception(request.getError().message));
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
        return new HttpBuilder(HttpMethod.GET, "endpoints/skills/" + player.getName() + "/" + skill).setSessionHeaders(player).isOk();
    }

    public static boolean playerHasSkill(Player player, SkillRarity rarity)
    {
        return new HttpBuilder(HttpMethod.HEAD, "endpoints/skills/" + player.getName() + "?rarity[]=" + rarity.toString()).setSessionHeaders(player).setHeader("Items", "0-1").isOk();
    }

    public static boolean playerHasSkillEnabled(Player player, String skill)
    {
        HttpBuilder request = new HttpBuilder(HttpMethod.GET, "endpoints/skills/" + player.getName() + "?toggle=true").setSessionHeaders(player);
        if(!request.isOk())
            return false;
        List<IRequestResult> skills = request.getResultAll(RequestSkill.class);
        for (IRequestResult element : skills)
        {
            if(((RequestSkill)element).skill.equals(skill))
                return true;
        }
        return false;
    }
}