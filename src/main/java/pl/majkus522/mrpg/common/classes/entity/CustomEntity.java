package pl.majkus522.mrpg.common.classes.entity;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_20_R1.CraftWorld;
import org.bukkit.entity.Player;
import pl.majkus522.mrpg.Config;
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
        setMaxHealth();
        this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(data.str);
        this.getAttribute(Attributes.ARMOR).setBaseValue(0);
        this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(Config.baseWalkSpeed * (1 + (((double)data.agl) / 200)));
    }

    public void setMaxHealth()
    {
        this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(data.vtl);
        this.setHealth(data.vtl);
    }

    public double handleDamage(double input)
    {
        double random = Math.random() * 100;
        if (random < ((double)data.dex) / 5)
            return 0;
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