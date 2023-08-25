package pl.majkus522.mrpg.commands;

import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import pl.majkus522.mrpg.Main;
import pl.majkus522.mrpg.common.ExtensionMethods;
import pl.majkus522.mrpg.common.classes.Character;
import pl.majkus522.mrpg.common.classes.CustomCommand;
import pl.majkus522.mrpg.common.classes.HttpBuilder;
import pl.majkus522.mrpg.common.enums.HttpMethod;
import pl.majkus522.mrpg.controllers.ScoreboardController;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class CommandLogin extends CustomCommand
{
    @Override
    public void onPlayerExecute(Player player, String[] args)
    {
        if(ExtensionMethods.isPlayerLogged(player))
        {
            player.sendMessage("You are already logged in");
            return;
        }
        if(args.length != 1)
        {
            player.sendMessage("Enter password");
            return;
        }
        HttpBuilder request = new HttpBuilder(HttpMethod.GET, "endpoints/players/" + player.getName() + "/logged").setHeader("Session-Type", "game").setHeader("Password", Base64.getEncoder().encodeToString(args[0].getBytes()));
        if(!request.isOk())
        {
            player.sendMessage(request.getError().message);
            return;
        }
        player.sendMessage("Logged in");
        Main.players.put(player.getName(), new Character(player, request.getResultString()));
        player.setScoreboard(ScoreboardController.createScoreboard(player));
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
        return "login";
    }
}
