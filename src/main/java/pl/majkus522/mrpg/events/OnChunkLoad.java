package pl.majkus522.mrpg.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import pl.majkus522.mrpg.controllers.ManaController;

public class OnChunkLoad implements Listener
{
    @EventHandler
    public void onChunkLoad(ChunkLoadEvent event)
    {
        if (event.isNewChunk())
        {
            ManaController.generateMana(event.getChunk());
        }
    }
}