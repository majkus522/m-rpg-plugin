package pl.majkus522.mrpg.common.classes.effects;

import pl.majkus522.mrpg.common.enums.DamageType;
import pl.majkus522.mrpg.common.enums.StatusEffectType;
import pl.majkus522.mrpg.common.interfaces.IStatusEffectTarget;

public class PoisonEffect extends StatusEffect
{
    public PoisonEffect(IStatusEffectTarget target, int duration)
    {
        super(target, duration);
    }

    @Override
    public void tick()
    {
        target.damage(4, DamageType.poison);
    }

    @Override
    public String getTitle()
    {
        return "Poison";
    }

    @Override
    public StatusEffectType getType()
    {
        return StatusEffectType.negative;
    }

    @Override
    public void end() { }
}