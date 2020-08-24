package xyz.kamefrede.rpsideas.spells.operator.vector;

import xyz.kamefrede.rpsideas.spells.base.SpellParams;
import xyz.kamefrede.rpsideas.util.helpers.SpellHelpers;
import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.SpellParam;
import vazkii.psi.api.spell.SpellRuntimeException;
import vazkii.psi.api.spell.param.ParamVector;
import vazkii.psi.api.spell.piece.PieceOperator;

public class PieceOperatorVectorAbsolute extends PieceOperator {

    public SpellParam vec;

    public PieceOperatorVectorAbsolute(Spell spell) {
        super(spell);
    }

    @Override
    public void initParams() {
        addParam(vec = new ParamVector(SpellParams.GENERIC_NAME_VECTOR, SpellParam.RED, false, false));
    }

    @Override
    public Object execute(SpellContext context) throws SpellRuntimeException {
        Vector3 vector = SpellHelpers.getVector3(this, context, vec, false, false);
        return new Vector3(Math.abs(vector.x), Math.abs(vector.y), Math.abs(vector.z));
    }

    @Override
    public Class<?> getEvaluationType() {
        return Vector3.class;
    }
}
