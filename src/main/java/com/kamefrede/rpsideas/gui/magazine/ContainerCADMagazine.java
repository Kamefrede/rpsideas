package com.kamefrede.rpsideas.gui.magazine;

import com.kamefrede.rpsideas.RPSIdeas;
import com.kamefrede.rpsideas.items.ItemCADMagazine;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import org.apache.commons.lang3.tuple.Pair;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.spell.ISpellAcceptor;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class ContainerCADMagazine extends Container {
    private static final List<Pair<Integer, Integer>> innerSlotPositions;
    private static final List<Pair<Integer, Integer>> outerSlotPositions;

    static {
        innerSlotPositions = new ArrayList<>();

        innerSlotPositions.add(Pair.of(135, 88));
        innerSlotPositions.add(Pair.of(129, 109));
        innerSlotPositions.add(Pair.of(110, 128));
        innerSlotPositions.add(Pair.of(89, 134));
        innerSlotPositions.add(Pair.of(68, 128));
        innerSlotPositions.add(Pair.of(49, 109));
        innerSlotPositions.add(Pair.of(43, 88));
        innerSlotPositions.add(Pair.of(49, 67));
        innerSlotPositions.add(Pair.of(68, 48));
        innerSlotPositions.add(Pair.of(89, 42));
        innerSlotPositions.add(Pair.of(110, 48));
        innerSlotPositions.add(Pair.of(129, 67));

        outerSlotPositions = new ArrayList<>();

        outerSlotPositions.add(Pair.of(173, 88));
        outerSlotPositions.add(Pair.of(159, 131));
        outerSlotPositions.add(Pair.of(130, 160));
        outerSlotPositions.add(Pair.of(89, 171));
        outerSlotPositions.add(Pair.of(48, 160));
        outerSlotPositions.add(Pair.of(19, 131));
        outerSlotPositions.add(Pair.of(5, 88));
        outerSlotPositions.add(Pair.of(19, 45));
        outerSlotPositions.add(Pair.of(48, 16));
        outerSlotPositions.add(Pair.of(89, 5));
        outerSlotPositions.add(Pair.of(130, 16));
        outerSlotPositions.add(Pair.of(159, 45));
    }

    private final EntityPlayer player;
    public boolean suppressNotify = false;
    public boolean notifyOnce = false;
    public float tooltipTime = 0;
    public String tooltipText = "";

    public ContainerCADMagazine(EntityPlayer player, ItemStack stack) {
        this.player = player;

        InventorySocketable inventory = new InventorySocketable(stack, ItemCADMagazine.getBandwidth(stack));
        InventorySocketable cadInventory = new InventorySocketable(PsiAPI.getPlayerCAD(player), -1);

        int slotIndex = 0;

        for (Pair<Integer, Integer> p : innerSlotPositions)
            addSlotToContainer(new SlotCustomBullet(inventory, slotIndex++, p.getLeft(), p.getRight(), true));

        slotIndex = 0;
        for (Pair<Integer, Integer> p : outerSlotPositions)
            addSlotToContainer(new SlotCustomBullet(cadInventory, slotIndex++, p.getLeft(), p.getRight(), false));
    }

    @Override
    public boolean canInteractWith(@Nonnull EntityPlayer playerIn) {
        return true;
    }

    @Nonnull
    @Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            int magazineStart = 0;
            int magazineEnd = innerSlotPositions.size() - 1;
            int cadStart = magazineEnd + 1;
            int cadEnd = cadStart + outerSlotPositions.size() - 1;

            suppressNotify = true;
            notifyOnce = true;

            if (index > magazineEnd) {
                if (!this.mergeItemStack(itemstack1, magazineStart, magazineEnd, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.mergeItemStack(itemstack1, cadStart, cadEnd, false)) {
                return ItemStack.EMPTY;
            }
            suppressNotify = false;
            notifyOnce = false;

            slot.onSlotChanged();

            if (itemstack1.isEmpty()) slot.putStack(ItemStack.EMPTY);
            else if (itemstack1.getCount() == itemstack.getCount())
                return ItemStack.EMPTY;
            slot.onTake(playerIn, itemstack1);
        }
        return itemstack;
    }

    public class SlotCustomBullet extends Slot {
        public final InventorySocketable socketable;
        public final int index;
        public final int xPosition;
        public final int yPosition;
        public final boolean dark;

        public SlotCustomBullet(InventorySocketable socketable, int index, int xPosition, int yPosition, boolean dark) {
            super(socketable, index, xPosition, yPosition);
            this.socketable = socketable;
            this.index = index;
            this.xPosition = xPosition;
            this.yPosition = yPosition;
            this.dark = dark;
        }

        @Override
        public boolean isItemValid(ItemStack stack) {
            if (ISpellAcceptor.isContainer(stack)) {
                ISpellAcceptor acceptor = ISpellAcceptor.acceptor(stack);
                if (acceptor.containsSpell() && isSlotEnabled()) {
                    boolean ret = socketable.isItemValidForSlot(getSlotIndex(), stack);
                    if (!ret && (!suppressNotify || notifyOnce) && player.world.isRemote) {
                        tooltipTime = 80;
                        tooltipText = RPSIdeas.MODID + ".misc.too_complex";
                        if (notifyOnce) notifyOnce = false;

                    } else if (ret)
                        tooltipTime = 0;
                    return ret;
                }
            }
            return false;
        }

        @Override
        public boolean canTakeStack(EntityPlayer playerIn) {
            boolean ret = socketable.isItemValidForSlot(getSlotIndex(), getStack());
            if (!ret && (!suppressNotify || notifyOnce) && player.world.isRemote) {
                tooltipTime = 80;
                tooltipText = RPSIdeas.MODID + ".misc.too_complex_bullet";
                if (notifyOnce) notifyOnce = false;
            } else if (ret)
                tooltipTime = 0;
            return ret;
        }

        @Override
        public boolean isEnabled() {
            return isSlotEnabled();
        }

        public boolean isSlotEnabled() {
            return getSlotIndex() <= socketable.getSizeInventory();
        }
    }
}
