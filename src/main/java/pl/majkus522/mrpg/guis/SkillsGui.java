package pl.majkus522.mrpg.guis;

import com.google.gson.Gson;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import pl.majkus522.mrpg.Main;
import pl.majkus522.mrpg.common.ExtensionMethods;
import pl.majkus522.mrpg.common.api.RequestResult;
import pl.majkus522.mrpg.common.api.RequestSkills;
import pl.majkus522.mrpg.common.enums.SkillRarity;

import java.util.HashMap;

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

    public SkillsGui(Player player, SkillRarity rarity)
    {
        inventory = Bukkit.createInventory(this, 6 * 9, "Skills - " + rarity.toString().substring(0, 1).toUpperCase() + rarity.toString().substring(1));
        ItemStack empty = ExtensionMethods.emptySlot();
        for(int index = 0; index < inventory.getSize(); index++)
            inventory.setItem(index, empty);
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Session-Key", Main.playersSessions.get(player.getName()));
        headers.put("Session-Type", "game");
        RequestResult request = ExtensionMethods.httpRequest("GET", Main.mainUrl + "endpoints/skills/" + player.getName() + "?rarity[]=" + rarity.toString(), headers);
        Gson gson = new Gson();
        int index = 0;
        for (RequestSkills skill : gson.fromJson(request.content, RequestSkills[].class))
        {
            inventory.setItem(index, skill(skill));
            index++;
        }
    }

    public void onItemTake(InventoryClickEvent event)
    {
        event.setCancelled(true);
        if(event.getCurrentItem().getType() != Material.GRAY_STAINED_GLASS_PANE && event.getInventory().getSize() < 6 * 9)
        {
            HumanEntity player = event.getWhoClicked();
            player.openInventory(new SkillsGui((Player)player, SkillRarity.fromString(event.getCurrentItem().getItemMeta().getDisplayName())).getInventory());
        }
    }

    ItemStack button(Material material, String label)
    {
        ItemStack item = new ItemStack(material, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(label);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        meta.addEnchant(Enchantment.DURABILITY, 1, true);
        item.setItemMeta(meta);
        return item;
    }

    ItemStack skill(RequestSkills skill)
    {
        ItemStack item = new ItemStack(Material.EMERALD);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(skill.skill);
        item.setItemMeta(meta);
        return item;
    }

    @Override
    public Inventory getInventory()
    {
        return inventory;
    }
}
