package pl.majkus522.mrpg.common.classes.entity.goal;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

public class GoalSummon extends Goal
{
    final PathfinderMob entity;
    LivingEntity owner;
    final double speed;
    final float minDistance;
    final float maxDistance;
    double x;
    double y;
    double z;

    public GoalSummon(PathfinderMob entity, float minDistance, float maxDistance, double speed, LivingEntity owner)
    {
        this.entity = entity;
        this.minDistance = Math.abs(minDistance);
        this.maxDistance = Math.abs(maxDistance);
        this.speed = speed;
        this.setFlags(EnumSet.of(Flag.MOVE));
        this.owner = owner;
    }

    @Override
    public boolean canUse()
    {
        if (owner == null)
            return false;
        if (owner.distanceTo(entity) > maxDistance * 5)
        {
            entity.setPos(owner.getX(), owner.getY(), owner.getZ());
            System.out.println("to far");
            return false;
        }
        if (owner.distanceTo(entity) < maxDistance)
            return false;
        this.x = owner.getX();
        this.y = owner.getY();
        this.z = owner.getZ();
        return true;
    }

    @Override
    public void start()
    {
        System.out.println("move");
        entity.getNavigation().moveTo(x, y, z, speed);
    }

    @Override
    public boolean canContinueToUse()
    {
        return entity.getNavigation().isInProgress() && owner.distanceTo(entity) > minDistance;
    }

    @Override
    public void stop()
    {
        System.out.println("stop");
        entity.getNavigation().stop();
    }
}