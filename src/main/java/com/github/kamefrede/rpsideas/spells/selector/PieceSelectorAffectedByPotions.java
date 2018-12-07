package com.github.kamefrede.rpsideas.spells.selector;

import com.github.kamefrede.rpsideas.spells.base.SpellRuntimeExceptions;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.SpellParam;
import vazkii.psi.api.spell.SpellRuntimeException;
import vazkii.psi.api.spell.param.ParamEntity;
import vazkii.psi.api.spell.piece.PieceSelector;

public class PieceSelectorAffectedByPotions extends PieceSelector {

    SpellParam entity;

    @Override
    public void initParams() {
        addParam(entity = new ParamEntity(SpellParam.GENERIC_NAME_TARGET, SpellParam.RED, false, false));
    }

    public PieceSelectorAffectedByPotions(Spell spell) {
        super(spell);
    }

    @Override
    public Object execute(SpellContext context) throws SpellRuntimeException {
        if(context.caster.world.isRemote) return null;
        Entity entVal = this.<Entity>getParamValue(context, entity);
        if(entVal == null){
            throw new SpellRuntimeException(SpellRuntimeException.NULL_TARGET);
        }
        if(entVal instanceof EntityLivingBase){
            if(((EntityLivingBase) entVal).getActivePotionEffects().size() > 0){
                return 1;
            } else {
                return 0;
            }
        } else {
            throw new SpellRuntimeException(SpellRuntimeExceptions.ENTITY_NOT_LIVING);
        }

    }

    @Override
    public Class<?> getEvaluationType() {
        return Double.class;
    }
}
