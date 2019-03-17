package com.kamefrede.rpsideas.spells.operator.vector;

import com.kamefrede.rpsideas.spells.base.SpellParams;
import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.*;
import vazkii.psi.api.spell.param.ParamVector;
import vazkii.psi.api.spell.piece.PieceOperator;

import javax.annotation.Nonnull;

public class PieceOperatorVectorFallback extends PieceOperator implements IErrorCatcher {

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
        Vector3 vec = this.getParamValue(context, vector);
        Vector3 fall = this.getParamValue(context, fallback);
        if (vec == null || vec.isZero()) {
            return fall;
        } else return vec;


    }

    @Override
    public Class<?> getEvaluationType() {
        return Vector3.class;
    }

    @Override
    public boolean catchException(SpellPiece errorPiece, SpellContext context, SpellRuntimeException exception) {
        SpellParam.Side side = paramSides.get(vector);
        return errorPiece == spell.grid.getPieceAtSideSafely(x, y, side)
                && errorPiece.getEvaluationType() == Vector3.class;
    }

    @Nonnull
    @Override
    public Object supplyReplacementValue(SpellPiece errorPiece, SpellContext context, SpellRuntimeException exception) {
        return this.getParamValue(context, fallback);
    }
}
