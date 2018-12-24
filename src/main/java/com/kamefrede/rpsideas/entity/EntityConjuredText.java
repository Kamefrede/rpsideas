package com.kamefrede.rpsideas.entity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.psi.api.cad.ICADColorizer;
import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.ISpellImmune;
import vazkii.psi.common.Psi;

import javax.annotation.Nonnull;
import java.awt.*;

public class EntityConjuredText extends Entity implements ISpellImmune {

    private static final DataParameter<ItemStack> COLORIZER_DATA = EntityDataManager.createKey(EntityConjuredText.class, DataSerializers.ITEM_STACK);
    private static final DataParameter<String> TEXT_DATA = EntityDataManager.createKey(EntityConjuredText.class, DataSerializers.STRING);
    private static final DataParameter<String> CASTER_NAME = EntityDataManager.createKey(EntityConjuredText.class, DataSerializers.STRING);

    private static final DataParameter<Float> LOOK_X = EntityDataManager.createKey(EntityConjuredText.class, DataSerializers.FLOAT);
    private static final DataParameter<Float> LOOK_Y = EntityDataManager.createKey(EntityConjuredText.class, DataSerializers.FLOAT);
    private static final DataParameter<Float> LOOK_Z = EntityDataManager.createKey(EntityConjuredText.class, DataSerializers.FLOAT);
    private static final DataParameter<Float> POS_X = EntityDataManager.createKey(EntityConjuredText.class, DataSerializers.FLOAT);
    private static final DataParameter<Float> POS_Y = EntityDataManager.createKey(EntityConjuredText.class, DataSerializers.FLOAT);
    private static final DataParameter<Float> POS_Z = EntityDataManager.createKey(EntityConjuredText.class, DataSerializers.FLOAT);

    private static final String TAG_COLORIZER = "colorizer";
    private static final String TAG_TEXT = "text";
    private static final String TAG_TIME_ALIVE = "timeAlive";
    private static final String TAG_CASTER_NAME = "casterName";

    private static final String TAG_LOOK_X = "savedLookX";
    private static final String TAG_LOOK_Y = "savedLookY";
    private static final String TAG_LOOK_Z = "savedLookZ";

    private static final String TAG_POS_X = "savedPosX";
    private static final String TAG_POS_Y = "savedPosY";
    private static final String TAG_POS_Z = "savedPosZ";


    public int timeAlive;

    public EntityConjuredText(World world){
        super(world);
        setSize(1F, 1F);

    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        int timeAlive = ticksExisted;
        if (timeAlive > getLiveTime())
            setDead();

        FontRenderer renderer = Minecraft.getMinecraft().fontRenderer;

        int colorVal = ICADColorizer.DEFAULT_SPELL_COLOR;
        ItemStack colorizer = dataManager.get(COLORIZER_DATA);
        if (!colorizer.isEmpty() && colorizer.getItem() instanceof ICADColorizer)
            colorVal = Psi.proxy.getColorizerColor(colorizer).getRGB();

        String text = dataManager.get(TEXT_DATA);

        Color color = new Color(colorVal);
        float r = color.getRed() / 255F;
        float g = color.getGreen() / 255F;
        float b = color.getBlue() / 255F;
        Vec3d pos = new Vec3d(posX, posY, posZ);


        setVelocity(0f,0f,0f);
        velocityChanged = true;
    }





    public void setInfo(EntityPlayer player, ItemStack colorizer, String text, Vector3 pos) {
        dataManager.set(COLORIZER_DATA, colorizer);
        dataManager.set(TEXT_DATA, text);
        dataManager.set(CASTER_NAME, player.getName());

        Vec3d lookVec = player.getLook(1F).scale(-1);
        dataManager.set(LOOK_X, (float) lookVec.x);
        dataManager.set(LOOK_Y, (float) lookVec.y);
        dataManager.set(LOOK_Z, (float) lookVec.z);
        Vec3d position = pos.toVec3D();
        dataManager.set(POS_X, (float)position.y);
        dataManager.set(POS_Y, (float)position.x);
        dataManager.set(POS_Z, (float)position.z);
        this.setPosition(position.x, position.y, position.z);
    }

