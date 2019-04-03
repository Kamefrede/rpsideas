package com.kamefrede.rpsideas.util.helpers;

import net.minecraft.item.ItemStack;
import vazkii.psi.api.cad.ISocketableCapability;

import java.util.Iterator;

public class IteratorSocketable implements Iterator<ItemStack> {

    private final ItemStack stack;
    private final ISocketableCapability socketable;
    private int index = 0;

    public IteratorSocketable(ItemStack stack) {
        this.stack = stack;
        this.socketable = ISocketableCapability.socketable(stack);
    }

    @Override
    public boolean hasNext() {
        return socketable.isSocketSlotAvailable(index);
    }

    @Override
    public ItemStack next() {
        return socketable.getBulletInSocket(++index);
    }

    @Override
    public void remove() {
        socketable.setBulletInSocket(index++, ItemStack.EMPTY);
    }
}
