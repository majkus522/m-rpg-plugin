package pl.majkus522.mrpg.common.classes.entity;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_20_R1.CraftWorld;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import pl.majkus522.mrpg.Config;
import pl.majkus522.mrpg.common.classes.data.EntityData;
import pl.majkus522.mrpg.common.classes.effects.StatusEffect;
import pl.majkus522.mrpg.common.enums.DamageType;
import pl.majkus522.mrpg.common.interfaces.IStatusEffectTarget;
import pl.majkus522.mrpg.controllers.SkillsController;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class CustomEntity extends PathfinderMob implements IStatusEffectTarget
{
    EntityData data;
    ArrayList<StatusEffect> statusEffects;

    public CustomEntity(Location location, EntityData data)
    {
        super(data.getType(), ((CraftWorld)location.getWorld()).getHandle());
        this.setPos(location.getX(), location.getY(), location.getZ());
        this.setCustomName(Component.Serializer.fromJson("\"" + ChatColor.translateAlternateColorCodes('$', data.name) + "\""));
        this.setCustomNameVisible(true);
        this.data = data;
        setMaxHealth();
        if (this.getAttributes().hasAttribute(Attributes.ATTACK_DAMAGE))
            this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(data.str);
        if (this.getAttributes().hasAttribute(Attributes.ARMOR))
            this.getAttribute(Attributes.ARMOR).setBaseValue(0);
        setSpeed();
        statusEffects = new ArrayList<>();
    }

    @Override
    public void setSpeed()
    {
        this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(Config.baseWalkSpeed * (1 + (((double)data.agl) / 200)));
    }

    @Override
    public void setSpeed(float value)
    {
        this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(value);
    }

    @Override
    public void damage(double value, DamageType type)
    {
        ((LivingEntity)this.getBukkitEntity()).damage(handleDamage(value, type));
    }

    public void setMaxHealth()
    {
        this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(data.vtl);
        this.setHealth(data.vtl);
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
                input -= (data.def * 0.25f);
                break;

            case physical:
                input -= data.def;
                break;
        }
        double random = Math.random() * 100;
        if (random < ((double)data.dex) / 5)
            return -1;
        return Math.max(input, 0);
    }

    public int getExp(Player killer)
    {
        float multiplayer = 1;
        if (SkillsController.playerHasSkill(killer, "fastLearning"))
            multiplayer = 1.5f;
        else if (SkillsController.playerHasSkill(killer, "superFastLearning"))
            multiplayer = 3f;
        else if (SkillsController.playerHasSkill(killer, "ultraFastLearning"))
            multiplayer = 5f;
        return (int)(data.exp * multiplayer);
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

    public String getEntityName()
    {
        return data.name;
    }
}