package pl.majkus522.mrpg.common.classes.effects;

import pl.majkus522.mrpg.common.classes.Character;
import pl.majkus522.mrpg.common.enums.DamageType;
import pl.majkus522.mrpg.common.enums.StatusEffectType;

public class PoisonEffect extends StatusEffect
{
    Character character;

    public PoisonEffect(Character character, int duration)
    {
        super(character, duration);
        this.character = character;
    }

    @Override
    public void tick()
    {
        character.damage(4, DamageType.poison);
    }

    @Override
    public String getTitle()
    {
        return "Poison";
    }

    @Override
    public StatusEffectType getType()
    {
        return StatusEffectType.secret;
    }

    @Override
    public void end() { }
}