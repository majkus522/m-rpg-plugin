package pl.majkus522.mrpg.commands;

import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import pl.majkus522.mrpg.common.ExtensionMethods;
import pl.majkus522.mrpg.common.classes.CustomCommand;
import pl.majkus522.mrpg.controllers.StatusController;

import java.util.ArrayList;
import java.util.List;

public class CommandStatus extends CustomCommand
{
    @Override
    public void onPlayerExecute(Player player, String[] args)
    {
        if(!ExtensionMethods.isPlayerLogged(player))
        {
            player.sendMessage("You must be logged in");
            return;
        }
        StatusController.sendPlayerStatus(player);
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
        return "status";
    }
}