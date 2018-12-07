package com.github.kamefrede.rpsideas.gui.cadcase;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nullable;

public class ContainerCADCase extends Container {
    @CapabilityInject(IItemHandler.class)
    public static final Capability<IItemHandler> ITEM_HANDLER_CAPABILITY = null;

    public ContainerCADCase(EntityPlayer player, ItemStack stack) {
        stackItemHandler = stack.getCapability(ITEM_HANDLER_CAPABILITY, null);

        //TODO: give these more descriptive names.
        slot0 = addSlotToContainer(new SlotItemHandler(stackItemHandler, 0, 132, 7));
        slot1 = addSlotToContainer(new SlotItemHandler(stackItemHandler, 1, 79, 7));

        //add the player inventory...
        InventoryPlayer playerInv = player.inventory;

        int xs = 34;
        int ys = 48;
        for(int row = 0; row < 3; row++) {
            for(int column = 0; column < 9; column++) {
                addSlotToContainer(new Slot(playerInv, column + row * 9 + 9, xs + column * 18, ys + row * 18));
            }
        }

        //...their hotbar...
        for(int hotbarSlot = 0; hotbarSlot < 9; hotbarSlot++) {
            addSlotToContainer(new Slot(playerInv, hotbarSlot, xs + hotbarSlot * 18, ys + 58) {
                @Override
                public boolean canTakeStack(EntityPlayer playerIn) {
                    //Don't let players pull away the item that they are opening this GUI from.
                    return getStack().getItem() != stack.getItem();
                }
            });
        }

        //...their armor slots...
        EntityEquipmentSlot[] armorSlotTypes = new EntityEquipmentSlot[]{
                EntityEquipmentSlot.HEAD,
                EntityEquipmentSlot.CHEST,
                EntityEquipmentSlot.LEGS,
                EntityEquipmentSlot.FEET
        };
        for(int armorSlot = 0; armorSlot < 4; armorSlot++) {
            EntityEquipmentSlot slotType = armorSlotTypes[armorSlot];

            addSlotToContainer(new Slot(playerInv, playerInv.getSizeInventory() - 2 - armorSlot, xs - 27, ys + 18 * armorSlot) {
                @Override
                public int getSlotStackLimit() {
                    return 1;
                }

                @Override
                public boolean isItemValid(ItemStack stack) {
                    return !stack.isEmpty() && stack.getItem().isValidArmor(stack, slotType, player);
                }

                @SideOnly(Side.CLIENT)
                @Nullable
                @Override
                public String getSlotTexture() {
                    return ItemArmor.EMPTY_SLOT_NAMES[slotType.getIndex()];
                }
            });
        }

        //...and their offhand slot
        addSlotToContainer(new Slot(playerInv, playerInv.getSizeInventory() - 1, 205, 48) {
            @SideOnly(Side.CLIENT)
            @Nullable
            @Override
            public String getSlotTexture() {
                return "minecraft:items/empty_armor_slot_shield";
            }

            @Override
            public boolean canTakeStack(EntityPlayer playerIn) {
                return getStack().getItem() != stack.getItem();
            }
        });
    }

    private final IItemHandler stackItemHandler;
    private final Slot slot0;
    private final Slot slot1;

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return true;
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int index) {
        ItemStack stack = ItemStack.EMPTY;
        Slot slot = inventorySlots.get(index);

        if(slot != null && slot.getHasStack()) {
            //TODO I want to refactor this........but its a transferstackinslot method
            //This is scary lmao
            ItemStack stack1 = slot.getStack();
            stack = stack1.copy();

            int invStart = 1;
            int hotbarStart = invStart + 25;
            int invEnd = hotbarStart + 9;

            if(index > invStart) {
                if(slot0.isItemValid(stack1)) {
                    if(!mergeItemStack(stack1, 0, 1, false)) {
                        return ItemStack.EMPTY; //Inventory --> CAD slot
                    }
                } else if(slot1.isItemValid(stack1)) {
                    if(!mergeItemStack(stack1, 1, 2, false)) {
                        return ItemStack.EMPTY; //Inventory --> Socket
                    }
                }
            } else if(stack1.getItem() instanceof ItemArmor) {
                ItemArmor armor1 = (ItemArmor) stack1.getItem();
                int armorSlot = 3 - armor1.armorType.getIndex();

                if(!mergeItemStack(stack1, invEnd + armorSlot, invEnd + armorSlot + 1, true) && !mergeItemStack(stack1, invStart, invEnd, true)) {
                    return ItemStack.EMPTY;
                }
            } else if(!mergeItemStack(stack1, invStart, invEnd, true)) {
                return ItemStack.EMPTY;
            }

            slot.onSlotChanged();

            if(stack1.isEmpty()) slot.putStack(ItemStack.EMPTY);
            else if(stack1.getCount() == stack.getCount()) return ItemStack.EMPTY;

            slot.onTake(player, stack1);
        }

        return stack;
    }
}
