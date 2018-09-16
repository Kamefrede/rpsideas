package com.github.kamefrede.rpsideas.spells.operator;

import com.github.kamefrede.rpsideas.spells.base.SpellParams;
import com.github.kamefrede.rpsideas.spells.base.SpellRuntimeExceptions;
import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.*;
import vazkii.psi.api.spell.param.ParamVector;
import vazkii.psi.api.spell.piece.PieceOperator;

public class PieceOperatorPlanarNormalVector extends PieceOperator {

    SpellParam vector;

    public PieceOperatorPlanarNormalVector(Spell spell){
        super(spell);
    }

    @Override
    public void addToMetadata(SpellMetadata meta) throws SpellCompilationException {
        super.addToMetadata(meta);
        meta.addStat(EnumSpellStat.COMPLEXITY, 2);
    }

    @Override
    public void initParams() {
        addParam(vector = new ParamVector(SpellParams.GENERIC_NAME_VECTOR, SpellParam.BLUE, false, false));
    }

    @Override
    public Object execute(SpellContext context) throws SpellRuntimeException {
        Vector3 v = this.<Vector3>getParamValue(context, vector);
        if(!v.isAxial()){
            throw new SpellRuntimeException(SpellRuntimeExceptions.NON_AXIAL_VECTOR);
        }
        return new Vector3(-v.y, v.x + v.z, 0.0).normalize();
    }

    @Override
    public Class<?> getEvaluationType() {
        return Vector3.class;
    }
}
