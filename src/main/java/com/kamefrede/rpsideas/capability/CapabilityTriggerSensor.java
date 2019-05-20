package com.kamefrede.rpsideas.capability;

import com.kamefrede.rpsideas.RPSIdeas;
import com.kamefrede.rpsideas.items.components.ItemTriggerSensor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import vazkii.psi.api.exosuit.ISensorHoldable;
import vazkii.psi.api.exosuit.PsiArmorEvent;
import vazkii.psi.api.spell.detonator.IDetonationHandler;
import vazkii.psi.common.item.armor.ItemPsimetalExosuitHelmet;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class CapabilityTriggerSensor implements IDetonationHandler, ICapabilityProvider {

    public final EntityPlayer player;
    public static final String EVENT_TRIGGER = RPSIdeas.MODID + ".event.spell_detonate";
    public static final String TRIGGER_TICK = RPSIdeas.MODID + ":LastTriggeredDetonation";

    public CapabilityTriggerSensor(EntityPlayer player){
        this.player = player;
    }


    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
        return hasTriggerSensor(player) && capability == IDetonationHandler.CAPABILITY;
    }

    @Nullable
    @Override
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
        return hasTriggerSensor(player) && capability == IDetonationHandler.CAPABILITY ? IDetonationHandler.CAPABILITY.cast(this) : null;
    }

    @Override
    public void detonate() {
        NBTTagCompound playerData = player.getEntityData();
        long detonated = playerData.getLong(TRIGGER_TICK);
        long worldTime = player.world.getTotalWorldTime();

        if (detonated != worldTime) {
            playerData.setLong(TRIGGER_TICK, worldTime);

            PsiArmorEvent.post(new PsiArmorEvent(player, ItemTriggerSensor.EVENT_TRIGGER));
        }
    }

    public boolean hasTriggerSensor(EntityPlayer player){
        ItemStack helmet = player.getItemStackFromSlot(EntityEquipmentSlot.HEAD);
        if(helmet.isEmpty())
            return false;
        if(!(helmet.getItem() instanceof ISensorHoldable))
            return false;
        ISensorHoldable holdable = (ISensorHoldable) helmet.getItem();
        return holdable.getAttachedSensor(helmet).getItem() instanceof ItemTriggerSensor;

    }
}
