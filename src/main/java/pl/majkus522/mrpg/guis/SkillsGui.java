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
import pl.majkus522.mrpg.common.ExtensionMethods;
import pl.majkus522.mrpg.common.classes.HttpBuilder;
import pl.majkus522.mrpg.common.classes.SkillData;
import pl.majkus522.mrpg.common.classes.api.RequestSkill;
import pl.majkus522.mrpg.common.enums.HttpMethod;
import pl.majkus522.mrpg.common.enums.SkillRarity;
import pl.majkus522.mrpg.common.interfaces.IRequestResult;
import pl.majkus522.mrpg.controllers.NBTController;
import pl.majkus522.mrpg.controllers.SkillsController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SkillsGui implements InventoryHolder
{
    Inventory inventory;
    public int page;
    public SkillRarity rarity;

    public SkillsGui(Player player)
    {
        basic(player);
    }

    void basic(Player player)
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
        HttpBuilder request = new HttpBuilder(HttpMethod.GET, "endpoints/skills/" + player.getName() + "?rarity[]=" + rarity.toString()).setSessionHeaders(player).setHeader("Items", (page * 45) + "-45");
        if (!request.isOk())
        {
            basic(player);
            return;
        }
        Gson gson = new Gson();
        int index = 0;
        for (IRequestResult skill : request.getResultAll(RequestSkill.class))
        {
            RequestSkill apiSkill = (RequestSkill) skill;
            SkillData data = gson.fromJson(ExtensionMethods.readJsonFile("data/skills/" + apiSkill.skill + ".json"), SkillData.class);
            inventory.setItem(index, skill(data, apiSkill));
            index++;
        }
        if(Integer.parseInt(request.getOutputHeader("Items-Count")) == 45)
        {
            request = new HttpBuilder(HttpMethod.HEAD, "endpoints/skills/" + player.getName() + "?rarity[]=" + rarity.toString()).setSessionHeaders(player).setHeader("Items", ((page + 1) * 45) + "-45");
            if(request.isOk())
                inventory.setItem(53, arrow(true));
        }
        if(page != 0)
            inventory.setItem(45, arrow(false));
    }

    public void onItemTake(InventoryClickEvent event)
    {
        event.setCancelled(true);
        ItemStack item = event.getCurrentItem();
        if(!NBTController.hasNBTTag(item, "gui-action"))
            return;
        Player player = (Player) event.getWhoClicked();
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

            case "toggle":
                ItemMeta meta = item.getItemMeta();
                boolean enabled = !meta.getLore().get(0).contains("Enabled");
                HttpBuilder request = new HttpBuilder(HttpMethod.PATCH, "endpoints/skills/" + player.getName() + "/" + part[1]).setSessionHeaders(player).setBody(Boolean.toString(enabled));
                if (!request.isOk())
                    throw new RuntimeException(new Exception(request.getError().message));
                List<String> lore = meta.getLore();
                lore.set(0, enabled ? (ChatColor.GREEN + "Enabled") : (ChatColor.RED + "Disabled"));
                meta.setLore(lore);
                item.setItemMeta(meta);
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

    ItemStack skill(SkillData skill, RequestSkill apiSkill)
    {
        ItemStack item = new ItemStack(Material.EMERALD);
        ArrayList<String> lore = new ArrayList<>();
        if (skill.toggle)
        {
            lore.add((apiSkill.getToggle() ? (ChatColor.GREEN + "Enabled") : (ChatColor.RED + "Disabled")));
            item = NBTController.putNBTString(item, "gui-action", "toggle-" + apiSkill.skill);
        }
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.RESET + skill.label);
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