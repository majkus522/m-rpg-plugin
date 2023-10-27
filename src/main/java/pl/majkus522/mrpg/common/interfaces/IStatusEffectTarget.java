package pl.majkus522.mrpg.common.interfaces;

import pl.majkus522.mrpg.common.classes.effects.StatusEffect;
import pl.majkus522.mrpg.common.enums.DamageType;

public interface IStatusEffectTarget
{
    void addEffect(StatusEffect effect);
    void removeEffect(StatusEffect effect);
    boolean hasEffect(StatusEffect effect);
    void setSpeed();
    void setSpeed(float value);
    void damage(double value, DamageType type);
}