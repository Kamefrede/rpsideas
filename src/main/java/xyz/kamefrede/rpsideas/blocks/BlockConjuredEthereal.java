package com.kamefrede.rpsideas.blocks;

import com.kamefrede.rpsideas.tiles.TileEthereal;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BlockConjuredEthereal extends BlockConjuredDirectional {

    public BlockConjuredEthereal() {
        super("conjuredblock", Material.GLASS);
        setLightOpacity(0);
    }

    @Override
    public boolean isSolid(IBlockState state) {
        return true;
    }

    @Override
    public boolean hasSolidProperty() {
        return false;
    }

    @Nullable
    @Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, @Nonnull IBlockAccess worldIn, @Nonnull BlockPos pos) {
        return null;
    }

    @Nonnull
    @Override
    public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state) {
        return new TileEthereal();
    }
}
