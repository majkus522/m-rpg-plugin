package pl.majkus522.mrpg;

import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import pl.majkus522.mrpg.commands.*;
import pl.majkus522.mrpg.common.classes.Character;
import pl.majkus522.mrpg.common.classes.CustomCommand;
import pl.majkus522.mrpg.common.classes.MySQL;
import pl.majkus522.mrpg.events.*;

import java.util.HashMap;

public final class Main extends JavaPlugin
{
    public static HashMap<String, Character> players;
    public static Main plugin;

    @Override
    public void onLoad()
    {
        System.out.println("M-RPG loaded");
        players = new HashMap<String, Character>();
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
        registerEvent(new OnEntityDamage());
        registerEvent(new OnPlayerKillsEntity());

        registerCommand(new CommandLogin());
        registerCommand(new CommandSkills());
        registerCommand(new CommandStatus());
        registerCommand(new CommandShout());
        registerCommand(new CommandEntity());

        MySQL.connect();
    }

    @Override
    public void onDisable()
    {
        System.out.println("M-RPG disabled");
        MySQL.disconnect();
    }

    void registerEvent(Listener event)
    {
        getServer().getPluginManager().registerEvents(event, this);
    }

    void registerCommand(CustomCommand executor)
    {
        this.getCommand(executor.getCommand()).setExecutor(executor);
    }
}