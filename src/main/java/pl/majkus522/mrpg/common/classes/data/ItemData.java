package pl.majkus522.mrpg.common.classes.data;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class ItemData
{
    public String label = null;
    public String[] lore = null;
    public String material;
    public Integer customModel = null;

    public ItemStack toItem()
    {
        ItemStack item = new ItemStack(Material.valueOf(material), 1);
        ItemMeta meta = item.getItemMeta();
        if (label != null)
            meta.setDisplayName(ChatColor.RESET + ChatColor.translateAlternateColorCodes('&', label));
        if (lore != null)
        {
            ArrayList<String> loreList = new ArrayList<>();
            for (String line : lore)
                loreList.add(ChatColor.RESET + "" + ChatColor.WHITE + ChatColor.translateAlternateColorCodes('&', line));
            meta.setLore(loreList);
        }
        if (customModel != null)
            meta.setCustomModelData(customModel);
        item.setItemMeta(meta);
        return item;
    }
}