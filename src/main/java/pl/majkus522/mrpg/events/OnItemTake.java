package pl.majkus522.mrpg.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import pl.majkus522.mrpg.common.classes.CustomInventory;

public class OnItemTake implements Listener
{
    @EventHandler
    public void onItemTake(InventoryClickEvent event)
    {
        if(event.getClickedInventory() == null)
            return;
        if(event.getClickedInventory().getHolder() == null)
            return;
        if(event.getClickedInventory().getHolder() instanceof CustomInventory)
            ((CustomInventory)event.getClickedInventory().getHolder()).interact(event);
    }
}
