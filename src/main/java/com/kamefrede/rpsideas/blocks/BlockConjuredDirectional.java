package com.kamefrede.rpsideas.blocks;

import com.google.common.collect.Maps;
import com.kamefrede.rpsideas.RPSIdeas;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.statemap.IStateMapper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Random;

/**
 * @author WireSegal
 * Created at 3:33 PM on 12/15/18.
 */
public abstract class BlockConjuredDirectional extends BlockModNoItem {
    public static final PropertyBool SOLID = PropertyBool.create("solid");
    public static final PropertyBool BLOCK_UP = PropertyBool.create("block_up");
    public static final PropertyBool BLOCK_DOWN = PropertyBool.create("block_down");
    public static final PropertyBool BLOCK_NORTH = PropertyBool.create("block_north");
    public static final PropertyBool BLOCK_SOUTH = PropertyBool.create("block_south");
    public static final PropertyBool BLOCK_WEST = PropertyBool.create("block_west");
    public static final PropertyBool BLOCK_EAST = PropertyBool.create("block_east");
    protected static final AxisAlignedBB PARTIAL_AABB = new AxisAlignedBB(0.25, 0.25, 0.25, 0.75, 0.75, 0.75);

    public BlockConjuredDirectional(String name, Material materialIn) {
        super(name, materialIn);
    }

    @Override
    public String getModNamespace() {
        return RPSIdeas.MODID;
    }

    public static IProperty[] getDirectionalProperties() {
        return new IProperty[]{BLOCK_UP, BLOCK_DOWN, BLOCK_NORTH, BLOCK_SOUTH, BLOCK_WEST, BLOCK_EAST};
    }

    public static IProperty[] getAllProperties() {
        return new IProperty[]{SOLID, BLOCK_UP, BLOCK_DOWN, BLOCK_NORTH, BLOCK_SOUTH, BLOCK_WEST, BLOCK_EAST};
    }

    public IProperty[] getProperties() {
        if (hasSolidProperty())
            return getAllProperties();
        else
            return getDirectionalProperties();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IStateMapper getStateMapper() {
        return blockIn -> Maps.newHashMap();
    }

    @Nonnull
    @Override
    public IBlockState getActualState(@Nonnull IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        IBlockState origState = state;
        state = state.withProperty(BLOCK_UP, worldIn.getBlockState(pos.up()).equals(origState));
        state = state.withProperty(BLOCK_DOWN, worldIn.getBlockState(pos.down()).equals(origState));
        state = state.withProperty(BLOCK_NORTH, worldIn.getBlockState(pos.north()).equals(origState));
        state = state.withProperty(BLOCK_SOUTH, worldIn.getBlockState(pos.south()).equals(origState));
        state = state.withProperty(BLOCK_WEST, worldIn.getBlockState(pos.west()).equals(origState));
        state = state.withProperty(BLOCK_EAST, worldIn.getBlockState(pos.east()).equals(origState));

        return state;
    }

    public boolean isSolid(IBlockState state) {
        return hasSolidProperty() && state.getValue(SOLID);
    }

    @Nonnull
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, getProperties());
    }

    @Nonnull
    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.INVISIBLE;
    }

    @Override
    public boolean hasTileEntity() {
        return true;
    }

    @Override
    public void breakBlock(@Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull IBlockState state) {
        super.breakBlock(worldIn, pos, state);
        worldIn.removeTileEntity(pos);
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullBlock(IBlockState state) {
        return false;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public int quantityDropped(Random random) {
        return 0;
    }

    @Override
    public boolean canSilkHarvest(World world, BlockPos pos, @Nonnull IBlockState state, EntityPlayer player) {
        return false;
    }

    @Override
    public boolean isPassable(IBlockAccess worldIn, BlockPos pos) {
        return isSolid(worldIn.getBlockState(pos));
    }

    @Nonnull
    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return isSolid(state) ? FULL_BLOCK_AABB : PARTIAL_AABB;
    }

    @Nonnull
    @Override
    public IBlockState getStateFromMeta(int meta) {
        IBlockState state = getDefaultState();
        if (!hasSolidProperty())
            return state;

        return state.withProperty(SOLID, (meta & 1) > 0);
    }

    @Nullable
    @Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, @Nonnull IBlockAccess worldIn, @Nonnull BlockPos pos) {
        return isSolid(blockState) ? super.getCollisionBoundingBox(blockState, worldIn, pos) : null;
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return isSolid(state) ? 1 : 0;
    }

    @Nonnull
    @Override
    public abstract TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state);

    public abstract boolean hasSolidProperty();

}
