package pl.majkus522.mrpg.common.classes.effects;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import pl.majkus522.mrpg.Main;
import pl.majkus522.mrpg.common.classes.Character;
import pl.majkus522.mrpg.common.enums.StatusEffectType;

public abstract class StatusEffect
{
    int times = 0;
    int task;
    int duration;

    public StatusEffect(Character character, int duration)
    {
        this.duration = duration;
        if (getType() != StatusEffectType.secret && getType() != StatusEffectType.hidden)
            character.player.sendMessage("Status effect " + getType().toColor() + getTitle() + ChatColor.WHITE + " was inflicted on you");
        character.statusEffects.add(this);
        task = Bukkit.getScheduler().runTaskTimer(Main.plugin, new Runnable()
        {
            @Override
            public void run()
            {
                times++;
                if (times >= duration - 1)
                {
                    Bukkit.getScheduler().cancelTask(task);
                    end();
                    character.statusEffects.remove(StatusEffect.this);
                    if (getType() != StatusEffectType.secret && getType() != StatusEffectType.hidden)
                        character.player.sendMessage("Status effect " + getType().toColor() + getTitle() + ChatColor.WHITE + " ended");
                }
                tick();
            }
        }, 0, 20).getTaskId();
    }

    public void tick() { }

    public abstract String getTitle();

    public abstract StatusEffectType getType();

    public abstract void end();

    public final int getTime()
    {
        return duration - times;
    }
}