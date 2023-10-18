package pl.majkus522.mrpg.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import pl.majkus522.mrpg.Main;
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
            player.sendMessage("Current world: " + player.getWorld().getName());
            return;
        }
        try
        {
            boolean isVoid = args.length >= 2 && args[1].equals("void");
            player.teleport(WorldController.getWorld("worlds/" + args[0], isVoid).getSpawnLocation());
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
        if (args.length < 1)
        {
            console.sendMessage("Enter world name");
            return;
        }
        Bukkit.getScheduler().runTask(Main.plugin, new Runnable()
        {
            @Override
            public void run()
            {
                console.sendMessage("Generating world");
                try
                {
                    boolean isVoid = args.length >= 2 && args[1].equals("void");
                    WorldController.getWorld("worlds/" + args[0], isVoid);
                }
                catch (Exception e)
                {
                    console.sendMessage("Incorrect world name");
                    throw new RuntimeException(e);
                }
                console.sendMessage("World generation complete");
            }
        });
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
