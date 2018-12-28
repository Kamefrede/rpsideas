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
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import vazkii.psi.common.core.handler.PsiSoundHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BlockPsiDampener extends BlockMod {

    public static final PropertyBool ACTIVATED = PropertyBool.create("activated");
    private static final AxisAlignedBB BOUNDS = new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 1 / 16.0, 1.0);

    public BlockPsiDampener() {
        super(RPSBlockNames.BLOCK_PSI_DAMPENER, Material.IRON);
        setBlockUnbreakable();
        setDefaultState(getDefaultState().withProperty(ACTIVATED, false));
    }

    @Nonnull
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, ACTIVATED);
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state) {
        return new TilePsiDampener();
    }

    @Override
    public boolean onBlockActivated(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull IBlockState state, @Nonnull EntityPlayer player, @Nonnull EnumHand hand, @Nonnull EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (player.isSneaking()) {
            if (!world.isRemote) {
                if (player.canUseCommandBlock()) {
                    boolean wasActive = state.getValue(ACTIVATED);
                    world.setBlockState(pos, state.cycleProperty(ACTIVATED), 2);
                    if (!wasActive)
                        world.playSound(null, pos, PsiSoundHandler.bulletCreate, SoundCategory.BLOCKS, 1f, 1f);
                } else player.sendMessage(new TextComponentTranslation("advMode.notAllowed"));
            }
            return true;
        }
        return false;
    }

    @Nonnull
    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return BOUNDS;
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
