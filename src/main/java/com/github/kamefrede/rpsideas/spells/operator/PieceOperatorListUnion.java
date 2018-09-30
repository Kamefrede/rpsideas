package com.github.kamefrede.rpsideas.spells.operator;

import com.github.kamefrede.rpsideas.spells.base.SpellParams;
import com.sun.xml.internal.ws.binding.FeatureListUtil;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.SpellParam;
import vazkii.psi.api.spell.SpellRuntimeException;
import vazkii.psi.api.spell.param.ParamEntityListWrapper;
import vazkii.psi.api.spell.piece.PieceOperator;
import vazkii.psi.api.spell.wrapper.EntityListWrapper;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PieceOperatorListUnion extends PieceOperator {

    SpellParam list1;
    SpellParam list2;

    public PieceOperatorListUnion(Spell spell) {
        super(spell);
    }

    @Override
    public void initParams() {
        addParam(list1 = new ParamEntityListWrapper(SpellParams.GENERIC_NAME_LIST1, SpellParam.BLUE, false, false));
        addParam(list2 = new ParamEntityListWrapper(SpellParams.GENERIC_NAME_LIST2, SpellParam.RED, false, false));
    }

    @Override
    public Object execute(SpellContext context) throws SpellRuntimeException {
        EntityListWrapper l1 = this.<EntityListWrapper>getParamValue(context, list1);
        EntityListWrapper l2 = this.<EntityListWrapper>getParamValue(context, list2);
        EntityListWrapper l3 = new EntityListWrapper(Stream.concat(l1.unwrap().stream(), l2.unwrap().stream()).collect(Collectors.toList()));

        return l3;



    }

    @Override
    public Class<?> getEvaluationType() {
        return EntityListWrapper.class;
    }
}
