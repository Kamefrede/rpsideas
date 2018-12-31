package com.kamefrede.rpsideas.spells.operator.math;

import com.kamefrede.rpsideas.spells.base.SpellParams;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.SpellParam;
import vazkii.psi.api.spell.SpellRuntimeException;
import vazkii.psi.api.spell.param.ParamNumber;
import vazkii.psi.api.spell.piece.PieceOperator;

public class PieceOperatorToDegrees extends PieceOperator {

    private SpellParam num;

    public PieceOperatorToDegrees(Spell spell) {
        super(spell);
    }

    @Override
    public void initParams() {
        addParam(num = new ParamNumber(SpellParams.GENERIC_NAME_RADIAN, SpellParam.BLUE, false, false));

    }

    @Override
    public Object execute(SpellContext context) throws SpellRuntimeException {
        double r = this.getParamValue(context, num);
        return Math.toDegrees(r);

    }

    @Override
    public Class<?> getEvaluationType() {
        return Double.class;
    }
}
