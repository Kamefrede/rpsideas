package xyz.kamefrede.rpsideas.blocks;

import xyz.kamefrede.rpsideas.tiles.TileEthereal;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.Random;

public class BlockConjuredGravityBlock extends BlockConjuredDirectional {

    public BlockConjuredGravityBlock() {
        super("conjured_gravity_block", Material.SAND);
        setLightOpacity(0);
    }

    public static boolean canFallThrough(IBlockState state) {
        Block block = state.getBlock();
        Material material = state.getMaterial();
        return block == Blocks.FIRE || material == Material.AIR || material == Material.WATER || material == Material.LAVA;
    }

    @Override
    public boolean hasSolidProperty() {
        return false;
    }

    @Override
    public boolean isSolid(IBlockState state) {
        return true;
    }

    @Nonnull
    @Override
    public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state) {
        return new TileEthereal();
    }

    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
        worldIn.scheduleUpdate(pos, this, this.tickRate(worldIn));
    }

    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
        worldIn.scheduleUpdate(pos, this, this.tickRate(worldIn));
    }

    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        if (!worldIn.isRemote)
            this.checkCanFall(worldIn, pos);
    }

    private void checkCanFall(World worldIn, BlockPos pos) {
        if ((worldIn.isAirBlock(pos.down()) || canFallThrough(worldIn.getBlockState(pos.down()))) && pos.getY() >= 0) {
            if (worldIn.isAreaLoaded(pos.add(-32, -32, -32), pos.add(32, 32, 32))) {
                if (!worldIn.isRemote) {
                    EntityFallingBlock sand = new EntityFallingBlock(worldIn, (double) pos.getX() + 0.5D, (double) pos.getY(), (double) pos.getZ() + 0.5D, worldIn.getBlockState(pos));
                    sand.shouldDropItem = false;

                    TileEntity here = worldIn.getTileEntity(pos);
                    if (here != null) {
                        NBTTagCompound data = here.writeToNBT(new NBTTagCompound());
                        data.setLong("FellAt", worldIn.getTotalWorldTime());
                        sand.tileEntityData = data;
                    }
                    worldIn.spawnEntity(sand);
                }
            } else {
                IBlockState state = worldIn.getBlockState(pos);
                worldIn.setBlockToAir(pos);
                BlockPos pointer = pos.down();

                while ((worldIn.isAirBlock(pointer) || canFallThrough(worldIn.getBlockState(pointer))) && pointer.getY() > 0)
                    pointer = pointer.down();

                if (pointer.getY() > 0)
                    worldIn.setBlockState(pointer.up(), state);
            }
        }
    }

    public int tickRate(World worldIn) {
        return 2;
    }
}
