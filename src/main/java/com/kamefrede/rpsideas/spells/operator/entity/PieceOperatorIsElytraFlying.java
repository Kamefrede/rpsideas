package com.kamefrede.rpsideas.spells.operator.entity;

import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.SpellRuntimeException;
import vazkii.psi.api.spell.piece.PieceOperator;

public class PieceOperatorIsElytraFlying extends PieceOperator {// TODO: 12/15/18 look at

    public PieceOperatorIsElytraFlying(Spell spell) {
        super(spell);
    }

    @Override
    public Object execute(SpellContext context) throws SpellRuntimeException {
        return context.caster.isElytraFlying() ? 1.0D : 0.0D;
    }

    @Override
    public Class<?> getEvaluationType() {
        return Double.class;
    }
}
