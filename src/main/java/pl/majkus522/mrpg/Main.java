package pl.majkus522.mrpg;

import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import pl.majkus522.mrpg.events.OnPlayerJoin;

public final class Main extends JavaPlugin
{
    @Override
    public void onLoad()
    {
        System.out.println("M-RPG loaded");
    }

    @Override
    public void onEnable()
    {
        System.out.println("M-RPG enabled");
        registerEvent(new OnPlayerJoin());
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

    void registerCommand()
    {

    }
}
