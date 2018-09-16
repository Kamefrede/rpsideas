package com.github.kamefrede.rpsideas.spells.operator;

import com.github.kamefrede.rpsideas.spells.base.SpellParams;
import vazkii.psi.api.spell.*;
import vazkii.psi.api.spell.param.ParamAny;
import vazkii.psi.api.spell.piece.PieceOperator;

public class PieceOperatorEquality extends PieceOperator {

    SpellParam first;
    SpellParam second;

    public PieceOperatorEquality(Spell spell){
        super(spell);
    }

    @Override
    public void initParams() {
        addParam(first = new ParamAny(SpellParams.GENERIC_TARGET1, SpellParam.RED, false));
        addParam(second = new ParamAny(SpellParams.GENERIC_TARGET2, SpellParam.BLUE, false));
    }

    @Override
    public void addToMetadata(SpellMetadata meta) throws SpellCompilationException {
        super.addToMetadata(meta);
    }

    @Override
    public Object execute(SpellContext context) throws SpellRuntimeException {
        Object fir = this.getParamValue(context, first);
        Object sec = this.getParamValue(context, second);
        if(first == null && second == null){
            return 1d;
        } else return first.equals(second) ? 1d : 0d;
    }

    @Override
    public Class<?> getEvaluationType() {
        return Double.class;
    }
}
