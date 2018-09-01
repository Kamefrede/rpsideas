package com.github.kamefrede.psiaddon.capability.stasis.time;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

public class StasisTimeStorage implements Capability.IStorage<IStasisTime> {

    @CapabilityInject(IStasisTime.class)
    public static  final Capability<IStasisTime> CAPABILITY_STASIS_TIME = null;

    @Override
    public NBTBase writeNBT(Capability<IStasisTime> capability, IStasisTime instance, EnumFacing side){
        final NBTTagCompound tag = new NBTTagCompound();
        tag.setDouble("stasistime", instance.getStasisTime());
        return tag;
    }

    @Override
    public void readNBT(Capability<IStasisTime> capability, IStasisTime instance,EnumFacing side, NBTBase nbt) {
        final NBTTagCompound tag = (NBTTagCompound) nbt;
        instance.setStasisTime(tag.getDouble("stasistime"));
    }
}
