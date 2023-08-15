package pl.majkus522.mrpg.controllers;

import com.google.gson.Gson;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import pl.majkus522.mrpg.Main;
import pl.majkus522.mrpg.common.ExtensionMethods;
import pl.majkus522.mrpg.common.classes.PlayerStatus;
import pl.majkus522.mrpg.common.classes.api.RequestErrorResult;
import pl.majkus522.mrpg.common.classes.api.RequestFakeStatus;
import pl.majkus522.mrpg.common.classes.api.RequestPlayer;
import pl.majkus522.mrpg.common.classes.api.RequestResult;

public class StatusController
{
    public static void sendPlayerStatus(Player player)
    {
        if (!ExtensionMethods.isPlayerLogged(player))
            return;

        RequestResult request = ExtensionMethods.httpRequest("GET", "endpoints/players/" + player.getName(), player);
        Gson gson = new Gson();
        if(request.isOk())
        {
            RequestPlayer playerData = gson.fromJson(request.content, RequestPlayer.class);
            player.sendMessage(ChatColor.BLUE + "=-=-=-=-= " + ChatColor.GREEN + "Status: " + player.getName() + ChatColor.BLUE + " =-=-=-=-=");
            player.sendMessage("Strength: " + playerData.strength);
            player.sendMessage("Agility: " + playerData.agility);
            player.sendMessage("Charisma: " + playerData.charisma);
            player.sendMessage("Intelligence: " + playerData.intelligence);
            player.sendMessage(ChatColor.BLUE + "=-=-=-=-= " + ChatColor.GREEN + "Status: " + player.getName() + ChatColor.BLUE + " =-=-=-=-=");
        }
        else
        {
            player.sendMessage(gson.fromJson(request.content, RequestErrorResult.class).message);
        }
    }

    public static void sendOtherPlayerStatus(Player sender, Player whose)
    {
        if (!(ExtensionMethods.isPlayerLogged(sender) || ExtensionMethods.isPlayerLogged(whose)))
            return;

        if(SkillsController.playerHasSkillEnabled(whose, "statusHide") && !SkillsController.playerHasSkill(sender, "statusVision"))
        {
            sender.sendMessage("You can't see this player status");
            return;
        }

        Gson gson = new Gson();
        boolean statusVision = SkillsController.playerHasSkill(sender, "statusVision");
        int senderLevel = -1;
        if(!statusVision)
        {
            RequestResult request = ExtensionMethods.httpRequest("GET", "endpoints/players/" + sender.getName(), sender);
            if(!request.isOk())
            {
                request.printError();
                sender.sendMessage("Server error");
                return;
            }
            senderLevel = gson.fromJson(request.content, RequestPlayer.class).level;
        }
        boolean statusFake = statusVision ? false : SkillsController.playerHasSkill(whose, "statusFake");
        PlayerStatus status;
        if(statusFake)
        {
            RequestResult request = ExtensionMethods.httpRequest("GET", "endpoints/fake-status/" + whose.getName(), whose);
            if(!request.isOk())
            {
                request.printError();
                sender.sendMessage("Server error");
                return;
            }
            status = gson.fromJson(request.content, RequestFakeStatus.class);
        }
        else
        {
            RequestResult request = ExtensionMethods.httpRequest("GET", "endpoints/players/" + whose.getName(), whose);
            if(!request.isOk())
            {
                request.printError();
                sender.sendMessage("Server error");
                return;
            }
            status = gson.fromJson(request.content, RequestPlayer.class);
        }
        int round = -1;
        if(status.level - senderLevel > 5)
            round = -2;
        if (statusVision)
            round = 0;
        sender.sendMessage(ChatColor.BLUE + "=-=-=-=-= " + ChatColor.GREEN + "Status: " + whose.getName() + ChatColor.BLUE + " =-=-=-=-=");
        sender.sendMessage("Level: " + round(status.level, round));
        sender.sendMessage("Money: " + (round == 0 ? status.money : round(status.money, round)));
        sender.sendMessage("Strength: " + round(status.strength, round));
        sender.sendMessage("Agility: " + round(status.agility, round));
        sender.sendMessage("Charisma: " + round(status.charisma, round));
        sender.sendMessage("Intelligence: " + round(status.intelligence, round));
        sender.sendMessage(ChatColor.BLUE + "=-=-=-=-= " + ChatColor.GREEN + "Status: " + whose.getName() + ChatColor.BLUE + " =-=-=-=-=");
    }

    static String round(double value, int decimalPlace)
    {
        return (decimalPlace < 0 ? "~" : "") + ExtensionMethods.roundInt(value, decimalPlace);
    }
}