package com.kamefrede.rpsideas.spells.operator.math;

import com.kamefrede.rpsideas.spells.base.SpellParams;
import com.kamefrede.rpsideas.spells.base.SpellRuntimeExceptions;
import vazkii.psi.api.spell.*;
import vazkii.psi.api.spell.param.ParamNumber;
import vazkii.psi.api.spell.piece.PieceOperator;

public class PieceOperatorRoot extends PieceOperator {// TODO: 12/15/18 look at

    SpellParam num;
    SpellParam root;

    public PieceOperatorRoot(Spell spell) {
        super(spell);
    }

    @Override
    public void initParams() {
        addParam(num = new ParamNumber(SpellParam.GENERIC_NAME_NUMBER, SpellParam.GREEN, false, false));
        addParam(root = new ParamNumber(SpellParams.GENERIC_NAME_ROOT, SpellParam.RED, false, false));
    }

    @Override
    public void addToMetadata(SpellMetadata meta) throws SpellCompilationException {
        super.addToMetadata(meta);
    }

    @Override
    public Object execute(SpellContext context) throws SpellRuntimeException {
        Double base = this.<Double>getParamValue(context, num);
        Double r = this.<Double>getParamValue(context, root);
        int nth = r.intValue();
        if (base < 0) {
            if (nth % 2 == 1) {
                throw new SpellRuntimeException(SpellRuntimeExceptions.EVEN_ROOT_NEGATIVE_NUMBER);
            }
        }
        return Math.pow(base, 1.0 / nth);

    }

    @Override
    public Class<?> getEvaluationType() {
        return Double.class;
    }

}
