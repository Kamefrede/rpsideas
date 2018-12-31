package com.kamefrede.rpsideas.entity.botania;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import vazkii.botania.common.entity.EntityManaBurst;
import vazkii.psi.api.cad.ICADColorizer;
import vazkii.psi.common.Psi;

/**
 * @author WireSegal
 * Created at 4:56 PM on 12/20/18.
 */
public class EntityPsiManaBurst extends EntityManaBurst {

    private static final DataParameter<ItemStack> COLORIZER = EntityDataManager.createKey(EntityPsiManaBurst.class, DataSerializers.ITEM_STACK);

    @SuppressWarnings("unused")
    public EntityPsiManaBurst(World world) {
        super(world);
    }

    public EntityPsiManaBurst(EntityPlayer player, EnumHand hand) {
        super(player, hand);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setTag("Colorizer", getColorizer().serializeNBT());
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        setColorizer(new ItemStack(compound.getCompoundTag("Colorizer")));
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        dataManager.register(COLORIZER, ItemStack.EMPTY);
    }

    public ItemStack getColorizer() {
        return dataManager.get(COLORIZER);
    }

    public void setColorizer(ItemStack colorizer) {
        dataManager.set(COLORIZER, colorizer);
    }

    @Override
    public int getColor() {
        ItemStack colorizer = getColorizer();
        if (colorizer.isEmpty() || !(colorizer.getItem() instanceof ICADColorizer))
            return super.getColor();

        return Psi.proxy.getColorizerColor(colorizer).getRGB();
    }
}
