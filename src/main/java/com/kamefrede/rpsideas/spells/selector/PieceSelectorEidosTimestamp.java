package com.kamefrede.rpsideas.spells.selector;

import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.SpellRuntimeException;
import vazkii.psi.api.spell.piece.PieceSelector;

public class PieceSelectorEidosTimestamp extends PieceSelector {// TODO: 12/15/18 look at

    public PieceSelectorEidosTimestamp(Spell spell) {
        super(spell);
    }

    @Override
    public Object execute(SpellContext context) throws SpellRuntimeException {
        return (double) context.caster.world.getTotalWorldTime();
    }

    @Override
    public Class<?> getEvaluationType() {
        return Double.class;
    }
}
