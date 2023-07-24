package pl.majkus522.mrpg.guis;

import com.google.gson.Gson;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.ChatPaginator;
import pl.majkus522.mrpg.Main;
import pl.majkus522.mrpg.common.ExtensionMethods;
import pl.majkus522.mrpg.common.classes.SkillData;
import pl.majkus522.mrpg.common.classes.api.RequestResult;
import pl.majkus522.mrpg.common.classes.api.RequestSkills;
import pl.majkus522.mrpg.common.enums.SkillRarity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class SkillsGui implements InventoryHolder
{
    Inventory inventory;
    public int page;
    public SkillRarity rarity;

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
        this(player, rarity, 0);
    }

    public SkillsGui(Player player, SkillRarity rarity, int page)
    {
        this.page = page;
        this.rarity = rarity;
        inventory = Bukkit.createInventory(this, 6 * 9, "Skills - " + rarity.toPrettyString());
        ItemStack empty = ExtensionMethods.emptySlot();
        for(int index = 0; index < inventory.getSize(); index++)
            inventory.setItem(index, empty);
        HashMap<String, String> headers = ExtensionMethods.getSessionHeaders(player);
        headers.put("Items", (page * 45) + "-45");
        RequestResult request = ExtensionMethods.httpRequest("GET", Main.mainUrl + "endpoints/skills/" + player.getName() + "?rarity[]=" + rarity.toString(), headers);
        Gson gson = new Gson();
        int index = 0;
        for (RequestSkills skill : gson.fromJson(request.content, RequestSkills[].class))
        {
            SkillData data = gson.fromJson(ExtensionMethods.readJsonFile("data/skills/" + skill.skill + ".json"), SkillData.class);
            inventory.setItem(index, skill(data));
            index++;
        }
        if(Integer.parseInt(request.headers.get("Items-Count")) == 45)
        {
            headers = ExtensionMethods.getSessionHeaders(player);
            headers.put("Items", ((page + 1) * 45) + "-45");
            request = ExtensionMethods.httpRequest("HEAD", Main.mainUrl + "endpoints/skills/" + player.getName() + "?rarity[]=" + rarity.toString(), headers);
            if(request.isOk())
            {
                ItemStack item = new ItemStack(Material.ARROW, 1);
                ItemMeta meta = item.getItemMeta();
                meta.setDisplayName(ChatColor.RESET + "Next page");
                item.setItemMeta(meta);
                inventory.setItem(53, item);
            }

        }
        if(page != 0)
        {
            ItemStack item = new ItemStack(Material.ARROW, 1);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(ChatColor.RESET + "Prevoius page");
            item.setItemMeta(meta);
            inventory.setItem(45, item);
        }
    }

    public void onItemTake(InventoryClickEvent event)
    {
        event.setCancelled(true);
        Player player = (Player) event.getWhoClicked();
        ItemStack item = event.getCurrentItem();
        if(item.getType() != Material.GRAY_STAINED_GLASS_PANE && event.getInventory().getSize() == 3 * 9)
        {
            player.openInventory(new SkillsGui((Player)player, SkillRarity.fromString(item.getItemMeta().getDisplayName())).getInventory());
        }
        else if(item.getType() == Material.ARROW && event.getInventory().getSize() == 6 * 9)
        {
            if(item.getItemMeta().getDisplayName().contains("Next"))
            {
                SkillsGui old = (SkillsGui)event.getClickedInventory().getHolder();
                player.openInventory(new SkillsGui((Player) player, old.rarity, old.page + 1).getInventory());
            }
            else if(item.getItemMeta().getDisplayName().contains("Prevoius"))
            {
                SkillsGui old = (SkillsGui)event.getClickedInventory().getHolder();
                player.openInventory(new SkillsGui((Player) player, old.rarity, old.page - 1).getInventory());
            }
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

    ItemStack skill(SkillData skill)
    {
        ItemStack item = new ItemStack(Material.EMERALD);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.RESET + skill.label);
        ArrayList<String> lore = new ArrayList<>();
        lore.add("");
        for (String line : Arrays.asList(ChatPaginator.wordWrap(skill.description, 35)))
            lore.add(ChatColor.RESET + "" + ChatColor.WHITE + line);
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    @Override
    public Inventory getInventory()
    {
        return inventory;
    }
}
