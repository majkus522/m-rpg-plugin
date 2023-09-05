package pl.majkus522.mrpg.common.classes.entity;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_20_R1.CraftWorld;
import org.bukkit.entity.Player;
import pl.majkus522.mrpg.controllers.SkillsController;

public class CustomEntity extends PathfinderMob
{
    EntityData data;

    public CustomEntity(Location location, EntityData data)
    {
        super(data.getType(), ((CraftWorld)location.getWorld()).getHandle());
        this.setPos(location.getX(), location.getY(), location.getZ());
        this.setCustomName((Component) Component.Serializer.fromJson("\"" + ChatColor.translateAlternateColorCodes('$', data.name) + "\""));
        this.setCustomNameVisible(true);
        this.data = data;
        this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(data.health);
        this.setHealth(data.health);
        this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(data.str * 2);
        this.getAttribute(Attributes.ARMOR).setBaseValue(0);
        this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(data.speed);
    }

    public double handleDamage(double input)
    {
        return input - data.def;
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
    protected void registerGoals() { }
}