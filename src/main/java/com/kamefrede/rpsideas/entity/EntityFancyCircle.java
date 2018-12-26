package com.kamefrede.rpsideas.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import vazkii.psi.api.cad.ICADColorizer;
import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.ISpellImmune;
import vazkii.psi.common.Psi;

import javax.annotation.Nonnull;


public class EntityFancyCircle extends Entity implements ISpellImmune {
    private static final DataParameter<ItemStack> COLORIZER_DATA = EntityDataManager.createKey(EntityFancyCircle.class, DataSerializers.ITEM_STACK);
    private static final DataParameter<String> CASTER_NAME = EntityDataManager.createKey(EntityFancyCircle.class, DataSerializers.STRING);
    private static final DataParameter<Integer> MAX_ALIVE = EntityDataManager.createKey(EntityFancyCircle.class, DataSerializers.VARINT);
    private static final DataParameter<Float> SCALE_DATA = EntityDataManager.createKey(EntityFancyCircle.class, DataSerializers.FLOAT);
    private static final DataParameter<Float> FACING_X = EntityDataManager.createKey(EntityFancyCircle.class, DataSerializers.FLOAT);
    private static final DataParameter<Float> FACING_Y = EntityDataManager.createKey(EntityFancyCircle.class, DataSerializers.FLOAT);
    private static final DataParameter<Float> FACING_Z = EntityDataManager.createKey(EntityFancyCircle.class, DataSerializers.FLOAT);

    private static final String TAG_COLORIZER = "colorizer";
    private static final String TAG_TIME_ALIVE = "timeAlive";
    private static final String TAG_CASTER_NAME = "casterName";
    private static final String TAG_MAX_ALIVE = "maxAlive";
    private static final String TAG_SCALE = "scaleFactor";
    private static final String TAG_FACING_X = "circleFacingX";
    private static final String TAG_FACING_Y = "circleFacingY";
    private static final String TAG_FACING_Z = "circleFacingZ";


    public EntityFancyCircle(World world){
        super(world);
    }

    public int timeAlive;

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (!world.isRemote && timeAlive++ > dataManager.get(MAX_ALIVE))
            setDead();
        timeAlive++;
    }


    public void setInfo(EntityPlayer player, ItemStack colorizer, Vector3 pos, int maxAlive, float scale, Vec3d vec) {
        dataManager.set(COLORIZER_DATA, colorizer);
        dataManager.set(CASTER_NAME, player.getName());
        dataManager.set(MAX_ALIVE, maxAlive);
        dataManager.set(SCALE_DATA, scale);
        dataManager.set(FACING_X, (float)vec.x);
        dataManager.set(FACING_Y, (float)vec.y);
        dataManager.set(FACING_Z, (float)vec.z);

        this.setPositionAndRotation(pos.x, pos.y, pos.z, player.rotationYaw, player.rotationPitch);
    }

    @Override
    protected void entityInit() {
        dataManager.register(COLORIZER_DATA, ItemStack.EMPTY);
        dataManager.register(CASTER_NAME, "");
        dataManager.register(MAX_ALIVE, 0);
        dataManager.register(SCALE_DATA, 0f);
        dataManager.register(FACING_X, 0f);
        dataManager.register(FACING_Y, 0f);
        dataManager.register(FACING_Z, 0f);
    }

    @Override
    public void writeEntityToNBT(@Nonnull NBTTagCompound tagCompound) {
        NBTTagCompound colorizerCmp = new NBTTagCompound();
        ItemStack colorizer = dataManager.get(COLORIZER_DATA);
        if (!colorizer.isEmpty())
            colorizer.writeToNBT(colorizerCmp);
        tagCompound.setTag(TAG_COLORIZER, colorizerCmp);
        tagCompound.setString(TAG_CASTER_NAME, dataManager.get(CASTER_NAME));

        tagCompound.setFloat(TAG_FACING_X, dataManager.get(FACING_X));
        tagCompound.setFloat(TAG_FACING_Y, dataManager.get(FACING_Y));
        tagCompound.setFloat(TAG_FACING_Z, dataManager.get(FACING_Z));

        tagCompound.setInteger(TAG_MAX_ALIVE, dataManager.get(MAX_ALIVE));
        tagCompound.setFloat(TAG_SCALE, dataManager.get(SCALE_DATA));


        tagCompound.setInteger(TAG_TIME_ALIVE, timeAlive);
    }

    @Override
    protected void readEntityFromNBT(@Nonnull NBTTagCompound compound) {
        dataManager.set(COLORIZER_DATA, new ItemStack(compound.getCompoundTag(TAG_COLORIZER)));
        dataManager.set(CASTER_NAME, compound.getString(TAG_CASTER_NAME));
        dataManager.set(MAX_ALIVE, compound.getInteger(TAG_MAX_ALIVE));
        dataManager.set(SCALE_DATA, compound.getFloat(TAG_SCALE));
        float xFacing = compound.getFloat(TAG_FACING_X);
        float yFacing = compound.getFloat(TAG_FACING_Y);
        float zFacing = compound.getFloat(TAG_FACING_Z);

        float facingMod = (float) MathHelper.fastInvSqrt(xFacing * xFacing + yFacing * yFacing + zFacing * zFacing);

        dataManager.set(FACING_X, xFacing * facingMod);
        dataManager.set(FACING_Y, yFacing * facingMod);
        dataManager.set(FACING_Z, zFacing * facingMod);
    }

    @Override
    public boolean isImmune() {
        return true;
    }


    public int getColor(){
        int colorVal = ICADColorizer.DEFAULT_SPELL_COLOR;
        ItemStack colorizer = dataManager.get(COLORIZER_DATA);
        if (!colorizer.isEmpty() && colorizer.getItem() instanceof ICADColorizer)
            colorVal = Psi.proxy.getColorizerColor(colorizer).getRGB();
        return colorVal;
    }

    public int getLiveTime(){
        return  timeAlive;
    }

    public float getScale(){
        return dataManager.get(SCALE_DATA);
    }

    public Vec3d getDirectionVector() {
        return new Vec3d(dataManager.get(FACING_X), dataManager.get(FACING_Y), dataManager.get(FACING_Z));
    }

    public float getZFacing(){
        return dataManager.get(FACING_Z);
    }

    public float getXFacing(){
        return dataManager.get(FACING_X);
    }

    public float getYFacing(){
        return dataManager.get(FACING_Y);
    }

    @Override
    public boolean hasNoGravity() {
        return true;
    }
}
