package pl.majkus522.mrpg.common.classes.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import javax.annotation.Nonnull;

public abstract class CustomEvent extends Event
{
    static final HandlerList handlers = new HandlerList();

    public static HandlerList getHandlerList()
    {
        return handlers;
    }

    @Override
    @Nonnull
    public HandlerList getHandlers()
    {
        return handlers;
    }
}