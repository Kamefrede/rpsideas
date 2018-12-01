package com.github.kamefrede.rpsideas.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import vazkii.psi.api.cad.ICADColorizer;
import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.*;
import vazkii.psi.common.Psi;
import vazkii.psi.common.entity.EntitySpellGrenade;
import vazkii.psi.common.entity.EntitySpellProjectile;
import vazkii.psi.common.spell.trick.entity.PieceTrickAddMotion;

import java.awt.*;
import java.util.function.Consumer;

public class EntitySniperProjectile extends EntityThrowable implements ISpellImmune {
    private static final String TAG_COLORIZER = "colorizer";
    private static final String TAG_BULLET = "bullet";
    private static final String TAG_TIME_ALIVE = "timeAlive";

    private static final String TAG_LAST_MOTION_X = "lastMotionX";
    private static final String TAG_LAST_MOTION_Y = "lastMotionY";
    private static final String TAG_LAST_MOTION_Z = "lastMotionZ";

    private static final String TAG_LOOK_X = "savedLookX";
    private static final String TAG_LOOK_Y = "savedLookY";
    private static final String TAG_LOOK_Z = "savedLookZ";

    private static final DataParameter<ItemStack> COLORIZER_DATA = EntityDataManager.createKey(EntitySniperProjectile.class, DataSerializers.ITEM_STACK);
    private static final DataParameter<ItemStack> BULLET_DATA = EntityDataManager.createKey(EntitySniperProjectile.class, DataSerializers.ITEM_STACK);
    private static final DataParameter<String> CASTER_NAME = EntityDataManager.createKey(EntitySniperProjectile.class, DataSerializers.STRING);

    private static final DataParameter LOOK_X = EntityDataManager.createKey(EntitySniperProjectile.class, DataSerializers.FLOAT);
    private static final DataParameter LOOK_Y = EntityDataManager.createKey(EntitySniperProjectile.class, DataSerializers.FLOAT);
    private static final DataParameter LOOK_Z = EntityDataManager.createKey(EntitySniperProjectile.class, DataSerializers.FLOAT);

    public SpellContext context;
    public int timeAlive;

    public EntitySniperProjectile(World worldIn) {
        super(worldIn);
        setSize(0F, 0F);
    }

    public EntitySniperProjectile(World worldIn, EntityLivingBase throwerIn) {
        super(worldIn, throwerIn);

        shoot(throwerIn, throwerIn.rotationPitch, throwerIn.rotationYaw, 0.0F, 1.5F, 1.0F);
        double speed = 1.5;
        motionX *= speed;
        motionY *= speed;
        motionZ *= speed;
    }

    public EntitySniperProjectile setInfo(EntityPlayer player, ItemStack colorizer, ItemStack bullet) {
        dataManager.set(COLORIZER_DATA, colorizer);
        dataManager.set(BULLET_DATA, bullet);
        dataManager.set(CASTER_NAME, player.getName());

        Vec3d lookVec = player.getLook(1F);
        dataManager.set(LOOK_X, (float) lookVec.x);
        dataManager.set(LOOK_Y, (float) lookVec.y);
        dataManager.set(LOOK_Z, (float) lookVec.z);
        return this;
    }

