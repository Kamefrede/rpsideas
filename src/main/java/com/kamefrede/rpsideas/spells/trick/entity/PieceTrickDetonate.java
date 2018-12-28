package com.kamefrede.rpsideas.spells.trick.entity;

import com.kamefrede.rpsideas.items.components.ItemTriggerSensor;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.WorldServer;
import vazkii.psi.api.exosuit.PsiArmorEvent;
import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.*;
import vazkii.psi.api.spell.param.ParamNumber;
import vazkii.psi.api.spell.piece.PieceTrick;
import vazkii.psi.common.entity.EntitySpellCharge;

import java.util.List;

public class PieceTrickDetonate extends PieceTrick {

    private SpellParam radius;

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
        if (radiusVal == null || radiusVal <= 0)
            throw new SpellCompilationException(SpellCompilationException.NON_POSITIVE_VALUE, x, y);
        meta.addStat(EnumSpellStat.POTENCY, radiusVal.intValue());
        meta.addStat(EnumSpellStat.COST, radiusVal.intValue() * 5);
    }

    @Override
    public Object execute(SpellContext context) throws SpellRuntimeException {
        Vector3 positionVal = Vector3.fromEntity(context.caster).add(0, context.caster.getEyeHeight(), 0);
        Double radiusVal = this.<Double>getParamValue(context, radius);

        if (!context.isInRadius(positionVal))
            throw new SpellRuntimeException(SpellRuntimeException.OUTSIDE_RADIUS);

        AxisAlignedBB axis = new AxisAlignedBB(positionVal.x - radiusVal, positionVal.y - radiusVal, positionVal.z - radiusVal, positionVal.x + radiusVal, positionVal.y + radiusVal, positionVal.z + radiusVal);


        List<EntitySpellCharge> list = context.caster.getEntityWorld().getEntitiesWithinAABB(EntitySpellCharge.class, axis,
                (EntitySpellCharge e) -> e != null && e != context.focalPoint && context.isInRadius(e) && e.getThrower() == context.caster);


        for (EntitySpellCharge ent : list)
            ent.doExplosion();

        if (context.caster.world instanceof WorldServer) {
            WorldServer server = (WorldServer) context.caster.world;
            server.addScheduledTask(() ->
                PsiArmorEvent.post(new PsiArmorEvent(context.caster, ItemTriggerSensor.EVENT_TRIGGER)));
        }
        return !list.isEmpty();
    }
}
