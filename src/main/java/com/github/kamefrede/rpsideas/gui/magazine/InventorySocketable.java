package com.github.kamefrede.rpsideas.gui.magazine;

import com.github.kamefrede.rpsideas.util.helpers.IteratorSocketable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import vazkii.psi.api.cad.ISocketable;
import vazkii.psi.api.spell.EnumSpellStat;
import vazkii.psi.api.spell.ISpellContainer;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.common.spell.SpellCompiler;

public class InventorySocketable implements IInventory {

    public InventorySocketable(ItemStack stack, int maxBandwidth) {
        this.stack = stack;
        this.maxBandwidth = maxBandwidth;

        socketable = (ISocketable) stack.getItem();
    }

    public InventorySocketable(ItemStack stack) {
        this(stack, -1);
    }

    private final ItemStack stack;
    private final ISocketable socketable;
    private final int maxBandwidth;

    private IteratorSocketable getSockerator() {
        return new IteratorSocketable(stack);
    }

    private int totalSlots() {
        IteratorSocketable sockerator = getSockerator();
        int count = 0;
        while(sockerator.hasNext()) {
            sockerator.next();
            count++;
        }
        return count - 1;
    }


    @Override
    public int getSizeInventory() {
        return totalSlots();
    }

    @Override
    public boolean isEmpty() {
        IteratorSocketable sockerator = getSockerator();
        while(sockerator.hasNext()) {
            if(!sockerator.next().getRight().isEmpty()) return true;
        }
        return false;
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        return socketable.getBulletInSocket(stack, index);
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        ItemStack bullet = socketable.getBulletInSocket(stack, index);
        if(!bullet.isEmpty()) socketable.setBulletInSocket(stack, index, ItemStack.EMPTY);
        return bullet;
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        return decrStackSize(index, 1);
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack bullet) {
        socketable.setBulletInSocket(stack, index, bullet);

    }

    @Override
    public int getInventoryStackLimit() {
        return 1;
    }

    @Override
    public void markDirty() {
        //no-op
    }

    @Override
    public boolean isUsableByPlayer(EntityPlayer player) {
        return true;
    }

    @Override
    public void openInventory(EntityPlayer player) {
        //no-op
    }

    @Override
    public void closeInventory(EntityPlayer player) {
        //no-op
    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        Item item = stack.getItem();
        if(item != null && !(item instanceof ISpellContainer)) return false;
        if(maxBandwidth == -1 ) return true;
        ISpellContainer container = (ISpellContainer) item;
        Spell spell = container.getSpell(stack);
        SpellCompiler cmp = new SpellCompiler(spell);
        return cmp.getCompiledSpell().metadata.stats.get(EnumSpellStat.BANDWIDTH) != null && cmp.getCompiledSpell().metadata.stats.get(EnumSpellStat.BANDWIDTH) <= maxBandwidth;
    }

    @Override
    public int getField(int id) {
        return 0;
    }

    @Override
    public void setField(int id, int value) {
        //no-op
    }

    @Override
    public int getFieldCount() {
        return 0;
    }

    @Override
    public void clear() {
        IteratorSocketable sockerator = getSockerator();
        while(sockerator.hasNext()) {
            sockerator.next();
            sockerator.remove();
        }
    }

    @Override
    public String getName() {
        return "rpsideas.container.socketable";
    }

    @Override
    public boolean hasCustomName() {
        return false;
    }

    @Override
    public ITextComponent getDisplayName() {
        return new TextComponentTranslation(getName());
    }
}
