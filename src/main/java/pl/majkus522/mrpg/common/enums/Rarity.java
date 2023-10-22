package pl.majkus522.mrpg.common.enums;

import com.google.gson.annotations.SerializedName;
import org.bukkit.ChatColor;

public enum Rarity
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

    public String toPrettyString()
    {
        String string = toString();
        return string.substring(0, 1).toUpperCase() + string.substring(1);
    }

    public String toColoredString()
    {
        return getColor() + toPrettyString();
    }

    public ChatColor getColor()
    {
        switch (this)
        {
            case extra:
                return ChatColor.DARK_GREEN;

            case unique:
                return ChatColor.BLUE;

            case ultimate:
                return ChatColor.DARK_PURPLE;

            case unknown:
                return ChatColor.BLACK;
        }
        return ChatColor.WHITE;
    }

    public static Rarity fromString(String input)
    {
        return Rarity.valueOf(ChatColor.stripColor(input.toLowerCase()));
    }
}