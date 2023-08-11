package pl.majkus522.mrpg.controllers;

import net.minecraft.nbt.CompoundTag;
import org.bukkit.craftbukkit.v1_20_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

public class NBTController
{
    public static ItemStack putNBTString(ItemStack item, String key, String value)
    {
        net.minecraft.world.item.ItemStack nmsCopy = CraftItemStack.asNMSCopy(item);
        CompoundTag tag = nmsCopy.getOrCreateTag();
        tag.putString(key, value);
        nmsCopy.setTag(tag);
        return CraftItemStack.asBukkitCopy(nmsCopy);
    }

    public static String getNBTString(ItemStack item, String key)
    {
        return CraftItemStack.asNMSCopy(item).getOrCreateTag().getString(key);
    }

    public static boolean hasNBTTag(ItemStack item, String key)
    {
        return CraftItemStack.asNMSCopy(item).getOrCreateTag().contains(key);
    }
}