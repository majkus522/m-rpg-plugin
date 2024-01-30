package pl.majkus522.mrpg.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import pl.majkus522.mrpg.common.classes.CustomCommand;
import pl.majkus522.mrpg.common.enums.Rarity;
import pl.majkus522.mrpg.controllers.FilesController;
import pl.majkus522.mrpg.controllers.PlayersController;
import pl.majkus522.mrpg.controllers.SkillsController;
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
            Rarity rarity = Rarity.fromString(args[0]);
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
        if(args.length == 0)
        {
            console.sendMessage("Enter skill");
            return;
        }
        else if(args.length == 1)
        {
            console.sendMessage("Enter option");
            return;
        }
        else if(args.length == 2)
        {
            console.sendMessage("Enter player");
            return;
        }
        if(!FilesController.fileExists("data/skills/" + args[0] + ".json"))
        {
            console.sendMessage("Skill doesn't exists");
            return;
        }
        Player player = Bukkit.getPlayer(args[2]);
        if(player == null)
        {
            console.sendMessage("Player doesn't exists");
            return;
        }
        switch (args[1])
        {
            case "add":
                SkillsController.playerObtainSkill(player, args[0], false);
                console.sendMessage("Skill has been added");
                break;

            case "remove":
                SkillsController.removeSkill(player, args[0]);
                console.sendMessage("Skill has been removed");
                break;

            default:
                console.sendMessage("Unknown option");
                break;
        }
    }

    @Override
    public List<String> autocomplete(Player player, String[] args)
    {
        ArrayList<String> list = new ArrayList<>();
        if (args.length == 1)
        {
            Rarity[] values = Rarity.values();
            for (Rarity element : values)
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