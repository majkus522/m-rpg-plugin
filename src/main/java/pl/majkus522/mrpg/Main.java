package pl.majkus522.mrpg;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import pl.majkus522.mrpg.commands.*;
import pl.majkus522.mrpg.common.classes.Character;
import pl.majkus522.mrpg.common.classes.CustomCommand;
import pl.majkus522.mrpg.common.classes.MySQL;
import pl.majkus522.mrpg.controllers.PlayersController;
import pl.majkus522.mrpg.controllers.WorldController;
import pl.majkus522.mrpg.events.*;

import java.util.Map;

public final class Main extends JavaPlugin
{
    public static Main plugin;

    @Override
    public void onLoad()
    {
        System.out.println("M-RPG loaded");
        plugin = this;
        Bukkit.getScheduler().cancelTasks(this);
    }

    @Override
    public void onEnable()
    {
        System.out.println("M-RPG enabled");
        try
        {
            registerEvent(new OnPlayerJoin());
            registerEvent(new OnPlayerLeave());
            registerEvent(new OnItemTake());
            registerEvent(new OnPlayerDeath());
            registerEvent(new OnPlayerRightClickEntity());
            registerEvent(new OnPlayerChat());
            registerEvent(new OnEntityDamage());
            registerEvent(new OnPlayerKillsEntity());
            registerEvent(new OnPlayerCommand());
            registerEvent(new OnChunkLoad());
            registerEvent(new OnSkillToggled());
            registerEvent(new OnPlayerDropItem());
            registerEvent(new OnItemClicked());
            registerEvent(new OnSkillUsed());
            registerEvent(new OnEntitySpawns());

            registerCommand(new CommandLogin());
            registerCommand(new CommandSkills());
            registerCommand(new CommandStatus());
            registerCommand(new CommandShout());
            registerCommand(new CommandEntity());
            registerCommand(new CommandPay());
            registerCommand(new CommandWorld());
            registerCommand(new CommandTaunt());
            registerCommand(new CommandItem());
            registerCommand(new CommandMana());

            MySQL.connect();
            Config.init();
            WorldController.init();
        }
        catch (Exception e)
        {
            Bukkit.getServer().shutdown();
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onDisable()
    {
        for (Map.Entry<String, Character> element : PlayersController.players.entrySet())
            element.getValue().playerLeave();
        MySQL.disconnect();
        System.out.println("M-RPG disabled");
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