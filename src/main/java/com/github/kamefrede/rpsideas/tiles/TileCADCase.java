package com.github.kamefrede.rpsideas.tiles;

import com.github.kamefrede.rpsideas.blocks.BlockCADCase;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.*;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.items.*;
import vazkii.arl.util.VanillaPacketDispatcher;

import vazkii.psi.api.cad.*;
import vazkii.psi.api.spell.ISpellContainer;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TileCADCase extends TileEntity {
    ItemStackHandler itemHandler = new CaseStackHandler(){
        @Override
        protected void onContentsChanged(int slot) {
            markDirty();
            VanillaPacketDispatcher.dispatchTEToNearbyPlayers(TileCADCase.this);
        }
    };

    EnumDyeColor color = EnumDyeColor.WHITE;
    String name = "";

    public EnumDyeColor getDyeColor() {
        return color;
    }

    public void setDyeColor(EnumDyeColor color) {
        this.color = color;
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

        if (clickedState.getValue(BlockCADCase.OPEN)){
            int slot = getSlot(facing, hitX, hitZ);
            ItemStack stackInSlot = itemHandler.getStackInSlot(slot);

            if(heldStack.isEmpty()) {
                //Take an item out of the case. They've got an empty hand, so just give it to them.
                if(!stackInSlot.isEmpty()) {
                    if(!world.isRemote) {
                        player.setHeldItem(hand, itemHandler.extractItem(slot, 1, false));
                    }
                    return true;
                }
            } else {
                if(stackInSlot.isEmpty()) {
                    //Try to put the item in the player's hand into the case.
                    //Does it fit in that slot?
                    //TODO this is sketch
                    ItemStack leftover = itemHandler.insertItem(slot, heldStack, true);
                    if(leftover.isEmpty()) { //It does fit!
                        if(!world.isRemote) {
                            player.setHeldItem(hand, itemHandler.insertItem(slot, heldStack.copy(), false));
                        }
                        return true;
                    }
                } else {
                    //Take an item out of the case. Their hand is full, so use a more complex routine to give it to them.
                    if(!world.isRemote) {
                        ItemHandlerHelper.giveItemToPlayer(player, itemHandler.extractItem(slot, 1, false));

                    }
                    return true;
                }
            }
        }


        return false;
    }

    private int getSlot(EnumFacing facing, float hitX, float hitZ) {
        float x = 0;
        switch(facing) {
            case NORTH: x = 1 - hitX; break;
            case SOUTH: x = hitX;     break;
            case EAST: x = 1 - hitZ;  break;
            case WEST: x = hitZ;      break;
        }
        return x < 0.5 ? 1 : 0;
    }

    @CapabilityInject(IItemHandler.class)
    public static final Capability<IItemHandler> ITEM_HANDLER_CAPABILITY = null;

    @Override
    @SuppressWarnings("ConstantConditions")
    public boolean hasCapability(@Nonnull Capability<?> cap, @Nullable EnumFacing facing) {
        if(cap == ITEM_HANDLER_CAPABILITY) return true;
        else return super.hasCapability(cap, facing);
    }

    @Nullable
    @Override
    @SuppressWarnings({"unchecked", "ConstantConditions"})
    public <T> T getCapability(@Nonnull Capability<T> cap, @Nullable EnumFacing facing) {
        if(cap == ITEM_HANDLER_CAPABILITY) return (T) itemHandler;
        else return super.getCapability(cap, facing);
    }

    @Nonnull
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        nbt.setTag("Items", itemHandler.serializeNBT());
        nbt.setString("Name", name);
        nbt.setInteger("Color", color.getMetadata());
        return super.writeToNBT(nbt);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        itemHandler.deserializeNBT(nbt.getCompoundTag("Items"));
        name = nbt.getString("Name");
        color = EnumDyeColor.byMetadata(nbt.getInteger("Color"));
    }


    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(pos, 0, getUpdateTag());
    }

    @MethodsReturnNonnullByDefault
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

    //Thanks mojang
    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
        return oldState.getBlock() != newState.getBlock();
    }

    public static class CaseStackHandler extends ItemStackHandler {
        public CaseStackHandler() {
            super(2);
        }

        @Override
        protected int getStackLimit(int slot, @Nonnull ItemStack stack) {
            return 1;
        }

        @Nonnull
        @Override
        public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
            Item item = stack.getItem();
            boolean allowed = false;

            if(slot == 0) {
                if(item instanceof ICAD) {
                    allowed = true;
                }
            } else if (slot == 1) {
                if(!(item instanceof ICAD) && item instanceof ISocketable || item instanceof ISocketableController){
                    allowed = true;
                }

                if(item instanceof ISpellContainer) {
                    allowed = true;
                }
            }

            return allowed ? super.insertItem(slot, stack, simulate) : stack;
        }
    }
}
