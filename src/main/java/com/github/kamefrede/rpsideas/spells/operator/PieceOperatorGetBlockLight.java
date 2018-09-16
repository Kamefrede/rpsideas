package com.github.kamefrede.rpsideas.spells.operator;

import com.github.kamefrede.rpsideas.util.BlockProperties;
import vazkii.psi.api.spell.*;

public class PieceOperatorGetBlockLight extends BasePieceOperatorBlockProperties<Double> {

    public PieceOperatorGetBlockLight(Spell spell){
        super(spell);
    }

    @Override
    public void addToMetadata(SpellMetadata meta) throws SpellCompilationException {
        super.addToMetadata(meta);
    }

    @Override
    Double getData(SpellContext context, BlockProperties props) throws SpellRuntimeException {
        return (double) props.getLight();
    }

    @Override
    public Class<Double> getEvaluationType() {
        return Double.class;
    }
}
