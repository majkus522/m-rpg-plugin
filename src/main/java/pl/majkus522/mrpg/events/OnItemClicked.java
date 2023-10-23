package pl.majkus522.mrpg.events;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import pl.majkus522.mrpg.common.classes.events.SkillUseEvent;
import pl.majkus522.mrpg.controllers.NBTController;

public class OnItemClicked implements Listener
{
    @EventHandler
    public void onItemClicked(PlayerInteractEvent event)
    {
        if (event.getHand() != EquipmentSlot.HAND)
            return;
        ItemStack item = event.getItem();
        if (item != null)
        {
            if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)
            {
                if (NBTController.hasNBTTag(item, "assign"))
                {
                    String[] part = NBTController.getNBTString(item, "assign").split("-");
                    switch (part[0])
                    {
                        case "skill":
                            if (item.getType() == Material.GRAY_DYE)
                                return;
                            else if (item.getType() == Material.RED_DYE)
                            {
                                event.getPlayer().sendMessage(ChatColor.RED + "Skill is on cooldown");
                                return;
                            }
                            Bukkit.getPluginManager().callEvent(new SkillUseEvent(event.getPlayer(), Integer.parseInt(part[1])));
                            break;
                    }
                }
            }
        }
    }
}