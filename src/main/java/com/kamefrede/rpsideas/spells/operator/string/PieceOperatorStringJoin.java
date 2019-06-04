package com.kamefrede.rpsideas.spells.operator.string;

import com.kamefrede.rpsideas.spells.base.SpellParams;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.SpellParam;
import vazkii.psi.api.spell.SpellRuntimeException;
import vazkii.psi.api.spell.param.ParamAny;
import vazkii.psi.api.spell.piece.PieceOperator;

public class PieceOperatorStringJoin extends PieceOperator {

    public PieceOperatorStringJoin(Spell spell) {
        super(spell);
    }


    private SpellParam param1;
    private SpellParam param2;


    @Override
    public void initParams() {
        addParam(param1 = new ParamAny(SpellParams.GENERIC_NAME_PARAM1, SpellParam.RED, false));
        addParam(param2 = new ParamAny(SpellParams.GENERIC_NAME_PARAM1, SpellParam.BLUE, false));
    }

    @Override
    public Object execute(SpellContext context) throws SpellRuntimeException {
        Object p1 = getParamValue(context, param1);
        Object p2 = getParamValue(context, param2);

        String s = "null";
        if (p1 != null && p2 != null)
            s = p1.toString() + " " + p2.toString();

        return s;
    }

    @Override
    public Class<?> getEvaluationType() {
        return String.class;
    }
}
