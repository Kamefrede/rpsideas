package com.kamefrede.rpsideas.tiles;

import com.kamefrede.rpsideas.blocks.BlockConjuredDirectional;
import com.kamefrede.rpsideas.util.helpers.SpellHelpers;
import com.kamefrede.rpsideas.util.libs.RPSBlockNames;
import com.teamwizardry.librarianlib.features.autoregister.TileRegister;
import com.teamwizardry.librarianlib.features.base.block.tile.TileModTickable;
import com.teamwizardry.librarianlib.features.saving.Save;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import org.jetbrains.annotations.NotNull;
import vazkii.psi.common.Psi;

import java.util.Arrays;

@TileRegister(RPSBlockNames.CONJURED_ETHEREAL_BLOCK)
public class TileEthereal extends TileModTickable {

    @Save
    public int time = -1;
    @Save
    public ItemStack colorizer = ItemStack.EMPTY;

    private long fellAt = -1;

    @Override
    public void tick() {
        if (getWorld().isRemote) {
            int color = SpellHelpers.getColor(colorizer);

            float r = SpellHelpers.getR(color);
            float g = SpellHelpers.getG(color);
            float b = SpellHelpers.getB(color);

            IBlockState state = getWorld().getBlockState(getPos());
            state = state.getBlock().getActualState(state, getWorld(), getPos());

            boolean[] edges = new boolean[12];
            Arrays.fill(edges, true);

            if (state.getValue(BlockConjuredDirectional.BLOCK_DOWN))
                removeEdges(edges, 0, 1, 2, 3);
            if (state.getValue(BlockConjuredDirectional.BLOCK_UP))
                removeEdges(edges, 4, 5, 6, 7);
            if (state.getValue(BlockConjuredDirectional.BLOCK_NORTH))
                removeEdges(edges, 3, 7, 8, 11);
            if (state.getValue(BlockConjuredDirectional.BLOCK_SOUTH))
                removeEdges(edges, 1, 5, 9, 10);
            if (state.getValue(BlockConjuredDirectional.BLOCK_EAST))
                removeEdges(edges, 2, 6, 10, 11);
            if (state.getValue(BlockConjuredDirectional.BLOCK_WEST))
                removeEdges(edges, 0, 4, 8, 9);

            double x = getPos().getX();
            double y = getPos().getY();
            double z = getPos().getZ();

            // Bottom
            makeParticle(edges[0], r, g, b, x + 0, y + 0, z + 0, 0, 0, 1);
            makeParticle(edges[1], r, g, b, x + 0, y + 0, z + 1, 1, 0, 0);
            makeParticle(edges[2], r, g, b, x + 1, y + 0, z + 0, 0, 0, 1);
            makeParticle(edges[3], r, g, b, x + 0, y + 0, z + 0, 1, 0, 0);

            // Top
            makeParticle(edges[4], r, g, b, x + 0, y + 1, z + 0, 0, 0, 1);
            makeParticle(edges[5], r, g, b, x + 0, y + 1, z + 1, 1, 0, 0);
            makeParticle(edges[6], r, g, b, x + 1, y + 1, z + 0, 0, 0, 1);
            makeParticle(edges[7], r, g, b, x + 0, y + 1, z + 0, 1, 0, 0);

            // Sides
            makeParticle(edges[8], r, g, b, x + 0, y + 0, z + 0, 0, 1, 0);
            makeParticle(edges[9], r, g, b, x + 0, y + 0, z + 1, 0, 1, 0);
            makeParticle(edges[10], r, g, b, x + 1, y + 0, z + 1, 0, 1, 0);
            makeParticle(edges[11], r, g, b, x + 1, y + 0, z + 0, 0, 1, 0);

        }


        if (time < 0)
            return;

        if (fellAt >= 0) {
            long now = world.getTotalWorldTime();
            long missed = now - fellAt;
            time -= Math.min(time, missed);
            fellAt = -1;
        }

        if (time == 0)
            getWorld().setBlockToAir(getPos());
        else time--;
    }

    public void makeParticle(boolean doit, float r, float g, float b, double xp, double yp, double zp, double xv, double yv, double zv) {
        if (doit && Math.random() < 0.3) {
            float m = 0.1F;
            xv *= m;
            yv *= m;
            zv *= m;

            double x = xp + Math.random() * xv;
            double y = yp + Math.random() * yv;
            double z = zp + Math.random() * zv;
            Psi.proxy.sparkleFX(getWorld(), x, y, z, r, g, b, (float) xv, (float) yv, (float) zv, 1.25F, 20);
        }
    }

    public void removeEdges(boolean[] edges, int... posArray) {
        for (int i : posArray)
            edges[i] = false;
    }

    @Override
    public void readCustomNBT(@NotNull NBTTagCompound cmp) {
        if (cmp.hasKey("FellAt"))
            fellAt = cmp.getLong("FellAt");
    }

}
