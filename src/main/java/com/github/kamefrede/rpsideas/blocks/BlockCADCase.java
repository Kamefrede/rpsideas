package com.github.kamefrede.rpsideas.blocks;

import com.github.kamefrede.rpsideas.items.ModItems;
import com.github.kamefrede.rpsideas.tiles.TileCADCase;
import com.github.kamefrede.rpsideas.util.RPSSoundHandler;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.*;
import net.minecraft.block.state.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.*;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.*;
import net.minecraft.block.Block;


import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;


public class BlockCADCase extends Block {
    public BlockCADCase() {
        super(Material.CLOTH);

        setHardness(0.5f);
        setSoundType(SoundType.METAL);

        setDefaultState(getDefaultState().withProperty(OPEN, false).withProperty(FACING, EnumFacing.NORTH).withProperty(COLOR, EnumDyeColor.WHITE));
    }

    public static final PropertyBool OPEN = PropertyBool.create("open");
    public static final PropertyDirection FACING = PropertyDirection.create("facing", Arrays.asList(EnumFacing.HORIZONTALS));
    public static final PropertyEnum<EnumDyeColor> COLOR = PropertyEnum.create("color", EnumDyeColor.class);

    private static final AxisAlignedBB WEST_AABB = new AxisAlignedBB(3.5 / 16.0, 0.0, 0.5 / 16.0, 12.5 / 16.0, 4.5 / 16.0, 15.5 / 16.0);
    private static final AxisAlignedBB NORTH_AABB = new AxisAlignedBB(0.5 / 16.0, 0.0, 3.5 / 16.0, 15.5 / 16.0, 4.5 / 16.0, 12.5 / 16.0);
    private static final AxisAlignedBB EAST_AABB = new AxisAlignedBB(3.5 / 16.0, 0.0, 0.5 / 16.0, 12.5 / 16.0, 4.5 / 16.0, 15.5 / 16.0);
    private static final AxisAlignedBB SOUTH_AABB = new AxisAlignedBB(0.5 / 16.0, 0.0, 3.5 / 16.0, 15.5 / 16.0, 4.5 / 16.0, 12.5 / 16.0);

