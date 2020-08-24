package com.kamefrede.rpsideas.spells.operator.math.bitwise;

import com.kamefrede.rpsideas.util.helpers.SpellHelpers;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.SpellParam;
import vazkii.psi.api.spell.SpellRuntimeException;
import vazkii.psi.api.spell.param.ParamNumber;
import vazkii.psi.api.spell.piece.PieceOperator;

/**
 * @author WireSegal
 * Created at 7:50 PM on 2/17/19.
 */
public class PieceOperatorRShift extends PieceOperator {
    private SpellParam num1;
    private SpellParam num2;

    public PieceOperatorRShift(Spell spell) {
        super(spell);
    }

    @Override
    public void initParams() {
        addParam(num1 = new ParamNumber(SpellParam.GENERIC_NAME_NUMBER1, SpellParam.RED, false, false));
        addParam(num2 = new ParamNumber(SpellParam.GENERIC_NAME_NUMBER2, SpellParam.GREEN, false, false));
    }

    @Override
    public Object execute(SpellContext context) throws SpellRuntimeException {
        int num1 = (int) SpellHelpers.getNumber(this, context, this.num1, 0);
        int num2 = (int) SpellHelpers.getNumber(this, context, this.num2, 0);

        return (double) (num1 >> num2);
    }

    @Override
    public Class<?> getEvaluationType() {
        return Double.class;
    }
}
