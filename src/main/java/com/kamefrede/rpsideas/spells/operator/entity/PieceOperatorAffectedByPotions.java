package com.kamefrede.rpsideas.spells.operator.entity;

import com.kamefrede.rpsideas.util.helpers.SpellHelpers;
import net.minecraft.entity.EntityLivingBase;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.SpellParam;
import vazkii.psi.api.spell.SpellRuntimeException;
import vazkii.psi.api.spell.param.ParamEntity;
import vazkii.psi.api.spell.piece.PieceOperator;

public class PieceOperatorAffectedByPotions extends PieceOperator {

    private SpellParam entity;

    public PieceOperatorAffectedByPotions(Spell spell) {
        super(spell);
    }

    @Override
    public void initParams() {
        addParam(entity = new ParamEntity(SpellParam.GENERIC_NAME_TARGET, SpellParam.RED, false, false));
    }

    @Override
    public Object execute(SpellContext context) throws SpellRuntimeException {
        EntityLivingBase ent = SpellHelpers.ensureNonnullAndLivingEntity(this, context, entity);
        return ent.getActivePotionEffects().size() > 0 ? 1.0 : 0.0;
    }

    @Override
    public Class<?> getEvaluationType() {
        return Double.class;
    }
}
