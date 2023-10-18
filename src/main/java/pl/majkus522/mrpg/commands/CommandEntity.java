package pl.majkus522.mrpg.commands;

import net.minecraft.server.level.ServerLevel;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.craftbukkit.v1_20_R1.CraftWorld;
import org.bukkit.entity.Player;
import pl.majkus522.mrpg.common.ExtensionMethods;
import pl.majkus522.mrpg.common.classes.CustomCommand;
import pl.majkus522.mrpg.common.classes.data.EntityData;
import pl.majkus522.mrpg.common.classes.entity.Enemy;
import pl.majkus522.mrpg.common.classes.entity.Summon;
import pl.majkus522.mrpg.controllers.FilesController;

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
            if (!FilesController.fileExists("data/entities/" + args[0]))
            {
                player.sendMessage("Incorrect entity type");
                return;
            }
            player.sendMessage("Enter entity");
            return;
        }
        if(!FilesController.fileExists("data/entities/" + args[0] + "/" + args[1] + ".json"))
        {
            player.sendMessage("Entity doesn't exits");
            return;
        }
        EntityData data = FilesController.readJsonFile("data/entities/" + args[0] + "/" + args[1], EntityData.class);
        ServerLevel world = ((CraftWorld)player.getWorld()).getHandle();
        switch (args[0].toLowerCase())
        {
            case "enemy":
                Enemy enemy = new Enemy(player.getLocation(), data);
                world.addFreshEntity(enemy);
                break;

            case "summon":
                Summon summon = new Summon(player.getLocation(), data, player);
                world.addFreshEntity(summon);
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
            if (FilesController.fileExists("data/entities/" + args[0]))
            {
                File[] files = ExtensionMethods.scanDir("data/entities/" + args[0] + "/");
                for (File file : files)
                    list.add(file.getName().replace(".json", ""));
            }
        }
        return list;
    }

    @Override
    public String getCommand()
    {
        return "entity";
    }
}