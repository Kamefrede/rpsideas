package com.github.kamefrede.rpsideas.util.helpers;


import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nullable;

//thanks quat
public class ItemNBTHelpers {
    @Nullable
    public static NBTTagCompound getCompound(ItemStack stack, String key) {
        if(!stack.hasTagCompound()) return null;
        if(!stack.getTagCompound().hasKey(key)) return null;
        return stack.getTagCompound().getCompoundTag(key);
    }

    public static void setCompound(ItemStack stack, String key, NBTTagCompound compound) {
        ensureHasCompound(stack);
        stack.getTagCompound().setTag(key, compound);
    }

    public static ItemStack getItemStack(ItemStack stack, String key) {
        if(!stack.hasTagCompound()) return ItemStack.EMPTY;
        if(!stack.getTagCompound().hasKey(key)) return ItemStack.EMPTY;
        NBTTagCompound stackNBT = stack.getTagCompound().getCompoundTag(key);
        return new ItemStack(stackNBT);
    }

    public static void setItemStack(ItemStack stack, String key, ItemStack stackToSet) {
        ensureHasCompound(stack);
        stack.getTagCompound().setTag(key, stackToSet.serializeNBT());
    }

    public static int getIntegerOrDefault(ItemStack stack, String key, int def) {
        if(stack.isEmpty()) return def;
        if(!stack.hasTagCompound()) return def;
        if(!stack.getTagCompound().hasKey(key)) return def;

        return stack.getTagCompound().getInteger(key);
    }

    public static void removeTag(ItemStack stack, String key) {
        if(!stack.hasTagCompound()) return; //WOW guess it's already gone, amazing

        stack.getTagCompound().removeTag(key);
    }

    private static void ensureHasCompound(ItemStack stack) {
        if(!stack.hasTagCompound()) stack.setTagCompound(new NBTTagCompound());
    }
}
