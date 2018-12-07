package com.github.kamefrede.rpsideas.spells.selector;

import vazkii.psi.api.spell.*;
import vazkii.psi.api.spell.piece.PieceSelector;
import vazkii.psi.common.core.handler.PlayerDataHandler;

public class PieceSelectorCasterEnergy extends PieceSelector {

    public PieceSelectorCasterEnergy(Spell spell){
        super(spell);
    }

    @Override
    public void addToMetadata(SpellMetadata meta) throws SpellCompilationException {
        super.addToMetadata(meta);
    }

    @Override
    public Object execute(SpellContext context) throws SpellRuntimeException {
        if(context.caster == null) return null;


        return 1.0D * PlayerDataHandler.get(context.caster).availablePsi;
    }

    @Override
    public Class<?> getEvaluationType() {
        return Double.class;
    }
}
