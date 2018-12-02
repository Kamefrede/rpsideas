package com.github.kamefrede.rpsideas.blocks;

import com.github.kamefrede.rpsideas.tiles.TileConjuredPulsar;
import com.github.kamefrede.rpsideas.tiles.TileEthereal;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.arl.block.BlockModContainer;
import vazkii.psi.common.block.BlockConjured;

import javax.annotation.Nullable;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class BlockPulsarLight extends BlockModContainer implements IPsiamBlock  {
    public BlockPulsarLight() {
        super("conjuredpulsarlight", Material.GLASS);
        setLightOpacity(0);
        setDefaultState(makeDefaultState());

    }

    public static final PropertyBool SOLID = ConjuredEtherealBlock.SOLID;
    public static final PropertyBool BLOCK_UP = ConjuredEtherealBlock.BLOCK_UP;
    public static final PropertyBool BLOCK_DOWN = ConjuredEtherealBlock.BLOCK_DOWN;
    public static final PropertyBool BLOCK_NORTH = ConjuredEtherealBlock.BLOCK_NORTH;
    public static final PropertyBool BLOCK_SOUTH = ConjuredEtherealBlock.BLOCK_SOUTH;
    public static final PropertyBool BLOCK_WEST = ConjuredEtherealBlock.BLOCK_WEST;
    public static final PropertyBool BLOCK_EAST = ConjuredEtherealBlock.BLOCK_EAST;

    public IBlockState makeDefaultState() {
        return getStateFromMeta(0);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, getAllProperties());
    }


    public IProperty[] getIgnoredProperties() {
        return getAllProperties();
    }

    public IProperty[] getAllProperties() {
        return new IProperty[]{SOLID, BLOCK_UP, BLOCK_DOWN, BLOCK_NORTH, BLOCK_SOUTH, BLOCK_WEST, BLOCK_EAST};
    }


    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.TRANSLUCENT;
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
    public boolean canSilkHarvest(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
        return false;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        IBlockState state = getDefaultState();
        return state.withProperty(SOLID, (meta & 1) > 0);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return (state.getValue(SOLID) ? 1 : 0);
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        IBlockState origState = state;
        state = state.withProperty(BLOCK_UP, worldIn.getBlockState(pos.up()).equals(origState));
        state = state.withProperty(BLOCK_DOWN, worldIn.getBlockState(pos.down()).equals(origState));
        state = state.withProperty(BLOCK_NORTH, worldIn.getBlockState(pos.north()).equals(origState));
        state = state.withProperty(BLOCK_SOUTH, worldIn.getBlockState(pos.south()).equals(origState));
        state = state.withProperty(BLOCK_WEST, worldIn.getBlockState(pos.west()).equals(origState));
        state = state.withProperty(BLOCK_EAST, worldIn.getBlockState(pos.east()).equals(origState));

        return state;
    }

    @Override
    public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {
        return 0;
    }

    @Override
    public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB aabb, List<AxisAlignedBB> list, Entity entity, boolean blarg) {
        if (state.getValue(SOLID))
            addCollisionBoxToList(pos, null, list, null);
    }

    @Override
    public AxisAlignedBB getSelectedBoundingBox(IBlockState state, World world, BlockPos pos) {
        boolean solid = state.getValue(SOLID);
        float f = solid ? 0F : 0.25F;

        double minX = f;
        double minY = f;
        double minZ = f;
        double maxX = 1F - f;
        double maxY = 1F - f;
        double maxZ = 1F - f;

        return new AxisAlignedBB(pos.getX() + minX, pos.getY() + minY, pos.getZ() + minZ, pos.getX() + maxX, pos.getY() + maxY, pos.getZ() + maxZ);
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileConjuredPulsar();
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return null;
    }

    public boolean canProvidePower(IBlockState state)
    {
        return true;
    }

    @Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
        return BlockFaceShape.UNDEFINED;
    }

    public int getWeakPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side)
    {
        return 15;
    }
}
