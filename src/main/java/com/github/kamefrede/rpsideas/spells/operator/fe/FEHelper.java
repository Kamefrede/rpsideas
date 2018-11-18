package com.github.kamefrede.rpsideas.spells.operator.fe;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

public class FEHelper {

    public static IEnergyStorage checkIfCapabilityExists(TileEntity ent){
        if(ent == null) return null;
        if(ent.hasCapability(CapabilityEnergy.ENERGY, null)){
            IEnergyStorage storage = ent.getCapability(CapabilityEnergy.ENERGY, null);
            assert storage != null;
            return storage;
        }
        return null;
    }
}
