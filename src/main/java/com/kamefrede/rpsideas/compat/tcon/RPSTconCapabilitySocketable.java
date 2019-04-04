package com.kamefrede.rpsideas.compat.tcon;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import slimeknights.tconstruct.library.modifiers.ModifierNBT;
import slimeknights.tconstruct.library.utils.TinkerUtil;
import vazkii.psi.api.cad.ICAD;
import vazkii.psi.api.cad.ISocketableCapability;
import vazkii.psi.api.spell.ISpellContainer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class RPSTconCapabilitySocketable implements ISocketableCapability, ICapabilityProvider {

    String TAG_REGEN_TIME = "regenTime";
    String TAG_BULLET_PREFIX = "bullet";
    String TAG_SELECTED_SLOT = "selectedSlot";

    String identifier = "socketable";


    private final ItemStack stack;

    public RPSTconCapabilitySocketable(ItemStack stack) {
        this.stack = stack;
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == ISocketableCapability.CAPABILITY;
    }

    @Nullable
    @Override
    @SuppressWarnings("ConstantConditions")
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
        return capability == ISocketableCapability.CAPABILITY ? ISocketableCapability.CAPABILITY.cast(this) : null;
    }

    @Override
    public boolean isSocketSlotAvailable(int slot) {
        return slot < getLevel(stack);
    }

    @Override
    public boolean showSlotInRadialMenu(int slot) {
        return isSocketSlotAvailable(slot - 1);
    }


    @Override
    public ItemStack getBulletInSocket(int slot) {
        String name = TAG_BULLET_PREFIX + slot;
        NBTTagCompound tag = TinkerUtil.getModifierTag(stack, identifier);
        if (tag == null)
            return ItemStack.EMPTY;

        return new ItemStack(tag);
    }

    @Override
    public void setBulletInSocket(int slot, ItemStack bullet) {
        String name = TAG_BULLET_PREFIX + slot;
        NBTTagCompound cmp = new NBTTagCompound();

        if (!bullet.isEmpty())
            bullet.writeToNBT(cmp);

        TinkerUtil.getModifierTag(stack, identifier).setTag(name, cmp);

    }

    @Override
    public int getSelectedSlot() {
        return TinkerUtil.getModifierTag(stack, identifier).getInteger(TAG_SELECTED_SLOT);
    }

    @Override
    public void setSelectedSlot(int slot) {
        TinkerUtil.getModifierTag(stack, identifier).setInteger(TAG_SELECTED_SLOT, slot);
    }

    @Override
    public boolean isItemValid(int slot, ItemStack bullet) {
        if (!isSocketSlotAvailable(slot))
            return false;

        if (bullet.isEmpty() || !(bullet.getItem() instanceof ISpellContainer))
            return false;

        ISpellContainer container = (ISpellContainer) bullet.getItem();

        if (!container.containsSpell(bullet))
            return false;

        return stack.getItem() instanceof ICAD || !container.isCADOnlyContainer(bullet);
    }

    @Override
    public boolean canLoopcast(ItemStack stack) {
        return false;
    }


    public int getLevel(ItemStack itemStack) {
        return ModifierNBT.readTag(TinkerUtil.getModifierTag(itemStack, "socketable")).level - 1;
    }

    public int getLevel(NBTTagCompound modifierTag) {
        ModifierNBT.IntegerNBT data = ModifierNBT.readInteger(modifierTag);
        return data.level - 1;
    }
}
