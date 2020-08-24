package com.kamefrede.rpsideas.entity;

import com.kamefrede.rpsideas.util.GameProfileSerializer;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.client.resources.SkinManager;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jline.utils.Log;
import vazkii.psi.api.internal.Vector3;

import java.util.Map;
import java.util.UUID;

// https://github.com/OpenMods/OpenBlocks/blob/master/src/main/java/openblocks/common/entity/EntityMiniMe.java


public class EntityClone extends EntityLiving implements IEntityAdditionalSpawnData {

    @SideOnly(Side.CLIENT)
    private ResourceLocation locationSkin;

    private GameProfile owner;

    private static final DataParameter<Integer> MAX_ALIVE = EntityDataManager.createKey(EntityClone.class, DataSerializers.VARINT);
    private static final String TAG_MAX_ALIVE = "max_alive";

    public EntityClone(World world) {
        super(world);
        setSize(0.6F, 1.8F);
        isImmuneToFire = true;
        experienceValue = 0;
    }


    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(0.1);
        getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(0.0D);
    }

    @SideOnly(Side.CLIENT)
    public ResourceLocation getSkinResourceLocation() {
        if (owner != null) {
            final SkinManager manager = Minecraft.getMinecraft().getSkinManager();
            Map<MinecraftProfileTexture.Type, MinecraftProfileTexture> map = manager.loadSkinFromCache(owner);

            if (map.containsKey(MinecraftProfileTexture.Type.SKIN)) {
                final MinecraftProfileTexture skin = map.get(MinecraftProfileTexture.Type.SKIN);
                return manager.loadSkin(skin, MinecraftProfileTexture.Type.SKIN);
            } else {
                UUID uuid = EntityPlayer.getUUID(owner);
                return DefaultPlayerSkin.getDefaultSkin(uuid);
            }
        }

        return null;
    }


    @Override
    public void onUpdate() {
        super.onUpdate();
        if (ticksExisted > getLiveTime())
            setDead();
    }

    @Override
    public void onDeath(DamageSource cause) {
        super.onDeath(cause);
    }

    public void setInfo(EntityPlayer player, Vector3 pos, Vector3 facing, int maxAlive, GameProfile owner) {
        dataManager.set(MAX_ALIVE, maxAlive);
        this.owner = owner != null ? TileEntitySkull.updateGameProfile(owner) : null;
        setCustomNameTag(player.getName());


        setPosition(pos.x, pos.y, pos.z);
        getLookHelper().setLookPosition(facing.x, facing.y, facing.z, player.rotationYaw, player.rotationPitch);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        dataManager.register(MAX_ALIVE, 0);
    }


    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        if (owner != null) {
            NBTTagCompound ownerTag = new NBTTagCompound();
            NBTUtil.writeGameProfile(ownerTag, owner);
            compound.setTag("Owner", ownerTag);
        }
        compound.setInteger(TAG_MAX_ALIVE, dataManager.get(MAX_ALIVE));
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        this.owner = readOwner(compound);
        dataManager.set(MAX_ALIVE, compound.getInteger(TAG_MAX_ALIVE));
    }

    @Override
    public boolean attackable() {
        return true;
    }

    public int getLiveTime() {
        return dataManager.get(MAX_ALIVE);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public boolean getAlwaysRenderNameTag() {
        return true;
    }


    @Override
    public boolean canBePushed() {
        return false;
    }

    @Override
    public void onKillCommand() {
        this.setHealth(0.0F);
    }

    @Override
    protected boolean canDespawn() {
        return false;
    }

    @Override
    protected void initEntityAI() {
        //NOOP
    }

    @Override
    public void writeSpawnData(ByteBuf data) {
        if (owner != null) {
            data.writeBoolean(true);
            GameProfileSerializer.write(owner, new PacketBuffer(data));
        } else data.writeBoolean(false);
    }

    @Override
    public void readSpawnData(ByteBuf data) {
        if (data.readBoolean()) {
            this.owner = GameProfileSerializer.read(new PacketBuffer(data));
        }
    }

    private static GameProfile readOwner(NBTTagCompound tag) {
        if (tag.hasKey("owner", Constants.NBT.TAG_STRING)) {
            String ownerName = tag.getString("owner");
            return TileEntitySkull.updateGameProfile(new GameProfile((UUID) null, ownerName));
        } else if (tag.hasKey("OwnerUUID", Constants.NBT.TAG_STRING)) {
            final String uuidStr = tag.getString("OwnerUUID");
            try {
                UUID uuid = UUID.fromString(uuidStr);
                return new GameProfile(uuid, null);
            } catch (IllegalArgumentException e) {
                Log.warn(e, "Failed to parse UUID: %s", uuidStr);
            }
        } else if (tag.hasKey("Owner", Constants.NBT.TAG_COMPOUND)) {
            return NBTUtil.readGameProfileFromNBT(tag.getCompoundTag("Owner"));
        }

        return null;
    }

}
