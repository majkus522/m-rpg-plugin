package pl.majkus522.mrpg.controllers;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import pl.majkus522.mrpg.common.ExtensionMethods;
import pl.majkus522.mrpg.common.classes.Character;
import pl.majkus522.mrpg.common.classes.HttpBuilder;
import pl.majkus522.mrpg.common.classes.PlayerStatus;
import pl.majkus522.mrpg.common.classes.api.RequestFakeStatus;
import pl.majkus522.mrpg.common.enums.HttpMethod;

public class StatusController
{
    public static void sendPlayerStatus(Player player)
    {
        if (!PlayersController.isPlayerLogged(player))
            return;
        Character character = PlayersController.getCharacter(player);
        player.sendMessage(ChatColor.BLUE + "=-=-=-=-= " + ChatColor.GREEN + "Status: " + player.getName() + ChatColor.BLUE + " =-=-=-=-=");
        player.sendMessage("Strength: " + character.getStr());
        player.sendMessage("Agility: " + character.getAgl());
        player.sendMessage("Charisma: " + character.getChr());
        player.sendMessage("Intelligence: " + character.getIntl());
        player.sendMessage("Defence: " + character.getDef());
        player.sendMessage("Vitality: " + character.getVtl());
        player.sendMessage("Dexterity: " + character.getDex());
        player.sendMessage(ChatColor.BLUE + "=-=-=-=-= " + ChatColor.GREEN + "Status: " + player.getName() + ChatColor.BLUE + " =-=-=-=-=");
    }

    public static void sendOtherPlayerStatus(Player sender, Player whose)
    {
        if (!(PlayersController.isPlayerLogged(sender) || PlayersController.isPlayerLogged(whose)))
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
            senderLevel = PlayersController.getCharacter(sender).getLevel();
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
            status = PlayersController.getCharacter(whose);
        }
        int round = -1;
        if(status.getLevel() - senderLevel > 5)
            round = -2;
        if (statusVision)
            round = 0;
        sender.sendMessage(ChatColor.BLUE + "=-=-=-=-= " + ChatColor.GREEN + "Status: " + whose.getName() + ChatColor.BLUE + " =-=-=-=-=");
        sender.sendMessage("Level: " + round(status.getLevel(), round));
        sender.sendMessage("Money: " + (round == 0 ? status.getMoney() : round(status.getMoney(), round)));
        sender.sendMessage("Strength: " + round(status.getStr(), round));
        sender.sendMessage("Agility: " + round(status.getAgl(), round));
        sender.sendMessage("Charisma: " + round(status.getChr(), round));
        sender.sendMessage("Intelligence: " + round(status.getIntl(), round));
        sender.sendMessage("Defence: " + round(status.getDef(), round));
        sender.sendMessage("Vitality: " + round(status.getVtl(), round));
        sender.sendMessage("Dexterity: " + round(status.getDex(), round));
        sender.sendMessage(ChatColor.BLUE + "=-=-=-=-= " + ChatColor.GREEN + "Status: " + whose.getName() + ChatColor.BLUE + " =-=-=-=-=");
    }

    static String round(double value, int decimalPlace)
    {
        return (decimalPlace < 0 ? "~" : "") + ExtensionMethods.roundInt(value, decimalPlace);
    }
}