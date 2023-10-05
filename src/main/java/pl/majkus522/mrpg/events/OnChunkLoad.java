package pl.majkus522.mrpg.events;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import pl.majkus522.mrpg.Main;
import pl.majkus522.mrpg.controllers.ManaController;

public class OnChunkLoad implements Listener
{
    @EventHandler
    public void onChunkLoad(ChunkLoadEvent event)
    {
        Chunk chunk = event.getChunk();
        if (event.isNewChunk())
            ManaController.generateMana(chunk);
        if (chunk.getWorld().getName().equals("login"))
            return;
        Bukkit.getScheduler().runTaskTimer(Main.plugin, new Runnable()
        {
            @Override
            public void run()
            {
                if (ManaController.getChunkMana(chunk) < ManaController.getChunkMaxMana(chunk))
                    ManaController.addMana(chunk, (int)ManaController.getChunkManaRegen(chunk));
            }
        }, 0, 20 * 60 * 10);
    }
}