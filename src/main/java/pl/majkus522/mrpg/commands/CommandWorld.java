package pl.majkus522.mrpg.commands;

import org.bukkit.Location;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import pl.majkus522.mrpg.common.classes.CustomCommand;
import pl.majkus522.mrpg.controllers.WorldController;

import java.util.ArrayList;
import java.util.List;

public class CommandWorld extends CustomCommand
{
    @Override
    public void onPlayerExecute(Player player, String[] args)
    {
        if (args.length < 1)
        {
            player.sendMessage("Enter world");
            return;
        }
        try
        {
            boolean isVoid = args.length >= 2 && args[1].equals("void");
            player.teleport(new Location(WorldController.getWorld("worlds/" + args[0], isVoid), 0, 400, 0));
        }
        catch (Exception e)
        {
            player.sendMessage("Incorrect world name");
            throw new RuntimeException(e);
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
        return new ArrayList<>();
    }

    @Override
    public String getCommand()
    {
        return "world";
    }
}
