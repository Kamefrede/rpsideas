package xyz.kamefrede.rpsideas.blocks;

import xyz.kamefrede.rpsideas.items.blocks.ItemCADCase;
import xyz.kamefrede.rpsideas.tiles.TileCADCase;
import xyz.kamefrede.rpsideas.util.RPSSoundHandler;
import com.teamwizardry.librarianlib.features.base.block.tile.BlockModContainer;
import com.teamwizardry.librarianlib.features.utilities.client.TooltipHelper;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;

public class BlockCADCase extends BlockModContainer {
    public static final PropertyBool OPEN = PropertyBool.create("open");
    public static final PropertyBool POWERED = PropertyBool.create("powered");
    public static final PropertyDirection FACING = PropertyDirection.create("facing", Arrays.asList(EnumFacing.HORIZONTALS));
    private static final AxisAlignedBB WEST_AABB = new AxisAlignedBB(3.5 / 16.0, 0.0, 0.5 / 16.0, 12.5 / 16.0, 4.5 / 16.0, 15.5 / 16.0);
    private static final AxisAlignedBB NORTH_AABB = new AxisAlignedBB(0.5 / 16.0, 0.0, 3.5 / 16.0, 15.5 / 16.0, 4.5 / 16.0, 12.5 / 16.0);
    private static final AxisAlignedBB EAST_AABB = new AxisAlignedBB(3.5 / 16.0, 0.0, 0.5 / 16.0, 12.5 / 16.0, 4.5 / 16.0, 15.5 / 16.0);
    private static final AxisAlignedBB SOUTH_AABB = new AxisAlignedBB(0.5 / 16.0, 0.0, 3.5 / 16.0, 15.5 / 16.0, 4.5 / 16.0, 12.5 / 16.0);

    public final EnumDyeColor color;

    public BlockCADCase(String name, EnumDyeColor color) {
        super(name, Material.CLOTH);
        this.color = color;

        setHardness(0.5f);
        setSoundType(SoundType.METAL);

        setDefaultState(getDefaultState().withProperty(OPEN, false).withProperty(FACING, EnumFacing.NORTH).withProperty(POWERED, false));
    }

    //Block Properties and State


    @Nullable
    @Override
    public ItemBlock createItemForm() {
        return new ItemCADCase(this);
    }

