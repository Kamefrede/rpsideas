package com.kamefrede.rpsideas.entity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class EntityHailParticle extends EntityThrowable {

    private static final DataParameter<String> CASTER_NAME = EntityDataManager.createKey(EntityHailParticle.class, DataSerializers.STRING);
    private static final DataParameter<Float> MASS = EntityDataManager.createKey(EntityHailParticle.class, DataSerializers.FLOAT);
    private static final DataParameter<Float> SIZE = EntityDataManager.createKey(EntityHailParticle.class, DataSerializers.FLOAT);
    private static final DataParameter<ItemStack> COLORIZER_DATA = EntityDataManager.createKey(EntityFancyCircle.class, DataSerializers.ITEM_STACK);
    private static final DataParameter<Integer> MAX_ALIVE = EntityDataManager.createKey(EntityFancyCircle.class, DataSerializers.VARINT);

    private static final String TAG_COLORIZER = "colorizer";
    private static final String TAG_TIME_ALIVE = "timeAlive";
    private static final String TAG_CASTER_NAME = "casterName";
    private static final String TAG_MAX_ALIVE = "maxAlive";
    private static final String TAG_SIZE = "size";
    private static final String TAG_MASS = "mass";
    private static final float drag = 0.98f;
    private static final Vec3d deviation = new Vec3d(0.5, 0.3, 0.2);
    public int timeAlive;


    public EntityHailParticle(World worldIn) {
        super(worldIn);
    }


    //idk maybe have the trick check for it and spawn it accordingly?
    public void createParticle(EntityPlayer player, ItemStack colorizer, BlockPos pos, float size, float mass) {
        BlockPos playerPos = player.getPosition();
        World world = player.world;
        this.setPosition(pos.getX(), pos.getY(), pos.getZ());
        dataManager.set(SIZE, size);
        dataManager.set(MASS, mass);
    }


    //TODO finish the creation code

    //TODO finish the melting code

    @Override
    public void onUpdate() {
        super.onUpdate();
    }

    @Override
    protected void onImpact(RayTraceResult result) {
        //TODO finish the impact code
    }


    @Override
    protected void entityInit() {
        dataManager.register(COLORIZER_DATA, ItemStack.EMPTY);
        dataManager.register(CASTER_NAME, "");
        dataManager.register(MASS, 0f);
        dataManager.register(MAX_ALIVE, 0);
        dataManager.register(SIZE, 0f);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        dataManager.set(COLORIZER_DATA, new ItemStack(compound.getCompoundTag(TAG_COLORIZER)));
        dataManager.set(SIZE, compound.getFloat(TAG_SIZE));
        dataManager.set(MASS, compound.getFloat(TAG_MASS));
        dataManager.set(CASTER_NAME, compound.getString(TAG_CASTER_NAME));
        dataManager.set(MAX_ALIVE, compound.getInteger(TAG_MAX_ALIVE));
        this.timeAlive = compound.getInteger(TAG_TIME_ALIVE);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        NBTTagCompound colorizerCmp = new NBTTagCompound();
        ItemStack colorizer = dataManager.get(COLORIZER_DATA);
        if (!colorizer.isEmpty())
            colorizer.writeToNBT(colorizerCmp);
        compound.setTag(TAG_COLORIZER, colorizerCmp);
        compound.setString(TAG_CASTER_NAME, dataManager.get(CASTER_NAME));

        compound.setFloat(TAG_MASS, dataManager.get(MASS));

        compound.setInteger(TAG_MAX_ALIVE, dataManager.get(MAX_ALIVE));
        compound.setFloat(TAG_SIZE, dataManager.get(SIZE));
        compound.setInteger(TAG_TIME_ALIVE, timeAlive);
    }

    public int getMaxAlive() {
        return dataManager.get(MAX_ALIVE);
    }

    public int getTimeAlive() {
        return timeAlive;
    }

    public float getSize() {
        return dataManager.get(SIZE);
    }

    public float getMass() {
        return dataManager.get(MASS);
    }
}
