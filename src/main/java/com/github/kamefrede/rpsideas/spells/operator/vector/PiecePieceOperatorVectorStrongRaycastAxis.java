package com.github.kamefrede.rpsideas.spells.operator.vector;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.RayTraceResult;
import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.SpellParam;
import vazkii.psi.api.spell.SpellRuntimeException;
import vazkii.psi.api.spell.param.ParamNumber;
import vazkii.psi.api.spell.param.ParamVector;
import vazkii.psi.api.spell.piece.PieceOperator;

public class PiecePieceOperatorVectorStrongRaycastAxis extends PieceOperator {

    public PiecePieceOperatorVectorStrongRaycastAxis(Spell spell){
        super(spell);
    }

    SpellParam origin;
    SpellParam ray;
    SpellParam max;


    @Override
    public void initParams() {
        addParam(origin = new ParamVector(SpellParam.GENERIC_NAME_POSITION, SpellParam.BLUE, false, false));
        addParam(ray = new ParamVector("psi.spellparam.ray", SpellParam.GREEN, false, false));
        addParam(max = new ParamNumber(SpellParam.GENERIC_NAME_MAX, SpellParam.PURPLE, true, false));
    }

    @Override
    public Class<?> getEvaluationType() {
        return Vector3.class;
    }

    @Override
    public Object execute(SpellContext context) throws SpellRuntimeException {
        Vector3 originVal = this.<Vector3>getParamValue(context, origin);
        Vector3 rayVal = this.<Vector3>getParamValue(context, ray);

        if(originVal == null || rayVal == null)
            throw new SpellRuntimeException(SpellRuntimeException.NULL_VECTOR);

        double maxLen = SpellContext.MAX_DISTANCE;
        Double numberVal = this.<Double>getParamValue(context, max);
        if(numberVal != null)
            maxLen = numberVal.doubleValue();
        maxLen = Math.min(SpellContext.MAX_DISTANCE, maxLen);

        Vector3 end = originVal.copy().add(rayVal.copy().normalize().multiply(maxLen));

        RayTraceResult pos = context.caster.getEntityWorld().rayTraceBlocks(originVal.toVec3D(), end.toVec3D(), false, true, false);
        if(pos == null || pos.getBlockPos() == null)
            throw new SpellRuntimeException(SpellRuntimeException.NULL_VECTOR);

        EnumFacing facing = pos.sideHit;
        return new Vector3(facing.getXOffset(), facing.getYOffset(), facing.getZOffset());
    }
}
