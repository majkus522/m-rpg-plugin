package pl.majkus522.mrpg.common.classes.data;

import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import pl.majkus522.mrpg.Config;
import pl.majkus522.mrpg.controllers.NBTController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class EquipmentData extends ItemData
{
    public HashMap<String, Integer> bonusStats = null;

    public ItemStack toItem(String id)
    {
        ItemStack item = super.toItem();
        ItemMeta meta = item.getItemMeta();
        List<String> lore = meta.getLore();
        if(bonusStats != null)
        {
            if(lore == null)
                lore = new ArrayList<>();
            lore.add("");
            for (Map.Entry<String, Integer> element : bonusStats.entrySet())
                lore.add(ChatColor.RESET + "" + (element.getValue() > 0 ? ChatColor.GREEN : ChatColor.RED) + Config.characterStats.stream().filter(p -> p.label.equals(element.getKey())).collect(Collectors.toList()).get(0).display + ": " + element.getValue());
            meta.setLore(lore);
        }
        item.setItemMeta(meta);
        if(bonusStats != null)
            for (Map.Entry<String, Integer> element : bonusStats.entrySet())
                item = NBTController.putNBTInt(item, element.getKey(), element.getValue());
        return NBTController.putNBTString(item, "id", id);
    }
}