package com.kamefrede.rpsideas.spells.operator.entity;

import com.kamefrede.rpsideas.spells.base.SpellParams;
import com.kamefrede.rpsideas.spells.base.SpellRuntimeExceptions;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.SpellParam;
import vazkii.psi.api.spell.SpellRuntimeException;
import vazkii.psi.api.spell.param.ParamEntity;
import vazkii.psi.api.spell.piece.PieceOperator;

public class PieceOperatorVisibleToEntity extends PieceOperator {

    private SpellParam viewer;
    private SpellParam viewed;

    public PieceOperatorVisibleToEntity(Spell spell) {
        super(spell);
    }

    @Override
    public void initParams() {
        super.initParams();
        addParam(viewer = new ParamEntity(SpellParams.GENERIC_NAME_VIEWER, SpellParam.BLUE, false, false));
        addParam(viewed = new ParamEntity(SpellParams.GENERIC_NAME_VIEWED, SpellParam.RED, false, false));
    }

    @Override
    public Object execute(SpellContext context) throws SpellRuntimeException {
        if (context.caster.world.isRemote) return 0.0;
        Entity viewerEntity = this.getParamValue(context, viewer);
        Entity viewedEntity = this.getParamValue(context, viewed);
        if (viewerEntity == null || viewedEntity == null)
            throw new SpellRuntimeException(SpellRuntimeException.NULL_TARGET);

        if (viewerEntity instanceof EntityLivingBase)
            return ((EntityLivingBase) viewerEntity).canEntityBeSeen(viewedEntity) ? 1.0 : 0.0;

        throw new SpellRuntimeException(SpellRuntimeExceptions.ENTITY_NOT_LIVING);
    }

    @Override
    public Class<?> getEvaluationType() {
        return Double.class;
    }
}
