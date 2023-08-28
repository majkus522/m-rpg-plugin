package pl.majkus522.mrpg.commands;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import pl.majkus522.mrpg.common.classes.Character;
import pl.majkus522.mrpg.common.classes.CustomCommand;
import pl.majkus522.mrpg.common.classes.HttpBuilder;
import pl.majkus522.mrpg.common.enums.HttpMethod;
import pl.majkus522.mrpg.controllers.PlayersController;
import pl.majkus522.mrpg.controllers.ScoreboardController;
import pl.majkus522.mrpg.controllers.WorldController;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class CommandLogin extends CustomCommand
{
    @Override
    public void onPlayerExecute(Player player, String[] args)
    {
        if(PlayersController.isPlayerLogged(player))
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
        player.setGameMode(GameMode.SURVIVAL);
        if (player.isOp())
            player.setGameMode(GameMode.CREATIVE);
        player.teleport(new Location(WorldController.getWorld("worlds/main"), 0.5, 100, 0.5));
        Character character = new Character(player, request.getResultString());
        PlayersController.playerJoin(character);
        ScoreboardController.createScoreboard(character);
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
