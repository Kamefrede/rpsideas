package com.github.kamefrede.rpsideas.tiles;

import com.github.kamefrede.rpsideas.blocks.BlockConjuredPulsar;
import com.github.kamefrede.rpsideas.blocks.BlockPulsarLight;
import com.github.kamefrede.rpsideas.blocks.ModBlocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import vazkii.arl.block.tile.TileMod;
import vazkii.psi.api.cad.ICADColorizer;
import vazkii.psi.common.Psi;
import vazkii.psi.common.block.BlockConjured;

import java.awt.*;
import java.util.Arrays;


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
                boolean[] edges = new boolean[12];
                Arrays.fill(edges, false);

                //The fun part
                if(state.getValue(BlockConjured.BLOCK_DOWN)) {
                    removeEdges(edges, 0, 1, 2, 3);
                }

                if(state.getValue(BlockConjured.BLOCK_UP)) {
                    removeEdges(edges, 4, 5, 6, 7);
                }

                if(state.getValue(BlockConjured.BLOCK_NORTH)) {
                    removeEdges(edges, 3, 7, 8, 11);
                }

                if(state.getValue(BlockConjured.BLOCK_SOUTH)) {
                    removeEdges(edges, 1, 5, 9, 10);
                }

                if(state.getValue(BlockConjured.BLOCK_EAST)) {
                    removeEdges(edges, 2, 6, 10, 11);
                }

                if(state.getValue(BlockConjured.BLOCK_WEST)) {
                    removeEdges(edges, 0, 4, 8, 9);
                }

                int x = pos.getX();
                int y = pos.getY();
                int z = pos.getZ();

                doSolidParticle(edges[0], red, green, blue, x + 0, y + 0, z + 0, 0f, 0f, 1f);
                doSolidParticle(edges[1], red, green, blue, x + 0, y + 0, z + 1, 1f, 0f, 0f);
                doSolidParticle(edges[2], red, green, blue, x + 1, y + 0, z + 0, 0f, 0f, 1f);
                doSolidParticle(edges[3], red, green, blue, x + 0, y + 0, z + 0, 1f, 0f, 0f);
                doSolidParticle(edges[4], red, green, blue, x + 0, y + 1, z + 0, 0f, 0f, 1f);
                doSolidParticle(edges[5], red, green, blue, x + 0, y + 1, z + 1, 1f, 0f, 0f);
                doSolidParticle(edges[6], red, green, blue, x + 1, y + 1, z + 0, 0f, 0f, 1f);
                doSolidParticle(edges[7], red, green, blue, x + 0, y + 1, z + 0, 1f, 0f, 0f);
                doSolidParticle(edges[8], red, green, blue, x + 0, y + 0, z + 0, 0f, 1f, 0f);
                doSolidParticle(edges[9], red, green, blue, x + 0, y + 0, z + 1, 0f, 1f, 0f);
                doSolidParticle(edges[10], red, green, blue, x + 1, y + 0, z + 1, 0f, 1f, 0f);
                doSolidParticle(edges[11], red, green, blue, x + 1, y + 0, z + 0, 0f, 1f, 0f);
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

    private void doSolidParticle(boolean doIt, float red, float green, float blue, double x, double y, double z, float xv, float yv, float zv) {
        if(!doIt || Math.random() > 0.3) return;

        Psi.proxy.sparkleFX(world, x, y, z, red, green, blue, xv * 0.1f, yv * 0.1f, zv * 0.1f, 1.25f, 20);
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
