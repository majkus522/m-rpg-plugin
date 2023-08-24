package pl.majkus522.mrpg.commands;

import com.google.gson.Gson;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.craftbukkit.v1_20_R1.CraftWorld;
import org.bukkit.entity.Player;
import pl.majkus522.mrpg.common.ExtensionMethods;
import pl.majkus522.mrpg.common.classes.CustomCommand;
import pl.majkus522.mrpg.common.classes.mobs.Enemy;
import pl.majkus522.mrpg.common.classes.mobs.EntityData;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CommandEntity extends CustomCommand
{
    @Override
    public void onPlayerExecute(Player player, String[] args)
    {
        if (args.length > 0)
        {
            if(!new File("data/entities/" + args[0] + ".json").exists())
            {
                player.sendMessage("Entity doesn't exits");
                return;
            }
            Enemy enemy = new Enemy(player.getLocation(), new Gson().fromJson(ExtensionMethods.readJsonFile("data/entities/" + args[0] + ".json"), EntityData.class));
            ((CraftWorld)player.getWorld()).getHandle().addFreshEntity(enemy);
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
        return new ArrayList<>();
    }

    @Override
    public String getCommand()
    {
        return "entity";
    }
}