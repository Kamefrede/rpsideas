package com.github.kamefrede.rpsideas.spells.trick.entity;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.*;
import vazkii.psi.api.spell.param.ParamNumber;
import vazkii.psi.api.spell.param.ParamVector;
import vazkii.psi.api.spell.piece.PieceTrick;
import vazkii.psi.common.entity.EntitySpellCharge;

import java.util.List;
import java.util.Objects;

public class PieceTrickDetonate extends PieceTrick {

    SpellParam radius;

    public PieceTrickDetonate(Spell spell) {
        super(spell);
    }
    @Override
    public void initParams() {
        addParam(radius = new ParamNumber(SpellParam.GENERIC_NAME_RADIUS, SpellParam.GREEN, false, true));
    }

    @Override
    public void addToMetadata(SpellMetadata meta) throws SpellCompilationException {
        super.addToMetadata(meta);



        Double radiusVal = this.<Double>getParamEvaluation(radius);
        if(radiusVal == null || radiusVal <= 0)
            throw new SpellCompilationException(SpellCompilationException.NON_POSITIVE_VALUE, x, y);
        meta.addStat(EnumSpellStat.POTENCY,  radiusVal.intValue());
        meta.addStat(EnumSpellStat.COST, radiusVal.intValue() * 15);
    }

    @Override
    public Object execute(SpellContext context) throws SpellRuntimeException {
        Vector3 positionVal = Vector3.fromEntity(context.caster).add(0, context.caster.getEyeHeight(),0);
        Double radiusVal = this.<Double>getParamValue(context, radius);

        if(!context.isInRadius(positionVal))
            throw new SpellRuntimeException(SpellRuntimeException.OUTSIDE_RADIUS);

        AxisAlignedBB axis = new AxisAlignedBB(positionVal.x - radiusVal, positionVal.y - radiusVal, positionVal.z - radiusVal, positionVal.x + radiusVal, positionVal.y + radiusVal, positionVal.z + radiusVal);


        List<Entity> list = context.caster.getEntityWorld().getEntitiesWithinAABB(Entity.class, axis, (Entity e) -> {
            return e != null  && e != context.caster && e != context.focalPoint && context.isInRadius(e) && e instanceof EntitySpellCharge && (Objects.requireNonNull(((EntitySpellCharge) e).getThrower()).getName().equals(context.caster.getName()));
        });


        if(list != null && list.size() > 0){
            for (Entity ent : list) {
                EntitySpellCharge charge = (EntitySpellCharge) ent;
                charge.doExplosion();

            }
            return true;
        }
        return false;
    }
}
