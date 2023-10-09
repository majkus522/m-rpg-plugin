package pl.majkus522.mrpg.common.classes.effects;

import pl.majkus522.mrpg.common.enums.StatusEffectType;
import pl.majkus522.mrpg.common.interfaces.IStatusEffectTarget;

public class StunEffect extends StatusEffect
{
    public StunEffect(IStatusEffectTarget target, int duration)
    {
        super(target, duration);
        target.setSpeed(0);
    }

    @Override
    public String getTitle()
    {
        return "Stun";
    }

    @Override
    public StatusEffectType getType()
    {
        return StatusEffectType.negative;
    }

    @Override
    public void end()
    {
        target.setSpeed();
    }
}