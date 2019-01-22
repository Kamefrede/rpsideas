package com.kamefrede.rpsideas.spells.trick.misc;

import com.kamefrede.rpsideas.items.base.IPsiAddonTool;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import vazkii.psi.api.spell.*;
import vazkii.psi.api.spell.piece.PieceTrick;
import vazkii.psi.common.item.tool.IPsimetalTool;

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
        if (!context.caster.world.isRemote) {
            if (!context.tool.isEmpty()) {
                ItemStack stack = context.tool;
                if (doRepair(stack)) return null;
            } else {
                int slot = context.getTargetSlot();
                if (!context.caster.inventory.getStackInSlot(slot).isEmpty()) {
                    ItemStack stack = context.caster.inventory.getStackInSlot(slot);
                    if (doRepair(stack)) return null;
                }
            }

        }
        return null;
    }

    private boolean doRepair(ItemStack stack) {
        Item item = stack.getItem();
        if (item.isRepairable() && item.getDamage(stack) > 0 && (item instanceof IPsimetalTool || item instanceof IPsiAddonTool)) {
            item.setDamage(stack, item.getDamage(stack) - 1);
            return true;
        }
        return false;
    }
}
