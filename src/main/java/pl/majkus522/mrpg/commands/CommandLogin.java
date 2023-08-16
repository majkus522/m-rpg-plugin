package pl.majkus522.mrpg.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.majkus522.mrpg.Main;
import pl.majkus522.mrpg.common.ExtensionMethods;
import pl.majkus522.mrpg.common.classes.HttpBuilder;
import pl.majkus522.mrpg.common.enums.HttpMethod;
import pl.majkus522.mrpg.controllers.ScoreboardController;

import java.util.Base64;

public class CommandLogin implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String string, String[] args)
    {
        if(!(sender instanceof Player))
        {
            sender.sendMessage("Command can only be used by player");
            return true;
        }
        Player player = (Player) sender;
        if(ExtensionMethods.isPlayerLogged(player))
        {
            player.sendMessage("You are already logged in");
            return true;
        }
        if(args.length != 1)
        {
            player.sendMessage("Enter password");
            return true;
        }
        HttpBuilder request = new HttpBuilder(HttpMethod.GET, "endpoints/players/" + player.getName() + "/logged").setHeader("Session-Type", "game").setHeader("Password", Base64.getEncoder().encodeToString(args[0].getBytes()));
        if(!request.isOk())
        {
            player.sendMessage(request.getError().message);
            return true;
        }
        player.sendMessage("Logged in");
        Main.playersSessions.put(player.getName(), request.getResultString());
        player.setScoreboard(ScoreboardController.createScoreboard(player));
        return true;
    }
}
