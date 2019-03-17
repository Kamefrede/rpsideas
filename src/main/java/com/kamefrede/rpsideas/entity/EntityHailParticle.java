package com.kamefrede.rpsideas.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntityHailParticle extends EntityThrowable {

    private static final DataParameter<String> CASTER_NAME = EntityDataManager.createKey(EntityHailParticle.class, DataSerializers.STRING);
    private static final DataParameter<Float> MASS = EntityDataManager.createKey(EntityHailParticle.class, DataSerializers.FLOAT);
    private static final DataParameter<Float> SIZE = EntityDataManager.createKey(EntityHailParticle.class, DataSerializers.FLOAT);
    private static final DataParameter<ItemStack> COLORIZER_DATA = EntityDataManager.createKey(EntityFancyCircle.class, DataSerializers.ITEM_STACK);

    private static final String TAG_COLORIZER = "colorizer";
    private static final String TAG_TIME_ALIVE = "timeAlive";
    private static final String TAG_CASTER_NAME = "casterName";
    private static final String TAG_SIZE = "size";
    private static final String TAG_MASS = "mass";
    private static final float drag = 0.02f;
    public int timeAlive;


    public EntityHailParticle(World worldIn) {
        super(worldIn);
    }


    public void createParticle(EntityPlayer player, ItemStack colorizer, BlockPos pos, float size, float mass) {
        World world = player.world;
        if (world.getBlockState(pos).causesSuffocation())
            this.setPosition(pos.getX(), pos.getY() + 1.5, pos.getZ());
        this.setPosition(pos.getX(), pos.getY() + 0.5, pos.getZ());
        dataManager.set(COLORIZER_DATA, colorizer);
        dataManager.set(SIZE, size);
        dataManager.set(MASS, mass);
        this.thrower = player;
    }


    @Override
    public void onUpdate() {
        super.onUpdate();
        if (timeAlive++ >= getMaxAlive())
            setDead();
        if (this.motionX > 0 || this.motionZ > 0 || this.motionY > 0) {
            this.motionX *= drag;
            this.motionY *= drag;
            this.motionZ *= drag;
        }


    }

    @Override
    protected void onImpact(RayTraceResult result) {
        Entity entity = result.entityHit;

        if (entity != null) {
            entity.attackEntityFrom(new EntityDamageSourceIndirect("arrow", this, thrower).setProjectile(), (float) Math.sqrt(motionX * motionX + motionY * motionY + motionZ * motionZ) * dataManager.get(MASS));
        }
        setDead();

    }


    @Override
    protected void entityInit() {
        dataManager.register(COLORIZER_DATA, ItemStack.EMPTY);
        dataManager.register(CASTER_NAME, "");
        dataManager.register(MASS, 0f);
        dataManager.register(SIZE, 0f);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        dataManager.set(COLORIZER_DATA, new ItemStack(compound.getCompoundTag(TAG_COLORIZER)));
        dataManager.set(SIZE, compound.getFloat(TAG_SIZE));
        dataManager.set(MASS, compound.getFloat(TAG_MASS));
        dataManager.set(CASTER_NAME, compound.getString(TAG_CASTER_NAME));
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
        compound.setFloat(TAG_SIZE, dataManager.get(SIZE));
        compound.setInteger(TAG_TIME_ALIVE, timeAlive);
    }

    public int getMaxAlive() {
        return (int) (600 * dataManager.get(MASS));
    }

    public int getTimeAlive() {
        return timeAlive;
    }

    public float getSize() {
        return dataManager.get(SIZE);
    }

    @Override
    public void setDead() {
        super.setDead();
    }

    public float getMass() {
        return dataManager.get(MASS);
    }

    @Override
    protected float getGravityVelocity() {
        return 0f;
    }
}
