package pl.majkus522.mrpg.common.classes.entity;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryHolder;
import pl.majkus522.mrpg.common.classes.data.EntityData;

public class Npc extends CustomEntity
{
    Class<? extends InventoryHolder> inventoryType = null;

    public Npc(Location location, EntityData data)
    {
        super(location, data);
        ((LivingEntity)this.getBukkitEntity()).setCollidable(false);
    }

    public void openInventory(Player player)
    {
        if (inventoryType == null)
            return;
        try
        {
            player.openInventory(inventoryType.getDeclaredConstructor(Player.class).newInstance(player).getInventory());
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }
}
