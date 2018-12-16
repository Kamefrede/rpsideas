package com.kamefrede.rpsideas.spells.operator.vector;

import com.kamefrede.rpsideas.spells.base.SpellParams;
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

    private SpellParam origin;
    private SpellParam ray;
    private SpellParam max;
    public PiecePieceOperatorVectorStrongRaycastAxis(Spell spell) {
        super(spell);
    }

    @Override
    public void initParams() {
        addParam(origin = new ParamVector(SpellParam.GENERIC_NAME_POSITION, SpellParam.BLUE, false, false));
        addParam(ray = new ParamVector(SpellParams.GENERIC_VAZKII_RAY, SpellParam.GREEN, false, false));
        addParam(max = new ParamNumber(SpellParam.GENERIC_NAME_MAX, SpellParam.PURPLE, true, false));
    }

    @Override
    public Class<?> getEvaluationType() {
        return Vector3.class;
    }

    @Override
    public Object execute(SpellContext context) throws SpellRuntimeException {
        Vector3 originVal = this.getParamValue(context, origin);
        Vector3 rayVal = this.getParamValue(context, ray);

        if (originVal == null || rayVal == null)
            throw new SpellRuntimeException(SpellRuntimeException.NULL_VECTOR);

        double maxLen = SpellContext.MAX_DISTANCE;
        Double numberVal = this.<Double>getParamValue(context, max);
        if (numberVal != null)
            maxLen = numberVal;
        maxLen = Math.min(SpellContext.MAX_DISTANCE, maxLen);

        Vector3 end = originVal.copy().add(rayVal.copy().normalize().multiply(maxLen));

        RayTraceResult pos = context.caster.getEntityWorld().rayTraceBlocks(originVal.toVec3D(), end.toVec3D(), false, true, false);
        if (pos == null)
            throw new SpellRuntimeException(SpellRuntimeException.NULL_VECTOR);

        EnumFacing facing = pos.sideHit;
        return new Vector3(facing.getXOffset(), facing.getYOffset(), facing.getZOffset());
    }
}
