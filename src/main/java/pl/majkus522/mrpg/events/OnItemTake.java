package pl.majkus522.mrpg.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import pl.majkus522.mrpg.guis.SkillsGui;

public class OnItemTake implements Listener
{
    @EventHandler
    public void onItemTake(InventoryClickEvent event)
    {
        if(event.getClickedInventory() == null)
            return;
        if(event.getClickedInventory().getHolder() == null)
            return;
        if(!(event.getAction() == InventoryAction.PICKUP_ALL || event.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY))
            return;
        if(event.getClickedInventory().getHolder() instanceof SkillsGui)
            ((SkillsGui)event.getClickedInventory().getHolder()).onItemTake(event);
    }
}