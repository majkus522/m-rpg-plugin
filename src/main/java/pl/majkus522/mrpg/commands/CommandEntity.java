package pl.majkus522.mrpg.commands;

import com.google.gson.Gson;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.craftbukkit.v1_20_R1.CraftWorld;
import org.bukkit.entity.Player;
import pl.majkus522.mrpg.common.ExtensionMethods;
import pl.majkus522.mrpg.common.classes.CustomCommand;
import pl.majkus522.mrpg.common.classes.entity.Enemy;
import pl.majkus522.mrpg.common.classes.entity.EntityData;
import pl.majkus522.mrpg.common.classes.entity.Summon;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CommandEntity extends CustomCommand
{
    @Override
    public void onPlayerExecute(Player player, String[] args)
    {
        if (args.length == 0)
        {
            player.sendMessage("Enter entity type");
            return;
        }
        if (args.length == 1)
        {
            player.sendMessage("Enter entity");
            return;
        }
        switch (args[0].toLowerCase())
        {
            case "enemy":
                if(!new File("data/entities/enemy/" + args[1] + ".json").exists())
                {
                    player.sendMessage("Entity doesn't exits");
                    return;
                }
                Enemy enemy = new Enemy(player.getLocation(), new Gson().fromJson(ExtensionMethods.readJsonFile("data/entities/enemy/" + args[0] + ".json"), EntityData.class));
                ((CraftWorld)player.getWorld()).getHandle().addFreshEntity(enemy);
                break;

            case "summon":
                if(!new File("data/entities/summon/" + args[1] + ".json").exists())
                {
                    player.sendMessage("Entity doesn't exits");
                    return;
                }
                Summon summon = new Summon(player.getLocation(), new Gson().fromJson(ExtensionMethods.readJsonFile("data/entities/summon/" + args[0] + ".json"), EntityData.class));
                ((CraftWorld)player.getWorld()).getHandle().addFreshEntity(summon);
                break;
        }

    }

    @Override
    public void onTerminalExecute(ConsoleCommandSender console, String[] args)
    {
        console.sendMessage("Command can only be used by admin");
    }

    @Override
    public List<String> autocomplete(Player player, String[] args)
    {
        ArrayList<String> list = new ArrayList<>();
        if (args.length == 1)
        {
            list.add("enemy");
            list.add("summon");
        }
        else if (args.length == 2)
        {
            File[] files = ExtensionMethods.scanDir("data/entities/" + args[0] + "/");
            for (File file : files)
                list.add(file.getName().replace(".json", ""));
        }
        return list;
    }

    @Override
    public String getCommand()
    {
        return "entity";
    }
}