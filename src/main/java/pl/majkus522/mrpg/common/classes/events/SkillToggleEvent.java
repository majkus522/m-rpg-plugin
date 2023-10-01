package pl.majkus522.mrpg.common.classes.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class SkillToggleEvent extends Event
{
    static final HandlerList handlers = new HandlerList();
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

    public static HandlerList getHandlerList()
    {
        return handlers;
    }

    @Override
    public HandlerList getHandlers()
    {
        return handlers;
    }
}