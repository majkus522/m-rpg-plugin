package pl.majkus522.mrpg.common.classes;

import com.google.gson.JsonParser;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import pl.majkus522.mrpg.Config;
import pl.majkus522.mrpg.Main;
import pl.majkus522.mrpg.common.ExtensionMethods;
import pl.majkus522.mrpg.common.classes.api.RequestLogin;
import pl.majkus522.mrpg.common.classes.api.RequestPlayer;
import pl.majkus522.mrpg.common.classes.api.RequestSkill;
import pl.majkus522.mrpg.common.classes.data.EquipmentData;
import pl.majkus522.mrpg.common.classes.data.SkillData;
import pl.majkus522.mrpg.common.classes.effects.ManaOverloadEffect;
import pl.majkus522.mrpg.common.classes.effects.StatusEffect;
import pl.majkus522.mrpg.common.enums.DamageType;
import pl.majkus522.mrpg.common.enums.EquipmentSlot;
import pl.majkus522.mrpg.common.enums.HttpMethod;
import pl.majkus522.mrpg.common.interfaces.IRequestResult;
import pl.majkus522.mrpg.common.interfaces.IStatusEffectTarget;
import pl.majkus522.mrpg.controllers.*;

import javax.annotation.Nullable;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Character extends PlayerStatus implements IStatusEffectTarget
{
    public Player player;
    public String session;
    boolean changes = false;
    public ArrayList<CharacterSkill> skills;
    int mana;
    ArrayList<String> assignedSkills;
    int taskManaDisplay;
    int taskUpdate;
    public ArrayList<StatusEffect> statusEffects;
    public ItemStack[] equipment;

    public Character(Player player, RequestLogin session)
    {
        equipment = new ItemStack[4];
        this.player = player;
        this.session = session.key;
        this.id = session.id;
        HttpBuilder request = new HttpBuilder(HttpMethod.GET, "players/" + player.getName()).setHeader("Session-Key", this.session).setHeader("Session-ID", Integer.toString(id));
        if(!request.isOk())
        {
            player.sendMessage("Server error");
            throw new RuntimeException(new Exception(request.getError().message));
        }
        RequestPlayer data = (RequestPlayer)request.getResult(RequestPlayer.class);
        initStats(JsonParser.parseString(request.content).getAsJsonObject());
        this.id = data.id;
        this.level = data.level;
        this.exp = data.exp;
        this.money = data.money;
        this.mana = getMaxMana();
        this.guild = data.guild;

        equip(EquipmentSlot.helmet, data.helmet);
        equip(EquipmentSlot.chestplate, data.chestplate);
        equip(EquipmentSlot.leggings, data.leggings);
        equip(EquipmentSlot.boots, data.boots);
        setMaxHealth();
        setSpeed();

        skills = new ArrayList<>();
        request = new HttpBuilder(HttpMethod.GET, "skills/" + player.getName()).setHeader("Session-Key", this.session).setHeader("Session-ID", Integer.toString(id)).setHeader("Result-Count", "999");
        if(request.isOk())
            for (IRequestResult element : request.getResultAll(RequestSkill.class))
                skills.add(new CharacterSkill((RequestSkill) element));

        statusEffects = new ArrayList<>();
        assignedSkills = new ArrayList<>();
        if (FilesController.fileExists("settings/" + player.getName() + ".json"))
        {
            PlayerSettings settings = FilesController.readJsonFile("settings/" + player.getName(), PlayerSettings.class);
            assignedSkills = new ArrayList<>(Arrays.asList(settings.skills));
            this.mana = Math.min(settings.mana, getMaxMana());
            player.setHealth(Math.min(settings.health, player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue()));
            player.teleport(settings.position.toLocation());
        }
        else
            player.teleport(WorldController.getWorld("worlds/main", false).getSpawnLocation());
        while(assignedSkills.size() > Config.characterSkills)
            assignedSkills.remove(assignedSkills.size() - 1);
        while (assignedSkills.size() < Config.characterSkills)
            assignedSkills.add(null);
        reassignSkills();

        taskUpdate = Bukkit.getScheduler().runTaskTimerAsynchronously(Main.plugin, () ->
        {
            if (changes)
                update();
            if(getMaxMana() - mana > 0)
                ManaController.gatherMana(player, 5);
        }, 20 * 60, 20 * 60).getTaskId();
    }

    public void playerLeave()
    {
        Bukkit.getScheduler().cancelTask(taskUpdate);
        update();
        FilesController.writeJsonFile("settings/" + player.getName(), new PlayerSettings(player, mana, assignedSkills.toArray(new String[0])));
    }

    void update()
    {
        try
        {
            String query = "update `players` set `money` = ?, `level` = ?, `exp` = ?, `helmet` = ?, `chestplate` = ?, `leggings` = ?, `boots` = ?";
            for (Map.Entry<String, Integer> element : stats.entrySet())
                query += ", `" + element.getKey() + "` = " + element.getValue();
            PreparedStatement stmt = MySQL.getConnection().prepareStatement(query + " where `username` = ?");
            stmt.setFloat(1, money);
            stmt.setInt(2, level);
            stmt.setInt(3, exp);
            stmt.setString(4, NBTController.getNBTString(equipment[0], "armorId"));
            stmt.setString(5, NBTController.getNBTString(equipment[1], "armorId"));
            stmt.setString(6, NBTController.getNBTString(equipment[2], "armorId"));
            stmt.setString(7, NBTController.getNBTString(equipment[3], "armorId"));
            stmt.setString(8, player.getName());
            stmt.executeUpdate();

            List<CharacterSkill> toAdd = skills.stream().filter(p -> p.status == CharacterSkill.Status.add).collect(Collectors.toList());
            if (!toAdd.isEmpty())
            {
                String data = "";
                boolean first = true;
                for(CharacterSkill element : toAdd)
                {
                    if (!first)
                        data += ",";
                    data += "(" + id + ",\"" + element.skill + "\")";
                    first = false;
                    element.status = CharacterSkill.Status.ok;
                }
                query = "insert into `skills`(`player`, `skill`) values ";
                if (toAdd.size() > 1)
                    query += "(";
                query += data;
                if (toAdd.size() > 1)
                    query += ")";
                stmt = MySQL.getConnection().prepareStatement(query);
                stmt.executeUpdate();
            }

            List<CharacterSkill> toRemove = skills.stream().filter(p -> p.status == CharacterSkill.Status.remove).collect(Collectors.toList());
            if (!toRemove.isEmpty())
            {
                query = "delete from `skills` where `player` = ? and (";
                boolean first = true;
                for(CharacterSkill element : toRemove)
                {
                    if (!first)
                        query += " or ";
                    query += "`skill` = \"" + element.skill + "\"";
                    first = false;
                    skills.remove(element);
                }
                stmt = MySQL.getConnection().prepareStatement(query + ")");
                stmt.setInt(1, id);
                stmt.executeUpdate();
            }
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    void setMaxHealth()
    {
        player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(getStat("vtl"));
        player.setHealth(getStat("vtl"));
    }

    @Override
    public void setSpeed()
    {
        player.setWalkSpeed(Config.baseWalkSpeed * (1 + ((float) getStat("agl") / 200)));
    }

    @Override
    public void setSpeed(float value)
    {
        player.setWalkSpeed(value);
    }

    public void deathPenalty()
    {
        float moneyLost = (float)ExtensionMethods.round(money * 0.01f, 2);
        player.sendMessage("You have lost " + exp + " EXP and " + moneyLost + "$");
        exp = 0;
        money -= moneyLost;
        ScoreboardController.update(this);
    }

    public double getDamage()
    {
        return getStat("str");
    }

    public void damage(double input, DamageType type)
    {
        player.damage(handleDamage(input, type));
    }

    public double handleDamage(double input)
    {
        return handleDamage(input, DamageType.physical);
    }

    public double handleDamage(double input, DamageType type)
    {
        switch (type)
        {
            case mental:
            case poison:
                return input;

            case magical:
                input -= (getStat("def") * 0.25f);
                break;

            case physical:
                input -= getStat("def");
                break;
        }
        double random = Math.random() * 100;
        if (random < ((double)getStat("dex")) / 5)
            return -1;
        return Math.max(input, 0);
    }

    public void addExp(int input)
    {
        exp += input;
        while(exp >= ExtensionMethods.levelExp(level))
            levelUp();
        changes = true;
        ScoreboardController.updateLevel(this);
    }

    @Override
    public int getStat(String label)
    {
        int extra = 0;
        for(ItemStack item : player.getInventory().getArmorContents())
            if(NBTController.hasNBTTag(item, label))
                extra += NBTController.getNBTInt(item, label);
        return stats.get(label) + extra;
    }

    public void setStat(String label, int value)
    {
        stats.put(label, value);
        switch (label)
        {
            case "agl":
                setSpeed();
                break;

            case "vtl":
                setMaxHealth();
                break;
        }
    }

    public boolean hasMoney(float input)
    {
        return money > input;
    }

    public void addMoney(float input)
    {
        money += input;
        changes = true;
        ScoreboardController.updateMoney(this);
    }

    public void levelUp()
    {
        exp -= ExtensionMethods.levelExp(level);
        level++;
        for (Map.Entry<String, Integer> element : stats.entrySet())
            element.setValue(element.getValue() + 1);
        player.sendMessage("Your level has increased");
        ScoreboardController.update(this);
    }

    public void displayMana()
    {
        taskManaDisplay = Bukkit.getScheduler().runTaskTimer(Main.plugin, () ->
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.AQUA + "Mana: " + ManaController.getChunkMana(player.getLocation()))), 0, 5).getTaskId();
    }

    public void hideMana()
    {
        Bukkit.getScheduler().cancelTask(taskManaDisplay);
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR,new TextComponent(""));
    }

    public int getMana()
    {
        return mana;
    }

    public int getMaxMana()
    {
        return getStat("intl") * 3 + 10;
    }

    public boolean hasMana(int amount)
    {
        return mana > amount;
    }

    public void useMana(int amount)
    {
        mana = Math.max(mana - amount, 0);
        ScoreboardController.updateMana(this);
        if (mana < getMaxMana())
        {
            List<StatusEffect> list = statusEffects.stream().filter(p -> p instanceof ManaOverloadEffect).collect(Collectors.toList());
            if (!list.isEmpty())
                list.get(0).end();
        }
    }

    public void addMana(int amount)
    {
        mana += amount;
        ScoreboardController.updateMana(this);
        if (mana > getMaxMana() * 1.25f)
            new ManaOverloadEffect(this);
    }

    public void assignSkill(String skill, int slot)
    {
        assignedSkills.set(slot, skill);
        reassignSkills();
    }

    @Nullable
    public String getAssignedSkill(int slot)
    {
        return assignedSkills.get(slot);
    }

    public boolean isSkillAssigned(String skill)
    {
        for (String element : assignedSkills)
        {
            if (element == null)
                continue;
            if (element.equals(skill))
                return true;
        }
        return false;
    }

    void reassignSkills()
    {
        for (int index = 0; index < assignedSkills.size(); index++)
        {
            player.getInventory().setItem(9 - Config.characterSkills + index, hotbarSkill(index, false));
        }
    }

    public void cooldownSkill(String skill)
    {
        SkillData data = SkillsController.getSkillData(skill);
        int index = assignedSkills.indexOf(skill);
        player.getInventory().setItem(9 - Config.characterSkills + index, hotbarSkill(index, true));
        Bukkit.getScheduler().runTaskLater(Main.plugin, () ->
                player.getInventory().setItem(9 - Config.characterSkills + index, hotbarSkill(index, false)), 20L * data.cooldown);
    }

    @Override
    public void addEffect(StatusEffect effect)
    {
        if (hasEffect(effect))
        {
            statusEffects.stream().filter(p -> p.getClass() == effect.getClass()).collect(Collectors.toList()).get(0).overrideTime(effect.getTime());
            return;
        }
        statusEffects.add(effect);
    }

    @Override
    public void removeEffect(StatusEffect effect)
    {
        statusEffects.remove(effect);
    }

    @Override
    public boolean hasEffect(StatusEffect effect)
    {
        return statusEffects.stream().anyMatch(p -> p.getClass() == effect.getClass());
    }

    public void equip(EquipmentSlot slot, ItemStack item)
    {
        if(item == null)
            item = new ItemStack(Material.AIR);
        equipment[slot.toIndex()] = item;
    }

    public void equip(EquipmentSlot slot, String item)
    {
        equip(slot, item == null || item.length() == 0 ? null : FilesController.readJsonFile("data/equipment/" + item, EquipmentData.class).toItem(item));
    }

    public static class CharacterSkill extends RequestSkill
    {
        public Status status = Status.ok;

        public CharacterSkill(String skill)
        {
            this.skill = skill;
            this.toggle = 0;
        }

        public CharacterSkill(String skill, Status status)
        {
            this(skill);
            this.status = status;
        }

        public CharacterSkill(RequestSkill request)
        {
            this.skill = request.skill;
            this.toggle = request.getToggle() ? 1 : 0;
        }

        public void setToggle(boolean value)
        {
            toggle = value ? 1 : 0;
        }

        public enum Status
        {
            ok, add, remove
        }
    }

    ItemStack hotbarSkill(int slot, boolean cooldown)
    {
        Material material = Material.GRAY_DYE;
        if (cooldown)
            material = Material.RED_DYE;
        else if (assignedSkills.get(slot) != null)
            material = Material.LIME_DYE;
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.RESET + (assignedSkills.get(slot) == null ? "Empty" : SkillsController.getSkillData(assignedSkills.get(slot)).label));
        item.setItemMeta(meta);
        return NBTController.putNBTString(item, "assign", "skill-" + slot);
    }
}