package com.github.kamefrede.rpsideas.spells.operator;

import vazkii.psi.api.spell.*;
import vazkii.psi.api.spell.piece.PieceOperator;
import vazkii.psi.common.core.handler.PlayerDataHandler;

public class OperatorCasterEnergy extends PieceOperator {

    public OperatorCasterEnergy(Spell spell){
        super(spell);
    }

    @Override
    public void addToMetadata(SpellMetadata meta) throws SpellCompilationException {
        super.addToMetadata(meta);
    }

    @Override
    public Object execute(SpellContext context) throws SpellRuntimeException {
        if (context.caster.getEntityWorld().isRemote) return null;
        return PlayerDataHandler.get(context.caster).getAvailablePsi();
    }

    @Override
    public Class<?> getEvaluationType() {
        return Double.class;
    }
}
