package com.kamefrede.rpsideas.spells.operator.vector;

import com.kamefrede.rpsideas.spells.base.SpellParams;
import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.*;
import vazkii.psi.api.spell.param.ParamVector;
import vazkii.psi.api.spell.piece.PieceOperator;

public class PieceOperatorPlanarNormalVector extends PieceOperator {

    private SpellParam vector;

    public PieceOperatorPlanarNormalVector(Spell spell) {
        super(spell);
    }

    @Override
    public void addToMetadata(SpellMetadata meta) throws SpellCompilationException {
        meta.addStat(EnumSpellStat.COMPLEXITY, 2);
    }

    @Override
    public void initParams() {
        addParam(vector = new ParamVector(SpellParams.GENERIC_NAME_VECTOR, SpellParam.BLUE, false, false));
    }

    @Override
    public Object execute(SpellContext context) throws SpellRuntimeException {
        Vector3 v = this.getParamValue(context, vector);
        return v.copy().rotate(Math.PI * 2 / 3, new Vector3(1, 1, 1));
    }

    @Override
    public Class<?> getEvaluationType() {
        return Vector3.class;
    }
}
