package com.kamefrede.rpsideas.spells.operator.math;

import com.kamefrede.rpsideas.util.helpers.SpellHelpers;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.SpellParam;
import vazkii.psi.api.spell.SpellRuntimeException;
import vazkii.psi.api.spell.param.ParamNumber;
import vazkii.psi.api.spell.piece.PieceOperator;

public class PieceOperatorTruncate extends PieceOperator {

    private SpellParam num;

    public PieceOperatorTruncate(Spell spell) {
        super(spell);
    }

    @Override
    public void initParams() {
        addParam(num = new ParamNumber(SpellParam.GENERIC_NAME_NUMBER, SpellParam.BLUE, false, false));
    }

    @Override
    public Object execute(SpellContext context) throws SpellRuntimeException {
        return (double) (int) SpellHelpers.getNumber(this, context, num, 0);
    }

    @Override
    public Class<?> getEvaluationType() {
        return Double.class;
    }

}
