package com.github.kamefrede.rpsideas.spells.operator;

import com.github.kamefrede.rpsideas.spells.base.SpellParams;
import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.*;
import vazkii.psi.api.spell.param.ParamVector;
import vazkii.psi.api.spell.piece.PieceOperator;

public class PieceOperatorVectorFallback extends PieceOperator {

    SpellParam vector;
    SpellParam fallback;

    public PieceOperatorVectorFallback(Spell spell){
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


    public SpellParam getFallback() {
        return fallback;
    }

    @Override
    public Object execute(SpellContext context) throws SpellRuntimeException {
        SpellParam.Side side = this.paramSides.get(vector);
        SpellPiece piece = context.cspell.sourceSpell.grid.getPieceAtSideSafely(this.x, this.y, side);
        context.actions.remove(context.cspell.actionMap.get(piece));
        Vector3 vec = this.<Vector3>getParamValue(context, vector);
        Vector3 fall = this.<Vector3>getParamValue(context, fallback);
        try{
            piece.execute(context);
        } catch (SpellRuntimeException ex){
            if(vec == null || vec.isZero() ){
                if(fall == null || fall.isZero()){
                    throw new SpellRuntimeException(SpellRuntimeException.NULL_VECTOR);
                }
                return fall;
            } else return vec;
        }

        return vec;


    }

    @Override
    public Class<?> getEvaluationType() {
        return Vector3.class;
    }
}
