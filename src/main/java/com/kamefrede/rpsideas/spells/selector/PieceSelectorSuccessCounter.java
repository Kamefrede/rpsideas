package com.kamefrede.rpsideas.spells.selector;

import com.kamefrede.rpsideas.spells.base.SpellRuntimeExceptions;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.SpellRuntimeException;
import vazkii.psi.api.spell.piece.PieceSelector;
import vazkii.psi.common.item.armor.ItemPsimetalArmor;

public class PieceSelectorSuccessCounter extends PieceSelector {

    public PieceSelectorSuccessCounter(Spell spell) {
        super(spell);
    }

    @Override
    public Object execute(SpellContext context) throws SpellRuntimeException {
        if (!(context.tool.getItem() instanceof ItemPsimetalArmor))
            throw new SpellRuntimeException(SpellRuntimeExceptions.ARMOR);
        if (context.tool.getTagCompound() != null)
            return context.tool.getTagCompound().getInteger("timesCast") * 1.0;
        return 0.0;
    }

    @Override
    public Class<?> getEvaluationType() {
        return Double.class;
    }
}
