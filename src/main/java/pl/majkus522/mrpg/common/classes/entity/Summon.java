package pl.majkus522.mrpg.common.classes.entity;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import pl.majkus522.mrpg.common.classes.data.EntityData;
import pl.majkus522.mrpg.common.classes.entity.goal.GoalSummon;

public class Summon extends CustomEntity
{
    LivingEntity owner;

    public Summon(Location location, EntityData data, Player owner)
    {
        super(location, data);
        this.owner = ((CraftPlayer)owner).getHandle();
        initGoals();
    }

    void initGoals()
    {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1, true));
        this.goalSelector.addGoal(2, new GoalSummon(this, 5, 20, 1, owner));
        this.goalSelector.addGoal(3, new RandomStrollGoal(this, 1));
        this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(0, new NearestAttackableTargetGoal<>(this, Enemy.class, false));
    }
}