package pl.majkus522.mrpg.commands;

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
        console.sendMessage("Command can only be used by player");
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
