package com.github.kamefrede.rpsideas.spells.trick.misc;

import vazkii.psi.api.spell.*;
import vazkii.psi.api.spell.param.ParamNumber;
import vazkii.psi.api.spell.piece.PieceTrick;

public class PieceTrickSpinChamber extends PieceTrick {

    SpellParam number;

    public PieceTrickSpinChamber(Spell spell) {
        super(spell);
    }

    @Override
    public void initParams() {
        addParam(number = new ParamNumber(SpellParam.GENERIC_NAME_NUMBER, SpellParam.RED, false, false));
    }

    @Override
    public void addToMetadata(SpellMetadata meta) throws SpellCompilationException, ArithmeticException {
        super.addToMetadata(meta);
        meta.addStat(EnumSpellStat.POTENCY, 1);
        meta.addStat(EnumSpellStat.COST, 5);
    }

    @Override
    public Object execute(SpellContext context) throws SpellRuntimeException {
        return super.execute(context);
    }
}
