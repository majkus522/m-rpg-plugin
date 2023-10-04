package pl.majkus522.mrpg.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import pl.majkus522.mrpg.common.classes.events.SkillUseEvent;

public class OnSkillUsed implements Listener
{
    @EventHandler
    public void onSkillUsed(SkillUseEvent event)
    {
        event.getPlayer().sendMessage("You used skill " + event.getSkill());
    }
}