package pl.majkus522.mrpg.commands;

import com.google.gson.Gson;
import org.apache.commons.lang3.math.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import pl.majkus522.mrpg.common.ExtensionMethods;
import pl.majkus522.mrpg.common.classes.CustomCommand;
import pl.majkus522.mrpg.common.classes.data.ItemData;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CommandItem extends CustomCommand
{
    @Override
    public void onPlayerExecute(Player player, String[] args)
    {
        ItemStack item = null;
        if(args.length >= 1)
        {
            if (!new File("data/items/" + args[0] + ".json").exists())
            {
                player.sendMessage("Item doesn't exists");
                return;
            }
            item = new Gson().fromJson(ExtensionMethods.readJsonFile("data/items/" + args[0] + ".json"), ItemData.class).toItem();
        }
        if (args.length >= 2)
        {
            if (!NumberUtils.isParsable(args[1]))
            {
                player.sendMessage("Incorrect number");
                return;
            }
            int amount = Integer.parseInt(args[1]);
            if (amount < 1)
            {
                player.sendMessage("Incorrect item amount");
                return;
            }
            item.setAmount(amount);
        }
        player.getInventory().addItem(item);
    }

    @Override
    public void onTerminalExecute(ConsoleCommandSender console, String[] args)
    {
        ItemStack item = null;
        Player player = null;
        if(args.length >= 1)
        {
            if (!new File("data/items/" + args[0] + ".json").exists())
            {
                console.sendMessage("Item doesn't exists");
                return;
            }
            item = new Gson().fromJson(ExtensionMethods.readJsonFile("data/items/" + args[0] + ".json"), ItemData.class).toItem();
        }
        if (args.length >= 2)
        {
            player = Bukkit.getPlayer(args[1]);
            if (player == null)
            {
                console.sendMessage("Player doesn't exists");
                return;
            }
        }
        if (args.length >= 3)
        {
            if (!NumberUtils.isParsable(args[2]))
            {
                console.sendMessage("Incorrect number");
                return;
            }
            int amount = Integer.parseInt(args[2]);
            if (amount < 1)
            {
                player.sendMessage("Incorrect item amount");
                return;
            }
            item.setAmount(amount);
        }
        player.getInventory().addItem(item);
    }

    @Override
    public List<String> autocomplete(Player player, String[] args)
    {
        ArrayList<String> list = new ArrayList<>();
        if (args.length == 1)
        {
            File[] files = ExtensionMethods.scanDir("data/items/");
            for (File file : files)
                list.add(file.getName().replace(".json", ""));
        }
        return list;
    }

    @Override
    public String getCommand()
    {
        return "item";
    }
}