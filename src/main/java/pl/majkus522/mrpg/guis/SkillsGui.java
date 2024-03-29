package pl.majkus522.mrpg.guis;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.ChatPaginator;
import pl.majkus522.mrpg.Config;
import pl.majkus522.mrpg.common.ExtensionMethods;
import pl.majkus522.mrpg.common.classes.Character;
import pl.majkus522.mrpg.common.classes.CustomInventory;
import pl.majkus522.mrpg.common.classes.HttpBuilder;
import pl.majkus522.mrpg.common.classes.data.SkillData;
import pl.majkus522.mrpg.common.classes.events.SkillToggleEvent;
import pl.majkus522.mrpg.common.enums.HttpMethod;
import pl.majkus522.mrpg.common.enums.Rarity;
import pl.majkus522.mrpg.controllers.NBTController;
import pl.majkus522.mrpg.controllers.PlayersController;
import pl.majkus522.mrpg.controllers.SkillsController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class SkillsGui extends CustomInventory
{
    public int page;
    public Rarity rarity;
    Player player;

    public SkillsGui(Player player)
    {
        super(SkillsController.playerHasSkill(player, Rarity.unknown) ? 5 : 3, "Skills");
        fillEmpty();
        if(SkillsController.playerHasSkill(player, Rarity.common))
            setItem(1, 1, button(Material.WHITE_CONCRETE, Rarity.common));
        if(SkillsController.playerHasSkill(player, Rarity.extra))
            setItem(3, 1, button(Material.LIME_CONCRETE, Rarity.extra));
        if(SkillsController.playerHasSkill(player, Rarity.unique))
            setItem(5, 1, button(Material.BLUE_CONCRETE, Rarity.unique));
        if(SkillsController.playerHasSkill(player, Rarity.ultimate))
            setItem(7, 1, button(Material.MAGENTA_CONCRETE, Rarity.ultimate));
        if(SkillsController.playerHasSkill(player, Rarity.unknown))
            setItem(4, 3, button(Material.BLACK_CONCRETE, Rarity.unknown));
    }

    public SkillsGui(Player player, Rarity rarity)
    {
        this(player, rarity, 0);
    }

    public SkillsGui(Player player, Rarity rarity, int page)
    {
        super(6, "Skills - " + rarity.toPrettyString());
        this.player = player;
        fillEmpty();
        fillRow(5, ExtensionMethods.emptySlot(Material.GREEN_STAINED_GLASS_PANE));
        this.page = page;
        this.rarity = rarity;
        List<Character.CharacterSkill> skills = PlayersController.getCharacter(player).skills.stream().filter(p -> p.rarity == rarity).collect(Collectors.toList());
        skills = skills.subList(page * 45, skills.size());
        if (skills.isEmpty())
        {
            inventory = new SkillsGui(player).getInventory();
            return;
        }
        int index = 0;
        for (Character.CharacterSkill skill : skills)
        {
            SkillData data = SkillsController.getSkillData(skill.skill);
            inventory.setItem(index, skill(data, skill));
            index++;
            if(index >= 45)
                break;
        }
        if(skills.size() > 45)
            setItem(8, 5, arrow(ArrowType.next));
        if(page != 0)
            setItem(0, 5, arrow(ArrowType.previous));
        setItem(4, 5, arrow(ArrowType.back));
    }

    public SkillsGui(Player player, String skill)
    {
        super(1, "Skills - select skill slot");
        this.player = player;
        fillEmpty();
        inventory.setItem(0, button(Material.RED_CONCRETE, ChatColor.RED + "Cancel", "assign-cancel"));
        for (int index = 0; index < Config.characterSkills; index++)
            inventory.setItem(index + 2, assign(index, skill));
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
                if (part[1].equals("assign") && part[2].equals("cancel"))
                {
                    player.openInventory(new SkillsGui(player).getInventory());
                }
                else
                    player.openInventory(new SkillsGui(player, Rarity.fromString(part[1])).getInventory());
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
                SkillsController.getSkill(player, part[1]).toggle = enabled;
                new HttpBuilder(HttpMethod.PATCH, "skills/" + player.getName() + "/" + part[1]).setSessionHeaders(player).setBody(enabled ? "true" : "false").getCode();
                List<String> lore = meta.getLore();
                lore.set(0, enabled ? (ChatColor.GREEN + "Enabled") : (ChatColor.RED + "Disabled"));
                meta.setLore(lore);
                item.setItemMeta(meta);
                Bukkit.getPluginManager().callEvent(new SkillToggleEvent(player, part[1], enabled));
                break;

            case "assign":
                player.openInventory(new SkillsGui(player, part[1]).getInventory());
                break;

            case "slot":
                PlayersController.getCharacter(player).assignSkill(part[2], Integer.parseInt(part[1]));
                player.openInventory(new SkillsGui(player).getInventory());
                break;
        }
    }

    ItemStack button(Material material, Rarity rarity)
    {
        return super.button(material, rarity.toColoredString(), rarity.toString());
    }

    ItemStack skill(SkillData skill, Character.CharacterSkill apiSkill)
    {
        ItemStack item = new ItemStack(Material.EMERALD);
        ArrayList<String> lore = new ArrayList<>();
        if (skill.toggle)
        {
            lore.add((apiSkill.toggle ? (ChatColor.GREEN + "Enabled") : (ChatColor.RED + "Disabled")));
            item = NBTController.putNBTString(item, "gui-action", "toggle-" + apiSkill.skill);
        }
        else if (skill.usable)
        {
            Character character = PlayersController.getCharacter(player);
            if (!character.isSkillAssigned(apiSkill.skill))
            {
                lore.add(ChatColor.GREEN + "Click to select skill");
                item = NBTController.putNBTString(item, "gui-action", "assign-" + apiSkill.skill);
            }
        }
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.RESET + skill.label);
        lore.add("");
        for (String line : ChatPaginator.wordWrap(skill.description, 35))
            lore.add(ChatColor.RESET + "" + ChatColor.WHITE + line);
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    ItemStack assign(int slot, String skill)
    {
        Character character = PlayersController.getCharacter(player);
        String old = character.getAssignedSkill(slot);
        ItemStack item;
        if (old == null)
        {
            item = new ItemStack(Material.GRAY_DYE);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(ChatColor.RESET + "Empty slot");
            meta.setLore(Collections.singletonList(ChatColor.RESET + "" + ChatColor.GREEN + "Click to assign"));
            item.setItemMeta(meta);
        }
        else
        {
            item = new ItemStack(Material.LIME_DYE);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(ChatColor.RESET + SkillsController.getSkillData(old).label);
            meta.setLore(Collections.singletonList(ChatColor.RESET + "" + ChatColor.GREEN + "Click to assign"));
            item.setItemMeta(meta);
        }
        return NBTController.putNBTString(item, "gui-action", "slot-" + slot + "-" + skill);
    }
}