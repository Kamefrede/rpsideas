package xyz.kamefrede.rpsideas.blocks;

import xyz.kamefrede.rpsideas.tiles.TileConjuredPulsar;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class BlockPulsarLight extends BlockConjuredDirectional {
    public BlockPulsarLight() {
        super("conjuredpulsarlight", Material.GLASS);
        setLightOpacity(0);
    }

    @Override
    public boolean hasSolidProperty() {
        return false;
    }

    @Nonnull
    @Override
    public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state) {
        return new TileConjuredPulsar();
    }

    @Override
    public boolean canProvidePower(IBlockState state) {
        return true;
    }

    @Nonnull
    @Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
        return BlockFaceShape.UNDEFINED;
    }

    public int getWeakPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
        return 15;
    }
}
