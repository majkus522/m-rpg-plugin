package pl.majkus522.mrpg.common.classes.effects;

import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import pl.majkus522.mrpg.common.classes.Character;
import pl.majkus522.mrpg.common.enums.StatusEffectType;

public class ManaOverloadEffect extends StatusEffect
{
    public ManaOverloadEffect(Character character)
    {
        super(character, -1);
        character.player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, -1, 1, true, false));
    }

    @Override
    public String getTitle()
    {
        return "Mana overload";
    }

    @Override
    public StatusEffectType getType()
    {
        return StatusEffectType.negative;
    }

    @Override
    public void end()
    {
        super.end();
        ((Character)target).player.removePotionEffect(PotionEffectType.CONFUSION);
    }
}