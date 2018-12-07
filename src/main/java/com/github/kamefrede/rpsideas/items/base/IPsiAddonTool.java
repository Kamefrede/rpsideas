package com.github.kamefrede.rpsideas.items.base;

import vazkii.psi.api.spell.ISpellSettable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import vazkii.arl.util.ItemNBTHelper;
import vazkii.psi.api.cad.ISocketable;
import vazkii.psi.api.spell.Spell;


public interface  IPsiAddonTool extends ISocketable, ISpellSettable {
    String TAG_BULLET_PREFIX = "bullet";
    String TAG_SELECTED_SLOT = "selectedSlot";

    @Override
    default boolean isSocketSlotAvailable(ItemStack stack, int slot) {
        return slot < 3;
    }

    @Override
    default boolean showSlotInRadialMenu(ItemStack stack, int slot) {
        return isSocketSlotAvailable(stack, slot - 1);
    }

    @Override
    default ItemStack getBulletInSocket(ItemStack stack, int slot) {
        String name = TAG_BULLET_PREFIX + slot;
        NBTTagCompound cmp = ItemNBTHelper.getCompound(stack, name, true);

        if(cmp == null)
            return ItemStack.EMPTY;

        return new ItemStack(cmp);
    }

    @Override
    default void setBulletInSocket(ItemStack stack, int slot, ItemStack bullet) {
        String name = TAG_BULLET_PREFIX + slot;
        NBTTagCompound cmp = new NBTTagCompound();

        if(!bullet.isEmpty())
            bullet.writeToNBT(cmp);

        ItemNBTHelper.setCompound(stack, name, cmp);
    }

    @Override
    default int getSelectedSlot(ItemStack stack) {
        return ItemNBTHelper.getInt(stack, TAG_SELECTED_SLOT, 0);
    }

    @Override
    default void setSelectedSlot(ItemStack stack, int slot) {
        ItemNBTHelper.setInt(stack, TAG_SELECTED_SLOT, slot);
    }

    @Override
    default void setSpell(EntityPlayer player, ItemStack stack, Spell spell) {
        int slot = getSelectedSlot(stack);
        ItemStack bullet = getBulletInSocket(stack, slot);
        if(!bullet.isEmpty() && bullet.getItem() instanceof ISpellSettable) {
            ((ISpellSettable) bullet.getItem()).setSpell(player, bullet, spell);
            setBulletInSocket(stack, slot, bullet);
        }
    }

    @Override
    default boolean requiresSneakForSpellSet(ItemStack stack) {
        return false;
    }
}
