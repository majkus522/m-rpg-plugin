package pl.majkus522.mrpg.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.majkus522.mrpg.Config;
import pl.majkus522.mrpg.common.ExtensionMethods;

public class CommandShout implements CommandExecutor
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
        if(args.length == 0)
        {
            player.sendMessage("Enter your message");
            return true;
        }
        StringBuilder message = new StringBuilder();
        for(String word : args)
        {
            message.append(word).append(" ");
        }
        ExtensionMethods.sendMessageToPlayers(ExtensionMethods.getPlayersInRange(player.getLocation(), Config.shoutRange), player.getName() + " > " + message);
        return true;
    }
}
