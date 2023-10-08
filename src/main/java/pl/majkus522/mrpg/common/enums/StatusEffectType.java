package pl.majkus522.mrpg.common.enums;

import org.bukkit.ChatColor;

public enum StatusEffectType
{
    neutral, positive, negative, hidden, secret;

    public ChatColor toColor()
    {
        switch (this)
        {
            case positive:
                return ChatColor.GREEN;

            case negative:
                return ChatColor.RED;

            case hidden:
                return ChatColor.BLUE;

            case secret:
                return ChatColor.MAGIC;
        }
        return ChatColor.WHITE;
    }
}