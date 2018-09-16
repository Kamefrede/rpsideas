package com.github.kamefrede.rpsideas.util;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.apache.commons.lang3.tuple.Pair;
import javax.annotation.Nonnull;

public class BlockProperties {

    @Nonnull
    private final IBlockState state;
    @Nonnull
    private final BlockPos pos;
    @Nonnull
    private final World world;

    @Nonnull
    public IBlockState getState() {
        return state;
    }

    @Nonnull
    public BlockPos getPos() {
        return pos;
    }

    @Nonnull
    public World getWorld() {
        return world;
    }

    public BlockProperties(@Nonnull IBlockState state, @Nonnull BlockPos pos, @Nonnull World world) {
        this.state = state;
        this.pos = pos;
        this.world = world;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof BlockProperties && ((BlockProperties) obj).state.equals(state);
    }

    @Override
    public int hashCode() {
        return state.hashCode();
    }

    @Override
    public String toString() {
        return String.format("%s\n@%d %d %d", state.toString(), pos.getX(), pos.getY(), pos.getZ());
    }

    public boolean sideSolid(@Nonnull EnumFacing facing) {
        return state.isSideSolid(world, pos, facing);
    }

    public int comparatorOutput(@Nonnull EnumFacing facing) {
        return MiscPropUtil.internalPropertyComparator.apply(Pair.of(this, facing));
    }

    public float getHardness() {
        return state.getBlockHardness(world, pos);
    }

    public float getLight() {
        return state.getLightValue(world, pos);
    }
}
