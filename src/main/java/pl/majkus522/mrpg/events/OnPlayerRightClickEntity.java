package pl.majkus522.mrpg.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import pl.majkus522.mrpg.controllers.StatusController;

public class OnPlayerRightClickEntity implements Listener
{
    @EventHandler
    public void onPlayerClick(PlayerInteractEntityEvent event)
    {
        if(event.getRightClicked() instanceof Player && event.getHand() == EquipmentSlot.HAND)
        {
            Player player = event.getPlayer();
            StatusController.sendOtherPlayerStatus(player, (Player)event.getRightClicked());
        }
    }
}