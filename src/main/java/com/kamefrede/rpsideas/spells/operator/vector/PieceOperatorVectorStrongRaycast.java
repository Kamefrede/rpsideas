package com.kamefrede.rpsideas.spells.operator.vector;

import com.kamefrede.rpsideas.spells.base.SpellParams;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.SpellParam;
import vazkii.psi.api.spell.SpellRuntimeException;
import vazkii.psi.api.spell.param.ParamNumber;
import vazkii.psi.api.spell.param.ParamVector;
import vazkii.psi.api.spell.piece.PieceOperator;

public class PieceOperatorVectorStrongRaycast extends PieceOperator {

    private SpellParam origin;
    private SpellParam ray;
    private SpellParam maxParam;

    public PieceOperatorVectorStrongRaycast(Spell spell) {
        super(spell);
    }

    public static RayTraceResult raycast(World world, Vector3 origin, Vector3 ray, double len) {
        Vector3 end = origin.copy().add(ray.copy().normalize().multiply(len));
        return world.rayTraceBlocks(origin.toVec3D(), end.toVec3D(), false, true, false);
    }

    @Override
    public void initParams() {
        addParam(origin = new ParamVector(SpellParam.GENERIC_NAME_POSITION, SpellParam.BLUE, false, false));
        addParam(ray = new ParamVector(SpellParams.GENERIC_VAZKII_RAY, SpellParam.GREEN, false, false));
        addParam(maxParam = new ParamNumber(SpellParam.GENERIC_NAME_MAX, SpellParam.PURPLE, true, false));
    }

    @Override
    public Object execute(SpellContext context) throws SpellRuntimeException {
        Vector3 originVal = this.getParamValue(context, origin);
        Vector3 rayVal = this.getParamValue(context, ray);

        if (originVal == null || rayVal == null)
            throw new SpellRuntimeException(SpellRuntimeException.NULL_VECTOR);

        double maxLen = SpellContext.MAX_DISTANCE;
        Double numberVal = this.<Double>getParamValue(context, maxParam);
        if (numberVal != null)
            maxLen = numberVal;
        maxLen = Math.min(SpellContext.MAX_DISTANCE, maxLen);

        RayTraceResult pos = raycast(context.caster.getEntityWorld(), originVal, rayVal, maxLen);
        if (pos == null)
            throw new SpellRuntimeException(SpellRuntimeException.NULL_VECTOR);

        return new Vector3(pos.getBlockPos().getX(), pos.getBlockPos().getY(), pos.getBlockPos().getZ());
    }

    @Override
    public Class<?> getEvaluationType() {
        return Vector3.class;
    }
}
