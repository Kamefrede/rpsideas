package com.github.kamefrede.rpsideas.spells.operator;

import com.github.kamefrede.rpsideas.spells.base.SpellParams;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.SpellParam;
import vazkii.psi.api.spell.SpellRuntimeException;
import vazkii.psi.api.spell.param.ParamNumber;
import vazkii.psi.api.spell.piece.PieceOperator;

public class PieceOperatorToRadians extends PieceOperator {

    SpellParam num;

    public PieceOperatorToRadians(Spell spell){
        super(spell);
    }

    @Override
    public void initParams(){
        addParam(num = new ParamNumber(SpellParams.GENERIC_NAME_DEGREE, SpellParam.BLUE, false, false));
    }

    @Override
    public Object execute(SpellContext context) throws SpellRuntimeException{
        Double n = this.<Double>getParamValue(context, num);

        return Math.toRadians(n);
    }

    @Override
    public Class<?> getEvaluationType(){
        return Double.class;
    }
}
