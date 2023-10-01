package pl.majkus522.mrpg.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import pl.majkus522.mrpg.common.classes.Character;
import pl.majkus522.mrpg.common.classes.events.SkillToggleEvent;
import pl.majkus522.mrpg.controllers.PlayersController;

public class OnSkillToggled implements Listener
{
    @EventHandler
    public void onSkillToggled(SkillToggleEvent event)
    {
        switch (event.getSkill())
        {
            case "manaSense":
                Character character = PlayersController.getCharacter(event.getPlayer());
                if (event.getToggle())
                    character.displayMana();
                else
                    character.hideMana();
                break;
        }
    }
}