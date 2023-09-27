package pl.majkus522.mrpg;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import pl.majkus522.mrpg.commands.*;
import pl.majkus522.mrpg.common.classes.CustomCommand;
import pl.majkus522.mrpg.common.classes.MySQL;
import pl.majkus522.mrpg.controllers.PlayersController;
import pl.majkus522.mrpg.events.*;

public final class Main extends JavaPlugin
{
    public static Main plugin;

    @Override
    public void onLoad()
    {
        System.out.println("M-RPG loaded");
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
        registerEvent(new OnPlayerCommand());

        registerCommand(new CommandLogin());
        registerCommand(new CommandSkills());
        registerCommand(new CommandStatus());
        registerCommand(new CommandShout());
        registerCommand(new CommandEntity());
        registerCommand(new CommandPay());
        registerCommand(new CommandWorld());
        registerCommand(new CommandTaunt());

        MySQL.connect();
        Config.init();
    }

    @Override
    public void onDisable()
    {
        for (Player player : Bukkit.getOnlinePlayers())
            PlayersController.getCharacter(player).update();
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