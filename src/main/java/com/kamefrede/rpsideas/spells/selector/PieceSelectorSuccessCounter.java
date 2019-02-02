package com.kamefrede.rpsideas.spells.selector;

import com.kamefrede.rpsideas.spells.base.SpellRuntimeExceptions;
import com.teamwizardry.librarianlib.features.helpers.ItemNBTHelper;
import vazkii.psi.api.exosuit.IPsiEventArmor;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.SpellRuntimeException;
import vazkii.psi.api.spell.piece.PieceSelector;

public class PieceSelectorSuccessCounter extends PieceSelector {

    public PieceSelectorSuccessCounter(Spell spell) {
        super(spell);
    }

    @Override
    public Object execute(SpellContext context) throws SpellRuntimeException {
        if (!(context.tool.getItem() instanceof IPsiEventArmor))
            throw new SpellRuntimeException(SpellRuntimeExceptions.ARMOR);
        return ItemNBTHelper.getInt(context.tool, "timesCast", 0) * 1.0;
    }

    @Override
    public Class<?> getEvaluationType() {
        return Double.class;
    }
}
