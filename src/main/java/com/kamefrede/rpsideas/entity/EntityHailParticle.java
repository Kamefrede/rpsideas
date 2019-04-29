package com.kamefrede.rpsideas.entity;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.kamefrede.rpsideas.network.MessageSparkleSphere;
import com.teamwizardry.librarianlib.features.network.PacketHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import vazkii.psi.api.cad.ICADColorizer;
import vazkii.psi.common.Psi;

import java.util.List;

import static com.teamwizardry.librarianlib.features.network.PacketExtensionKt.sendToAllAround;

public class EntityHailParticle extends EntityThrowable {

    private static final DataParameter<String> CASTER_NAME = EntityDataManager.createKey(EntityHailParticle.class, DataSerializers.STRING);
    private static final DataParameter<Float> MASS = EntityDataManager.createKey(EntityHailParticle.class, DataSerializers.FLOAT);
    private static final DataParameter<ItemStack> COLORIZER_DATA = EntityDataManager.createKey(EntityHailParticle.class, DataSerializers.ITEM_STACK);

    private static final String TAG_COLORIZER = "colorizer";
    private static final String TAG_TIME_ALIVE = "timeAlive";
    private static final String TAG_CASTER_NAME = "casterName";
    private static final String TAG_MASS = "mass";
    private static final float drag = 0.99f;
    public int timeAlive;


    public EntityHailParticle(World worldIn) {
        super(worldIn);
        setSize(0.25f, 0.25f);
    }


    public void createParticle(EntityPlayer player, ItemStack colorizer, BlockPos pos, float mass) {
        World world = player.world;
        if (world.getBlockState(pos).causesSuffocation())
            this.setPosition(pos.getX(), pos.getY() + 1.5, pos.getZ());
        this.setPosition(pos.getX(), pos.getY() + 0.5, pos.getZ());
        dataManager.set(COLORIZER_DATA, colorizer);
        dataManager.set(MASS, mass);
        dataManager.set(CASTER_NAME, player.getName());
        this.thrower = player;
    }


    @Override
    public void onUpdate() {
        super.onUpdate();
        if (timeAlive++ >= getMaxAlive())
            setDead();
        if (this.motionX != 0 || this.motionZ != 0 || this.motionY != 0) {
            this.motionX *= drag;
            this.motionY *= drag;
            this.motionZ *= drag;
        }
        Vec3d position = new Vec3d(posX, posY, posZ);
        Vec3d projected = new Vec3d(posX + motionX, posY + motionY, posZ + motionZ);

        RayTraceResult trace = world.rayTraceBlocks(position, projected, false, true, false);

        if (trace != null)
            projected = new Vec3d(trace.hitVec.x, trace.hitVec.y, trace.hitVec.z);
        Entity entity = findEntityOnPath(position, projected);

        if (trace != null)
            onImpact(trace);

        if (Math.sqrt(motionX * motionX + motionY * motionY + motionZ * motionZ) > getMeltingPoint())
            setDead();


    }

    @Override
    protected void onImpact(RayTraceResult result) {
        Entity entity = result.entityHit;

        if (entity != null) {
            entity.attackEntityFrom(new EntityDamageSourceIndirect("arrow", this, thrower).setProjectile(), (float) Math.ceil(Math.sqrt(motionX * motionX + motionY * motionY + motionZ * motionZ) * dataManager.get(MASS)));
        }
        setDead();

    }

    protected Entity findEntityOnPath(Vec3d start, Vec3d end) {
        Predicate<Entity> predicate = Predicates.and(EntitySelectors.NOT_SPECTATING, EntitySelectors.IS_ALIVE, Entity::canBeCollidedWith, (Entity e) -> e != this);

        Entity entity = null;
        List<Entity> list = world.getEntitiesInAABBexcluding(this, getEntityBoundingBox().expand(motionX, motionY, motionZ).grow(1.0), predicate);
        double maxDistance = 0.0;

        for (Entity onPath : list) {
            AxisAlignedBB boundingBox = onPath.getEntityBoundingBox().grow(0.3);
            RayTraceResult trace = boundingBox.calculateIntercept(start, end);

            if (trace != null) {
                double d1 = start.squareDistanceTo(trace.hitVec);

                if (d1 < maxDistance || maxDistance == 0.0) {
                    entity = onPath;
                    maxDistance = d1;
                }
            }
        }

        return entity;
    }

    @Override
    protected void entityInit() {
        dataManager.register(COLORIZER_DATA, ItemStack.EMPTY);
        dataManager.register(CASTER_NAME, "");
        dataManager.register(MASS, 0f);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        dataManager.set(COLORIZER_DATA, new ItemStack(compound.getCompoundTag(TAG_COLORIZER)));
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
        compound.setInteger(TAG_TIME_ALIVE, timeAlive);
    }

    public int getMaxAlive() {
        return (int) (600 * dataManager.get(MASS));
    }



    @Override
    public void setDead() {
        super.setDead();
        sendToAllAround(PacketHandler.NETWORK, new MessageSparkleSphere(getPositionVector(), EntityGaussPulse.AmmoStatus.PSI), world, getPositionVector(), 128.0);
    }

    public float getMass() {
        return dataManager.get(MASS);
    }

    public int getColor() {
        int colorVal = ICADColorizer.DEFAULT_SPELL_COLOR;
        ItemStack colorizer = dataManager.get(COLORIZER_DATA);
        if (!colorizer.isEmpty() && colorizer.getItem() instanceof ICADColorizer)
            colorVal = Psi.proxy.getColorForColorizer(colorizer);
        return colorVal;
    }

    public double getMeltingPoint() {
        return 300;
    }

    @Override
    protected boolean canTriggerWalking() {
        return false;
    }

    @Override
    public boolean canBeAttackedWithItem() {
        return false;
    }

    @Override
    protected float getGravityVelocity() {
        return 0f;
    }
}
