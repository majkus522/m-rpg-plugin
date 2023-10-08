package pl.majkus522.mrpg.common.classes.effects;

import pl.majkus522.mrpg.common.classes.Character;
import pl.majkus522.mrpg.common.enums.StatusEffectType;

public class StunEffect extends StatusEffect
{
    Character character;

    public StunEffect(Character character, int duration)
    {
        super(character, duration);
        this.character = character;
        character.player.setWalkSpeed(0);
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
        character.setSpeed();
    }
}