package com.github.kamefrede.rpsideas.spells.operator;

import net.minecraft.entity.Entity;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.SpellParam;
import vazkii.psi.api.spell.SpellRuntimeException;
import vazkii.psi.api.spell.param.ParamEntity;
import vazkii.psi.api.spell.piece.PieceOperator;

public class OperatorDistanceFromGround extends PieceOperator {

    SpellParam target;

    public OperatorDistanceFromGround(Spell spell) {
        super(spell);
    }

    @Override
    public void initParams() {
        addParam(target = new ParamEntity(SpellParam.GENERIC_NAME_TARGET, SpellParam.BLUE, false, false));
    }

    @Override
    public Object execute(SpellContext context) throws SpellRuntimeException {
        if(context.caster.world.isRemote) return null;
        Entity targetVal = this.<Entity>getParamValue(context, target);
        if(targetVal == null) return null;
        return (double)targetVal.fallDistance;

    }

    @Override
    public Class<?> getEvaluationType() {
        return Double.class;
    }
}
