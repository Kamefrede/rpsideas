package com.kamefrede.rpsideas.spells.operator.entity;

import com.kamefrede.rpsideas.spells.base.SpellRuntimeExceptions;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.SpellParam;
import vazkii.psi.api.spell.SpellRuntimeException;
import vazkii.psi.api.spell.param.ParamVector;
import vazkii.psi.api.spell.piece.PieceOperator;
import vazkii.psi.common.spell.operator.vector.PieceOperatorVectorRaycast;

public class OperatorDistanceFromGround extends PieceOperator {// TODO: 12/15/18 look at

    SpellParam target;

    public OperatorDistanceFromGround(Spell spell) {
        super(spell);
    }

    public static int getDistance(BlockPos pos, SpellContext context) throws SpellRuntimeException {
        World world = context.caster.world;
        double y = pos.getY();
        int distance = 0;
        for (double i = y; i >= 0; i--) {
            BlockPos pos1 = new BlockPos(pos.getX(), i, pos.getZ());
            IBlockState state = world.getBlockState(pos1);
            if (!state.getBlock().isAir(state, world, pos1)) break;
            if (i == 0 && state.getBlock().isAir(state, world, pos1))
                throw new SpellRuntimeException(SpellRuntimeExceptions.NO_GROUND);
            distance++;

        }
        return distance;
    }

    @Override
    public void initParams() {
        addParam(target = new ParamVector(SpellParam.GENERIC_NAME_TARGET, SpellParam.BLUE, false, false));
    }

    @Override
    public Object execute(SpellContext context) throws SpellRuntimeException {
        double maxheight = 256;
        Vector3 targetVal = this.<Vector3>getParamValue(context, target);
        if (targetVal == null || targetVal.isZero()) throw new SpellRuntimeException(SpellRuntimeException.NULL_TARGET);
        if (targetVal.y <= 256) maxheight = targetVal.y;
        BlockPos pos = new BlockPos(targetVal.x, maxheight, targetVal.z);

        RayTraceResult res = PieceOperatorVectorRaycast.raycast(context.caster.getEntityWorld(), new Vector3(targetVal.x, maxheight, targetVal.z), new Vector3(0, -1, 0), 256);
        if (res == null || res.getBlockPos() == null)
            throw new SpellRuntimeException(SpellRuntimeExceptions.NO_GROUND);

        return (double) maxheight - res.getBlockPos().getY() - 1.0D * 1.0D;

        //rip all the precision
        //  return getDistance(pos, context) - 2D * 1.0D;


    }

    @Override
    public Class<?> getEvaluationType() {
        return Double.class;
    }
}
