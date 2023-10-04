package pl.majkus522.mrpg.common.classes.events;

import org.bukkit.entity.Player;
import pl.majkus522.mrpg.controllers.PlayersController;

public class SkillUseEvent extends CustomEvent
{
    final Player player;
    final String skill;

    public SkillUseEvent(Player player, int slot)
    {
        this.player = player;
        this.skill = PlayersController.getCharacter(player).getAssagnedSkill(slot);
    }

    public Player getPlayer()
    {
        return player;
    }

    public String getSkill()
    {
        return skill;
    }
}