    //Block Properties and State

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, OPEN, FACING, COLOR);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(FACING).getHorizontalIndex() | (state.getValue(OPEN) ? 4 : 0);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        int horizontalIndex = meta & 3;
        boolean isOpen = (meta & 4) != 0;
        return getDefaultState().withProperty(FACING, EnumFacing.byHorizontalIndex(horizontalIndex)).withProperty(OPEN, isOpen);
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
        TileEntity tile;
        if(world instanceof ChunkCache) {
            tile = ((ChunkCache)world).getTileEntity(pos, Chunk.EnumCreateEntityType.CHECK);
        } else {
            tile = world.getTileEntity(pos);
        }

        if(tile instanceof TileCADCase) {
            TileCADCase cadCase = (TileCADCase) tile;
            return state.withProperty(COLOR, cadCase.getDyeColor());
        } else return state;
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

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        switch(state.getValue(FACING)) {
            case NORTH: return NORTH_AABB;
            case EAST: return EAST_AABB;
            case SOUTH: return SOUTH_AABB;
            case WEST: return WEST_AABB;
            default: return Block.FULL_BLOCK_AABB; //Can't happen
        }
    }

    //Item form

    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
        return new ItemStack(this, 1, getActualState(state, world, pos).getValue(COLOR).getMetadata());
    }

    //Tile Entity
    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileCADCase();
    }

    @Override
    public boolean hasComparatorInputOverride(IBlockState state) {
        return true;
    }

    @Override
    public int getComparatorInputOverride(IBlockState blockState, World world, BlockPos pos) {
        TileEntity tile = world.getTileEntity(pos);
        if(tile instanceof TileCADCase) {
            TileCADCase cadCase = (TileCADCase) tile;
            IItemHandler handler = cadCase.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
            if(handler == null) return 0;
            else return ItemHandlerHelper.calcRedstoneFromInventory(handler);
        } else return 0;
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        TileEntity tileentity = worldIn.getTileEntity(pos);
        if (tileentity instanceof TileCADCase) {
            TileCADCase cadCase = (TileCADCase) tileentity;
            ItemStack itemstack = new ItemStack(ModItems.cadCaseItem, 1, getActualState(state, worldIn, pos).getValue(COLOR).getMetadata());
            IItemHandler handler = itemstack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
            for(int i=0; i < handler.getSlots(); i++){
                handler.insertItem(i, tileentity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).getStackInSlot(i), false);
            }
            if(cadCase.getDisplayName() != null){
                itemstack.setStackDisplayName(cadCase.getName());
            }
            spawnAsEntity(worldIn, pos, itemstack);

            worldIn.updateComparatorOutputLevel(pos, state.getBlock());
        }

        super.breakBlock(worldIn, pos, state);
    }

    @Override
    public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {

    }


    //Events

    @Override
    public boolean canPlaceBlockAt(World world, BlockPos pos) {
        IBlockState belowState = world.getBlockState(pos.down());
        return belowState.getBlockFaceShape(world, pos, EnumFacing.UP) == BlockFaceShape.SOLID || belowState.getBlock() == Blocks.GLOWSTONE;
    }

    @Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        return getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite()).withProperty(OPEN, false);
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        TileEntity tile = world.getTileEntity(pos);
        if(tile instanceof TileCADCase) {
            TileCADCase cadCase = (TileCADCase) tile;

            //TODO Metadata hacking is bad for your health
            //Flatten lol
            cadCase.setDyeColor(EnumDyeColor.byMetadata(stack.getItemDamage()));
            if(stack.hasDisplayName()) cadCase.setName(stack.getDisplayName());

            IItemHandler stackHandler = stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
            if(stackHandler == null) return;
            IItemHandler caseHandler = cadCase.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
            if(caseHandler == null) return;

            for(int i=0; i < caseHandler.getSlots(); i++) {
                caseHandler.insertItem(i, stackHandler.getStackInSlot(i).copy(), false);
            }
        }
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        TileEntity tile = world.getTileEntity(pos);
        if (player.isSneaking()) {
            if (!world.isRemote) {
                world.setBlockState(pos, state.cycleProperty(OPEN), 2);
            }
            return true;
        }
        if(tile instanceof TileCADCase) {
            return ((TileCADCase)tile).whenClicked(state, player, hand, hitX, hitZ);
        }
        return false;
    }

    @Override
    public void neighborChanged(IBlockState state, World world, BlockPos pos, Block neighbor, BlockPos neighborPos) {
        if(world.isRemote) return;

        if(canPlaceBlockAt(world, pos)) {
            boolean shouldOpen = world.isBlockPowered(pos);
            boolean isOpen = state.getValue(OPEN);

            if(shouldOpen != isOpen) {
                world.setBlockState(pos, state.withProperty(OPEN, shouldOpen), 2);
                playOpenCloseSound(world, pos, shouldOpen);
            }
        } else {
            world.destroyBlock(pos, true);
        }
    }

    void playOpenCloseSound(World world, BlockPos pos, boolean closing) {
        //TODO: use a real sound event, instead of borrowing a vanilla one
        world.playSound(null, pos, closing ? RPSSoundHandler.CAD_CASE_CLOSE : RPSSoundHandler.CAD_CASE_OPEN, SoundCategory.BLOCKS, 1f, 1f);
    }

    //Client

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag) {
        IItemHandler stackHandler = stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);


        boolean shifting = GuiScreen.isShiftKeyDown();

        if(stackHandler != null) {
            for(int i=0; i < stackHandler.getSlots(); i++) {
                ItemStack slot = stackHandler.getStackInSlot(i);
                if(slot.isEmpty()) continue;

                if(i != 0 && shifting) tooltip.add("");
                tooltip.add("| " + TextFormatting.WHITE + slot.getDisplayName());

                List<String> slotTooltip = slot.getTooltip(Minecraft.getMinecraft().player, ITooltipFlag.TooltipFlags.NORMAL); //thanks mojang
                if(slotTooltip.size() > 1) {
                    if(shifting) {
                        for(String line : slotTooltip) tooltip.add("|   " + line);
                    } else {
                        tooltip.add("|   " + I18n.translateToLocal("rpsideas.misc.hold") + TextFormatting.AQUA + I18n.translateToLocal("rpsideas.misc.shift") + TextFormatting.RESET + I18n.translateToLocal("rpsideas.misc.info") );
                    }
                }
            }
        }
    }
}
