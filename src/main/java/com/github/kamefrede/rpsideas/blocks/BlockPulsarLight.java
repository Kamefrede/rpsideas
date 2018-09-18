package com.github.kamefrede.rpsideas.blocks;

import com.github.kamefrede.rpsideas.tiles.TileConjuredPulsar;
import com.github.kamefrede.rpsideas.tiles.TileEthereal;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
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
import vazkii.psi.common.block.tile.TileConjured;

import javax.annotation.Nullable;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class BlockPulsarLight extends BlockModContainer implements IPsiamBlock  {
    public BlockPulsarLight() {
        super("conjuredpulsarlight", Material.GLASS);
        setLightOpacity(0);
        disableStats();
        translucent = true;

    }

    public static final PropertyBool SOLID = BlockConjured.SOLID;
    public static final PropertyBool LIGHT = BlockConjured.LIGHT;
    public static final PropertyBool BLOCK_UP = BlockConjured.BLOCK_UP;
    public static final PropertyBool BLOCK_DOWN = BlockConjured.BLOCK_DOWN;
    public static final PropertyBool BLOCK_NORTH = BlockConjured.BLOCK_NORTH;
    public static final PropertyBool BLOCK_EAST = BlockConjured.BLOCK_EAST;
    public static final PropertyBool BLOCK_SOUTH = BlockConjured.BLOCK_SOUTH;
    public static final PropertyBool BLOCK_WEST = BlockConjured.BLOCK_WEST;

    private static final EnumMap<EnumFacing, PropertyBool> FACING_MAP = new EnumMap<>(EnumFacing.class);

    static {
        FACING_MAP.put(EnumFacing.UP, BLOCK_UP);
        FACING_MAP.put(EnumFacing.DOWN, BLOCK_DOWN);
        FACING_MAP.put(EnumFacing.NORTH, BLOCK_NORTH);
        FACING_MAP.put(EnumFacing.EAST, BLOCK_EAST);
        FACING_MAP.put(EnumFacing.SOUTH, BLOCK_SOUTH);
        FACING_MAP.put(EnumFacing.WEST, BLOCK_WEST);
    }

    //Blockstate

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, SOLID, LIGHT, BLOCK_UP, BLOCK_DOWN, BLOCK_NORTH, BLOCK_SOUTH, BLOCK_EAST, BLOCK_WEST);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return (state.getValue(SOLID) ? 1 : 0) | (state.getValue(LIGHT) ? 2 : 0);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(SOLID, (meta & 1) != 0).withProperty(LIGHT, (meta & 2) != 0);
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
        IBlockState ret = state;
        for(Map.Entry<EnumFacing, PropertyBool> entry : FACING_MAP.entrySet()) {
            EnumFacing whichWay = entry.getKey();
            PropertyBool prop = entry.getValue();
            ret = state.withProperty(prop, checkBlock(world, pos.offset(whichWay), state));
        }
        return ret;
    }

    private static boolean checkBlock(IBlockAccess world, BlockPos checkPos, IBlockState matchedState) {
        IBlockState checkedState = world.getBlockState(checkPos);
        return checkedState.getBlock() == matchedState.getBlock() && checkedState.getValue(SOLID);
    }

    //block properties

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

    @SideOnly(Side.CLIENT)
    @Override
    public float getAmbientOcclusionLightValue(IBlockState state) {
        return 1f;
    }

    @Override
    public int quantityDropped(Random random) {
        return 0;
    }

    @Override
    protected boolean canSilkHarvest() {
        return false;
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.INVISIBLE;
    }

    @Override
    public int getLightValue(IBlockState state) {
        return state.getValue(LIGHT) ? 15 : 0;
    }

	/*
	@Override
	public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean isActualState) {
		if(state.getValue(SOLID)) {
			addCollisionBoxToList(pos, entityBox, collidingBoxes, FULL_BLOCK_AABB);
		}
		//TODO Can I override getCollisionBoundingBox instead? Or is that like, cached somewhere
	}*/

    @Nullable
    @Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
        return state.getValue(SOLID) ? FULL_BLOCK_AABB : NULL_AABB;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public AxisAlignedBB getSelectedBoundingBox(IBlockState state, World worldIn, BlockPos pos) {
        boolean solid = state.getValue(SOLID);
        double radius = solid ? 1 : 0.75;

        return new AxisAlignedBB(1 - radius, 1 - radius, 1 - radius, radius, radius, radius).offset(pos);
    }

    //tile

    @Override
    public boolean hasTileEntity() {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return null;
    }


    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileConjuredPulsar();
    }

    public boolean canProvidePower(IBlockState state)
    {
        return true;
    }

    public int getWeakPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side)
    {
        return 15;
    }
}
