package com.kamefrede.rpsideas.spells.trick.misc;

import com.kamefrede.rpsideas.spells.base.SpellCompilationExceptions;
import com.kamefrede.rpsideas.spells.base.SpellParams;
import com.kamefrede.rpsideas.util.helpers.SpellHelpers;
import vazkii.psi.api.spell.*;
import vazkii.psi.api.spell.param.ParamNumber;
import vazkii.psi.api.spell.piece.PieceTrick;

public class PieceTrickSlotChange extends PieceTrick {

    private SpellParam slot;

    public PieceTrickSlotChange(Spell spell) {
        super(spell);
    }

    @Override
    public void addToMetadata(SpellMetadata meta) throws SpellCompilationException, ArithmeticException {
        super.addToMetadata(meta);
        double slt = SpellHelpers.ensurePositiveOrZero(this, slot);
        if (slt < 0 || slt > 35)
            throw new SpellCompilationException(SpellCompilationExceptions.SLOT, x, y);
        meta.addStat(EnumSpellStat.COMPLEXITY, 1);
    }

    @Override
    public void initParams() {
        addParam(slot = new ParamNumber(SpellParams.GENERIC_NAME_SLOT, SpellParam.RED, false, true));
    }

    @Override
    public Object execute(SpellContext context) throws SpellRuntimeException {
        double slt = SpellHelpers.getNumber(this, context, slot, 0);
        context.customTargetSlot = true;
        context.targetSlot = (int) slt;
        return null;
    }
}
