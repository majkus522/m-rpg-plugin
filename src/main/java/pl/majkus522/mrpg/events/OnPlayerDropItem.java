package pl.majkus522.mrpg.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import pl.majkus522.mrpg.controllers.NBTController;

public class OnPlayerDropItem implements Listener
{
    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event)
    {
        if (NBTController.hasNBTTag(event.getItemDrop().getItemStack(), "assign"))
            event.setCancelled(true);
    }
}