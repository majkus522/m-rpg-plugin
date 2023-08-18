package pl.majkus522.mrpg.common.classes;

import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.List;

public abstract class CustomCommand implements CommandExecutor, TabCompleter
{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String string, String[] args)
    {
        if (sender instanceof Player)
            onPlayerExecute((Player) sender, args);
        else if (sender instanceof ConsoleCommandSender)
            onTerminalExecute((ConsoleCommandSender)sender, args);
        return true;
    }

    public abstract void onPlayerExecute(Player player, String[] args);

    public abstract void onTerminalExecute(ConsoleCommandSender console, String[] args);

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String string, String[] args)
    {
        return autocomplete((Player)sender, args);
    }

    public abstract List<String> autocomplete(Player player, String[] args);

    public abstract String getCommand();
}