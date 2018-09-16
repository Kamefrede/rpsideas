package com.github.kamefrede.rpsideas.spells.operator;

import com.github.kamefrede.rpsideas.spells.base.SpellParams;
import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.*;
import vazkii.psi.api.spell.param.ParamNumber;
import vazkii.psi.api.spell.param.ParamVector;
import vazkii.psi.api.spell.piece.PieceOperator;

public class PieceOperatorVectorRotate extends PieceOperator {

    SpellParam vector;
    SpellParam axis;
    SpellParam angle;

    public PieceOperatorVectorRotate(Spell spell){
        super(spell);
    }

    @Override
    public void initParams() {
        addParam(vector = new ParamVector(SpellParams.GENERIC_NAME_VECTOR, SpellParam.RED, false, false));
        addParam(axis = new ParamVector(SpellParams.GENERIC_NAME_AXIS, SpellParam.CYAN, false, false));
        addParam(angle = new ParamNumber(SpellParams.GENERIC_NAME_ANGLE, SpellParam.GREEN, false, false));
    }

    @Override
    public void addToMetadata(SpellMetadata meta) throws SpellCompilationException {
        super.addToMetadata(meta);
    }

    @Override
    public Object execute(SpellContext context) throws SpellRuntimeException {
        Vector3 v = this.<Vector3>getParamValue(context, vector);
        Vector3 a = this.<Vector3>getParamValue(context, axis);
        Double an = this.<Double>getParamValue(context, angle);
        if(v == null || axis == null){
            throw new SpellRuntimeException(SpellRuntimeException.NULL_VECTOR);
        }
        return v.rotate(an, a);
    }

    @Override
    public Class<?> getEvaluationType() {
        return Vector3.class;
    }
}
