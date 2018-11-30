package com.github.kamefrede.rpsideas.spells.operator;

import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.piece.PieceOperator;

public class PieceOperatorGetMetadata extends PieceOperator {
    public PieceOperatorGetMetadata(Spell spell) {
        super(spell);
    }

    @Override
    public Class<?> getEvaluationType() {
        return Double.class;
    }
}
