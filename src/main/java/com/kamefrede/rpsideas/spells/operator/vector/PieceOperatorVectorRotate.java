package com.kamefrede.rpsideas.spells.operator.vector;

import com.kamefrede.rpsideas.spells.base.SpellParams;
import com.kamefrede.rpsideas.util.helpers.SpellHelpers;
import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.*;
import vazkii.psi.api.spell.param.ParamNumber;
import vazkii.psi.api.spell.param.ParamVector;
import vazkii.psi.api.spell.piece.PieceOperator;

public class PieceOperatorVectorRotate extends PieceOperator {

    private SpellParam vector;
    private SpellParam axis;
    private SpellParam angle;

    public PieceOperatorVectorRotate(Spell spell) {
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
        Vector3 v = this.getParamValue(context, vector);
        Vector3 a = this.getParamValue(context, axis);
        double an = SpellHelpers.getNumber(this, context, angle, 0);
        if (v == null || axis == null)
            throw new SpellRuntimeException(SpellRuntimeException.NULL_VECTOR);
        return v.copy().rotate(an, a.copy());
    }

    @Override
    public Class<?> getEvaluationType() {
        return Vector3.class;
    }
}
