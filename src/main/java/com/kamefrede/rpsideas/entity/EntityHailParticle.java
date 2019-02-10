package com.kamefrede.rpsideas.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class EntityHailParticle extends Entity {

    private static final DataParameter<String> CASTER_NAME = EntityDataManager.createKey(EntityHailParticle.class, DataSerializers.STRING);
    private static final DataParameter<Float> MASS = EntityDataManager.createKey(EntityHailParticle.class, DataSerializers.FLOAT);
    private static final DataParameter<Float> DRAG = EntityDataManager.createKey(EntityHailParticle.class, DataSerializers.FLOAT);
    private static final DataParameter<Float> SIZE = EntityDataManager.createKey(EntityHailParticle.class, DataSerializers.FLOAT);
    private static final DataParameter<Float> DEVIATION_X = EntityDataManager.createKey(EntityHailParticle.class, DataSerializers.FLOAT);
    private static final DataParameter<Float> DEVIATION_Y = EntityDataManager.createKey(EntityHailParticle.class, DataSerializers.FLOAT);
    private static final DataParameter<Float> DEVIATION_Z = EntityDataManager.createKey(EntityHailParticle.class, DataSerializers.FLOAT);
    private static final DataParameter<ItemStack> COLORIZER_DATA = EntityDataManager.createKey(EntityFancyCircle.class, DataSerializers.ITEM_STACK);
    private static final DataParameter<Integer> MAX_ALIVE = EntityDataManager.createKey(EntityFancyCircle.class, DataSerializers.VARINT);

    private static final String TAG_COLORIZER = "colorizer";
    private static final String TAG_TIME_ALIVE = "timeAlive";
    private static final String TAG_CASTER_NAME = "casterName";
    private static final String TAG_MAX_ALIVE = "maxAlive";
    private static final String TAG_SIZE = "size";
    private static final String TAG_DEVIATION_X = "deviationX";
    private static final String TAG_DEVIATION_Y = "deviationY";
    private static final String TAG_DEVIATION_Z = "deviationZ";
    private static final String TAG_MASS = "mass";
    private static final String TAG_DRAG = "drag";
    public int timeAlive;


    public EntityHailParticle(World worldIn) {
        super(worldIn);
    }


    //idk maybe have the trick check for it and spawn it accordingly?
    public boolean createParticle(EntityPlayer player, ItemStack colorizer, int radius) {
        BlockPos playerPos = player.getPosition();
        World world = player.world;
        BlockPos h2opos = checkForH2O(world, playerPos, radius);
        if (h2opos != null) {
            //do the thing bob
        }
        if (world.canSnowAt(playerPos, false) || world.canBlockFreezeNoWater(playerPos)) {
            //if there's no water but shit's cold it can still spawn
        }
        return false;
    }

    public BlockPos checkForH2O(World world, BlockPos pos, int radius) {
        BlockPos max = new BlockPos(pos.getX() + radius, pos.getY() + radius, pos.getZ() + radius);
        BlockPos min = new BlockPos(pos.getX() - radius, pos.getY() - radius, pos.getZ() - radius);

        for (int i = min.getX(); i < max.getX(); i++)
            for (int j = min.getY(); j < max.getY(); j++)
                for (int k = min.getZ(); k < max.getZ(); k++) {
                    //check for water or snow or ice h2o really
                }
        return null;
    }

    //TODO finish the creation code
    //TODO finish the impact code
    //TODO finish the melting code

    @Override
    public void onUpdate() {
        super.onUpdate();
    }


    @Override
    protected void entityInit() {
        dataManager.register(COLORIZER_DATA, ItemStack.EMPTY);
        dataManager.register(CASTER_NAME, "");
        dataManager.register(DEVIATION_Y, 0f);
        dataManager.register(DEVIATION_Z, 0f);
        dataManager.register(MASS, 0f);
        dataManager.register(DRAG, 0f);
        dataManager.register(MAX_ALIVE, 0);
        dataManager.register(SIZE, 0f);
        dataManager.register(DEVIATION_X, 0f);
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound compound) {
        dataManager.set(COLORIZER_DATA, new ItemStack(compound.getCompoundTag(TAG_COLORIZER)));
        dataManager.set(SIZE, compound.getFloat(TAG_SIZE));
        dataManager.set(DRAG, compound.getFloat(TAG_DRAG));
        dataManager.set(MASS, compound.getFloat(TAG_DRAG));
        dataManager.set(CASTER_NAME, compound.getString(TAG_CASTER_NAME));
        dataManager.set(DEVIATION_X, compound.getFloat(TAG_DEVIATION_X));
        dataManager.set(DEVIATION_X, compound.getFloat(TAG_DEVIATION_Y));
        dataManager.set(DEVIATION_X, compound.getFloat(TAG_DEVIATION_Z));
        dataManager.set(MAX_ALIVE, compound.getInteger(TAG_MAX_ALIVE));
        this.timeAlive = compound.getInteger(TAG_TIME_ALIVE);
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound compound) {
        NBTTagCompound colorizerCmp = new NBTTagCompound();
        ItemStack colorizer = dataManager.get(COLORIZER_DATA);
        if (!colorizer.isEmpty())
            colorizer.writeToNBT(colorizerCmp);
        compound.setTag(TAG_COLORIZER, colorizerCmp);
        compound.setString(TAG_CASTER_NAME, dataManager.get(CASTER_NAME));

        compound.setFloat(TAG_DEVIATION_X, dataManager.get(DEVIATION_X));
        compound.setFloat(TAG_DEVIATION_Y, dataManager.get(DEVIATION_Y));
        compound.setFloat(TAG_DRAG, dataManager.get(DRAG));
        compound.setFloat(TAG_DEVIATION_Z, dataManager.get(DEVIATION_Z));
        compound.setFloat(TAG_MASS, dataManager.get(MASS));

        compound.setInteger(TAG_MAX_ALIVE, dataManager.get(MAX_ALIVE));
        compound.setFloat(TAG_SIZE, dataManager.get(SIZE));
        compound.setInteger(TAG_TIME_ALIVE, timeAlive);
    }

    public Vec3d getDeviationVector() {
        return new Vec3d(dataManager.get(DEVIATION_X), dataManager.get(DEVIATION_Y), dataManager.get(DEVIATION_Z));
    }

    public float getSize() {
        return dataManager.get(SIZE);
    }

    public float getDrag() {
        return dataManager.get(DRAG);
    }

    public float getMass() {
        return dataManager.get(MASS);
    }
}
