package com.github.kamefrede.rpsideas.spells.operator.vector;

import com.github.kamefrede.rpsideas.spells.base.SpellRuntimeExceptions;
import com.github.kamefrede.rpsideas.util.helpers.SpellHelpers;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.*;
import vazkii.psi.api.spell.param.ParamNumber;
import vazkii.psi.api.spell.param.ParamVector;
import vazkii.psi.api.spell.piece.PieceOperator;

public class PieceOperatorVectorStrongRaycast extends PieceOperator {

    SpellParam origin;
    SpellParam ray;
    SpellParam maxParam;

    private static final double MAX_MAX = 32d;



    public PieceOperatorVectorStrongRaycast(Spell spell){
        super(spell);
    }

    @Override
    public void initParams() {
        addParam(origin = new ParamVector(SpellParam.GENERIC_NAME_POSITION, SpellParam.BLUE, false, false));
        addParam(ray = new ParamVector("psi.spellparam.ray", SpellParam.GREEN, false, false));
        addParam(maxParam = new ParamNumber(SpellParam.GENERIC_NAME_MAX, SpellParam.PURPLE, true, false));
    }

    @Override
    public void addToMetadata(SpellMetadata meta) throws SpellCompilationException {
        super.addToMetadata(meta);
    }

    @Override
    public Object execute(SpellContext context) throws SpellRuntimeException {
        if(context.caster.world.isRemote) return null;
        Vector3 originVec = this.<Vector3>getParamValue(context, origin);
        Vector3 rayVec = this.<Vector3>getParamValue(context, ray);
        if(originVec == null || rayVec == null) {
            throw new SpellRuntimeException(SpellRuntimeException.NULL_VECTOR);
        }
        double max = SpellHelpers.Runtime.getNumber(this, context, maxParam, MAX_MAX);
        max = Math.min(max, MAX_MAX);
        if(max < 0) throw new SpellRuntimeException(SpellRuntimeExceptions.NEGATIVE_LENGTH);

        Vector3 endVec = originVec.copy().add(rayVec.copy().normalize().multiply(max));
        RayTraceResult res = context.caster.world.rayTraceBlocks(originVec.toVec3D(), endVec.toVec3D(), false, true, false);

        if(res != null && res.getBlockPos() != null) {
            return createResult(res);
        } else throw new SpellRuntimeException(SpellRuntimeException.NULL_VECTOR);
    }

    protected Vector3 createResult(RayTraceResult res) {
        return Vector3.fromBlockPos(res.getBlockPos());
    }

    @Override
    public Class<?> getEvaluationType() {
        return Vector3.class;
    }
}
