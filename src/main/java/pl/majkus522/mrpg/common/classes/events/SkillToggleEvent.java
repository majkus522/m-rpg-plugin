package pl.majkus522.mrpg.common.classes.events;

import org.bukkit.entity.Player;

public class SkillToggleEvent extends CustomEvent
{
    final Player player;
    final String skill;
    final boolean toggle;

    public SkillToggleEvent(Player player, String skill, boolean toggle)
    {
        this.player = player;
        this.skill = skill;
        this.toggle = toggle;
    }

    public Player getPlayer()
    {
        return player;
    }

    public String getSkill()
    {
        return skill;
    }

    public boolean getToggle()
    {
        return toggle;
    }
}