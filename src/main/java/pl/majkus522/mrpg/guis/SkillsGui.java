package pl.majkus522.mrpg.guis;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import pl.majkus522.mrpg.common.ExtensionMethods;

public class SkillsGui implements InventoryHolder
{
    Inventory inventory;

    public SkillsGui()
    {
        inventory = Bukkit.createInventory(this, 3 * 9, "Skills");
        ItemStack empty = ExtensionMethods.emptySlot();
        for(int index = 0; index < inventory.getSize(); index++)
            inventory.setItem(index, empty);
        inventory.setItem(10, button(Material.WHITE_CONCRETE, ChatColor.WHITE + "Common"));
        inventory.setItem(12, button(Material.LIME_CONCRETE, ChatColor.DARK_GREEN + "Extra"));
        inventory.setItem(14, button(Material.BLUE_CONCRETE, ChatColor.BLUE + "Unique"));
        inventory.setItem(16, button(Material.MAGENTA_CONCRETE, ChatColor.DARK_PURPLE + "Ultimate"));
    }

    ItemStack button(Material material, String label)
    {
        ItemStack item = new ItemStack(material, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(label);
        item.setItemMeta(meta);
        return item;
    }

    @Override
    public Inventory getInventory()
    {
        return inventory;
    }
}
