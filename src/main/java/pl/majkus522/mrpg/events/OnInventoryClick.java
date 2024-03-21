package pl.majkus522.mrpg.events;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import pl.majkus522.mrpg.common.classes.Character;
import pl.majkus522.mrpg.common.classes.CustomInventory;
import pl.majkus522.mrpg.common.enums.EquipmentSlot;
import pl.majkus522.mrpg.controllers.NBTController;
import pl.majkus522.mrpg.controllers.PlayersController;

public class OnInventoryClick implements Listener
{
    @EventHandler
    public void onItemTake(InventoryClickEvent event)
    {
        if(event.getAction() == InventoryAction.NOTHING)
            return;
        if(event.getClickedInventory() == null)
            return;
        if(event.getClickedInventory().getHolder() == null)
            return;
        if(event.getClickedInventory().getHolder() instanceof CustomInventory)
            ((CustomInventory)event.getClickedInventory().getHolder()).interact(event);
        else if (event.getClickedInventory() instanceof PlayerInventory)
        {
            if (NBTController.hasNBTTag(event.getCurrentItem(), "assign"))
                event.setCancelled(true);
            if(event.getSlotType() == InventoryType.SlotType.ARMOR)
            {
                ItemStack item = event.getClickedInventory().getItem(event.getSlot());
                Character character = PlayersController.getCharacter((Player)event.getWhoClicked());
                if(item == null)
                    character.equip(EquipmentSlot.fromMaterial(event.getCursor().getType()), event.getCursor());
                else
                    character.equip(EquipmentSlot.fromMaterial(item.getType()), new ItemStack(Material.AIR));
            }
        }
    }
}
