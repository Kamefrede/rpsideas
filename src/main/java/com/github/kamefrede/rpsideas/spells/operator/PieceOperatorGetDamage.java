package com.github.kamefrede.rpsideas.spells.operator;

import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.piece.PieceOperator;

public class PieceOperatorGetDamage extends PieceOperator {
    public PieceOperatorGetDamage(Spell spell) {
        super(spell);
    }

    @Override
    public Class<?> getEvaluationType() {
        return null;
    }
}
