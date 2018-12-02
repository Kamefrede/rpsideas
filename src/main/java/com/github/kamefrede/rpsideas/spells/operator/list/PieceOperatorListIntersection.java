package com.github.kamefrede.rpsideas.spells.operator.list;

import com.github.kamefrede.rpsideas.spells.base.SpellParams;
import com.github.kamefrede.rpsideas.spells.base.SpellRuntimeExceptions;
import org.codehaus.plexus.util.CollectionUtils;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.SpellParam;
import vazkii.psi.api.spell.SpellRuntimeException;
import vazkii.psi.api.spell.param.ParamEntityListWrapper;
import vazkii.psi.api.spell.piece.PieceOperator;
import vazkii.psi.api.spell.wrapper.EntityListWrapper;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PieceOperatorListIntersection extends PieceOperator {

    SpellParam list1;
    SpellParam list2;

    public PieceOperatorListIntersection(Spell spell) {
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
        if(l1 == null || l2 == null) throw new SpellRuntimeException(SpellRuntimeExceptions.NULL_LIST);
        EntityListWrapper l3 = new EntityListWrapper(l1.unwrap().stream().filter(l2.unwrap()::contains).collect(Collectors.toList()));

        return l3;

    }


    @Override
    public Class<?> getEvaluationType() {
        return EntityListWrapper.class;
    }
}
