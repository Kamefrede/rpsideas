package com.github.kamefrede.rpsideas.util.helpers;

import net.minecraft.item.ItemStack;
import org.apache.commons.lang3.tuple.Pair;
import vazkii.psi.api.cad.ISocketable;

import java.util.Iterator;

public class IteratorSocketable implements Iterator<Pair<Integer, ItemStack>> {
    public IteratorSocketable(ItemStack stack) {
        this.stack = stack;
        assert stack.getItem() instanceof ISocketable;

        socketable = (ISocketable) stack.getItem();
    }

    private final ItemStack stack;
    private final ISocketable socketable;
    private int progress = -1;

    @Override
    public boolean hasNext() {
        return socketable.isSocketSlotAvailable(stack, progress + 1);
    }

    @Override
    public Pair<Integer, ItemStack> next() {
        progress++;
        return Pair.of(progress, socketable.getBulletInSocket(stack, progress));
    }

    @Override
    public void remove() {
        socketable.setBulletInSocket(stack, progress, ItemStack.EMPTY);
    }
}
