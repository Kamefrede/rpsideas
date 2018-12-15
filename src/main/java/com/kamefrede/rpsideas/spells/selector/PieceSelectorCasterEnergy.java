package com.kamefrede.rpsideas.spells.selector;

import com.kamefrede.rpsideas.util.helpers.SpellHelpers;
import vazkii.psi.api.spell.*;
import vazkii.psi.api.spell.piece.PieceSelector;

public class PieceSelectorCasterEnergy extends PieceSelector {// TODO: 12/15/18 look at

    public PieceSelectorCasterEnergy(Spell spell) {
        super(spell);
    }

    @Override
    public void addToMetadata(SpellMetadata meta) throws SpellCompilationException {
        super.addToMetadata(meta);
    }

    @Override
    public Object execute(SpellContext context) throws SpellRuntimeException {
        if (context.caster == null) return null;


        return 1.0D * SpellHelpers.getPlayerData(context.caster).availablePsi;
    }

    @Override
    public Class<?> getEvaluationType() {
        return Double.class;
    }
}
