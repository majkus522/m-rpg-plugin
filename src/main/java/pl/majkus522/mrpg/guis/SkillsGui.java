package pl.majkus522.mrpg.guis;

import com.google.gson.Gson;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.ChatPaginator;
import pl.majkus522.mrpg.common.ExtensionMethods;
import pl.majkus522.mrpg.common.classes.CustomInventory;
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

public class SkillsGui extends CustomInventory
{
    public int page;
    public SkillRarity rarity;

    public SkillsGui(Player player)
    {
        super(SkillsController.playerHasSkill(player, SkillRarity.unknown) ? 5 : 3, "Skills");
        fillEmpty();
        if(SkillsController.playerHasSkill(player, SkillRarity.common))
            setItem(1, 1, button(Material.WHITE_CONCRETE, SkillRarity.common));
        if(SkillsController.playerHasSkill(player, SkillRarity.extra))
            setItem(3, 1, button(Material.LIME_CONCRETE, SkillRarity.extra));
        if(SkillsController.playerHasSkill(player, SkillRarity.unique))
            setItem(5, 1, button(Material.BLUE_CONCRETE, SkillRarity.unique));
        if(SkillsController.playerHasSkill(player, SkillRarity.ultimate))
            setItem(7, 1, button(Material.MAGENTA_CONCRETE, SkillRarity.ultimate));
        if(SkillsController.playerHasSkill(player, SkillRarity.unknown))
            setItem(4, 3, button(Material.BLACK_CONCRETE, SkillRarity.unknown));
    }

    public SkillsGui(Player player, SkillRarity rarity)
    {
        this(player, rarity, 0);
    }

    public SkillsGui(Player player, SkillRarity rarity, int page)
    {
        super(6, "Skills - " + rarity.toPrettyString());
        fillEmpty();
        fillRow(5, ExtensionMethods.emptySlot(Material.GREEN_STAINED_GLASS_PANE));
        this.page = page;
        this.rarity = rarity;
        HttpBuilder request = new HttpBuilder(HttpMethod.GET, "endpoints/skills/" + player.getName() + "?rarity[]=" + rarity.toString()).setSessionHeaders(player).setHeader("Items", (page * 45) + "-45");
        if (!request.isOk())
        {
            inventory = new SkillsGui(player).getInventory();
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
                setItem(8, 5, arrow(ArrowType.next));
        }
        if(page != 0)
            setItem(0, 5, arrow(ArrowType.previous));
        setItem(4, 5, arrow(ArrowType.back));
    }

    @Override
    public void interact(InventoryClickEvent event)
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
                ArrowType arrow = ArrowType.valueOf(part[1]);
                if (arrow == ArrowType.back)
                {
                    player.openInventory(new SkillsGui(player).getInventory());
                    return;
                }
                player.openInventory(new SkillsGui(player, old.rarity, old.page + (arrow == ArrowType.next ? 1 : -1)).getInventory());
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
        return super.button(material, rarity.toColoredString(), rarity.toString());
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
}