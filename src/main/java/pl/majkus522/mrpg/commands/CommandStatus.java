package pl.majkus522.mrpg.commands;

import com.google.gson.Gson;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.majkus522.mrpg.Main;
import pl.majkus522.mrpg.common.ExtensionMethods;
import pl.majkus522.mrpg.common.classes.api.RequestErrorResult;
import pl.majkus522.mrpg.common.classes.api.RequestPlayer;
import pl.majkus522.mrpg.common.classes.api.RequestResult;

import java.util.HashMap;

public class CommandStatus implements CommandExecutor
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
        if(!ExtensionMethods.isPlayerLogged(player))
        {
            player.sendMessage("You must be logged in");
            return true;
        }
        RequestResult request = ExtensionMethods.httpRequest("GET", Main.mainUrl + "endpoints/players/" + player.getName(), ExtensionMethods.getSessionHeaders(player));
        Gson gson = new Gson();
        if(request.isOk())
        {
            RequestPlayer playerData = gson.fromJson(request.content, RequestPlayer.class);
            player.sendMessage(ChatColor.BLUE + "=-=-=-=-= " + ChatColor.GREEN + "Status: " + player.getName() + ChatColor.BLUE + " =-=-=-=-=");
            player.sendMessage("Strength: " + playerData.strength);
            player.sendMessage("Agility: " + playerData.agility);
            player.sendMessage("Charisma: " + playerData.charisma);
            player.sendMessage("Intelligence: " + playerData.intelligence);
            player.sendMessage(ChatColor.BLUE + "=-=-=-=-= " + ChatColor.GREEN + "Status: " + player.getName() + ChatColor.BLUE + " =-=-=-=-=");
        }
        else
        {
            player.sendMessage(gson.fromJson(request.content, RequestErrorResult.class).message);
        }
        return true;
    }
}