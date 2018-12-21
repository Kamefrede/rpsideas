package com.kamefrede.rpsideas.spells.operator.math;

import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.SpellParam;
import vazkii.psi.api.spell.SpellRuntimeException;
import vazkii.psi.api.spell.param.ParamNumber;
import vazkii.psi.api.spell.piece.PieceOperator;

public class PieceOperatorSignum extends PieceOperator {

    private SpellParam num;

    public PieceOperatorSignum(Spell spell) {
        super(spell);
    }

    @Override
    public void initParams() {
        addParam(num = new ParamNumber(SpellParam.GENERIC_NAME_TARGET, SpellParam.BLUE, false, false));
    }

    @Override
    public Object execute(SpellContext context) throws SpellRuntimeException {
        Double number = this.<Double>getParamValue(context, num);

        if (number == null)
            return 0.0;

        return Math.signum(number);
    }

    @Override
    public Class<?> getEvaluationType() {
        return Double.class;
    }


}
