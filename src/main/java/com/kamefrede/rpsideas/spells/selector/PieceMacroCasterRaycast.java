package com.kamefrede.rpsideas.spells.selector;

import net.minecraft.util.math.RayTraceResult;
import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.*;
import vazkii.psi.api.spell.param.ParamNumber;
import vazkii.psi.common.spell.operator.vector.PieceOperatorVectorRaycast;

public class PieceMacroCasterRaycast extends SpellPiece {

    private SpellParam maxDistance;

    public PieceMacroCasterRaycast(Spell spell) {
        super(spell);
    }

    @Override
    public void initParams() {
        addParam(maxDistance = new ParamNumber(SpellParam.GENERIC_NAME_MAX, SpellParam.RED, true, false));
    }

    @Override
    public void addToMetadata(SpellMetadata meta) throws SpellCompilationException {
        meta.addStat(EnumSpellStat.COMPLEXITY, 4);
    }

    @Override
    public Object evaluate() {
        return null;
    }

    @Override
    public Object execute(SpellContext context) throws SpellRuntimeException {
        Vector3 originVal = Vector3.fromEntity(context.caster).add(0, context.caster.getEyeHeight(), 0);
        Vector3 rayVal = new Vector3(context.caster.getLook(1F));

        double maxLen = SpellContext.MAX_DISTANCE;
        Double numberVal = this.<Double>getParamValue(context, maxDistance);
        if (numberVal != null)
            maxLen = numberVal;
        if (maxLen <= 0)
            throw new SpellRuntimeException(SpellRuntimeException.NEGATIVE_NUMBER);
        maxLen = Math.min(SpellContext.MAX_DISTANCE, maxLen);

        RayTraceResult pos = PieceOperatorVectorRaycast.raycast(context.caster.getEntityWorld(), originVal, rayVal, maxLen);
        if (pos == null)
            throw new SpellRuntimeException(SpellRuntimeException.NULL_VECTOR);

        return new Vector3(pos.getBlockPos().getX(), pos.getBlockPos().getY(), pos.getBlockPos().getZ());
    }

    @Override
    public EnumPieceType getPieceType() {
        return EnumPieceType.SELECTOR;
    }

    @Override
    public Class<?> getEvaluationType() {
        return Vector3.class;
    }


}
