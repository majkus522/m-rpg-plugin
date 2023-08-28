package pl.majkus522.mrpg.commands;

import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import pl.majkus522.mrpg.Config;
import pl.majkus522.mrpg.common.ExtensionMethods;
import pl.majkus522.mrpg.common.classes.CustomCommand;
import pl.majkus522.mrpg.controllers.PlayersController;

import java.util.ArrayList;
import java.util.List;

public class CommandShout extends CustomCommand
{
    @Override
    public void onPlayerExecute(Player player, String[] args)
    {
        if(!PlayersController.isPlayerLogged(player))
        {
            player.sendMessage("You must be logged in");
            return;
        }
        if(args.length == 0)
            return;
        StringBuilder message = new StringBuilder();
        for(String word : args)
        {
            message.append(word).append(" ");
        }
        ExtensionMethods.sendMessageToPlayers(ExtensionMethods.getPlayersInRange(player.getLocation(), Config.shoutRange), player.getName() + " > " + message);
    }

    @Override
    public void onTerminalExecute(ConsoleCommandSender console, String[] args)
    {
        console.sendMessage("Command can only be used by player");
    }

    @Override
    public List<String> autocomplete(Player player, String[] args)
    {
        return new ArrayList<>();
    }

    @Override
    public String getCommand()
    {
        return "shout";
    }
}
