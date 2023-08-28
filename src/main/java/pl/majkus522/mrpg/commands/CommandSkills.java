package pl.majkus522.mrpg.commands;

import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import pl.majkus522.mrpg.common.classes.CustomCommand;
import pl.majkus522.mrpg.common.enums.SkillRarity;
import pl.majkus522.mrpg.controllers.PlayersController;
import pl.majkus522.mrpg.guis.SkillsGui;

import java.util.ArrayList;
import java.util.List;

public class CommandSkills extends CustomCommand
{
    @Override
    public void onPlayerExecute(Player player, String[] args)
    {
        if(!PlayersController.isPlayerLogged(player))
        {
            player.sendMessage("You must be logged in");
            return;
        }
        if (args.length > 0)
        {
            SkillRarity rarity = SkillRarity.fromString(args[0]);
            if (rarity != null)
            {
                player.openInventory(new SkillsGui(player, rarity).getInventory());
                return;
            }
        }
        player.openInventory(new SkillsGui(player).getInventory());
    }

    @Override
    public void onTerminalExecute(ConsoleCommandSender console, String[] args)
    {
        console.sendMessage("Command can only be used by player");
    }

    @Override
    public List<String> autocomplete(Player player, String[] args)
    {
        ArrayList<String> list = new ArrayList<>();
        if (args.length == 1)
        {
            SkillRarity[] values = SkillRarity.values();
            for (SkillRarity element : values)
                list.add(element.toString());
        }
        return list;
    }

    @Override
    public String getCommand()
    {
        return "skills";
    }
}