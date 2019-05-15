package com.kamefrede.rpsideas.entity;

import com.kamefrede.rpsideas.network.MessageSparkleSphere;
import com.kamefrede.rpsideas.util.helpers.SpellHelpers;
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
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import vazkii.psi.api.internal.Vector3;

import javax.annotation.Nonnull;

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
    private static final float gravity = 0.03f;

    private static final float stableVelocity = 1.5f; //The velocity the particle can move at without losing bonus mass
    private static final float gain = 50f; //Amount of bonus mass gained per tick, out of 1000 total
    private static final float melt = 5f; //The amount of bonus mass lost per unit of velocity over

    private static final float bonusMassAdditive = 0.75f; //Adds the equivalent of this mass to the equation for full bonus mass
    private static final float bonusMassMultiplier = 0.25f; //Adds this to the damage multiplier for a fully grown particle

    private static final float floatingMass = 0.75f; //Maximum mass at which a particle experiences no gravity
    private static final float gravityMass = 1f; //Minimum mass at which there is only gravity influencing the projectile

    public int bonusMass;

    public int timeAlive;

    public EntityHailParticle(World worldIn) {
        super(worldIn);
        setSize(0.25f, 0.25f);
    }

    public void createParticle(EntityPlayer player, ItemStack colorizer, Vector3 pos, float mass) {
        World world = player.world;
        this.setPosition(pos.x, pos.y, pos.z);
        dataManager.set(COLORIZER_DATA, colorizer);
        dataManager.set(MASS, mass);
        dataManager.set(CASTER_NAME, player.getName());
        this.thrower = player;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        double velocityMagnitude = Math.sqrt(motionX * motionX + motionY * motionY + motionZ * motionZ);

        if (timeAlive++ >= getMaxAlive())
            setDead();

        if (velocityMagnitude > getMeltingPoint())
            setDead();

        bonusMass += Math.max(gain * (1 - (velocityMagnitude / stableVelocity)), 0) - Math.max(melt * (velocityMagnitude - stableVelocity), 0);
        bonusMass = Math.min(Math.max(bonusMass, 0), 1000);

        float mass = getMass() + gravityMass * getBonusMassPercent() ;
        if (mass <= floatingMass) {
            if (this.motionX != 0 || this.motionZ != 0 || this.motionY != 0) {
                this.motionX *= drag;
                this.motionY *= drag;
                this.motionZ *= drag;
            }
        } else if (mass >= gravityMass) {
            this.motionY -= Math.min(Math.max(dataManager.get(MASS),0.5),2)*gravity;
        } else {
            float ratio = Math.min(Math.max((mass - floatingMass) / (gravityMass - floatingMass), 0), 1);
            if (this.motionX != 0 || this.motionZ != 0 || this.motionY != 0) {
                this.motionX *= (drag + (1-drag) * ratio);
                this.motionY *= (drag + (1-drag) * ratio);
                this.motionZ *= (drag + (1-drag) * ratio);
            }
            this.motionY -= ratio*Math.min(Math.max(dataManager.get(MASS),0.5),2)*gravity;
        }

        Vec3d position = new Vec3d(posX, posY, posZ);
        Vec3d projected = new Vec3d(posX + motionX, posY + motionY, posZ + motionZ);

        RayTraceResult trace = world.rayTraceBlocks(position, projected, false, true, false);

     /*   if (trace != null)
            projected = new Vec3d(trace.hitVec.x, trace.hitVec.y, trace.hitVec.z);
        Entity entity = findEntityOnPath(position, projected);*/

        if (trace != null)
            onImpact(trace);



    }

    private float calculateDamage() {
        double effectiveMass = getMass() * (1 + bonusMassMultiplier * getBonusMassPercent()) + bonusMassAdditive * getBonusMassPercent();
        return (float) Math.ceil(Math.sqrt(Math.sqrt(motionX * motionX + motionY * motionY + motionZ * motionZ)) * effectiveMass * 6.5) + 1;
    }

    @Override
    protected void onImpact(@Nonnull RayTraceResult result) {
        Entity entity = result.entityHit;
        BlockPos pos = new BlockPos(result.hitVec.x, result.hitVec.y, result.hitVec.z);

        if (entity != null) {
            entity.attackEntityFrom(new EntityDamageSourceIndirect("magic", this, thrower).setProjectile(), calculateDamage());
            setDead();
            return;
        }
        if (!world.getBlockState(pos).isSideSolid(world, pos, result.sideHit)) {
            return;
        }
        setDead();


    }

    /*protected Entity findEntityOnPath(Vec3d start, Vec3d end) {
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
    }*/

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
        return 600;
    }

    @Override
    public void setDead() {
        super.setDead();
        sendToAllAround(PacketHandler.NETWORK, new MessageSparkleSphere(getPositionVector(), EntityGaussPulse.AmmoStatus.PSI), world, getPositionVector(), 128.0);
    }

    public float getMass() {
        return dataManager.get(MASS);
    }

    private float getBonusMassPercent() { return bonusMass / 1000f; }
    
    public int getColor() {
        return SpellHelpers.getColor(dataManager.get(COLORIZER_DATA));
    }

    public double getMeltingPoint() {
        return 75;
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
