package com.kamefrede.rpsideas.spells.operator.vector;

import com.kamefrede.rpsideas.spells.base.SpellParams;
import com.kamefrede.rpsideas.util.helpers.SpellHelpers;
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
        Vector3 originVal = SpellHelpers.getVector3(this, context, origin, false, false);
        Vector3 rayVal = SpellHelpers.getVector3(this, context, ray, false, false);

        double maxLen = SpellHelpers.getBoundedNumber(this, context, maxParam, SpellContext.MAX_DISTANCE);

        RayTraceResult pos = raycast(context.caster.world, originVal, rayVal, maxLen);
        if (pos == null)
            throw new SpellRuntimeException(SpellRuntimeException.NULL_VECTOR);

        return new Vector3(pos.getBlockPos().getX(), pos.getBlockPos().getY(), pos.getBlockPos().getZ());
    }

    @Override
    public Class<?> getEvaluationType() {
        return Vector3.class;
    }
}
