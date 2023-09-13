package pl.majkus522.mrpg.controllers;

import com.google.gson.Gson;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import pl.majkus522.mrpg.common.ExtensionMethods;
import pl.majkus522.mrpg.common.classes.HttpBuilder;
import pl.majkus522.mrpg.common.classes.MySQL;
import pl.majkus522.mrpg.common.classes.SkillData;
import pl.majkus522.mrpg.common.classes.api.RequestSkill;
import pl.majkus522.mrpg.common.enums.HttpMethod;
import pl.majkus522.mrpg.common.enums.SkillRarity;
import pl.majkus522.mrpg.common.interfaces.IRequestResult;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SkillsController
{
    public static void playerObtainSkill(Player player, String skill)
    {
        try
        {
            PreparedStatement stmt = MySQL.getConnection().prepareStatement("insert into `skills` (`player`, `skill`) values ((select `id` from `players` where `username` = ?), ?)");
            stmt.setString(1, player.getName());
            stmt.setString(2, skill);
            stmt.executeUpdate();
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
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
        HttpBuilder request = new HttpBuilder(HttpMethod.GET, "endpoints/skills/" + player.getName() + "?toggle=true&search=" + skill).setSessionHeaders(player);
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
            String query = "delete from `skills` where `player` = (select `id` from `players` where `username` = ?) and (";
            ArrayList<String> params = new ArrayList<String>(Arrays.asList(player.getName()));
            boolean first = true;
            for(String element : data.evolution)
            {
                if (!first)
                    query += " or";
                query += " `skill` = ?";
                params.add(element);
            }
            try
            {
                PreparedStatement stmt = MySQL.getConnection().prepareStatement(query + ")");
                for (int index = 0; index < params.size(); index++)
                    stmt.setString(index + 1, params.get(index));
                stmt.executeUpdate();
            }
            catch (Exception e)
            {
                throw new RuntimeException(e);
            }
        }
        playerObtainSkill(player, skill);
    }
}