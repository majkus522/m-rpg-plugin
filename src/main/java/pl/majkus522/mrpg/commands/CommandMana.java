package pl.majkus522.mrpg.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import pl.majkus522.mrpg.common.ExtensionMethods;
import pl.majkus522.mrpg.common.classes.Character;
import pl.majkus522.mrpg.common.classes.CustomCommand;
import pl.majkus522.mrpg.controllers.ManaController;
import pl.majkus522.mrpg.controllers.PlayersController;
import pl.majkus522.mrpg.controllers.SkillsController;

import java.util.ArrayList;
import java.util.List;

public class CommandMana extends CustomCommand
{
    @Override
    public void onPlayerExecute(Player player, String[] args)
    {
        if (args.length >= 1)
        {
            switch (args[0])
            {
                case "burst":
                    int amount = ExtensionMethods.randomRange(10, 90);
                    Character character = PlayersController.getCharacter(player);
                    amount = character.getMana() * amount / 100;
                    character.useMana(amount);
                    ManaController.addMana(player.getLocation(), amount);
                    break;

                case "gather":
                    if (!SkillsController.playerHasSkill(player, "manaGathering"))
                    {
                        player.sendMessage("You can't gather mana");
                        return;
                    }
                    ManaController.gatherMana(player);
                    break;
            }
        }
    }

    @Override
    public void onTerminalExecute(ConsoleCommandSender console, String[] args)
    {
        if (args.length == 0)
        {
            console.sendMessage("Enter player");
            return;
        }
        if (args.length == 1)
        {
            console.sendMessage("Enter option");
            return;
        }
        if (args.length == 2)
        {
            console.sendMessage("Enter amount");
            return;
        }
        Player player = Bukkit.getPlayer(args[0]);
        if (player == null)
        {
            console.sendMessage("Player doesn't exists");
            return;
        }
        Character character = PlayersController.getCharacter(player);
        if (character == null)
        {
            console.sendMessage("Player isn't logged in");
            return;
        }
        int amount;
        try
        {
            amount = Integer.parseInt(args[2]);
        }
        catch (Exception e)
        {
            console.sendMessage("Incorrect number");
            return;
        }
        switch (args[1].toLowerCase())
        {
            case "add":
                character.addMana(amount);
                console.sendMessage("Added mana to player");
                break;

            case "remove":
                character.useMana(amount);
                console.sendMessage("Removed mana from player");
                break;

            default:
                console.sendMessage("Incorrect option");
                break;
        }
    }

    @Override
    public List<String> autocomplete(Player player, String[] args)
    {
        ArrayList<String> list = new ArrayList<>();
        list.add("burst");
        if (SkillsController.playerHasSkill(player, "manaGathering"))
            list.add("gather");
        return list;
    }

    @Override
    public String getCommand()
    {
        return "mana";
    }
}
