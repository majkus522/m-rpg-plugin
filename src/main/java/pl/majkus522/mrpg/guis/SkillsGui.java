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
import pl.majkus522.mrpg.controllers.NBTController;
import pl.majkus522.mrpg.controllers.SkillsController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class SkillsGui implements InventoryHolder
{
    Inventory inventory;
    public int page;
    public SkillRarity rarity;

    public SkillsGui(Player player)
    {
        boolean unknownRarity = SkillsController.playerHasSkill(player, SkillRarity.unknown);
        inventory = Bukkit.createInventory(this, unknownRarity ? 45 : 27, "Skills");
        ItemStack empty = ExtensionMethods.emptySlot();
        for(int index = 0; index < inventory.getSize(); index++)
            inventory.setItem(index, empty);
        if(SkillsController.playerHasSkill(player, SkillRarity.common))
            inventory.setItem(10, button(Material.WHITE_CONCRETE, SkillRarity.common));
        if(SkillsController.playerHasSkill(player, SkillRarity.extra))
            inventory.setItem(12, button(Material.LIME_CONCRETE, SkillRarity.extra));
        if(SkillsController.playerHasSkill(player, SkillRarity.unique))
            inventory.setItem(14, button(Material.BLUE_CONCRETE, SkillRarity.unique));
        if(SkillsController.playerHasSkill(player, SkillRarity.ultimate))
            inventory.setItem(16, button(Material.MAGENTA_CONCRETE, SkillRarity.ultimate));
        if(unknownRarity)
            inventory.setItem(31, button(Material.BLACK_CONCRETE, SkillRarity.unknown));
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
                inventory.setItem(53, arrow(true));
        }
        if(page != 0)
            inventory.setItem(45, arrow(false));
    }

    public void onItemTake(InventoryClickEvent event)
    {
        event.setCancelled(true);
        Player player = (Player) event.getWhoClicked();
        ItemStack item = event.getCurrentItem();
        if(!NBTController.hasNBTTag(item, "gui-action"))
            return;
        String action = NBTController.getNBTString(item, "gui-action");
        String[] part = action.split("-");
        switch (part[0])
        {
            case "button":
                player.openInventory(new SkillsGui(player, SkillRarity.fromString(part[1])).getInventory());
                break;

            case "arrow":
                SkillsGui old = (SkillsGui)event.getClickedInventory().getHolder();
                player.openInventory(new SkillsGui(player, old.rarity, old.page + (part[1].equals("next") ? 1 : -1)).getInventory());
                break;
        }
    }

    ItemStack button(Material material, SkillRarity rarity)
    {
        ItemStack item = new ItemStack(material, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(rarity.toColoredString());
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        meta.addEnchant(Enchantment.DURABILITY, 1, true);
        item.setItemMeta(meta);
        return NBTController.putNBTString(item, "gui-action", "button-" + rarity.toString());
    }

    ItemStack arrow(boolean next)
    {
        ItemStack item = new ItemStack(Material.ARROW, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.RESET + (next ? "Next page" : "Prevoius page"));
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        meta.addEnchant(Enchantment.DURABILITY, 1, true);
        item.setItemMeta(meta);
        return NBTController.putNBTString(item, "gui-action", "arrow-" + (next ? "next" : "prevoius"));
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
