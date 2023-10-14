package pl.majkus522.mrpg.common.interfaces;

import pl.majkus522.mrpg.common.classes.effects.StatusEffect;
import pl.majkus522.mrpg.common.enums.DamageType;

public interface IStatusEffectTarget
{
    public void addEffect(StatusEffect effect);
    public void removeEffect(StatusEffect effect);
    public boolean hasEffect(StatusEffect effect);
    public void setSpeed();
    public void setSpeed(float value);
    public void damage(double value, DamageType type);
}