package com.kamefrede.rpsideas.blocks;

import com.kamefrede.rpsideas.tiles.TilePsiDampener;
import com.kamefrede.rpsideas.util.libs.RPSBlockNames;
import com.teamwizardry.librarianlib.features.base.block.BlockMod;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BlockPsiDampener extends BlockMod {

    public static final PropertyBool ACTIVATED = PropertyBool.create("activated");

    public BlockPsiDampener() {
        super(RPSBlockNames.BLOCK_PSI_DAMPENER, Material.IRON);
        setBlockUnbreakable();
        setDefaultState(getDefaultState().withProperty(ACTIVATED, false));
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, ACTIVATED);
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TilePsiDampener();
    }

    @Override
    public boolean onBlockActivated(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull IBlockState state, @Nonnull EntityPlayer player, @Nonnull EnumHand hand, @Nonnull EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (player.isSneaking()) {
            if (!world.isRemote) {
                boolean wasOpen = state.getValue(ACTIVATED);
                world.setBlockState(pos, state.cycleProperty(ACTIVATED), 2);
                //play a sound
            }
            return true;
        }

        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof TilePsiDampener)
            return true;//add the action here
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
    public boolean isFullCube(IBlockState state) {
        return false;
    }
}
