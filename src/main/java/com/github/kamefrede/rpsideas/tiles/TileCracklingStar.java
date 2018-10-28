package com.github.kamefrede.rpsideas.tiles;

import net.minecraft.block.state.IBlockState;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import vazkii.arl.block.tile.TileMod;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.common.util.Constants;
import vazkii.psi.api.cad.ICADColorizer;
import vazkii.psi.api.internal.Vector3;
import vazkii.psi.common.Psi;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Set;

public class TileCracklingStar extends TileMod implements ITickable {
    private int time = -1;
    private ItemStack colorizer = ItemStack.EMPTY;
    private Set<Vec3d> rays = new HashSet<>();

    public void setTime(int time) {
        this.time = time;
    }

    public void setColorizer(ItemStack colorizer) {
        this.colorizer = colorizer;
    }

    public void addRay(Vec3d ray) {
        rays.add(ray);
    }

    @Override
    public void update() {
        if(world.isRemote) {
            int color;
            if(colorizer.isEmpty()) {
                color = ICADColorizer.DEFAULT_SPELL_COLOR;
            } else {
                color = ((ICADColorizer)colorizer.getItem()).getColor(colorizer);
            }

            float red = ((color & 0xFF0000) >> 16) / 255f;
            float green = ((color & 0x00FF00) >> 8) / 255f;
            float blue = (color & 0x0000FF) / 255f;

            for(Vec3d ray : rays) {
                makeLine(ray, red, green, blue);
            }

            Psi.proxy.wispFX(world, pos.getX() + .5, pos.getY() + .5, pos.getZ() + .5, red, green, blue, 0.25f);
        } else {
            if(time > 0) time--;
            else if(time == 0) world.setBlockToAir(pos);
        }
    }

    private void makeLine(Vec3d vec, float red, float green, float blue) {
        Vector3 start = Vector3.fromBlockPos(pos).add(.5 + (Math.random() - .5) * 0.05, .5 + (Math.random() - .5) * 0.05, .5 + (Math.random() - .5) * 0.05);
        double stepsPer = (Math.random() * 6d) + 0.0001;

        double length = vec.length();
        if(length == 0) length = 0.0001; //will this really happen? ¯\_(ツ)_/¯
        Vec3d ray = vec.scale(1 / length);
        int stepCount = (int) (length * stepsPer);

        for(int step = 0; step < stepCount; step++) {
            Vec3d ext = ray.scale(step / stepsPer);

            Psi.proxy.wispFX(world, start.x + ext.x, start.y + ext.y, start.z + ext.z, red, green, blue, 0.125f);
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        nbt.setInteger("Time", time);
        nbt.setTag("Colorizer", colorizer.serializeNBT());

        NBTTagList rayList = new NBTTagList();
        for(Vec3d ray : rays) {
            NBTTagCompound rayCmp = new NBTTagCompound();
            rayCmp.setDouble("x", ray.x);
            rayCmp.setDouble("y", ray.y);
            rayCmp.setDouble("z", ray.z);
            rayList.appendTag(rayCmp);
        }
        nbt.setTag("Rays", rayList);

        return super.writeToNBT(nbt);
    }

    @Override
    public void markDirty() {
        super.markDirty();
        notifyBlockUpdate();
    }

    private void notifyBlockUpdate() {
        IBlockState state = world.getBlockState(pos);
        world.notifyBlockUpdate(pos, state, state, 3);
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
        return oldState.getBlock() != newState.getBlock();
    }

    @Nullable
    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(pos, 0, getUpdateTag());
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        return writeToNBT(new NBTTagCompound());
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        readFromNBT(pkt.getNbtCompound());
        notifyBlockUpdate();
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);

        time = nbt.getInteger("Time");
        colorizer = new ItemStack(nbt.getCompoundTag("Colorizer"));

        rays.clear();
        NBTTagList rayList = nbt.getTagList("Rays", Constants.NBT.TAG_COMPOUND);
        for(int rayIndex = 0; rayIndex < rayList.tagCount(); rayIndex++) {
            NBTTagCompound rayCmp = rayList.getCompoundTagAt(rayIndex);

            rays.add(new Vec3d(rayCmp.getDouble("x"), rayCmp.getDouble("y"), rayCmp.getDouble("z")));
        }
    }
}
