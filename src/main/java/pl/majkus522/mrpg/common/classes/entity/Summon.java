package pl.majkus522.mrpg.common.classes.entity;

import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import org.bukkit.Location;

public class Summon extends CustomEntity
{
    public Summon(Location location, EntityData data)
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

        this.targetSelector.addGoal(0, new NearestAttackableTargetGoal<Enemy>(this, Enemy.class, false));
    }
}