package com.kamefrede.rpsideas.tiles;

import com.kamefrede.rpsideas.blocks.BlockCADCase;
import com.kamefrede.rpsideas.blocks.RPSBlocks;
import com.kamefrede.rpsideas.items.ItemGaussRifle;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;
import vazkii.arl.util.VanillaPacketDispatcher;
import vazkii.psi.api.cad.ICAD;
import vazkii.psi.api.cad.ISocketable;
import vazkii.psi.api.cad.ISocketableController;
import vazkii.psi.api.spell.ISpellContainer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TileCADCase extends TileEntity {
    private ItemStackHandler itemHandler = new CaseTileHandler();

    private String name = "";

    private int dyeColor = -1; // Legacy

    private BlockCADCase getCaseBlock() {
        if (needsLegacyDyeUpdate())
            return RPSBlocks.cadCases[dyeColor % RPSBlocks.cadCases.length];

        if (blockType instanceof BlockCADCase)
            return (BlockCADCase) blockType;

        return RPSBlocks.cadCases[0];
    }

    private boolean needsLegacyDyeUpdate() {
        return dyeColor >= 0;
    }

    @Override
    public void onLoad() {
        if (!world.isRemote && needsLegacyDyeUpdate()) {
            BlockCADCase cadCase = getCaseBlock();
            IBlockState inWorld = world.getBlockState(pos);

            if (cadCase != inWorld.getBlock()) {
                Chunk chunk = world.getChunk(pos);

                IBlockState toPlace = cadCase.getDefaultState();
                if (inWorld.getBlock() instanceof BlockCADCase)
                    toPlace = toPlace.withProperty(BlockCADCase.FACING, inWorld.getValue(BlockCADCase.FACING))
                        .withProperty(BlockCADCase.OPEN, inWorld.getValue(BlockCADCase.OPEN))
                        .withProperty(BlockCADCase.POWERED, inWorld.getValue(BlockCADCase.POWERED));

                chunk.getBlockStorageArray()[pos.getY() >> 4]
                        .set(pos.getX() & 15, pos.getY() & 15, pos.getZ() & 15, toPlace);
            }
        }
    }

    public static boolean isAllowed(int slot, Item item) {
        return (slot == 0 && (item instanceof ICAD || item instanceof ItemGaussRifle)) ||
                (slot == 1 && !(item instanceof ICAD) &&
                        (item instanceof ISpellContainer ||
                                item instanceof ISocketable ||
                                item instanceof ISocketableController));
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean whenClicked(IBlockState clickedState, EntityPlayer player, EnumHand hand, float hitX, float hitZ) {
        EnumFacing facing = clickedState.getValue(BlockCADCase.FACING);
        ItemStack heldStack = player.getHeldItem(hand);

        if (clickedState.getValue(BlockCADCase.OPEN)) {
            int slot = getSlot(facing, hitX, hitZ);
            ItemStack stackInSlot = itemHandler.getStackInSlot(slot);

            if (heldStack.isEmpty()) {
                //Take an item out of the case. They've got an empty hand, so just give it to them.
                if (!stackInSlot.isEmpty()) {
                    if (!world.isRemote)
                        player.setHeldItem(hand, itemHandler.extractItem(slot, 1, false));
                    return true;
                }
            } else {
                if (stackInSlot.isEmpty()) {
                    //Try to put the item in the player's hand into the case.
                    //Does it fit in that slot?
                    ItemStack leftover = itemHandler.insertItem(slot, heldStack, true);
                    if (leftover.isEmpty()) {
                        if (!world.isRemote)
                            player.setHeldItem(hand, itemHandler.insertItem(slot, heldStack.copy(), false));
                        return true;
                    }
                } else {
                    //Take an item out of the case. Their hand is full, so use a more complex routine to give it to them.
                    if (!world.isRemote)
                        ItemHandlerHelper.giveItemToPlayer(player, itemHandler.extractItem(slot, 1, false));
                    return true;
                }
            }
        }


        return false;
    }

    private int getSlot(EnumFacing facing, float hitX, float hitZ) {
        float x = 0;
        switch (facing) {
            case NORTH:
                x = 1 - hitX;
                break;
            case SOUTH:
                x = hitX;
                break;
            case EAST:
                x = 1 - hitZ;
                break;
            case WEST:
                x = hitZ;
                break;
        }
        return x < 0.5 ? 1 : 0;
    }

    @Override
    public boolean hasCapability(@Nonnull Capability<?> cap, @Nullable EnumFacing facing) {
        return cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(cap, facing);
    }

    @Nullable
    @Override
    public <T> T getCapability(@Nonnull Capability<T> cap, @Nullable EnumFacing facing) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
            return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(itemHandler);
        else
            return super.getCapability(cap, facing);
    }

    @Nonnull
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        nbt.setTag("Items", itemHandler.serializeNBT());
        nbt.setString("Name", name);
        return super.writeToNBT(nbt);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        if (nbt.hasKey("Color"))
            dyeColor = nbt.getInteger("Color");
        else
            dyeColor = -1;

        itemHandler.deserializeNBT(nbt.getCompoundTag("Items"));
        name = nbt.getString("Name");
    }

    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(pos, 0, getUpdateTag());
    }

    @Nonnull
    @Override
    public NBTTagCompound getUpdateTag() {
        return writeToNBT(new NBTTagCompound());
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        readFromNBT(pkt.getNbtCompound());
        notifyBlockUpdate();
    }

    private void notifyBlockUpdate() {
        IBlockState state = world.getBlockState(pos);
        world.notifyBlockUpdate(pos, state, state, 3);
    }

    @Override
    public void markDirty() {
        super.markDirty();
        notifyBlockUpdate();
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, @Nonnull IBlockState oldState, @Nonnull IBlockState newState) {
        return !(newState.getBlock() instanceof BlockCADCase);
    }

    public class CaseTileHandler extends ItemStackHandler {
        public CaseTileHandler() {
            super(2);
        }

        @Override
        protected int getStackLimit(int slot, @Nonnull ItemStack stack) {
            return TileCADCase.isAllowed(slot, stack.getItem()) ? 1 : 0;
        }

        @Override
        protected void onContentsChanged(int slot) {
            markDirty();
            VanillaPacketDispatcher.dispatchTEToNearbyPlayers(TileCADCase.this);
        }
    }
}
