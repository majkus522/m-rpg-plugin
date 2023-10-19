package pl.majkus522.mrpg.events;

import net.minecraft.world.entity.Entity;
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import pl.majkus522.mrpg.common.classes.entity.Npc;
import pl.majkus522.mrpg.controllers.StatusController;

public class OnPlayerRightClickEntity implements Listener
{
    @EventHandler
    public void onPlayerClick(PlayerInteractEntityEvent event)
    {
        if (event.getHand() != EquipmentSlot.HAND)
            return;
        if(event.getRightClicked() instanceof Player)
        {
            StatusController.sendOtherPlayerStatus(event.getPlayer(), (Player)event.getRightClicked());
            return;
        }
        Entity entity = ((CraftEntity)event.getRightClicked()).getHandle();
        if (entity instanceof Npc)
            ((Npc)entity).openInventory(event.getPlayer());
    }
}