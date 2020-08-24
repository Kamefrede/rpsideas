package xyz.kamefrede.rpsideas.spells.operator.vector;

import xyz.kamefrede.rpsideas.spells.base.SpellParams;
import xyz.kamefrede.rpsideas.util.helpers.SpellHelpers;
import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.*;
import vazkii.psi.api.spell.param.ParamVector;
import vazkii.psi.api.spell.piece.PieceOperator;

public class PieceOperatorVectorFallback extends PieceOperator {

    private SpellParam vector;
    private SpellParam fallback;

    public PieceOperatorVectorFallback(Spell spell) {
        super(spell);
    }

    @Override
    public void initParams() {
        addParam(vector = new ParamVector(SpellParam.GENERIC_NAME_TARGET, SpellParam.RED, false, false));
        addParam(fallback = new ParamVector(SpellParams.GENERIC_FALLBACK, SpellParam.GREEN, false, false));
    }

    @Override
    public void addToMetadata(SpellMetadata meta) throws SpellCompilationException {
        super.addToMetadata(meta);

    }

    @Override
    public Object execute(SpellContext context) throws SpellRuntimeException {
        Vector3 fall = this.getParamValue(context, fallback);
        return SpellHelpers.getDefaultedVector(this, context, vector, false, false, fall);


    }

    @Override
    public Class<?> getEvaluationType() {
        return Vector3.class;
    }
}
