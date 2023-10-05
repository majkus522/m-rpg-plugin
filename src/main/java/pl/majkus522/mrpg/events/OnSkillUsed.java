package pl.majkus522.mrpg.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import pl.majkus522.mrpg.common.classes.events.SkillUseEvent;
import pl.majkus522.mrpg.controllers.ManaController;

public class OnSkillUsed implements Listener
{
    @EventHandler
    public void onSkillUsed(SkillUseEvent event)
    {
        switch (event.getSkill())
        {
            case "manaGathering":
                ManaController.gatherMana(event.getPlayer());
                break;
        }
    }
}