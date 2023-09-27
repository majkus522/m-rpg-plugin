package pl.majkus522.mrpg.common.classes.entity;

import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftPlayer;
import org.bukkit.event.entity.EntityTargetEvent;
import pl.majkus522.mrpg.common.classes.data.EntityData;

public class Enemy extends CustomEntity
{
    public Enemy(Location location, EntityData data)
    {
        super(location, data);
    }

    @Override
    protected void registerGoals()
    {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new RandomStrollGoal(this, 1));
        this.goalSelector.addGoal(2, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1, true));

        this.targetSelector.addGoal(0, new NearestAttackableTargetGoal<Summon>(this, Summon.class, false));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<Player>(this, Player.class, false));
    }

    public void taunt(org.bukkit.entity.Player player)
    {
        setTarget(((CraftPlayer)player).getHandle(), EntityTargetEvent.TargetReason.CUSTOM, false);
    }

    public void cancelTaunt()
    {
        setTarget(null);
    }
}