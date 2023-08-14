package pl.majkus522.mrpg;

import org.bukkit.command.CommandExecutor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import pl.majkus522.mrpg.commands.CommandLogin;
import pl.majkus522.mrpg.commands.CommandShout;
import pl.majkus522.mrpg.commands.CommandSkills;
import pl.majkus522.mrpg.commands.CommandStatus;
import pl.majkus522.mrpg.events.*;

import java.util.HashMap;

public final class Main extends JavaPlugin
{
    public static HashMap<String, String> playersSessions;
    public static String mainUrl = "http://127.0.0.1/m-rpg/api/";
    public static Main plugin;

    @Override
    public void onLoad()
    {
        System.out.println("M-RPG loaded");
        playersSessions = new HashMap<String, String>();
        plugin = this;
    }

    @Override
    public void onEnable()
    {
        System.out.println("M-RPG enabled");
        registerEvent(new OnPlayerJoin());
        registerEvent(new OnPlayerLeave());
        registerEvent(new OnItemTake());
        registerEvent(new OnPlayerDeath());
        registerEvent(new OnPlayerRightClickEntity());
        registerEvent(new OnPlayerChat());

        registerCommand("login", new CommandLogin());
        registerCommand("skills", new CommandSkills());
        registerCommand("status", new CommandStatus());
        registerCommand("shout", new CommandShout());
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
