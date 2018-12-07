package com.github.kamefrede.rpsideas.spells.operator.list;

import com.github.kamefrede.rpsideas.spells.base.SpellParams;
import com.github.kamefrede.rpsideas.spells.base.SpellRuntimeExceptions;
import vazkii.psi.api.spell.*;
import vazkii.psi.api.spell.param.ParamEntityListWrapper;
import vazkii.psi.api.spell.piece.PieceOperator;
import vazkii.psi.api.spell.wrapper.EntityListWrapper;

public class PieceOperatorListSize extends PieceOperator {

    SpellParam list;

    public PieceOperatorListSize(Spell spell){
        super(spell);
    }

    @Override
    public void initParams() {
        addParam(list = new ParamEntityListWrapper(SpellParams.GENERIC_NAME_LIST, SpellParam.BLUE, false, false));
    }

    @Override
    public void addToMetadata(SpellMetadata meta) throws SpellCompilationException {
        super.addToMetadata(meta);
    }

    @Override
    public Object execute(SpellContext context) throws SpellRuntimeException {
        EntityListWrapper l = this.<EntityListWrapper>getParamValue(context, list);
        if(l == null ){
            throw new SpellRuntimeException(SpellRuntimeExceptions.NULL_LIST);
        }
        return (double)l.unwrap().size();
    }

    @Override
    public Class<?> getEvaluationType() {
        return Double.class;
    }
}
