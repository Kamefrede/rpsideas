package com.kamefrede.rpsideas.spells.base;

import vazkii.psi.api.spell.*;

public abstract class PieceMacro extends SpellPiece {// TODO: 12/15/18 look at

    public PieceMacro(Spell spell) {
        super(spell);
    }

    @Override
    public EnumPieceType getPieceType() {
        return EnumPieceType.SELECTOR;
    }

    @Override
    public void addToMetadata(SpellMetadata meta) throws SpellCompilationException {
        meta.addStat(EnumSpellStat.COMPLEXITY, 4);
    }

    @Override
    public Object evaluate() {
        return null;
    }

    @Override
    public Object execute(SpellContext context) throws SpellRuntimeException {
        return null;
    }
}
