package com.kamefrede.rpsideas.compat.tcon;

import com.teamwizardry.librarianlib.features.helpers.ItemNBTHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import slimeknights.tconstruct.library.modifiers.ModifierNBT;
import slimeknights.tconstruct.library.utils.TinkerUtil;
import vazkii.psi.api.cad.ICAD;
import vazkii.psi.api.cad.ISocketableCapability;
import vazkii.psi.api.spell.ISpellAcceptor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static com.kamefrede.rpsideas.compat.tcon.RPSTconCompat.hasSocketeer;

public class RPSTconCapabilitySocketable implements ISocketableCapability, ICapabilityProvider {

    private static String TAG_REGEN_TIME = "regenTime";
    private static String TAG_BULLET_PREFIX = "bullet";
    private static String TAG_SELECTED_SLOT = "selectedSlot";

    private static String identifier = "socketable";


    private final ItemStack stack;


    public RPSTconCapabilitySocketable(ItemStack stack) {
        this.stack = stack;
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == ISocketableCapability.CAPABILITY && hasSocketeer(stack);
    }

    @Nullable
    @Override
    @SuppressWarnings("ConstantConditions")
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
        return capability == ISocketableCapability.CAPABILITY && hasSocketeer(stack) ? ISocketableCapability.CAPABILITY.cast(this) : null;
    }

    @Override
    public boolean isSocketSlotAvailable(int slot) {
        return slot < Math.min(getLevel(stack), 3);
    }

    @Override
    public boolean showSlotInRadialMenu(int slot) {
        return isSocketSlotAvailable(slot - 1);
    }


    @Override
    public ItemStack getBulletInSocket(int slot) {
        String name = TAG_BULLET_PREFIX + slot;
        NBTTagCompound tag = ItemNBTHelper.getCompound(stack, name);
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


        ItemNBTHelper.setCompound(stack, name, cmp);

    }

    @Override
    public int getSelectedSlot() {
        return ItemNBTHelper.getInt(stack, TAG_SELECTED_SLOT, 0);
    }

    @Override
    public void setSelectedSlot(int slot) {
        ItemNBTHelper.setInt(stack, TAG_SELECTED_SLOT, slot);
    }

    @Override
    public boolean isItemValid(int slot, ItemStack bullet) {
        if (!isSocketSlotAvailable(slot))
            return false;

        if (bullet.isEmpty() || !ISpellAcceptor.hasSpell(bullet))
            return false;

        ISpellAcceptor container = ISpellAcceptor.acceptor(bullet);

        return stack.getItem() instanceof ICAD || !container.isCADOnlyContainer();
    }

    @Override
    public boolean canLoopcast(ItemStack stack) {
        return false;
    }

    public int getLevel(ItemStack itemStack) {
        return ModifierNBT.readTag(TinkerUtil.getModifierTag(itemStack, "socketable")).level;
    }

    public int getLevel(NBTTagCompound modifierTag) {
        ModifierNBT.IntegerNBT data = ModifierNBT.readInteger(modifierTag);
        return data.level;
    }


}
