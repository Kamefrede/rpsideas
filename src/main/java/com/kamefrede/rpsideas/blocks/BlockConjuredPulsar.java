package com.kamefrede.rpsideas.blocks;

import com.kamefrede.rpsideas.tiles.TileEthereal;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class BlockConjuredPulsar extends BlockConjuredDirectional {

    public BlockConjuredPulsar() {
        super("conjuredpulsar", Material.GLASS);
        setDefaultState(getDefaultState().withProperty(SOLID, false));
        setLightOpacity(0);
    }

    @Override
    public boolean hasSolidProperty() {
        return true;
    }

    @Nonnull
    @Override
    public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state) {
        return new TileEthereal();
    }

    public boolean canProvidePower(IBlockState state) {
        return true;
    }

    public int getWeakPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
        return 15;
    }
}
