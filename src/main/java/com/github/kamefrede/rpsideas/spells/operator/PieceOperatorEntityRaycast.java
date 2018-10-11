package com.github.kamefrede.rpsideas.spells.operator;

import net.minecraft.entity.Entity;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.SpellParam;
import vazkii.psi.api.spell.SpellRuntimeException;
import vazkii.psi.api.spell.param.ParamEntity;
import vazkii.psi.api.spell.piece.PieceOperator;

public class PieceOperatorEntityRaycast extends PieceOperator {

    SpellParam target;

    public PieceOperatorEntityRaycast(Spell spell) {
        super(spell);
    }

    @Override
    public void addParam(SpellParam param) {
        addParam(target = new ParamEntity(SpellParam.GENERIC_NAME_TARGET, SpellParam.RED, false, false));
    }

    @Override
    public Object execute(SpellContext context) throws SpellRuntimeException {
        return super.execute(context);
    }

    @Override
    public Class<?> getEvaluationType() {
        return Entity.class;
    }
}
