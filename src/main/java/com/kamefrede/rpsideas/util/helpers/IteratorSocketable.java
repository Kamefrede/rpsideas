package com.kamefrede.rpsideas.util.helpers;

import net.minecraft.item.ItemStack;
import vazkii.psi.api.cad.ISocketable;

import java.util.Iterator;

public class IteratorSocketable implements Iterator<ItemStack> {

    private final ItemStack stack;
    private final ISocketable socketable;
    private int index = 0;

    public IteratorSocketable(ItemStack stack) {
        this.stack = stack;
        this.socketable = (ISocketable) stack.getItem();
    }

    @Override
    public boolean hasNext() {
        return socketable.isSocketSlotAvailable(stack, index);
    }

    @Override
    public ItemStack next() {
        return socketable.getBulletInSocket(stack, ++index);
    }

    @Override
    public void remove() {
        socketable.setBulletInSocket(stack, index++, ItemStack.EMPTY);
    }
}
