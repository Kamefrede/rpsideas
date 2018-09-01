package com.github.kamefrede.psiaddon.capability.stasis.damage;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

public class StasisDamageStorage implements Capability.IStorage<IStasisDamage> {

    @CapabilityInject(IStasisDamage.class)
    public static  final Capability<IStasisDamage> CAPABILITY_STASIS_DAMAGE = null;

    @Override
    public NBTBase writeNBT(Capability<IStasisDamage> capability, IStasisDamage instance, EnumFacing side){
        final NBTTagCompound tag = new NBTTagCompound();
        tag.setFloat("stasisdamage", instance.getDamageReceived());
        return tag;
    }

    @Override
    public void readNBT(Capability<IStasisDamage> capability, IStasisDamage instance,EnumFacing side, NBTBase nbt){
        final NBTTagCompound tag = (NBTTagCompound) nbt;
        instance.setDamageReceived(tag.getFloat("stasisdamage"));
    }

}
