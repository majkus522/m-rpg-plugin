package pl.majkus522.mrpg.commands;

import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import pl.majkus522.mrpg.Config;
import pl.majkus522.mrpg.common.ExtensionMethods;
import pl.majkus522.mrpg.common.classes.Character;
import pl.majkus522.mrpg.common.classes.CustomCommand;
import pl.majkus522.mrpg.controllers.PlayersController;
import pl.majkus522.mrpg.controllers.ScoreboardController;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class CommandPay extends CustomCommand
{
    @Override
    public void onPlayerExecute(Player sender, String[] args)
    {
        if(!PlayersController.isPlayerLogged(sender))
        {
            sender.sendMessage("You must be logged in");
            return;
        }
        if (args.length == 0)
        {
            sender.sendMessage("Enter player");
            return;
        }
        if (args.length == 1)
        {
            sender.sendMessage("Enter amount");
            return;
        }
        if (!Pattern.compile("-?\\d+(\\.\\d+)?").matcher(args[1]).matches())
        {
            sender.sendMessage("Amount should be a number");
            return;
        }
        if (sender.getName().equals(args[0]))
        {
            sender.sendMessage("You can't pay yourself");
            return;
        }
        for (Player player : ExtensionMethods.getPlayersInRange(sender.getLocation(), Config.payRange))
        {
            if (player.getName().equals(args[0]))
            {
                float amount = (float)ExtensionMethods.round(Math.abs(Float.parseFloat(args[1])), 2);
                Character senderCharacter = PlayersController.getCharacter(sender);
                if (!senderCharacter.hasMoney(amount))
                {
                    sender.sendMessage("You don't have enough money");
                    return;
                }
                senderCharacter.addMoney(-amount);
                PlayersController.getCharacter(player).addMoney(amount);
                ScoreboardController.updateMoney(senderCharacter);
                ScoreboardController.updateMoney(player);
                sender.sendMessage("You give " + player.getName() + " " + amount + "$");
                player.sendMessage(sender.getName() + " give you " + amount + "$");
                return;
            }
        }
        sender.sendMessage("Player is to far away");
    }

    @Override
    public void onTerminalExecute(ConsoleCommandSender console, String[] args)
    {
        console.sendMessage("Command can only be used by player");
    }

    @Override
    public List<String> autocomplete(Player player, String[] args)
    {
        ArrayList<String> list = new ArrayList<>();
        if (args.length == 1)
        {
            for (Player element : ExtensionMethods.getPlayersInRange(player.getLocation(), Config.payRange))
                list.add(element.getName());
            list.remove(player.getName());
        }
        return list;
    }

    @Override
    public String getCommand()
    {
        return "pay";
    }
}