package com.github.kamefrede.rpsideas.spells.operator;

import com.github.kamefrede.rpsideas.spells.base.SpellParams;
import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.*;
import vazkii.psi.api.spell.param.ParamVector;
import vazkii.psi.api.spell.piece.PieceOperator;

public class PieceOperatorVectorFallback extends PieceOperator {

    SpellParam vector;
    SpellParam fallback;

    public PieceOperatorVectorFallback(Spell spell){
        super(spell);
    }

    @Override
    public void initParams() {
        addParam(vector = new ParamVector(SpellParam.GENERIC_NAME_TARGET, SpellParam.RED, false, false));
        addParam(fallback = new ParamVector(SpellParams.GENERIC_FALLBACK, SpellParam.GREEN, false, false));
    }

    @Override
    public void addToMetadata(SpellMetadata meta) throws SpellCompilationException {
        super.addToMetadata(meta);
    }

    @Override
    public Object execute(SpellContext context) throws SpellRuntimeException {
        Vector3 vec = this.<Vector3>getParamValue(context, vector);
        if(vec == null || vec.isZero()){
            return this.getParamValue(context, fallback);
        } else return vec;

    }

    @Override
    public Class<?> getEvaluationType() {
        return Vector3.class;
    }
}
