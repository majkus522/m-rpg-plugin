package pl.majkus522.mrpg;

import org.bukkit.command.CommandExecutor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import pl.majkus522.mrpg.commands.CommandLogin;
import pl.majkus522.mrpg.events.OnPlayerJoin;

import java.util.ArrayList;

public final class Main extends JavaPlugin
{
    public static ArrayList<String> unloggedPlayers;
    public static String mainUrl = "http://127.0.0.1/m-rpg/api/";

    @Override
    public void onLoad()
    {
        System.out.println("M-RPG loaded");
        unloggedPlayers = new ArrayList<>();
    }

    @Override
    public void onEnable()
    {
        System.out.println("M-RPG enabled");
        registerEvent(new OnPlayerJoin());
        registerCommand("login", new CommandLogin());
    }

    @Override
    public void onDisable()
    {
        System.out.println("M-RPG disabled");
    }

    void registerEvent(Listener event)
    {
        getServer().getPluginManager().registerEvents(event, this);
    }

    void registerCommand(String name, CommandExecutor executor)
    {
        this.getCommand(name).setExecutor(executor);
    }
}
