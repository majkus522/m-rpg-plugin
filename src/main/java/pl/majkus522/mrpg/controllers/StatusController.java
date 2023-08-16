package pl.majkus522.mrpg.controllers;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import pl.majkus522.mrpg.common.ExtensionMethods;
import pl.majkus522.mrpg.common.classes.HttpBuilder;
import pl.majkus522.mrpg.common.classes.PlayerStatus;
import pl.majkus522.mrpg.common.classes.api.RequestFakeStatus;
import pl.majkus522.mrpg.common.classes.api.RequestPlayer;
import pl.majkus522.mrpg.common.enums.HttpMethod;

public class StatusController
{
    public static void sendPlayerStatus(Player player)
    {
        if (!ExtensionMethods.isPlayerLogged(player))
            return;

        HttpBuilder request = new HttpBuilder(HttpMethod.GET, "endpoints/players/" + player.getName()).setSessionHeaders(player);
        if(!request.isOk())
            throw new RuntimeException(new Exception(request.getError().message));
        RequestPlayer playerData = (RequestPlayer) request.getResult(RequestPlayer.class);
        player.sendMessage(ChatColor.BLUE + "=-=-=-=-= " + ChatColor.GREEN + "Status: " + player.getName() + ChatColor.BLUE + " =-=-=-=-=");
        player.sendMessage("Strength: " + playerData.str);
        player.sendMessage("Agility: " + playerData.agl);
        player.sendMessage("Charisma: " + playerData.chr);
        player.sendMessage("Intelligence: " + playerData.intl);
        player.sendMessage(ChatColor.BLUE + "=-=-=-=-= " + ChatColor.GREEN + "Status: " + player.getName() + ChatColor.BLUE + " =-=-=-=-=");
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

        boolean statusVision = SkillsController.playerHasSkill(sender, "statusVision");
        int senderLevel = -1;
        if(!statusVision)
        {
            HttpBuilder request = new HttpBuilder(HttpMethod.GET, "endpoints/players/" + sender.getName()).setSessionHeaders(sender);
            if(!request.isOk())
            {
                sender.sendMessage("Server error");
                throw new RuntimeException(new Exception(request.getError().message));
            }
            senderLevel = ((RequestPlayer)request.getResult(RequestPlayer.class)).level;
        }
        boolean statusFake = statusVision ? false : SkillsController.playerHasSkill(whose, "statusFake");
        PlayerStatus status;
        if(statusFake)
        {
            HttpBuilder request = new HttpBuilder(HttpMethod.GET, "endpoints/fake-status/" + whose.getName()).setSessionHeaders(whose);
            if(!request.isOk())
            {
                sender.sendMessage("Server error");
                throw new RuntimeException(new Exception(request.getError().message));
            }
            status = (RequestFakeStatus)request.getResult(RequestFakeStatus.class);
        }
        else
        {
            HttpBuilder request = new HttpBuilder(HttpMethod.GET, "endpoints/players/" + whose.getName()).setSessionHeaders(whose);
            if(!request.isOk())
            {
                sender.sendMessage("Server error");
                throw new RuntimeException(new Exception(request.getError().message));
            }
            status = (RequestPlayer)request.getResult(RequestPlayer.class);
        }
        int round = -1;
        if(status.level - senderLevel > 5)
            round = -2;
        if (statusVision)
            round = 0;
        sender.sendMessage(ChatColor.BLUE + "=-=-=-=-= " + ChatColor.GREEN + "Status: " + whose.getName() + ChatColor.BLUE + " =-=-=-=-=");
        sender.sendMessage("Level: " + round(status.level, round));
        sender.sendMessage("Money: " + (round == 0 ? status.money : round(status.money, round)));
        sender.sendMessage("Strength: " + round(status.str, round));
        sender.sendMessage("Agility: " + round(status.agl, round));
        sender.sendMessage("Charisma: " + round(status.chr, round));
        sender.sendMessage("Intelligence: " + round(status.intl, round));
        sender.sendMessage(ChatColor.BLUE + "=-=-=-=-= " + ChatColor.GREEN + "Status: " + whose.getName() + ChatColor.BLUE + " =-=-=-=-=");
    }

    static String round(double value, int decimalPlace)
    {
        return (decimalPlace < 0 ? "~" : "") + ExtensionMethods.roundInt(value, decimalPlace);
    }
}