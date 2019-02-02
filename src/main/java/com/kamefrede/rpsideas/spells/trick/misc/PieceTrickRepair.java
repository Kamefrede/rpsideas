package com.kamefrede.rpsideas.spells.trick.misc;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import vazkii.psi.api.cad.ISocketable;
import vazkii.psi.api.spell.*;
import vazkii.psi.api.spell.piece.PieceTrick;

public class PieceTrickRepair extends PieceTrick {

    public PieceTrickRepair(Spell spell) {
        super(spell);
    }

    @Override
    public void addToMetadata(SpellMetadata meta) throws SpellCompilationException {
        super.addToMetadata(meta);

        meta.addStat(EnumSpellStat.POTENCY, 3);
        meta.addStat(EnumSpellStat.COST, 600);
    }

    @Override
    public Object execute(SpellContext context) throws SpellRuntimeException {
        if (doRepair(context.tool))
            doRepair(context.caster.inventory.getStackInSlot(context.getTargetSlot()));
        return null;
    }

    private boolean doRepair(ItemStack stack) {
        if (stack.isEmpty())
            return true;
        Item item = stack.getItem();
        if (item.isRepairable() && item.getDamage(stack) > 0 && item instanceof ISocketable)
            item.setDamage(stack, item.getDamage(stack) - 1);
        return false;
    }
}
