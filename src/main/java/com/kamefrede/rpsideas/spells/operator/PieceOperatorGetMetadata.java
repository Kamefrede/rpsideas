package com.kamefrede.rpsideas.spells.operator;

import vazkii.psi.api.spell.*;
import vazkii.psi.api.spell.piece.PieceOperator;

public class PieceOperatorGetMetadata extends PieceOperator {// TODO: 12/15/18 look at

    public PieceOperatorGetMetadata(Spell spell) {
        super(spell);
    }

    @Override
    public void addToMetadata(SpellMetadata meta) throws SpellCompilationException {
        super.addToMetadata(meta);

        meta.addStat(EnumSpellStat.COMPLEXITY, 1);

    }

    @Override
    public Object execute(SpellContext context) throws SpellRuntimeException {
        if (!context.caster.world.isRemote) {
            int slot = context.getTargetSlot();
            if (!context.caster.inventory.getStackInSlot(slot).isEmpty()) {
                return context.caster.inventory.getStackInSlot(slot).getMetadata() * 1D;
            }
        }
        return 0D;
    }

    @Override
    public Class<?> getEvaluationType() {
        return Double.class;
    }
}
