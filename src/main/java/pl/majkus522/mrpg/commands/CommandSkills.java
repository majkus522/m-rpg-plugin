package pl.majkus522.mrpg.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.majkus522.mrpg.common.ExtensionMethods;
import pl.majkus522.mrpg.guis.SkillsGui;

public class CommandSkills implements CommandExecutor
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
        player.openInventory(new SkillsGui().getInventory());
        return true;
    }
}