    @Nonnull
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, OPEN, FACING, POWERED);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(FACING).getHorizontalIndex() | (state.getValue(OPEN) ? 4 : 0) | (state.getValue(POWERED) ? 8 : 0);
    }

    @Nonnull
    @Override
    public IBlockState getStateFromMeta(int meta) {
        int horizontalIndex = meta & 3;
        boolean isOpen = (meta & 4) != 0;
        boolean isPowered = (meta & 8) != 0;
        return getDefaultState()
                .withProperty(FACING, EnumFacing.byHorizontalIndex(horizontalIndex))
                .withProperty(OPEN, isOpen)
                .withProperty(POWERED, isPowered);
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

    @Nonnull
    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        switch (state.getValue(FACING)) {
            case NORTH:
                return NORTH_AABB;
            case EAST:
                return EAST_AABB;
            case SOUTH:
                return SOUTH_AABB;
            case WEST:
                return WEST_AABB;
            default:
                return Block.FULL_BLOCK_AABB; //Can't happen
        }
    }

    //Item form

    @Nonnull
    @Override
    public ItemStack getPickBlock(@Nonnull IBlockState state, RayTraceResult target, @Nonnull World world, @Nonnull BlockPos pos, EntityPlayer player) {
        TileEntity tileentity = world.getTileEntity(pos);
        if (tileentity instanceof TileCADCase) {
            TileCADCase cadCase = (TileCADCase) tileentity;
            ItemStack itemstack = new ItemStack(this);
            IItemHandler handler = itemstack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
            IItemHandler tileHandler = tileentity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
            if (handler != null && tileHandler != null) for (int i = 0; i < handler.getSlots(); i++)
                handler.insertItem(i, tileHandler.getStackInSlot(i).copy(), false);
            if (cadCase.getDisplayName() != null)
                itemstack.setStackDisplayName(cadCase.getName());
            return itemstack;
        }
        return ItemStack.EMPTY;
    }

    @Override
    public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state) {
        return new TileCADCase();
    }

    @Override
    public void breakBlock(@Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull IBlockState state) {
        TileEntity tileentity = worldIn.getTileEntity(pos);
        if (tileentity instanceof TileCADCase) {
            TileCADCase cadCase = (TileCADCase) tileentity;
            ItemStack itemstack = new ItemStack(this);
            IItemHandler handler = itemstack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
            IItemHandler tileHandler = tileentity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
            if (handler != null && tileHandler != null) for (int i = 0; i < handler.getSlots(); i++)
                handler.insertItem(i, tileHandler.extractItem(i, 64, false), false);
            if (cadCase.getDisplayName() != null)
                itemstack.setStackDisplayName(cadCase.getName());
            spawnAsEntity(worldIn, pos, itemstack);

            worldIn.updateComparatorOutputLevel(pos, state.getBlock());
        }

        super.breakBlock(worldIn, pos, state);
    }

    @Override
    public void getDrops(@Nonnull NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, @Nonnull IBlockState state, int fortune) {
        // NO-OP
    }

    //Events

    @Override
    public boolean canPlaceBlockAt(World world, @Nonnull BlockPos pos) {
        IBlockState belowState = world.getBlockState(pos.down());
        return belowState.getBlockFaceShape(world, pos, EnumFacing.UP) == BlockFaceShape.SOLID || belowState.getBlock() == Blocks.GLOWSTONE;
    }

    @Nonnull
    @Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        return getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite());
    }


    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof TileCADCase) {
            TileCADCase cadCase = (TileCADCase) tile;

            if (stack.hasDisplayName()) cadCase.setName(stack.getDisplayName());

            IItemHandler stackHandler = stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
            if (stackHandler == null) return;
            IItemHandler caseHandler = TileCADCase.getHandler(cadCase);
            if (caseHandler == null) return;

            for (int i = 0; i < caseHandler.getSlots(); i++)
                caseHandler.insertItem(i, stackHandler.getStackInSlot(i).copy(), false);
        }
    }

    @Override
    public boolean onBlockActivated(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull IBlockState state, @Nonnull EntityPlayer player, @Nonnull EnumHand hand, @Nonnull EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (player.isSneaking()) {
            if (!world.isRemote) {
                boolean wasOpen = state.getValue(OPEN);
                world.setBlockState(pos, state.cycleProperty(OPEN), 2);
                playOpenCloseSound(world, pos, wasOpen);
            }
            return true;
        }

        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof TileCADCase)
            return ((TileCADCase) tile).whenClicked(state, player, hand, hitX, hitZ);
        return false;
    }

    @Override
    public IProperty[] getIgnoredProperties() {
        return new IProperty[]{POWERED};
    }

    @Override
    public void neighborChanged(IBlockState state, World world, BlockPos pos, Block neighbor, BlockPos neighborPos) {
        if (world.isRemote) return;

        if (canPlaceBlockAt(world, pos)) {
            boolean wasPowered = state.getValue(POWERED);

            boolean isPowered = world.isBlockPowered(pos);
            boolean isOpen = state.getValue(OPEN);

            IBlockState set = state;

            if (wasPowered == isOpen && isPowered != wasPowered) {
                set = set.withProperty(OPEN, isPowered);
                playOpenCloseSound(world, pos, isPowered);
            }

            if (isPowered != wasPowered)
                set = set.withProperty(POWERED, isPowered);

            if (set != state)
                world.setBlockState(pos, set, 2);
        } else
            world.destroyBlock(pos, true);
    }

    private void playOpenCloseSound(World world, BlockPos pos, boolean closing) {
        world.playSound(null, pos, closing ? RPSSoundHandler.CAD_CASE_CLOSE : RPSSoundHandler.CAD_CASE_OPEN, SoundCategory.BLOCKS, 1f, 1f);
    }

    //Client

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag) {
        IItemHandler stackHandler = stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);

        boolean shifting = GuiScreen.isShiftKeyDown();

        boolean inFirst = false;

        if (stackHandler != null) {
            for (int i = 0; i < stackHandler.getSlots(); i++) {
                ItemStack slot = stackHandler.getStackInSlot(i);
                if (slot.isEmpty()) continue;
                if (i == 0)
                    inFirst = true;

                if (inFirst && shifting) tooltip.add("");
                tooltip.add("| " + TextFormatting.WHITE + slot.getDisplayName());

                List<String> slotTooltip = slot.getTooltip(Minecraft.getMinecraft().player, flag);
                if (slotTooltip.size() > 1) {
                    if (shifting)
                        for (String line : slotTooltip) tooltip.add("|   " + line);
                    else
                        tooltip.add("|   " + TooltipHelper.local("librarianlib.shiftinfo").replace("&", "\u00a7"));
                }
            }
        }

    }
}
