package pl.majkus522.mrpg.common.enums;

import org.bukkit.ChatColor;

public enum SkillRarity
{
    common, extra, unique, ultimate;

    @Override
    public String toString()
    {
        return super.toString();
    }

    public String toPrettyString()
    {
        String string = toString();
        return string.substring(0, 1).toUpperCase() + string.substring(1);
    }

    public static SkillRarity fromString(String input)
    {
        switch (ChatColor.stripColor(input.toLowerCase()))
        {
            case "common":
                return SkillRarity.common;

            case "extra":
                return SkillRarity.extra;

            case "unique":
                return SkillRarity.unique;

            case "ultimate":
                return SkillRarity.ultimate;
        }
        return null;
    }
}