    @Override
    protected void entityInit() {
        dataManager.register(COLORIZER_DATA, ItemStack.EMPTY);
        dataManager.register(TEXT_DATA, "");
        dataManager.register(CASTER_NAME, "");
        dataManager.register(LOOK_X, 0F);
        dataManager.register(LOOK_Y, 0F);
        dataManager.register(LOOK_Z, 0F);
        dataManager.register(POS_X, 0F);
        dataManager.register(POS_Y, 0F);
        dataManager.register(POS_Z, 0F);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound tagCompound) {
        NBTTagCompound colorizerCmp = new NBTTagCompound();
        ItemStack colorizer = dataManager.get(COLORIZER_DATA);
        if (!colorizer.isEmpty())
            colorizer.writeToNBT(colorizerCmp);
        tagCompound.setTag(TAG_COLORIZER, colorizerCmp);
        tagCompound.setString(TAG_CASTER_NAME, dataManager.get(CASTER_NAME));


        String text = dataManager.get(TEXT_DATA);
        tagCompound.setString(TAG_TEXT, text);

        tagCompound.setInteger(TAG_TIME_ALIVE, timeAlive);


        tagCompound.setFloat(TAG_POS_X, dataManager.get(POS_X));
        tagCompound.setFloat(TAG_POS_Y, dataManager.get(POS_Y));
        tagCompound.setFloat(TAG_POS_Z, dataManager.get(POS_Z));

        tagCompound.setFloat(TAG_LOOK_X, dataManager.get(LOOK_X));
        tagCompound.setFloat(TAG_LOOK_Y, dataManager.get(LOOK_Y));
        tagCompound.setFloat(TAG_LOOK_Z, dataManager.get(LOOK_Z));
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound compound) {


        NBTTagCompound colorizerCmp = compound.getCompoundTag(TAG_COLORIZER);
        ItemStack colorizer = new ItemStack(colorizerCmp);
        dataManager.set(COLORIZER_DATA, colorizer);

       String casterName = compound.getString(TAG_CASTER_NAME);
       dataManager.set(CASTER_NAME, casterName);

        String text = compound.getString(TAG_TEXT);
        dataManager.set(TEXT_DATA, text);

        timeAlive = compound.getInteger(TAG_TIME_ALIVE);

        dataManager.set(LOOK_X, compound.getFloat(TAG_LOOK_X));
        dataManager.set(LOOK_Y, compound.getFloat(TAG_LOOK_Y));
        dataManager.set(LOOK_Z, compound.getFloat(TAG_LOOK_Z));
        dataManager.set(POS_X, compound.getFloat(TAG_POS_X));
        dataManager.set(POS_Y, compound.getFloat(TAG_POS_Y));
        dataManager.set(POS_Z, compound.getFloat(TAG_POS_Z));
    }

    @Override
    public boolean isImmune() {
        return true;
    }

    @Nonnull
    @Override
    public Vec3d getLook(float f) {
        float x = dataManager.get(LOOK_X);
        float y = dataManager.get(LOOK_Y);
        float z = dataManager.get(LOOK_Z);
        return new Vec3d(x, y, z);
    }

    public Vec3d getPositionVector(){
        float x = dataManager.get(POS_X);
        float y = dataManager.get(POS_Y);
        float z = dataManager.get(POS_Z);
        return new Vec3d(x,y,z);
    }

    public String getText(){
        return dataManager.get(TEXT_DATA);
    }

    public int getColor(){
        int colorVal = ICADColorizer.DEFAULT_SPELL_COLOR;
        ItemStack colorizer = dataManager.get(COLORIZER_DATA);
        if (!colorizer.isEmpty() && colorizer.getItem() instanceof ICADColorizer)
            colorVal = Psi.proxy.getColorizerColor(colorizer).getRGB();
        return colorVal;
    }

    @Override
    public boolean hasNoGravity() {
        return true;
    }


    public int getLiveTime() {
        return 600;
    }

}
