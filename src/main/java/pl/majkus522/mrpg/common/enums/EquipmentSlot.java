package pl.majkus522.mrpg.common.enums;

import org.bukkit.Material;

public enum EquipmentSlot
{
    helmet, chestplate, leggings, boots;

    public int toIndex()
    {
        switch (this)
        {
            case helmet:
                return 0;

            case chestplate:
                return 1;

            case leggings:
                return 2;

            case boots:
                return 3;
        }
        return -1;
    }

    public static EquipmentSlot fromMaterial(Material material)
    {
        switch (material)
        {
            case CHAINMAIL_HELMET:
            case DIAMOND_HELMET:
            case GOLDEN_HELMET:
            case IRON_HELMET:
            case LEATHER_HELMET:
            case NETHERITE_HELMET:
            case TURTLE_HELMET:
                return helmet;

            case CHAINMAIL_CHESTPLATE:
            case DIAMOND_CHESTPLATE:
            case GOLDEN_CHESTPLATE:
            case IRON_CHESTPLATE:
            case LEATHER_CHESTPLATE:
            case NETHERITE_CHESTPLATE:
                return chestplate;

            case LEATHER_LEGGINGS:
            case CHAINMAIL_LEGGINGS:
            case DIAMOND_LEGGINGS:
            case GOLDEN_LEGGINGS:
            case IRON_LEGGINGS:
            case NETHERITE_LEGGINGS:
                return leggings;

            case CHAINMAIL_BOOTS:
            case DIAMOND_BOOTS:
            case GOLDEN_BOOTS:
            case IRON_BOOTS:
            case LEATHER_BOOTS:
            case NETHERITE_BOOTS:
                return boots;
        }
        return null;
    }
}