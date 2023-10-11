package pl.majkus522.mrpg.common.classes.effects;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import pl.majkus522.mrpg.Main;
import pl.majkus522.mrpg.common.classes.Character;
import pl.majkus522.mrpg.common.enums.StatusEffectType;
import pl.majkus522.mrpg.common.interfaces.IStatusEffectTarget;

public abstract class StatusEffect
{
    int task;
    int duration;
    IStatusEffectTarget target;

    public StatusEffect(IStatusEffectTarget target, int time)
    {
        this.duration = time;
        this.target = target;
        target.addEffect(this);
        if (getType() != StatusEffectType.secret && getType() != StatusEffectType.hidden && target instanceof Character)
            ((Character)target).player.sendMessage("Status effect " + getType().toColor() + getTitle() + ChatColor.WHITE + " was inflicted on you");
        task = Bukkit.getScheduler().runTaskTimer(Main.plugin, new Runnable()
        {
            @Override
            public void run()
            {
                if (duration > 0)
                    duration--;
                if (duration == 0)
                    end();
                tick();
            }
        }, 0, 20).getTaskId();
    }

    public void tick() { }

    public abstract String getTitle();

    public abstract StatusEffectType getType();

    public void end()
    {
        Bukkit.getScheduler().cancelTask(task);
        target.removeEffect(StatusEffect.this);
        if (getType() != StatusEffectType.secret && getType() != StatusEffectType.hidden && target instanceof Character)
            ((Character)target).player.sendMessage("Status effect " + getType().toColor() + getTitle() + ChatColor.WHITE + " ended");
    }

    public final int getTime()
    {
        return duration;
    }

    public final boolean isInfinite()
    {
        return duration < 0;
    }
}