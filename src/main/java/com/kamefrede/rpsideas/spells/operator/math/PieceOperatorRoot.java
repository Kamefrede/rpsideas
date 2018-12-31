package com.kamefrede.rpsideas.spells.operator.math;

import com.kamefrede.rpsideas.spells.base.SpellParams;
import com.kamefrede.rpsideas.spells.base.SpellRuntimeExceptions;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.SpellParam;
import vazkii.psi.api.spell.SpellRuntimeException;
import vazkii.psi.api.spell.param.ParamNumber;
import vazkii.psi.api.spell.piece.PieceOperator;

public class PieceOperatorRoot extends PieceOperator {

    private SpellParam num;
    private SpellParam root;

    public PieceOperatorRoot(Spell spell) {
        super(spell);
    }

    @Override
    public void initParams() {
        addParam(num = new ParamNumber(SpellParam.GENERIC_NAME_NUMBER, SpellParam.GREEN, false, false));
        addParam(root = new ParamNumber(SpellParams.GENERIC_NAME_ROOT, SpellParam.RED, false, false));
    }

    @Override
    public Object execute(SpellContext context) throws SpellRuntimeException {
        double base = this.getParamValue(context, num);
        double r = this.getParamValue(context, root);
        if (base < 0 && r % 2 != 0)
            throw new SpellRuntimeException(SpellRuntimeExceptions.EVEN_ROOT_NEGATIVE_NUMBER);
        return Math.pow(base, 1.0 / r);

    }

    @Override
    public Class<?> getEvaluationType() {
        return Double.class;
    }

}
