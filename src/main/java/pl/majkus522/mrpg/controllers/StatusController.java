package pl.majkus522.mrpg.controllers;

import com.google.gson.Gson;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import pl.majkus522.mrpg.Main;
import pl.majkus522.mrpg.common.ExtensionMethods;
import pl.majkus522.mrpg.common.classes.api.RequestErrorResult;
import pl.majkus522.mrpg.common.classes.api.RequestPlayer;
import pl.majkus522.mrpg.common.classes.api.RequestResult;

public class StatusController
{
    public static void sendPlayerStatus(Player player)
    {
        if (!ExtensionMethods.isPlayerLogged(player))
            return;

        RequestResult request = ExtensionMethods.httpRequest("GET", Main.mainUrl + "endpoints/players/" + player.getName(), player);
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

        RequestResult senderRequest = ExtensionMethods.httpRequest("GET", Main.mainUrl + "endpoints/players/" + sender.getName(), sender);
        if(!senderRequest.isOk())
        {
            senderRequest.printError();
            sender.sendMessage("Server error");
            return;
        }
        RequestResult whoseRequest = ExtensionMethods.httpRequest("GET", Main.mainUrl + "endpoints/players/" + whose.getName(), whose);
        if(!whoseRequest.isOk())
        {
            whoseRequest.printError();
            sender.sendMessage("Server error");
            return;
        }
        Gson gson = new Gson();
        int level = gson.fromJson(senderRequest.content, RequestPlayer.class).level;
        RequestPlayer playerData = gson.fromJson(whoseRequest.content, RequestPlayer.class);
        int round = -1;
        if(playerData.level - level > 5)
            round = -2;
        if (SkillsController.playerHasSkill(sender, "statusVision"))
            round = 0;
        sender.sendMessage(ChatColor.BLUE + "=-=-=-=-= " + ChatColor.GREEN + "Status: " + whose.getName() + ChatColor.BLUE + " =-=-=-=-=");
        sender.sendMessage("Level: " + round(playerData.level, round));
        sender.sendMessage("Money: " + (round == 0 ? playerData.money : round(playerData.money, round)));
        sender.sendMessage("Strength: " + round(playerData.strength, round));
        sender.sendMessage("Agility: " + round(playerData.agility, round));
        sender.sendMessage("Charisma: " + round(playerData.charisma, round));
        sender.sendMessage("Intelligence: " + round(playerData.intelligence, round));
        sender.sendMessage(ChatColor.BLUE + "=-=-=-=-= " + ChatColor.GREEN + "Status: " + whose.getName() + ChatColor.BLUE + " =-=-=-=-=");
    }

    static String round(double value, int decimalPlace)
    {
        return (decimalPlace < 0 ? "~" : "") + ExtensionMethods.roundInt(value, decimalPlace);
    }
}