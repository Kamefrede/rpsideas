package com.kamefrede.rpsideas.compat.tcon;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import slimeknights.tconstruct.library.modifiers.ModifierTrait;
import vazkii.psi.api.cad.ISocketableCapability;

public class RPSModSocketable extends ModifierTrait {
    @CapabilityInject(ISocketableCapability.class)
    private static Capability<ISocketableCapability> SOCKETABLE = null;

    protected static final int maxLevel = 4;

    public RPSModSocketable() {
        super("socketable", 0x2d51e2, maxLevel, 0);
    }

    @Override
    public void applyEffect(NBTTagCompound rootCompound, NBTTagCompound modifierTag) {
        //noop
    }

    @Override
    public String getLocalizedDesc() {
        return super.getLocalizedDesc();
    }

    @Override
    public String getTooltip(NBTTagCompound modifierTag, boolean detailed) {
        return super.getTooltip(modifierTag, detailed);
    }


}
