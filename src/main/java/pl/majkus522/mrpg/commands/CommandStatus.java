package pl.majkus522.mrpg.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import pl.majkus522.mrpg.Config;
import pl.majkus522.mrpg.common.ExtensionMethods;
import pl.majkus522.mrpg.common.classes.CustomCommand;
import pl.majkus522.mrpg.controllers.PlayersController;
import pl.majkus522.mrpg.controllers.StatusController;

import java.util.ArrayList;
import java.util.List;

public class CommandStatus extends CustomCommand
{
    @Override
    public void onPlayerExecute(Player player, String[] args)
    {
        if(!PlayersController.isPlayerLogged(player))
        {
            player.sendMessage("You must be logged in");
            return;
        }
        if (args.length == 0 || player.getName().equals(args[0]))
            StatusController.sendPlayerStatus(player);
        else
        {
            ArrayList<Player> players = ExtensionMethods.getPlayersInRange(player.getLocation(), Config.statusRange);
            Player whose = Bukkit.getPlayerExact(args[0]);
            if (whose == null)
            {
                player.sendMessage("Player doesn't exists");
                return;
            }
            if (!players.contains(whose))
            {
                player.sendMessage("Player is to far away");
                return;
            }
            StatusController.sendOtherPlayerStatus(player, whose);
        }
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
            for (Player element : ExtensionMethods.getPlayersInRange(player.getLocation(), Config.statusRange))
                list.add(element.getName());
            list.remove(player.getName());
        }
        return list;
    }

    @Override
    public String getCommand()
    {
        return "status";
    }
}