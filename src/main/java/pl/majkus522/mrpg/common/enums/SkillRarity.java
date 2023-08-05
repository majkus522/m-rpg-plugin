package pl.majkus522.mrpg.common.enums;

import com.google.gson.annotations.SerializedName;
import org.bukkit.ChatColor;

public enum SkillRarity
{
    @SerializedName("common")
    common,

    @SerializedName("extra")
    extra,

    @SerializedName("unique")
    unique,

    @SerializedName("ultimate")
    ultimate,

    @SerializedName("unknown")
    unknown;

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

    public String toColoredString()
    {
        ChatColor color = ChatColor.WHITE;
        switch (this)
        {
            case extra:
                color = ChatColor.DARK_GREEN;
                break;

            case unique:
                color = ChatColor.BLUE;
                break;

            case ultimate:
                color = ChatColor.DARK_PURPLE;
                break;

            case unknown:
                color = ChatColor.BLACK;
                break;
        }
        return color + toPrettyString();
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

            case "unknown":
                return SkillRarity.unknown;
        }
        return null;
    }
}