package pl.majkus522.mrpg.common.classes;

import org.bukkit.command.*;
import org.bukkit.entity.Player;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import java.util.List;
import java.util.stream.Collectors;

public abstract class CustomCommand implements CommandExecutor, TabCompleter
{
    @Override
    public boolean onCommand(@Nonnull CommandSender sender, @Nonnull Command command, @Nonnull String string, @Nonnull String[] args)
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
    public List<String> onTabComplete(@Nonnull CommandSender sender, @Nonnull Command command, @Nonnull String string, @Nonnull String[] args)
    {
        return autocomplete((Player)sender, args).stream().filter(p -> p.startsWith(args[args.length - 1])).collect(Collectors.toList());
    }

    public abstract List<String> autocomplete(Player player, String[] args);

    @CheckForNull
    public abstract String getCommand();
}