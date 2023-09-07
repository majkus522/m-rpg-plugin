package pl.majkus522.mrpg.commands;

import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.BoundingBox;
import pl.majkus522.mrpg.Config;
import pl.majkus522.mrpg.common.classes.CustomCommand;
import pl.majkus522.mrpg.common.classes.entity.Enemy;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CommandTaunt extends CustomCommand
{
    @Override
    public void onPlayerExecute(Player player, String[] args)
    {
        Collection<Entity> entities = player.getWorld().getNearbyEntities(BoundingBox.of(player.getLocation(), Config.tauntRange, Config.tauntRange, Config.tauntRange));
        for (Entity entity : entities)
            if (entity != null && ((CraftEntity)entity).getHandle() instanceof Enemy)
                ((Enemy) ((CraftEntity)entity).getHandle()).taunt(player);
    }

    @Override
    public void onTerminalExecute(ConsoleCommandSender console, String[] args)
    {
        console.sendMessage("Command can only be used by player");
    }

    @Override
    public List<String> autocomplete(Player player, String[] args)
    {
        return new ArrayList<>();
    }

    @Override
    public String getCommand()
    {
        return "taunt";
    }
}
