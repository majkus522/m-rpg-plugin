package pl.majkus522.mrpg;

import org.bukkit.command.CommandExecutor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import pl.majkus522.mrpg.commands.CommandLogin;
import pl.majkus522.mrpg.commands.CommandSkills;
import pl.majkus522.mrpg.events.OnItemTake;
import pl.majkus522.mrpg.events.OnPlayerJoin;

import java.util.HashMap;

public final class Main extends JavaPlugin
{
    public static HashMap<String, String> playersSessions;
    public static String mainUrl = "http://127.0.0.1/m-rpg/api/";

    @Override
    public void onLoad()
    {
        System.out.println("M-RPG loaded");
        playersSessions = new HashMap<String, String>();
    }

    @Override
    public void onEnable()
    {
        System.out.println("M-RPG enabled");
        registerEvent(new OnPlayerJoin());
        registerEvent(new OnItemTake());

        registerCommand("login", new CommandLogin());
        registerCommand("skills", new CommandSkills());
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
