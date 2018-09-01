package com.github.kamefrede.psiaddon.capability.stasis.damage;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

import static com.github.kamefrede.psiaddon.capability.stasis.damage.StasisDamageStorage.CAPABILITY_STASIS_DAMAGE;

public class StasisDamageProvider implements ICapabilitySerializable<NBTTagCompound> {

    IStasisDamage instance = CAPABILITY_STASIS_DAMAGE.getDefaultInstance();

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing){
        return capability == CAPABILITY_STASIS_DAMAGE;
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing){
        return hasCapability(capability, facing) ? CAPABILITY_STASIS_DAMAGE.<T>cast(instance) : null;
    }

    @Override
    public NBTTagCompound serializeNBT(){
        return (NBTTagCompound) CAPABILITY_STASIS_DAMAGE.getStorage().writeNBT(CAPABILITY_STASIS_DAMAGE, instance, null);
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt){
        CAPABILITY_STASIS_DAMAGE.getStorage().readNBT(CAPABILITY_STASIS_DAMAGE, instance, null, nbt);
    }
}