    @Override
    protected void entityInit() {
        dataManager.register(COLORIZER_DATA, new ItemStack(Blocks.STONE));
        dataManager.register(BULLET_DATA, new ItemStack(Blocks.STONE));
        dataManager.register(CASTER_NAME, "");
        dataManager.register(LOOK_X, 0F);
        dataManager.register(LOOK_Y, 0F);
        dataManager.register(LOOK_Z, 0F);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound tagCompound) {
        super.writeEntityToNBT(tagCompound);

        NBTTagCompound colorizerCmp = new NBTTagCompound();
        ItemStack colorizer = dataManager.get(COLORIZER_DATA);
        if(!colorizer.isEmpty())
            colorizer.writeToNBT(colorizerCmp);
        tagCompound.setTag(TAG_COLORIZER, colorizerCmp);

        NBTTagCompound bulletCmp = new NBTTagCompound();
        ItemStack bullet = dataManager.get(BULLET_DATA);
        if(!bullet.isEmpty())
            bullet.writeToNBT(bulletCmp);
        tagCompound.setTag(TAG_BULLET, bulletCmp);

        tagCompound.setInteger(TAG_TIME_ALIVE, timeAlive);

        tagCompound.setDouble(TAG_LAST_MOTION_X, motionX);
        tagCompound.setDouble(TAG_LAST_MOTION_Y, motionY);
        tagCompound.setDouble(TAG_LAST_MOTION_Z, motionZ);

        tagCompound.setFloat(TAG_LOOK_X, (float) dataManager.get(LOOK_X));
        tagCompound.setFloat(TAG_LOOK_Y, (float) dataManager.get(LOOK_Y));
        tagCompound.setFloat(TAG_LOOK_Z, (float) dataManager.get(LOOK_Z));
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound tagCompound) {
        super.readEntityFromNBT(tagCompound);

        NBTTagCompound colorizerCmp = tagCompound.getCompoundTag(TAG_COLORIZER);
        ItemStack colorizer = new ItemStack(colorizerCmp);
        dataManager.set(COLORIZER_DATA, colorizer);

        NBTTagCompound bulletCmp = tagCompound.getCompoundTag(TAG_BULLET);
        ItemStack bullet = new ItemStack(bulletCmp);
        dataManager.set(BULLET_DATA, bullet);

        EntityLivingBase thrower = getThrower();
        if(thrower != null && thrower instanceof EntityPlayer)
            dataManager.set(CASTER_NAME, ((EntityPlayer) thrower).getName());

        timeAlive = tagCompound.getInteger(TAG_TIME_ALIVE);

        dataManager.set(LOOK_X, tagCompound.getFloat(TAG_LOOK_X));
        dataManager.set(LOOK_Y, tagCompound.getFloat(TAG_LOOK_Y));
        dataManager.set(LOOK_Z, tagCompound.getFloat(TAG_LOOK_Z));

    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        int timeAlive = ticksExisted;
        if(timeAlive > getLiveTime())
            setDead();
        timeAlive++;

        int colorVal = ICADColorizer.DEFAULT_SPELL_COLOR;
        ItemStack colorizer = dataManager.get(COLORIZER_DATA);
        if(!colorizer.isEmpty() && colorizer.getItem() instanceof ICADColorizer)
            colorVal = Psi.proxy.getColorizerColor(colorizer).getRGB();

        Color color = new Color(colorVal);
        float r = color.getRed() / 255F;
        float g = color.getGreen() / 255F;
        float b = color.getBlue() / 255F;

        double x = posX;
        double y = posY;
        double z = posZ;

        Vec3d lookie = getLook(1F);
        Vector3 entLook = new Vector3(lookie.x, lookie.y, lookie.z);

        Vector3 lookOrig = new Vector3(motionX, motionY, motionZ).normalize();
        for(int i = 0; i < getParticleCount(); i++) {
            Vector3 look = lookOrig.copy();
            double spread = 0.6;
            double dist = 0.15;

            look.x += (Math.random() - 0.5) * spread;
            look.y += (Math.random() - 0.5) * spread;
            look.z += (Math.random() - 0.5) * spread;

            look.normalize().multiply(dist);

            Psi.proxy.sparkleFX(getEntityWorld(), x, y, z, r, g, b, (float) look.x, (float) look.y, (float) look.z, 1.2F, 12);
        }
        if(timeAlive > 0){
            addMotion(entLook, (43.299 * Math.log(timeAlive) -29.311));
        }




    }

    @Override
    public Vec3d getLook(float f) {
        float x = (float) dataManager.get(LOOK_X);
        float y = (float) dataManager.get(LOOK_Y);
        float z = (float) dataManager.get(LOOK_Z);
        return new Vec3d(x, y, z);
    }

    public void addMotion(Vector3 dir, double speed){

        dir = dir.copy().normalize().multiply(0.3* speed);

        boolean added = false;

        if(Math.abs(dir.x) > 0.0001) {
                motionX = dir.x;
                added = true;
        }

        if(Math.abs(dir.y) > 0.0001) {
                motionY = dir.y;
                added = true;
            if(motionY >= 0)
                fallDistance = 0;
            }


        if(Math.abs(dir.z) > 0.0001) {
                motionZ = dir.z;
                added = true;
        }

        if(added)
            velocityChanged = true;
    }

    public int getLiveTime() {
        return 600;
    }

    public int getParticleCount() {
        return 12;
    }

    @Override
    protected void onImpact(RayTraceResult pos) {
        if(pos.entityHit != null && pos.entityHit instanceof EntityLivingBase) {
            EntityLivingBase e = (EntityLivingBase) pos.entityHit;
            cast((SpellContext context) -> {
                if (context != null) {
                    context.attackedEntity = e;
                }
            });
        } else cast();
    }

    public void cast() {
        cast(null);
    }

    public void cast(Consumer<SpellContext> callback) {
        Entity thrower = getThrower();
        boolean canCast = false;

        if(thrower != null && thrower instanceof EntityPlayer) {
            ItemStack spellContainer = dataManager.get(BULLET_DATA);
            if(spellContainer != null && spellContainer.getItem() instanceof ISpellContainer) {
                Spell spell = ((ISpellContainer) spellContainer.getItem()).getSpell(spellContainer);
                if(spell != null) {
                    canCast = true;
                    if(context == null)
                        context = new SpellContext().setPlayer((EntityPlayer) thrower).setFocalPoint(this).setSpell(spell);
                    context.setFocalPoint(this);
                }
            }
        }

        if(callback != null)
            callback.accept(context);

        if(canCast && context != null)
            context.cspell.safeExecute(context);

        setDead();
    }

    @Override
    public EntityLivingBase getThrower() {
        EntityLivingBase superThrower = super.getThrower();
        if(superThrower != null)
            return superThrower;

        String name = (String) dataManager.get(CASTER_NAME);
        EntityPlayer player = getEntityWorld().getPlayerEntityByName(name);
        return player;
    }

    @Override
    protected float getGravityVelocity() {
        return 0F;
    }

    @Override
    public boolean isImmune() {
        return true;
    }
}
