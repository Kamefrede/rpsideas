package com.kamefrede.rpsideas.spells.selector;

import net.minecraft.item.ItemStack;
import vazkii.arl.util.ItemNBTHelper;
import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.SpellRuntimeException;
import vazkii.psi.api.spell.piece.PieceSelector;
import vazkii.psi.common.item.ItemVectorRuler;

public class PieceSelectorVectorRuleOrigin extends PieceSelector {

    private static final String TAG_SRC_X = "srcX";
    private static final String TAG_SRC_Y = "srcY";
    private static final String TAG_SRC_Z = "srcZ";

    public PieceSelectorVectorRuleOrigin(Spell spell) {
        super(spell);
    }

    @Override
    public Class<?> getEvaluationType() {
        return Vector3.class;
    }

    @Override
    public Object execute(SpellContext context) throws SpellRuntimeException {
        for (int i = 0; i < context.caster.inventory.getSizeInventory(); i++) {
            ItemStack stack = context.caster.inventory.getStackInSlot(i);
            if (!stack.isEmpty() && stack.getItem() instanceof ItemVectorRuler) {
                int srcX = ItemNBTHelper.getInt(stack, TAG_SRC_X, 0);
                int srcY = ItemNBTHelper.getInt(stack, TAG_SRC_Y, 0);
                int srcZ = ItemNBTHelper.getInt(stack, TAG_SRC_Z, 0);
                return new Vector3(srcX, srcY, srcZ);
            }

        }

        return Vector3.zero;
    }
}
