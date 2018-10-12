package com.github.kamefrede.rpsideas.blocks;

import com.github.kamefrede.rpsideas.tiles.TileEthereal;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.arl.block.BlockModContainer;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class BlockConjuredGravityBlock extends BlockModContainer implements IPsiamBlock {
    public static boolean fallInstantly;
    public BlockConjuredGravityBlock() {
        super("conjured_gravity_block", Material.SAND);
        setDefaultState(makeDefaultState());
        setLightOpacity(0);

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
        return new IProperty[] { SOLID, BLOCK_UP, BLOCK_DOWN, BLOCK_NORTH, BLOCK_SOUTH, BLOCK_WEST, BLOCK_EAST };
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
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileEthereal();
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return null;
    }

    /**
     * copypasta from blockfalling begins here
     */

    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
    {
        worldIn.scheduleUpdate(pos, this, this.tickRate(worldIn));
    }

    /**
     * Called when a neighboring block was changed and marks that this state should perform any checks during a neighbor
     * change. Cases may include when redstone power is updated, cactus blocks popping off due to a neighboring solid
     * block, etc.
     */
    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos)
    {
        worldIn.scheduleUpdate(pos, this, this.tickRate(worldIn));
    }

    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
    {
        if (!worldIn.isRemote)
        {
            this.checkFallable(worldIn, pos);
        }
    }




    private void checkFallable(World worldIn, BlockPos pos)
    {
        if ((worldIn.isAirBlock(pos.down()) || canFallThrough(worldIn.getBlockState(pos.down()))) && pos.getY() >= 0)
        {
            int i = 32;

            if (!fallInstantly && worldIn.isAreaLoaded(pos.add(-32, -32, -32), pos.add(32, 32, 32)))
            {
                if (!worldIn.isRemote)
                {
                    EntityFallingBlock entityfallingblock = new EntityFallingBlock(worldIn, (double)pos.getX() + 0.5D, (double)pos.getY(), (double)pos.getZ() + 0.5D, worldIn.getBlockState(pos));
                    entityfallingblock.shouldDropItem = false;
                    this.onStartFalling(entityfallingblock);
                    worldIn.spawnEntity(entityfallingblock);
                }
            }
            else
            {
                IBlockState state = worldIn.getBlockState(pos);
                worldIn.setBlockToAir(pos);
                BlockPos blockpos;

                for (blockpos = pos.down(); (worldIn.isAirBlock(blockpos) || canFallThrough(worldIn.getBlockState(blockpos))) && blockpos.getY() > 0; blockpos = blockpos.down())
                {
                    ;
                }

                if (blockpos.getY() > 0)
                {
                    worldIn.setBlockState(blockpos.up(), state); //Forge: Fix loss of state information during world gen.
                }
            }
        }
    }



    protected void onStartFalling(EntityFallingBlock fallingEntity)
    {
    }

    /**
     * How many world ticks before ticking
     */
    public int tickRate(World worldIn)
    {
        return 2;
    }


    public static boolean canFallThrough(IBlockState state)
    {
        Block block = state.getBlock();
        Material material = state.getMaterial();
        return block == Blocks.FIRE || material == Material.AIR || material == Material.WATER || material == Material.LAVA;
    }

    public void onEndFalling(World worldIn, BlockPos pos, IBlockState p_176502_3_, IBlockState p_176502_4_)
    {
    }


    public void onBroken(World worldIn, BlockPos pos)
    {
    }

    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand)
    {
        if (rand.nextInt(16) == 0)
        {
            BlockPos blockpos = pos.down();

            if (canFallThrough(worldIn.getBlockState(blockpos)))
            {
                double d0 = (double)((float)pos.getX() + rand.nextFloat());
                double d1 = (double)pos.getY() - 0.05D;
                double d2 = (double)((float)pos.getZ() + rand.nextFloat());
                worldIn.spawnParticle(EnumParticleTypes.FALLING_DUST, d0, d1, d2, 0.0D, 0.0D, 0.0D, Block.getStateId(stateIn));
            }
        }
    }

    @SideOnly(Side.CLIENT)
    public int getDustColor(IBlockState state)
    {
        return -16777216;
    }

    /**
     * ends hhere
     */
}
