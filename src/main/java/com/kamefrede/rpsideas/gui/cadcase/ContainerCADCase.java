package com.kamefrede.rpsideas.gui.cadcase;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ContainerCADCase extends Container {

    private static final int xMin = 34;
    private static final int yMin = 48;

    private final Slot cadSlot;
    private final Slot socketableSlot;

    private final int cadSlotBegin;
    private final int cadSlotEnd;
    private final int socketSlotBegin;
    private final int socketSlotEnd;
    private final int playerInventoryBegin;
    private final int playerHotbarEnd;
    private final int playerArmorBegin;

    public ContainerCADCase(EntityPlayer player, ItemStack stack) {
        IItemHandler stackItemHandler = stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
        InventoryPlayer playerInv = player.inventory;

        cadSlotBegin = inventorySlots.size();
        cadSlot = addSlotToContainer(new SlotItemHandler(stackItemHandler, 0, 132, 7));
        cadSlotEnd = inventorySlots.size();

        socketSlotBegin = inventorySlots.size();
        socketableSlot = addSlotToContainer(new SlotItemHandler(stackItemHandler, 1, 79, 7));
        socketSlotEnd = inventorySlots.size();

        playerInventoryBegin = inventorySlots.size();
        for (int row = 0; row < 3; row++)
            for (int column = 0; column < 9; column++)
                addSlotToContainer(new Slot(playerInv, column + row * 9 + 9, xMin + column * 18, yMin + row * 18));


        for (int slot = 0; slot < 9; slot++)
            addSlotToContainer(new Slot(playerInv, slot, xMin + slot * 18, yMin + 58) {
                @Override
                public boolean canTakeStack(EntityPlayer playerIn) {
                    return getStack().getItem() != stack.getItem();
                }
            });
        playerHotbarEnd = inventorySlots.size();

        playerArmorBegin = inventorySlots.size();
        for (EntityEquipmentSlot slot : EntityEquipmentSlot.values())
            if (slot.getSlotType() == EntityEquipmentSlot.Type.ARMOR)
                addSlotToContainer(new Slot(playerInv, 36 + slot.getIndex(), xMin - 27, yMin + 18 * (3 - slot.getIndex())) {
                    @Override
                    public int getSlotStackLimit() {
                        return 1;
                    }

                    @Override
                    public boolean isItemValid(ItemStack stack) {
                        return !stack.isEmpty() && stack.getItem().isValidArmor(stack, slot, player);
                    }

                    @SideOnly(Side.CLIENT)
                    @Nullable
                    @Override
                    public String getSlotTexture() {
                        return ItemArmor.EMPTY_SLOT_NAMES[slot.getIndex()];
                    }
                });

        //...and their offhand slot
        addSlotToContainer(new Slot(playerInv, playerInv.getSizeInventory() - 1, 205, 48) {
            @SideOnly(Side.CLIENT)
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

    @Override
    public boolean canInteractWith(@Nonnull EntityPlayer player) {
        return true;
    }

    @Nonnull
    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int index) {
        ItemStack stack = ItemStack.EMPTY;
        Slot slot = inventorySlots.get(index);

        if (slot != null && slot.getHasStack()) {
            ItemStack stackInSlot = slot.getStack();
            stack = stackInSlot.copy();


            if (index > playerInventoryBegin) {
                if (cadSlot.isItemValid(stackInSlot)) {
                    if (!mergeItemStack(stackInSlot, cadSlotBegin, cadSlotEnd, false))
                        return ItemStack.EMPTY; //Inventory --> CAD slot
                } else if (socketableSlot.isItemValid(stackInSlot)) {
                    if (!mergeItemStack(stackInSlot, socketSlotBegin, socketSlotEnd, false))
                        return ItemStack.EMPTY; //Inventory --> Socket
                }
            } else if (stackInSlot.getItem() instanceof ItemArmor) {
                ItemArmor armor = (ItemArmor) stackInSlot.getItem();
                int armorSlot = playerArmorBegin + armor.armorType.getSlotIndex() - 1;

                if (!mergeItemStack(stackInSlot, armorSlot, armorSlot + 1, true) && !mergeItemStack(stackInSlot, playerInventoryBegin, playerHotbarEnd, true))
                    return ItemStack.EMPTY;
            } else if (!mergeItemStack(stackInSlot, playerInventoryBegin, playerHotbarEnd, true))
                return ItemStack.EMPTY;

            slot.onSlotChanged();

            if (stackInSlot.isEmpty()) slot.putStack(ItemStack.EMPTY);
            else if (stackInSlot.getCount() == stack.getCount()) return ItemStack.EMPTY;

            slot.onTake(player, stackInSlot);
        }

        return stack;
    }
}
