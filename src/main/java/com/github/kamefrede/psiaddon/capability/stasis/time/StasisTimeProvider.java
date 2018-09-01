package com.github.kamefrede.psiaddon.capability.stasis.time;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

import static com.github.kamefrede.psiaddon.capability.stasis.time.StasisTimeStorage.CAPABILITY_STASIS_TIME;

public class StasisTimeProvider implements ICapabilitySerializable<NBTTagCompound> {

    IStasisTime instance = CAPABILITY_STASIS_TIME.getDefaultInstance();

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing){
        return capability == CAPABILITY_STASIS_TIME;
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing){
        return hasCapability(capability, facing) ? CAPABILITY_STASIS_TIME.<T>cast(instance) : null;
    }

    @Override
    public NBTTagCompound serializeNBT(){
        return (NBTTagCompound) CAPABILITY_STASIS_TIME.getStorage().writeNBT(CAPABILITY_STASIS_TIME, instance, null);
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt){
        CAPABILITY_STASIS_TIME.getStorage().readNBT(CAPABILITY_STASIS_TIME, instance, null, nbt);
    }
}
