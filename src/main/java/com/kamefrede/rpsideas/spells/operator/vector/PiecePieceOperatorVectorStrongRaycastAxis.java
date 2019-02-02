package com.kamefrede.rpsideas.spells.operator.vector;

import com.kamefrede.rpsideas.spells.base.SpellParams;
import com.kamefrede.rpsideas.util.helpers.SpellHelpers;
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
        Vector3 originVal = SpellHelpers.getVector3(this, context, origin, false, false);
        Vector3 rayVal = SpellHelpers.getVector3(this, context, ray, false, false);

        double maxLen = SpellHelpers.getBoundedNumber(this, context, max, SpellContext.MAX_DISTANCE);

        Vector3 end = originVal.copy().add(rayVal.copy().normalize().multiply(maxLen));

        RayTraceResult pos = context.caster.world.rayTraceBlocks(originVal.toVec3D(), end.toVec3D(), false, true, false);
        if (pos == null)
            throw new SpellRuntimeException(SpellRuntimeException.NULL_VECTOR);

        EnumFacing facing = pos.sideHit;
        return new Vector3(facing.getXOffset(), facing.getYOffset(), facing.getZOffset());
    }
}
