package xyz.kamefrede.rpsideas.capability;

import xyz.kamefrede.rpsideas.RPSIdeas;
import xyz.kamefrede.rpsideas.items.components.ItemTriggerSensor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import vazkii.psi.api.exosuit.PsiArmorEvent;
import vazkii.psi.api.spell.detonator.IDetonationHandler;

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
        return capability == IDetonationHandler.CAPABILITY;
    }

    @Nullable
    @Override
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
        return capability == IDetonationHandler.CAPABILITY ? IDetonationHandler.CAPABILITY.cast(this) : null;
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

    @Override
    public Vec3d objectLocus() {
        return player.getPositionVector();
    }
}
