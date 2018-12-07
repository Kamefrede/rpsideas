package com.github.kamefrede.rpsideas.tiles;

import com.github.kamefrede.rpsideas.blocks.BlockPulsarLight;
import com.github.kamefrede.rpsideas.blocks.ModBlocks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import vazkii.arl.block.tile.TileMod;
import vazkii.psi.api.cad.ICADColorizer;
import vazkii.psi.common.Psi;


public class TileConjuredPulsar extends TileMod implements ITickable {
    private int time = -1;
    private ItemStack colorizer = ItemStack.EMPTY;

    private int particleCounter = 0;

    public void setTime(int time) {
        this.time = time;
    }

    public void setColorizer(ItemStack colorizer) {
        this.colorizer = colorizer;
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

            IBlockState state = world.getBlockState(pos);
            state = state.getBlock().getActualState(state, world, pos);
            if(state.getBlock() != ModBlocks.conjuredPulsarLight) return;

            if(state.getValue(BlockPulsarLight.SOLID)) {

            } else if(Math.random() < 0.5) {
                doNonSolidParticle(red, green, blue);
            }

            if(particleCounter == 0) {
                doNonSolidParticle(red, green, blue);
            }

            particleCounter %= ++particleCounter % 10;
        } else {
            if(time > 0) time--;
            else if(time == 0) world.setBlockToAir(pos);
        }
    }

    private static void removeEdges(boolean[] edges, int edge1, int edge2, int edge3, int edge4) {
        edges[edge1] = false;
        edges[edge2] = false;
        edges[edge3] = false;
        edges[edge4] = false;
    }


    private void doNonSolidParticle(float red, float green, float blue) {
        double w = 0.15f;
        double h = 0.05f;
        double x = pos.getX() + 0.5 + (Math.random() - 0.5) * w;
        double y = pos.getY() + 0.25 + (Math.random() - 0.5) * h;
        double z = pos.getZ() + 0.5 + (Math.random() - 0.5) * w;
        float s = 0.2f + (float) Math.random() * 0.1f;
        float m = 0.01f + (float) Math.random() * 0.015f;
        Psi.proxy.wispFX(this.world, x, y, z, red, green, blue, s, -m);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        nbt.setInteger("Time", time);
        nbt.setTag("Colorizer", colorizer.serializeNBT());
        return super.writeToNBT(nbt);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        time = nbt.getInteger("Time");
        colorizer = new ItemStack(nbt.getCompoundTag("Colorizer"));
    }

    //Thanks mojang
    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
        return oldState.getBlock() != newState.getBlock();
    }
}
