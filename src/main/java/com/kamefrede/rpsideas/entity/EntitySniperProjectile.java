package com.kamefrede.rpsideas.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
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
import vazkii.psi.api.spell.ISpellContainer;
import vazkii.psi.api.spell.ISpellImmune;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.common.Psi;

import javax.annotation.Nonnull;
import java.awt.*;
import java.util.function.Consumer;

import static com.kamefrede.rpsideas.entity.EntityGaussPulse.sparkle;

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

    private static final DataParameter<Float> LOOK_X = EntityDataManager.createKey(EntitySniperProjectile.class, DataSerializers.FLOAT);
    private static final DataParameter<Float> LOOK_Y = EntityDataManager.createKey(EntitySniperProjectile.class, DataSerializers.FLOAT);
    private static final DataParameter<Float> LOOK_Z = EntityDataManager.createKey(EntitySniperProjectile.class, DataSerializers.FLOAT);

    public SpellContext context;
    public int timeAlive;

    public EntitySniperProjectile(World worldIn) {
        super(worldIn);
        setSize(0F, 0F);
    }

    public EntitySniperProjectile(World worldIn, EntityLivingBase throwerIn) {
        super(worldIn, throwerIn);

        shoot(throwerIn, throwerIn.rotationPitch, throwerIn.rotationYaw, 0.0F, 2.7F, 0.0F);
        double speed = 1.5;
        motionX *= speed;
        motionY *= speed;
        motionZ *= speed;
    }

    public void setInfo(EntityPlayer player, ItemStack colorizer, ItemStack bullet) {
        dataManager.set(COLORIZER_DATA, colorizer);
        dataManager.set(BULLET_DATA, bullet);
        dataManager.set(CASTER_NAME, player.getName());

        Vec3d lookVec = player.getLook(1F);
        dataManager.set(LOOK_X, (float) lookVec.x);
        dataManager.set(LOOK_Y, (float) lookVec.y);
        dataManager.set(LOOK_Z, (float) lookVec.z);
    }

    @Override
    protected void entityInit() {
        dataManager.register(COLORIZER_DATA, ItemStack.EMPTY);
        dataManager.register(BULLET_DATA, ItemStack.EMPTY);
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
        if (!colorizer.isEmpty())
            colorizer.writeToNBT(colorizerCmp);
        tagCompound.setTag(TAG_COLORIZER, colorizerCmp);

        NBTTagCompound bulletCmp = new NBTTagCompound();
        ItemStack bullet = dataManager.get(BULLET_DATA);
        if (!bullet.isEmpty())
            bullet.writeToNBT(bulletCmp);
        tagCompound.setTag(TAG_BULLET, bulletCmp);

        tagCompound.setInteger(TAG_TIME_ALIVE, timeAlive);

        tagCompound.setDouble(TAG_LAST_MOTION_X, motionX);
        tagCompound.setDouble(TAG_LAST_MOTION_Y, motionY);
        tagCompound.setDouble(TAG_LAST_MOTION_Z, motionZ);

        tagCompound.setFloat(TAG_LOOK_X, dataManager.get(LOOK_X));
        tagCompound.setFloat(TAG_LOOK_Y, dataManager.get(LOOK_Y));
        tagCompound.setFloat(TAG_LOOK_Z, dataManager.get(LOOK_Z));
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
        if (thrower instanceof EntityPlayer)
            dataManager.set(CASTER_NAME, thrower.getName());

        timeAlive = tagCompound.getInteger(TAG_TIME_ALIVE);

        dataManager.set(LOOK_X, tagCompound.getFloat(TAG_LOOK_X));
        dataManager.set(LOOK_Y, tagCompound.getFloat(TAG_LOOK_Y));
        dataManager.set(LOOK_Z, tagCompound.getFloat(TAG_LOOK_Z));

        double lastMotionX = tagCompound.getDouble(TAG_LAST_MOTION_X);
        double lastMotionY = tagCompound.getDouble(TAG_LAST_MOTION_Y);
        double lastMotionZ = tagCompound.getDouble(TAG_LAST_MOTION_Z);

        motionX = lastMotionX;
        motionY = lastMotionY;
        motionZ = lastMotionZ;

    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        int timeAlive = ticksExisted;
        if (timeAlive > getLiveTime())
            setDead();

        int colorVal = ICADColorizer.DEFAULT_SPELL_COLOR;
        ItemStack colorizer = dataManager.get(COLORIZER_DATA);
        if (!colorizer.isEmpty() && colorizer.getItem() instanceof ICADColorizer)
            colorVal = Psi.proxy.getColorizerColor(colorizer).getRGB();

        Color color = new Color(colorVal);
        float r = color.getRed() / 255F;
        float g = color.getGreen() / 255F;
        float b = color.getBlue() / 255F;

        double x = posX;
        double y = posY;
        double z = posZ;

        Vector3 lookOrig = new Vector3(motionX, motionY, motionZ).normalize();
        sparkle(getEntityWorld(), getParticleCount(), lookOrig, x, y, z, r, g, b);
    }

    @Nonnull
    @Override
    public Vec3d getLook(float f) {
        float x = dataManager.get(LOOK_X);
        float y = dataManager.get(LOOK_Y);
        float z = dataManager.get(LOOK_Z);
        return new Vec3d(x, y, z);
    }

    public int getLiveTime() {
        return 600;
    }

    public int getParticleCount() {
        return 6;
    }

    @Override
    protected void onImpact(@Nonnull RayTraceResult pos) {
        if (pos.entityHit instanceof EntityLivingBase) {
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

        if (thrower instanceof EntityPlayer) {
            ItemStack spellContainer = dataManager.get(BULLET_DATA);
            if (spellContainer.getItem() instanceof ISpellContainer) {
                Spell spell = ((ISpellContainer) spellContainer.getItem()).getSpell(spellContainer);
                if (spell != null) {
                    canCast = true;
                    if (context == null)
                        context = new SpellContext().setPlayer((EntityPlayer) thrower).setFocalPoint(this).setSpell(spell);
                    context.setFocalPoint(this);
                }
            }
        }

        if (callback != null)
            callback.accept(context);

        if (canCast && context != null)
            context.cspell.safeExecute(context);

        setDead();
    }

    @Override
    public EntityLivingBase getThrower() {
        EntityLivingBase superThrower = super.getThrower();
        if (superThrower != null)
            return superThrower;

        String name = dataManager.get(CASTER_NAME);
        return getEntityWorld().getPlayerEntityByName(name);
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
