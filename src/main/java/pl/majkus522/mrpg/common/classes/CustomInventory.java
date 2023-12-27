package pl.majkus522.mrpg.common.classes;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import pl.majkus522.mrpg.common.ExtensionMethods;
import pl.majkus522.mrpg.controllers.NBTController;

import javax.annotation.Nonnull;

public abstract class CustomInventory implements InventoryHolder
{
    protected Inventory inventory;

    public CustomInventory(int size, String title)
    {
        inventory = Bukkit.createInventory(this, size <= 6 ? size * 9 : size, title);
    }

    public CustomInventory(InventoryType type, String title)
    {
        inventory = Bukkit.createInventory(this, type, title);
    }

    public void fillEmpty()
    {
        ItemStack empty = ExtensionMethods.emptySlot();
        for (int index = 0; index < inventory.getSize(); index++)
            inventory.setItem(index, empty);
    }

    public void fillRow(int row, ItemStack item)
    {
        row %= 6;
        for (int index = row * 9; index < (row + 1) * 9; index++)
            inventory.setItem(index, item);
    }

    public void fillColumn(int column, ItemStack item)
    {
        column %= 9;
        for (int index = column; index < inventory.getSize(); index += 9)
            inventory.setItem(index, item);
    }

    public void setItem(int x, int y, ItemStack item)
    {
        inventory.setItem((x % 9) + ((y % 6) * 9), item);
    }

    protected ItemStack arrow(ArrowType type)
    {
        ItemStack item = new ItemStack(Material.ARROW, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.RESET + type.toPrettyString());
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        meta.addEnchant(Enchantment.DURABILITY, 1, true);
        item.setItemMeta(meta);
        return NBTController.putNBTString(item, "gui-action", "arrow-" + type);
    }

    protected ItemStack button(Material material, String label, String action)
    {
        ItemStack item = new ItemStack(material, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(label);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        meta.addEnchant(Enchantment.DURABILITY, 1, true);
        item.setItemMeta(meta);
        return NBTController.putNBTString(item, "gui-action", "button-" + action);
    }

    public void interact(InventoryClickEvent event) { }

    @Override
    @Nonnull
    public final Inventory getInventory()
    {
        return inventory;
    }

    public enum ArrowType
    {
        previous, back, next;

        public String toPrettyString()
        {
            String string = toString();
            return string.substring(0, 1).toUpperCase() + string.substring(1);
        }
    }
}
