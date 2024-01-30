package pl.majkus522.mrpg.commands;

import org.apache.commons.lang3.math.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import pl.majkus522.mrpg.common.classes.CustomCommand;
import pl.majkus522.mrpg.common.classes.data.EquipmentData;
import pl.majkus522.mrpg.common.classes.data.ItemData;
import pl.majkus522.mrpg.controllers.FilesController;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CommandItem extends CustomCommand
{
    @Override
    public void onPlayerExecute(Player player, String[] args)
    {
        if(args.length == 0)
        {
            player.sendMessage("Enter item type");
            return;
        }
        else if(args.length == 1)
        {
            player.sendMessage("Enter item");
            return;
        }
        if (!(FilesController.fileExists("data/" + args[0] + "/" + args[1] + ".json")))
        {
            player.sendMessage("Item doesn't exists");
            return;
        }
        ItemStack item = getItem(args[0], args[1]);
        if (args.length >= 3)
            item.setAmount(parseNumber(args[1], player));
        player.getInventory().addItem(item);
    }

    @Override
    public void onTerminalExecute(ConsoleCommandSender console, String[] args)
    {
        if (args.length == 0)
        {
            console.sendMessage("Enter player");
            return;
        }
        else if(args.length == 1)
        {
            console.sendMessage("Enter item type");
            return;
        }
        else if(args.length == 2)
        {
            console.sendMessage("Enter item");
            return;
        }
        if (!FilesController.fileExists("data/" + args[1] + "/" + args[2] + ".json"))
        {
            console.sendMessage("Item doesn't exists");
            return;
        }
        ItemStack item = getItem(args[1], args[2]);
        Player player = Bukkit.getPlayer(args[0]);
        if (player == null)
        {
            console.sendMessage("Player doesn't exists");
            return;
        }
        if (args.length >= 4)
            item.setAmount(parseNumber(args[2], console));
        if (item.getAmount() < 1)
            return;
        console.sendMessage("Added " + item.getAmount() + " items to " + player.getName());
        player.getInventory().addItem(item);
    }

    @Override
    public List<String> autocomplete(Player player, String[] args)
    {
        ArrayList<String> list = new ArrayList<>();
        if (args.length == 1)
        {
            list.add("items");
            list.add("equipment");
        }
        else if (args.length == 2)
        {
            File[] files = FilesController.scanDir("data/" + args[0] + "/");
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

    int parseNumber(String text, CommandSender sender)
    {
        if (!NumberUtils.isParsable(text))
        {
            sender.sendMessage("Incorrect item amount");
            return 0;
        }
        int amount = Integer.parseInt(text);
        if (amount < 1)
        {
            sender.sendMessage("Incorrect item amount");
            return 0;
        }
        return amount;
    }

    ItemStack getItem(String type, String item)
    {
        switch (type)
        {
            case "items":
                return FilesController.readJsonFile("data/items/" + item, ItemData.class).toItem();

            case "equipment":
                return FilesController.readJsonFile("data/equipment/" + item, EquipmentData.class).toItem(item);
        }
        return null;
    }
}