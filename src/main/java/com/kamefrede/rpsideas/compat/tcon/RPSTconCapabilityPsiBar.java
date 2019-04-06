package com.kamefrede.rpsideas.compat.tcon;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import vazkii.psi.api.cad.IPsiBarDisplay;
import vazkii.psi.api.internal.IPlayerData;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static com.kamefrede.rpsideas.compat.tcon.RPSTconCompat.hasSocketeer;

public class RPSTconCapabilityPsiBar implements IPsiBarDisplay, ICapabilityProvider {

    public final ItemStack stack;

    public RPSTconCapabilityPsiBar(ItemStack stack) {
        this.stack = stack;
    }

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == IPsiBarDisplay.CAPABILITY && hasSocketeer(stack);
    }

    @Nullable
    @Override
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
        return capability == IPsiBarDisplay.CAPABILITY && hasSocketeer(stack) ? IPsiBarDisplay.CAPABILITY.cast(this) : null;
    }

    @Override
    public boolean shouldShow(IPlayerData data) {
        return true;
    }
}
