package com.github.kamefrede.rpsideas.spells.constant;

import vazkii.psi.api.spell.*;

public class PieceConstantTau extends SpellPiece {
    public PieceConstantTau(Spell spell) {
        super(spell);
    }

    @Override
    public EnumPieceType getPieceType() {
        return EnumPieceType.CONSTANT;
    }

    @Override
    public Class<?> getEvaluationType() {
        return Double.class;
    }

    @Override
    public Object evaluate() {
        return 2*Math.PI;
    }

    @Override
    public Object execute(SpellContext context) throws SpellRuntimeException {
        return evaluate();
    }
}
