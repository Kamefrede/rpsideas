package com.github.kamefrede.rpsideas.spells.operator;

import net.minecraft.util.EnumFacing;
import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.SpellParam;
import vazkii.psi.api.spell.SpellRuntimeException;
import vazkii.psi.api.spell.param.ParamVector;
import vazkii.psi.api.spell.piece.PieceOperator;

public class TestPiecePleaseIgnore extends PieceOperator {

    SpellParam direction;

    public TestPiecePleaseIgnore(Spell spell) {
        super(spell);
    }

    @Override
    public void initParams() {
        addParam(direction = new ParamVector(SpellParam.GENERIC_NAME_TARGET, SpellParam.RED, false, false));
    }

    @Override
    public Object execute(SpellContext context) throws SpellRuntimeException {
        Vector3 vec = this.<Vector3>getParamValue(context, direction);

        return EnumFacing.getFacingFromVector((float) vec.x, (float) vec.y, (float)vec.z);
    }

    @Override
    public Class<?> getEvaluationType() {
        return EnumFacing.class;
    }
}
