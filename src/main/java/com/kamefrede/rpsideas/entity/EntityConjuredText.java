package com.kamefrede.rpsideas.entity;

import com.kamefrede.rpsideas.util.helpers.SpellHelpers;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.World;
import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.ISpellImmune;

import javax.annotation.Nonnull;

public class EntityConjuredText extends Entity implements ISpellImmune {

    private static final DataParameter<ItemStack> COLORIZER_DATA = EntityDataManager.createKey(EntityConjuredText.class, DataSerializers.ITEM_STACK);
    private static final DataParameter<String> TEXT_DATA = EntityDataManager.createKey(EntityConjuredText.class, DataSerializers.STRING);
    private static final DataParameter<Integer> MAX_ALIVE = EntityDataManager.createKey(EntityConjuredText.class, DataSerializers.VARINT);

    private static final String TAG_COLORIZER = "colorizer";
    private static final String TAG_TEXT = "text";
    private static final String TAG_MAX_ALIVE = "maxAlive";


    public EntityConjuredText(World world) {
        super(world);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (ticksExisted > getLiveTime())
            setDead();

    }


    public void setInfo(EntityPlayer player, ItemStack colorizer, String text, Vector3 pos, int maxAlive) {
        dataManager.set(COLORIZER_DATA, colorizer);
        dataManager.set(TEXT_DATA, text);
        dataManager.set(MAX_ALIVE, maxAlive);

        this.setPositionAndRotation(pos.x, pos.y, pos.z, player.rotationYaw, player.rotationPitch);
    }

    @Override
    protected void entityInit() {
        dataManager.register(COLORIZER_DATA, ItemStack.EMPTY);
        dataManager.register(TEXT_DATA, "");
        dataManager.register(MAX_ALIVE, 0);
    }

    @Override
    public void writeEntityToNBT(@Nonnull NBTTagCompound tagCompound) {
        NBTTagCompound colorizerCmp = new NBTTagCompound();
        ItemStack colorizer = dataManager.get(COLORIZER_DATA);
        if (!colorizer.isEmpty())
            colorizer.writeToNBT(colorizerCmp);
        tagCompound.setTag(TAG_COLORIZER, colorizerCmp);


        String text = dataManager.get(TEXT_DATA);
        tagCompound.setString(TAG_TEXT, text);
        tagCompound.setInteger(TAG_MAX_ALIVE, dataManager.get(MAX_ALIVE));

    }

    @Override
    protected void readEntityFromNBT(@Nonnull NBTTagCompound compound) {
        dataManager.set(COLORIZER_DATA, new ItemStack(compound.getCompoundTag(TAG_COLORIZER)));
        dataManager.set(TEXT_DATA, compound.getString(TAG_TEXT));
        dataManager.set(MAX_ALIVE, compound.getInteger(TAG_MAX_ALIVE));

    }

    @Override
    public boolean isImmune() {
        return true;
    }

    public String getText() {
        return dataManager.get(TEXT_DATA);
    }

    public int getColor() {
        return SpellHelpers.getColor(dataManager.get(COLORIZER_DATA));
    }

    public int getLiveTime() {
        return dataManager.get(MAX_ALIVE);
    }

    @Override
    public boolean hasNoGravity() {
        return true;
    }
}
