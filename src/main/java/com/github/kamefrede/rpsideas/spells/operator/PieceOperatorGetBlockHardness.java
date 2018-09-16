package com.github.kamefrede.rpsideas.spells.operator;

import com.github.kamefrede.rpsideas.util.BlockProperties;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.SpellRuntimeException;

public class PieceOperatorGetBlockHardness extends BasePieceOperatorBlockProperties<Double> {

    public PieceOperatorGetBlockHardness(Spell spell){
        super(spell);
    }

    @Override
    Double getData(SpellContext context, BlockProperties props) throws SpellRuntimeException {
        return (double) props.getHardness();
    }

    @Override
    public Class<Double> getEvaluationType() {
        return Double.class;
    }
}
