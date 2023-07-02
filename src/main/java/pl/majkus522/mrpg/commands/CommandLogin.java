package pl.majkus522.mrpg.commands;

import com.google.gson.Gson;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.majkus522.mrpg.Main;
import pl.majkus522.mrpg.common.ExtensionMethods;
import pl.majkus522.mrpg.common.api.RequestErrorResult;
import pl.majkus522.mrpg.common.api.RequestResult;

import java.util.Base64;
import java.util.HashMap;

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
        if(!Main.unloggedPlayers.contains(player.getName()))
        {
            player.sendMessage("You are already logged in");
            return true;
        }
        if(args.length != 1)
        {
            player.sendMessage("Enter password");
            return true;
        }
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Password", Base64.getEncoder().encodeToString(args[0].getBytes()));
        RequestResult request = ExtensionMethods.httpRequest("GET", Main.mainUrl + "endpoints/players/" + player.getName() + "/logged", headers);
        if(!request.isOk())
        {
            Gson gson = new Gson();
            player.sendMessage(gson.fromJson(request.content, RequestErrorResult.class).message);
        }
        player.sendMessage("Logged in");
        Main.unloggedPlayers.remove(player.getName());
        return true;
    }
}
