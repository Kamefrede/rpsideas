package com.kamefrede.rpsideas.spells.trick.block;

import vazkii.psi.api.spell.*;
import vazkii.psi.api.spell.param.ParamVector;
import vazkii.psi.api.spell.piece.PieceTrick;

public class PieceTrickDrainLiquid extends PieceTrick {

    private SpellParam position;

    public PieceTrickDrainLiquid(Spell spell) {
        super(spell);
    }

    @Override
    public void initParams() {
        addParam(position = new ParamVector(SpellParam.GENERIC_NAME_POSITION, SpellParam.BLUE, false, false));
    }

    @Override
    public void addToMetadata(SpellMetadata meta) throws SpellCompilationException, ArithmeticException {
        super.addToMetadata(meta);
    }

    @Override
    public Object execute(SpellContext context) throws SpellRuntimeException {
        return super.execute(context);
    }
}
